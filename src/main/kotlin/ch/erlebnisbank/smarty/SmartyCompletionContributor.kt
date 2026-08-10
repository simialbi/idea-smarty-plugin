package ch.erlebnisbank.smarty

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionInitializationContext
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.completion.CompletionUtilCore
import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext

/**
 * Completes Smarty tags, their attributes, modifiers and variables inside `{...}`.
 *
 * What is offered depends on where the caret sits, see [SmartyTagContext]:
 *
 * ```
 * {fo<caret>              -> {foreach}, {for}, {function} ...
 * {/<caret>               -> the block tags that are still open, innermost first
 * {include <caret>        -> file, assign, scope ...
 * {$user|<caret>          -> upper, truncate, date_format ...
 * {$<caret>               -> the variables used or assigned in this template
 * {$smarty.<caret>        -> get, post, foreach, now ...
 * ```
 *
 * Read more: https://plugins.jetbrains.com/docs/intellij/completion-contributor.html
 */
class SmartyCompletionContributor : CompletionContributor() {

    init {
        // The language is already narrowed by the completion.contributor registration, so the
        // pattern only has to accept the leaf under the caret. The interesting part - what may
        // appear at this position - is decided by the provider.
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(), SmartyCompletionProvider())
    }

    /**
     * The platform splices a dummy identifier into the file copy at the caret so that a half
     * typed construct still parses. Its default value ends with a space, which inside `{...}`
     * turns `{inc` into `{inc IntellijIdeaRulezzz }` and closes the tag. The trimmed variant
     * keeps the tag open.
     */
    override fun beforeCompletion(context: CompletionInitializationContext) {
        context.dummyIdentifier = CompletionUtilCore.DUMMY_IDENTIFIER_TRIMMED
    }
}

/** What the caret is completing. */
private enum class SmartyCompletionKind { TAG, CLOSING_TAG, ATTRIBUTE, MODIFIER, VARIABLE, SMARTY_PROPERTY }

/**
 * The position of the caret inside a `{...}` tag.
 *
 * This is derived from the text in front of the caret rather than from the PSI tree: while
 * completion is being invoked the tag is usually still incomplete, so the parse of the
 * surrounding tag cannot be relied on.
 *
 * @property kind what may be inserted here
 * @property prefix what has been typed of it so far
 * @property tagName the name the enclosing tag starts with, empty when it is still being typed
 */
private data class SmartyTagContext(
    val kind: SmartyCompletionKind,
    val prefix: String,
    val tagName: String
) {
    companion object {
        /** Guards against treating a stray `{` in CSS or JavaScript as the start of a tag. */
        private const val MAX_TAG_LENGTH = 200

        fun at(parameters: CompletionParameters): SmartyTagContext? {
            val text = parameters.position.containingFile.text
            val offset = parameters.offset.coerceIn(0, text.length)

            val open = text.lastIndexOf('{', offset - 1)
            if (open < 0) return null
            if (text.lastIndexOf('}', offset - 1) > open) return null
            if (text.startsWith("{*", open)) return null

            val typed = text.substring(open + 1, offset)
            if (typed.length > MAX_TAG_LENGTH || typed.contains('\n') || typed.contains('{')) return null

            val prefix = typed.takeLastWhile(::isNamePart)
            val before = typed.dropLast(prefix.length)
            val tagName = typed.trimStart().trimStart('/').takeWhile(::isNamePart)

            return when {
                before.isEmpty() -> SmartyTagContext(SmartyCompletionKind.TAG, prefix, tagName)
                before == "/" -> SmartyTagContext(SmartyCompletionKind.CLOSING_TAG, prefix, tagName)
                before.endsWith('|') -> SmartyTagContext(SmartyCompletionKind.MODIFIER, prefix, tagName)
                before.endsWith('$') -> SmartyTagContext(SmartyCompletionKind.VARIABLE, prefix, tagName)
                before.endsWith('.') -> smartyProperty(before, prefix, tagName)
                before.last().isWhitespace() -> SmartyTagContext(SmartyCompletionKind.ATTRIBUTE, prefix, tagName)
                else -> null
            }
        }

        /** Only `$smarty` has statically known keys; the properties of anything else are unknown. */
        private fun smartyProperty(before: String, prefix: String, tagName: String): SmartyTagContext? {
            val chain = before.dropLast(1).takeLastWhile { isNamePart(it) || it == '$' }
            if (!chain.equals("\$smarty", ignoreCase = true)) return null
            return SmartyTagContext(SmartyCompletionKind.SMARTY_PROPERTY, prefix, tagName)
        }
    }
}

private class SmartyCompletionProvider : CompletionProvider<CompletionParameters>() {

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        val tag = SmartyTagContext.at(parameters) ?: return

        // The prefix is read from the tag text, not from the leaf under the caret, so the
        // result set has to be told about it or nothing would match.
        val matching = result.withPrefixMatcher(tag.prefix)

        when (tag.kind) {
            SmartyCompletionKind.TAG -> {
                addTags(matching)
                addClosingTags(parameters, matching, slash = true)
            }

            SmartyCompletionKind.CLOSING_TAG -> addClosingTags(parameters, matching, slash = false)
            SmartyCompletionKind.ATTRIBUTE -> addAttributes(tag, matching)
            SmartyCompletionKind.MODIFIER -> addModifiers(matching)
            SmartyCompletionKind.VARIABLE -> addVariables(parameters, matching)
            SmartyCompletionKind.SMARTY_PROPERTY -> addSmartyProperties(matching)
        }
    }

    private fun addTags(result: CompletionResultSet) {
        for ((name, tag) in SmartyBuiltins.TAGS) {
            var element = LookupElementBuilder.create(name)
                .withIcon(AllIcons.Nodes.Function)
                .withTypeText(tag.description, true)
            if (tag.block) {
                element = element.withTailText(" … {/$name}", true)
            }
            result.addElement(element)
        }
    }

    /**
     * Offers the block tags that are still open at the caret, innermost first, so that
     * `{/` completes to the tag that actually needs closing.
     *
     * @param slash whether the `/` still has to be inserted
     */
    private fun addClosingTags(parameters: CompletionParameters, result: CompletionResultSet, slash: Boolean) {
        val text = parameters.position.containingFile.text
        val open = openBlockTags(text, parameters.offset.coerceIn(0, text.length))

        for ((index, name) in open.withIndex()) {
            val element = LookupElementBuilder.create(if (slash) "/$name" else name)
                .withIcon(AllIcons.Nodes.Function)
                .withTypeText("close {$name}", true)
            // The innermost open tag is the most likely one, so it goes to the top.
            result.addElement(PrioritizedLookupElement.withPriority(element, (open.size - index).toDouble()))
        }
    }

    /**
     * Reconstructs the stack of unclosed block tags by scanning the text in front of the caret.
     *
     * @return the open tags, innermost first
     */
    private fun openBlockTags(text: String, offset: Int): List<String> {
        val stack = ArrayDeque<String>()

        for (match in TAG_START.findAll(text.substring(0, offset))) {
            val name = match.groupValues[2].lowercase()
            if (name !in SmartyBuiltins.BLOCK_TAGS) continue

            if (match.groupValues[1] == "/") {
                if (stack.lastOrNull() == name) stack.removeLast()
            } else {
                stack.addLast(name)
            }
        }

        return stack.reversed()
    }

    private fun addAttributes(context: SmartyTagContext, result: CompletionResultSet) {
        val tag = SmartyBuiltins.TAGS[context.tagName.lowercase()] ?: return

        for (attribute in tag.attributes) {
            result.addElement(
                LookupElementBuilder.create(attribute)
                    .withIcon(AllIcons.Nodes.Parameter)
                    .withTypeText(context.tagName, true)
                    .withInsertHandler(APPEND_EQUALS)
            )
        }
    }

    private fun addModifiers(result: CompletionResultSet) {
        for ((name, parameters) in SmartyBuiltins.MODIFIERS) {
            val description = if (parameters == 0) "modifier" else "modifier, up to $parameters parameters"
            result.addElement(
                LookupElementBuilder.create(name)
                    .withIcon(AllIcons.Nodes.Method)
                    .withTypeText(description, true)
            )
        }
    }

    /**
     * Offers every variable the template uses or assigns. Smarty variables are handed in from
     * PHP, so the template itself is the only place they can be discovered.
     */
    private fun addVariables(parameters: CompletionParameters, result: CompletionResultSet) {
        val text = parameters.originalFile.text
        val names = sortedSetOf<String>()

        VARIABLE_USAGE.findAll(text).mapTo(names) { it.groupValues[1] }
        ASSIGNED_NAME.findAll(text).mapTo(names) { it.groupValues[1] }
        names.addAll(SmartyBuiltins.RESERVED_VARIABLES)

        for (name in names) {
            val reserved = name in SmartyBuiltins.RESERVED_VARIABLES
            result.addElement(
                LookupElementBuilder.create(name)
                    .withIcon(if (reserved) AllIcons.Nodes.Constant else AllIcons.Nodes.Variable)
                    .withTypeText(if (reserved) "Smarty super global" else "variable", true)
            )
        }
    }

    private fun addSmartyProperties(result: CompletionResultSet) {
        for (property in SmartyBuiltins.SMARTY_PROPERTIES) {
            result.addElement(
                LookupElementBuilder.create(property)
                    .withIcon(AllIcons.Nodes.Field)
                    .withTypeText("\$smarty", true)
            )
        }
    }

    private companion object {
        private val TAG_START = Regex("""\{\s*(/?)\s*([a-zA-Z_]\w*)""")
        private val VARIABLE_USAGE = Regex("""\$([a-zA-Z_]\w*)""")
        private val ASSIGNED_NAME =
            Regex("""\b(?:var|item|key|name)\s*=\s*["']?([a-zA-Z_]\w*)""", RegexOption.IGNORE_CASE)

        /** Attributes are always followed by a value, so the `=` is inserted right away. */
        private val APPEND_EQUALS = InsertHandler<LookupElement> { context, _ ->
            val document = context.document
            val offset = context.tailOffset
            if (offset >= document.textLength || document.charsSequence[offset] != '=') {
                document.insertString(offset, "=")
            }
            context.editor.caretModel.moveToOffset(offset + 1)
        }
    }
}

private fun isNamePart(character: Char): Boolean = character.isLetterOrDigit() || character == '_'
