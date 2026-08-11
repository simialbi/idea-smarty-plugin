package ch.erlebnisplus.smarty.psi

import com.intellij.psi.TokenType
import com.intellij.psi.tree.TokenSet

/**
 * The token groupings the platform asks for: by the parser definition to know what to skip, and
 * by the words scanner behind Find Usages to know what kind of occurrence a word is.
 */
object SmartyTokenSets {

    /**
     * The lexer emits its own [SmartyTypes.WS] rather than [TokenType.WHITE_SPACE]; both are
     * listed so whitespace never reaches the parser as a meaningful token.
     */
    val WHITE_SPACES: TokenSet = TokenSet.create(SmartyTypes.WS, TokenType.WHITE_SPACE)

    val COMMENTS: TokenSet = TokenSet.create(SmartyTypes.COMMENT)

    /**
     * Strings are scanned as identifiers, not as literals.
     *
     * In Smarty a declaration is normally written quoted - `{block name="content"}` - so the
     * name is a code occurrence that happens to sit inside a string. Indexing it as a literal
     * would file it under `IN_STRINGS`, which the default reference search skips, and Find
     * Usages would silently miss every override written in the common syntax.
     */
    val IDENTIFIERS: TokenSet = TokenSet.create(SmartyTypes.IDENTIFIER, SmartyTypes.STRING)

    /** What the parser definition reports as string literals; unrelated to the words scanner. */
    val STRINGS: TokenSet = TokenSet.create(SmartyTypes.STRING)

    /**
     * The keywords that open a tag needing a `{/tag}` counterpart.
     *
     * `literal` and `nocache` are absent on purpose: the grammar matches those as whole blocks,
     * so they never need pairing up. `else`, `elseif`, `foreachelse` and `sectionelse` are not
     * openers either - they continue a block rather than starting one.
     */
    val BLOCK_OPENERS: TokenSet = TokenSet.create(
        SmartyTypes.IF, SmartyTypes.FOREACH, SmartyTypes.FOR, SmartyTypes.WHILE,
        SmartyTypes.SECTION, SmartyTypes.SWITCH, SmartyTypes.BLOCK, SmartyTypes.FUNCTION,
        SmartyTypes.CAPTURE, SmartyTypes.STRIP, SmartyTypes.SETFILTER
    )

    /**
     * The four separators that open one step of an access chain: `{$obj.property}`,
     * `{$obj->property}`, `{Foo::CONSTANT}` and the loop property of `{$row@index}`.
     *
     * A `[…]` step has none of them, which is how the PSI helpers and the annotator tell a
     * subscript from a named step without looking at the rule that matched.
     */
    val ACCESS_SEPARATORS: TokenSet = TokenSet.create(
        SmartyTypes.DOT, SmartyTypes.ARROW, SmartyTypes.DOUBLE_COLON, SmartyTypes.AT
    )

    /**
     * Every binary and unary operator, symbolic and textual alike, so that `{if $a eq $b}` is
     * treated exactly like `{if $a == $b}` by highlighting and by the PSI helpers.
     */
    val OPERATORS: TokenSet = TokenSet.create(
        // comparison
        SmartyTypes.EQ, SmartyTypes.NEQ, SmartyTypes.EQEQ, SmartyTypes.NEQEQ,
        SmartyTypes.LT, SmartyTypes.GT, SmartyTypes.LE, SmartyTypes.GE,
        SmartyTypes.EQ_KEYWORD, SmartyTypes.NEQ_KEYWORD, SmartyTypes.LT_KEYWORD,
        SmartyTypes.GT_KEYWORD, SmartyTypes.LE_KEYWORD, SmartyTypes.GE_KEYWORD,
        SmartyTypes.MATCHES,
        // logical
        SmartyTypes.AND, SmartyTypes.OR, SmartyTypes.NOT,
        SmartyTypes.AND_KEYWORD, SmartyTypes.OR_KEYWORD, SmartyTypes.NOT_KEYWORD,
        // arithmetic
        SmartyTypes.PLUS, SmartyTypes.MINUS, SmartyTypes.MULT, SmartyTypes.DIV,
        SmartyTypes.MOD, SmartyTypes.MOD_KEYWORD, SmartyTypes.DIV_KEYWORD
    )
}
