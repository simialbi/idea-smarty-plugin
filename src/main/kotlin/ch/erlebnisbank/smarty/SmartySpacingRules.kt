package ch.erlebnisbank.smarty

import ch.erlebnisbank.smarty.psi.SmartyTokenSets
import ch.erlebnisbank.smarty.psi.SmartyTypes
import com.intellij.formatting.Block
import com.intellij.formatting.Spacing
import com.intellij.formatting.SpacingBuilder
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.tree.TokenSet

/**
 * Where Smarty wants space inside a tag and where it does not.
 *
 * The rules only ever apply between two tokens of the same `{...}` tag. Everything a template
 * emits verbatim - the markup and the text between the tags - is lexed as [SmartyTypes.TEXT], never
 * as whitespace, so none of it is reachable from here. What happens out there is decided by the
 * data language through the block merge in `SmartyBlock`, which is also where the whitespace
 * between a tag and the markup around it is settled.
 *
 * Two of the rules are correctness, not taste:
 *
 * - the textual operators (`eq`, `and`, `matches`, ...) are words, so `$a eq $b` has to keep its
 *   spaces or it lexes as the single identifier `$aeq$b`. They are pinned to one space and
 *   deliberately not wired to a code style setting.
 * - `=` is two different operators. In `{include file="header.tpl"}` it separates an attribute
 *   from its value and is written tight; in `{assign $x = 1}` it is an assignment and is written
 *   with spaces. Which one it is depends on what stands to the left, so it is decided in
 *   [spacing] rather than by a token rule.
 */
internal class SmartySpacingRules(settings: CodeStyleSettings) {

    private val common = settings.getCommonSettings(SmartyLanguage.INSTANCE)

    private val builder: SpacingBuilder = SpacingBuilder(settings, SmartyLanguage.INSTANCE)
        // Tag delimiters hug their contents: `{ if $a }` is `{if $a}`.
        .after(SmartyTypes.LDELIM).none()
        .before(SmartyTypes.RDELIM).none()

        // Property access and subscripts bind tighter than anything else in an expression.
        .before(SmartyTypes.MEMBER_ACCESS).none()
        .before(SmartyTypes.ARRAY_ACCESS).none()
        .after(SmartyTypes.DOT).none()
        .after(SmartyTypes.ARROW).none()
        .after(SmartyTypes.LBRACKET).none()
        .before(SmartyTypes.RBRACKET).none()
        .after(SmartyTypes.DOLLAR).none()

        // A hash binds to its config key the way a dollar binds to a variable name: {#pageTitle#}.
        .aroundInside(SmartyTypes.HASH, SmartyTypes.CONFIG_VARIABLE).none()

        // Nothing reaches this rule today: the lexer emits `@` but no grammar rule consumes it
        // since config variables stopped being spelled with it. It is the answer the loop
        // properties ({$item@index}) and array modifiers ({$rows|@count}) will want.
        .after(SmartyTypes.AT).none()

        // Modifier chains are written without air: {$title|truncate:30:"..."|escape}
        .before(SmartyTypes.MODIFIER_CHAIN).none()
        .before(SmartyTypes.MODIFIER).none()
        .aroundInside(SmartyTypes.PIPE, SmartyTypes.MODIFIER).none()
        .aroundInside(SmartyTypes.COLON, SmartyTypes.MODIFIER).none()

        // A call sticks to its parentheses; a parenthesised expression keeps its contents tight.
        .beforeInside(SmartyTypes.LPAREN, SmartyTypes.FUNCTION_CALL).none()
        .afterInside(SmartyTypes.LPAREN, SmartyTypes.FUNCTION_CALL)
        .spaceIf(common.SPACE_WITHIN_METHOD_CALL_PARENTHESES)
        .beforeInside(SmartyTypes.RPAREN, SmartyTypes.FUNCTION_CALL)
        .spaceIf(common.SPACE_WITHIN_METHOD_CALL_PARENTHESES)
        .afterInside(SmartyTypes.LPAREN, SmartyTypes.EXPR).none()
        .beforeInside(SmartyTypes.RPAREN, SmartyTypes.EXPR).none()

        // Separators. The `;` ones are the three clauses of {for $i=0; $i<10; $i=$i+1}.
        .before(SmartyTypes.COMMA).spaceIf(common.SPACE_BEFORE_COMMA)
        .after(SmartyTypes.COMMA).spaceIf(common.SPACE_AFTER_COMMA)
        .before(SmartyTypes.SEMICOLON).none()
        .after(SmartyTypes.SEMICOLON).spaces(1)

        // Word operators cannot be glued to their operands - see the class comment.
        .around(TEXTUAL_OPERATORS).spaces(1)
        .around(SmartyTypes.AS).spaces(1)

        // Symbolic operators follow the usual code style switches, so they stay configurable
        // once a code style page exists for the language.
        .after(SmartyTypes.NOT).none()
        .around(EQUALITY_OPERATORS).spaceIf(common.SPACE_AROUND_EQUALITY_OPERATORS)
        .around(RELATIONAL_OPERATORS).spaceIf(common.SPACE_AROUND_RELATIONAL_OPERATORS)
        .around(LOGICAL_OPERATORS).spaceIf(common.SPACE_AROUND_LOGICAL_OPERATORS)
        .around(ADDITIVE_OPERATORS).spaceIf(common.SPACE_AROUND_ADDITIVE_OPERATORS)
        .around(MULTIPLICATIVE_OPERATORS).spaceIf(common.SPACE_AROUND_MULTIPLICATIVE_OPERATORS)
        .around(SmartyTypes.FAT_ARROW).spaces(1)
        .around(SmartyTypes.QUESTION).spaces(1)
        .aroundInside(SmartyTypes.COLON, SmartyTypes.EXPR).spaces(1)

        // Whatever else a tag keyword is followed by, it needs one space to stand apart from it.
        // Closing tags are exempt and never reach the builder.
        .after(TAG_KEYWORDS).spaces(1)

    fun spacing(parent: SmartyBlock, child1: Block?, child2: Block): Spacing? {
        val left = (child1 as? SmartyBlock)?.node ?: return null
        val right = (child2 as? SmartyBlock)?.node ?: return null

        // Between two template items there is no whitespace token to adjust, only text the
        // template prints. Say so explicitly rather than relying on the rules below to miss.
        if (parent.node.psi is PsiFile) return null

        // `{/foreach}` is four tokens that belong together.
        if (parent.node.elementType === SmartyTypes.SMARTY_CLOSING_TAG) return GLUED

        // `-1` is a signed number, `$a - 1` is a subtraction, and the token is the same either
        // way. Only the sign form is decided here; the binary form falls through to the rules.
        if (isSign(left)) return GLUED

        val assign = when {
            left.elementType === SmartyTypes.ASSIGN -> left
            right.elementType === SmartyTypes.ASSIGN -> right
            else -> null
        }
        if (assign != null) {
            return if (assignsToVariable(assign)) {
                spaceIf(common.SPACE_AROUND_ASSIGNMENT_OPERATORS)
            } else {
                NO_SPACE
            }
        }

        // A rule of its own, or else: collapse a run of spaces to one, but never invent one.
        // Falling back to `null` would leave `{if    $a}` alone, which is the main thing a
        // reformat is asked to clean up.
        return builder.getSpacing(parent, child1, child2) ?: COLLAPSE
    }

    private fun spaceIf(wanted: Boolean): Spacing = if (wanted) ONE_SPACE else NO_SPACE

    /** True when this `+`/`-` starts an operand instead of joining two of them. */
    private fun isSign(node: ASTNode): Boolean {
        if (!ADDITIVE_OPERATORS.contains(node.elementType)) return false
        val previous = previousMeaningful(node) ?: return true
        return OPERAND_STARTERS.contains(previous.elementType)
    }

    /** Distinguishes `{assign $x = 1}` from the attribute `=` of `{include file="x.tpl"}`. */
    private fun assignsToVariable(assign: ASTNode): Boolean =
        previousMeaningful(assign)?.elementType === SmartyTypes.VARIABLE

    private fun previousMeaningful(node: ASTNode): ASTNode? {
        var previous = node.treePrev
        while (previous != null && SmartyTokenSets.WHITE_SPACES.contains(previous.elementType)) {
            previous = previous.treePrev
        }
        return previous
    }

    companion object {

        /** No space and no line break either - for tokens that are one atom to a reader. */
        private val GLUED = Spacing.createSpacing(0, 0, 0, false, 0)

        private val NO_SPACE = Spacing.createSpacing(0, 0, 0, true, 0)
        private val ONE_SPACE = Spacing.createSpacing(1, 1, 0, true, 0)

        /** At most one space, at least none: shrinks a run, leaves a missing space missing. */
        private val COLLAPSE = Spacing.createSpacing(0, 1, 0, true, 0)

        private val EQUALITY_OPERATORS = TokenSet.create(
            SmartyTypes.EQ, SmartyTypes.NEQ, SmartyTypes.EQEQ, SmartyTypes.NEQEQ
        )

        private val RELATIONAL_OPERATORS = TokenSet.create(
            SmartyTypes.LT, SmartyTypes.GT, SmartyTypes.LE, SmartyTypes.GE
        )

        private val LOGICAL_OPERATORS = TokenSet.create(SmartyTypes.AND, SmartyTypes.OR)

        private val ADDITIVE_OPERATORS = TokenSet.create(SmartyTypes.PLUS, SmartyTypes.MINUS)

        private val MULTIPLICATIVE_OPERATORS = TokenSet.create(
            SmartyTypes.MULT, SmartyTypes.DIV, SmartyTypes.MOD
        )

        /**
         * The operators Smarty spells as words. `mod` and `div` are in here rather than with the
         * symbols for the same reason `eq` is: they are identifiers to the lexer until they are
         * surrounded by space.
         */
        private val TEXTUAL_OPERATORS = TokenSet.create(
            SmartyTypes.EQ_KEYWORD, SmartyTypes.NEQ_KEYWORD, SmartyTypes.LT_KEYWORD,
            SmartyTypes.GT_KEYWORD, SmartyTypes.LE_KEYWORD, SmartyTypes.GE_KEYWORD,
            SmartyTypes.MATCHES, SmartyTypes.AND_KEYWORD, SmartyTypes.OR_KEYWORD,
            SmartyTypes.NOT_KEYWORD, SmartyTypes.MOD_KEYWORD, SmartyTypes.DIV_KEYWORD
        )

        /**
         * Keywords that open a tag and are followed by operands. `literal` and `nocache` are
         * missing because their blocks are never taken apart, and `else`, `break` and the other
         * bare keywords are harmless here - a rule on the left of a pair does nothing when there
         * is no right-hand side.
         */
        private val TAG_KEYWORDS = TokenSet.create(
            SmartyTypes.IF, SmartyTypes.ELSEIF, SmartyTypes.FOREACH, SmartyTypes.FOR,
            SmartyTypes.WHILE, SmartyTypes.SECTION, SmartyTypes.SWITCH, SmartyTypes.CASE,
            SmartyTypes.BLOCK, SmartyTypes.FUNCTION, SmartyTypes.CALL, SmartyTypes.EXTENDS,
            SmartyTypes.INCLUDE, SmartyTypes.INSERT, SmartyTypes.ASSIGN_KW, SmartyTypes.APPEND,
            SmartyTypes.PREPEND, SmartyTypes.CAPTURE, SmartyTypes.CONFIG_LOAD,
            SmartyTypes.SETFILTER, SmartyTypes.STRIP
        )

        /** What can stand in front of a `+`/`-` that is a sign rather than an operator. */
        private val OPERAND_STARTERS = TokenSet.orSet(
            SmartyTokenSets.OPERATORS,
            TAG_KEYWORDS,
            TokenSet.create(
                SmartyTypes.LPAREN, SmartyTypes.LBRACKET, SmartyTypes.COMMA, SmartyTypes.COLON,
                SmartyTypes.ASSIGN, SmartyTypes.FAT_ARROW, SmartyTypes.QUESTION,
                SmartyTypes.SEMICOLON, SmartyTypes.AS, SmartyTypes.PIPE
            )
        )
    }
}
