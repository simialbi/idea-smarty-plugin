package ch.erlebnisbank.smarty

import ch.erlebnisbank.smarty.psi.*
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType

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
     * Colors `$name` and every `.property` / `->property` of the access chain. The Smarty
     * super global `$smarty` gets its own attributes so that `{$smarty.foreach.row.index}`
     * is distinguishable from a template variable.
     */
    private fun annotateVariable(element: Variable, holder: AnnotationHolder) {
        val text = element.text
        val offset = element.textRange.startOffset

        val name = VARIABLE_NAME.find(text) ?: return
        val attributes = if (name.groupValues[1].lowercase() in SmartyBuiltins.RESERVED_VARIABLES) {
            SmartySyntaxHighlighter.RESERVED_VARIABLE
        } else {
            SmartySyntaxHighlighter.VARIABLE
        }
        highlight(holder, name.rangeIn(offset), attributes)

        for (property in PROPERTY_ACCESS.findAll(text)) {
            val group = property.groups[1] ?: continue
            highlight(holder, group.rangeIn(offset), SmartySyntaxHighlighter.PROPERTY)
        }
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
        }
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
        private val PROPERTY_ACCESS = Regex("""(?:\.|->)([a-zA-Z_]\w*)""")
        private val MODIFIER_NAME = Regex("""^\|\s*([a-zA-Z_]\w*)""")
        private val FUNCTION_NAME = Regex("""^([a-zA-Z_]\w*)\s*\(""")
    }
}
