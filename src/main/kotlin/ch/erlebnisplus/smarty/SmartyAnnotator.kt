package ch.erlebnisplus.smarty

import ch.erlebnisplus.smarty.psi.*
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.ASTNode
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
import com.intellij.psi.util.PsiTreeUtil

/**
 * Adds semantic highlighting and problem reporting on top of the purely lexical
 * [SmartySyntaxHighlighter].
 *
 * The annotator only ever inspects the element it is called with, so ranges are always
 * computed relative to that element (start inclusive, end exclusive). Highlighting-only
 * annotations are created as silent informational annotations, real problems use a
 * severity plus a [ProblemHighlightType].
 *
 * Read more: https://plugins.jetbrains.com/docs/intellij/annotator.html
 */
class SmartyAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element) {
            is Variable -> annotateVariable(element, holder)
            is ConfigVariable -> highlight(holder, element.textRange, SmartySyntaxHighlighter.CONSTANT)
            is MemberAccess -> annotateMemberAccess(element, holder)
            is MethodCall -> annotateMethodCall(element, holder)
            is Modifier -> annotateModifier(element, holder)
            is FunctionCall -> annotateFunctionCall(element, holder)
            is IncludeStatement, is ExtendsStatement, is InsertStatement ->
                annotateTemplateReference(element, holder)

            is BlockStatement, is FunctionStatement -> annotateDeclarationName(element, holder)
            else -> annotateLeaf(element, holder)
        }
    }

    // ========================================================================
    // VARIABLES
    // ========================================================================

    /**
     * Colors `$name`. The Smarty super global `$smarty` gets its own attributes so that
     * `{$smarty.foreach.row.index}` is distinguishable from a template variable; the steps of
     * the access chain behind it are colored by [annotateMemberAccess], which the annotator
     * reaches on its own way down the tree.
     */
    private fun annotateVariable(element: Variable, holder: AnnotationHolder) {
        val name = VARIABLE_NAME.find(element.text) ?: return
        val attributes = if (name.groupValues[1].lowercase() in SmartyBuiltins.RESERVED_VARIABLES) {
            SmartySyntaxHighlighter.RESERVED_VARIABLE
        } else {
            SmartySyntaxHighlighter.VARIABLE
        }
        highlight(holder, name.rangeIn(element.textRange.startOffset), attributes)
    }

    // ========================================================================
    // ACCESS CHAINS
    // ========================================================================

    /**
     * Colors one step of an access chain, wherever the chain hangs: behind a variable in
     * `{$obj->property}`, behind a class name in `{DynamicModal::SIZE}`, and behind the `@` of
     * `{$row@index}`.
     *
     * Three steps are not this method's to color. A `[…]` subscript names nothing; a step whose
     * name is a whole variable - `{$smarty.config.$key}` - colors itself through
     * [annotateVariable]; and a call is a [MethodCall], which gets the color of a call rather
     * than of a property.
     */
    private fun annotateMemberAccess(element: MemberAccess, holder: AnnotationHolder) {
        val separator = meaningful(element.node.firstChildNode) ?: return
        if (!SmartyTokenSets.ACCESS_SEPARATORS.contains(separator.elementType)) return

        val name = meaningful(separator.treeNext) ?: return
        if (name.firstChildNode != null) return

        // Behind a `::` a plain word is a class constant - a static property is written with the
        // `$` that makes it a variable, and colors itself as one.
        val attributes = if (separator.elementType === SmartyTypes.DOUBLE_COLON) {
            SmartySyntaxHighlighter.CONSTANT
        } else {
            SmartySyntaxHighlighter.PROPERTY
        }
        highlight(holder, name.textRange, attributes)
    }

    /** Colors the name of `{$this->head()}` and of `{Foo::bar()}` like any other call. */
    private fun annotateMethodCall(element: MethodCall, holder: AnnotationHolder) {
        val separator = meaningful(element.node.firstChildNode) ?: return
        val name = meaningful(separator.treeNext) ?: return
        if (name.firstChildNode != null) return

        highlight(holder, name.textRange, SmartySyntaxHighlighter.FUNCTION_CALL)
    }

    /** [node] itself, or the first of its following siblings that is not whitespace. */
    private fun meaningful(node: ASTNode?): ASTNode? {
        var current = node
        while (current != null && SmartyTokenSets.WHITE_SPACES.contains(current.elementType)) {
            current = current.treeNext
        }
        return current
    }

    // ========================================================================
    // MODIFIERS
    // ========================================================================

    /**
     * Colors known modifier names and flags unknown ones. Any PHP function may be used as a
     * modifier, so an unknown name is only a weak warning - it is not necessarily an error.
     */
    private fun annotateModifier(element: Modifier, holder: AnnotationHolder) {
        val offset = element.textRange.startOffset
        val group = MODIFIER_NAME.find(element.text)?.groups?.get(1) ?: return
        val name = group.value.lowercase()

        val maximumParameters = SmartyBuiltins.MODIFIERS[name]
        if (maximumParameters == null) {
            holder.newAnnotation(
                HighlightSeverity.WEAK_WARNING,
                "Unknown modifier '${group.value}': must be provided by a Smarty plugin or a PHP function"
            )
                .range(group.rangeIn(offset))
                .highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL)
                .create()
            return
        }

        highlight(holder, group.rangeIn(offset), SmartySyntaxHighlighter.MODIFIER)

        val parameters = countParameters(element.text)
        if (parameters > maximumParameters) {
            val message = if (maximumParameters == 0) {
                "Modifier '${group.value}' does not take any parameters"
            } else {
                "Modifier '${group.value}' takes at most $maximumParameters parameters, but $parameters were given"
            }
            holder.newAnnotation(HighlightSeverity.WARNING, message)
                .range(element)
                .create()
        }
    }

    /**
     * Counts the top level `:` separators of a modifier, ignoring the ones inside string
     * literals, parentheses or brackets.
     */
    private fun countParameters(text: String): Int {
        var parameters = 0
        var depth = 0
        var quote = NO_QUOTE
        var index = 0

        while (index < text.length) {
            val character = text[index]
            when {
                quote != NO_QUOTE -> when (character) {
                    '\\' -> index++
                    quote -> quote = NO_QUOTE
                }

                character == '"' || character == '\'' -> quote = character
                character == '(' || character == '[' -> depth++
                character == ')' || character == ']' -> depth--

                // The `::` of `{$a|cat:Foo::BAR}` reaches a class member and separates nothing;
                // counted as two parameters it would report a one-parameter modifier as taking
                // three. Skipping the second colon keeps the pair out of the count entirely.
                character == ':' && text.getOrNull(index + 1) == ':' -> index++
                character == ':' && depth == 0 -> parameters++
            }
            index++
        }

        return parameters
    }

    // ========================================================================
    // FUNCTIONS & DECLARATIONS
    // ========================================================================

    private fun annotateFunctionCall(element: FunctionCall, holder: AnnotationHolder) {
        val group = FUNCTION_NAME.find(element.text)?.groups?.get(1) ?: return
        highlight(
            holder,
            group.rangeIn(element.textRange.startOffset),
            SmartySyntaxHighlighter.FUNCTION_CALL
        )
    }

    /** Colors the name of a `{block}` or `{function}` declaration. */
    private fun annotateDeclarationName(element: PsiElement, holder: AnnotationHolder) {
        val declaration = SmartyUtil.findDeclarationName(element) ?: return
        highlight(holder, declaration.range, SmartySyntaxHighlighter.FUNCTION_DECLARATION)
    }

    // ========================================================================
    // TEMPLATE REFERENCES
    // ========================================================================

    /**
     * Reports `{include}`, `{extends}` and `{insert}` targets that cannot be found in the
     * project. Dynamic paths and non file resources (`string:`, `eval:`, `db:`, ...) are
     * left alone because they cannot be resolved statically.
     */
    private fun annotateTemplateReference(element: PsiElement, holder: AnnotationHolder) {
        val template = SmartyUtil.findTemplatePath(element) ?: return
        if (SmartyUtil.resolveTemplateFile(element, template.value) != null) return

        holder.newAnnotation(HighlightSeverity.WARNING, "Cannot resolve template '${template.value}'")
            .range(template.range)
            .highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL)
            .create()
    }

    // ========================================================================
    // LEAF ELEMENTS
    // ========================================================================

    private fun annotateLeaf(element: PsiElement, holder: AnnotationHolder) {
        val type = element.node?.elementType ?: return
        if (type === SmartyTypes.BAD_CHARACTER || type === TokenType.BAD_CHARACTER) {
            holder.newAnnotation(HighlightSeverity.ERROR, "Unexpected character")
                .range(element)
                .highlightType(ProblemHighlightType.GENERIC_ERROR)
                .create()
            return
        }

        annotateOrphanVariableName(element, holder)
    }

    /**
     * Colors a variable name the parser could not place.
     *
     * [annotateVariable] needs a `Variable` node, and inside a tag the grammar rejects there is
     * none - the name ends up as a bare leaf under a [com.intellij.psi.PsiErrorElement]. The `$`
     * in front of it is colored lexically either way, which is what made a broken tag show a
     * colored `$` next to a plain name. Smarty allows no space after the `$`, so the leaf
     * immediately behind one is the name and nothing else can be.
     */
    private fun annotateOrphanVariableName(element: PsiElement, holder: AnnotationHolder) {
        if (element.parent is Variable) return
        if (!BARE_NAME.matches(element.text)) return
        if (PsiTreeUtil.prevLeaf(element)?.node?.elementType !== SmartyTypes.DOLLAR) return

        val attributes = if (element.text.lowercase() in SmartyBuiltins.RESERVED_VARIABLES) {
            SmartySyntaxHighlighter.RESERVED_VARIABLE
        } else {
            SmartySyntaxHighlighter.VARIABLE
        }
        highlight(holder, element.textRange, attributes)
    }

    // ========================================================================
    // UTILITIES
    // ========================================================================

    private fun highlight(holder: AnnotationHolder, range: TextRange, attributes: TextAttributesKey) {
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(range)
            .textAttributes(attributes)
            .create()
    }

    /** Converts a match inside an element's text into an absolute [TextRange]. */
    private fun MatchGroup.rangeIn(offset: Int): TextRange =
        TextRange(offset + range.first, offset + range.last + 1)

    private fun MatchResult.rangeIn(offset: Int): TextRange =
        TextRange(offset + range.first, offset + range.last + 1)

    companion object {
        /** Sentinel for "currently not inside a string literal"; cannot occur in source. */
        private const val NO_QUOTE = '\u0000'

        private val VARIABLE_NAME = Regex("""\$([a-zA-Z_]\w*)""")

        /** A whole leaf that could be a variable name; used by [annotateOrphanVariableName]. */
        private val BARE_NAME = Regex("""[a-zA-Z_]\w*""")

        /** The optional `@` is the array-modifier prefix of `{$rows|@count}`. */
        private val MODIFIER_NAME = Regex("""^\|\s*@?\s*([a-zA-Z_]\w*)""")
        private val FUNCTION_NAME = Regex("""^([a-zA-Z_]\w*)\s*\(""")
    }
}
