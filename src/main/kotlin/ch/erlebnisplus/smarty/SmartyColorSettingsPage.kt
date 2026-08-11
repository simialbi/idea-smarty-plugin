package ch.erlebnisplus.smarty

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import com.intellij.openapi.util.NlsContexts
import org.jetbrains.annotations.NonNls
import javax.swing.Icon

/**
 * *Settings | Editor | Color Scheme | Smarty*.
 *
 * The descriptors follow the way the Smarty documentation divides the language up - comments, tags,
 * variables, modifiers - rather than the way the plugin happens to produce the colours, because that
 * is the vocabulary someone changing a colour is looking for. There is one entry per concept, and
 * every entry is reachable from the sample below.
 *
 * The sample is coloured by [SmartySyntaxHighlighter] alone: the preview builds no PSI, so nothing
 * [SmartyAnnotator] contributes would show up. That is what [getAdditionalHighlightingTagToDescriptorMap]
 * is for - the `<tag>` markers name the ranges the annotator would colour in a real file, and the
 * platform strips them out before displaying the text.
 *
 * https://smarty-php.github.io/smarty/stable/designers/language-basic-syntax/
 */
class SmartyColorSettingsPage : ColorSettingsPage {

    override fun getDisplayName(): @NlsContexts.ConfigurableName String = "Smarty"

    override fun getIcon(): Icon = SmartyIcons.FILE

    override fun getHighlighter(): SyntaxHighlighter = SmartySyntaxHighlighter()

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = DESCRIPTORS

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey> = TAGS

    override fun getDemoText(): @NonNls String = DEMO_TEXT

    private companion object {

        /**
         * `//` puts an entry under a heading in the settings tree, so the groups below are the
         * documentation's sections. Nothing is listed that no part of the plugin produces: a
         * descriptor for an unreachable key is a setting that silently does nothing.
         */
        val DESCRIPTORS = arrayOf(
            AttributesDescriptor("Comments", SmartySyntaxHighlighter.COMMENT),

            AttributesDescriptor("Tags//Delimiters", SmartySyntaxHighlighter.DELIMITERS),
            AttributesDescriptor("Tags//Builtin tag name", SmartySyntaxHighlighter.KEYWORD),

            AttributesDescriptor("Variables//Template variable", SmartySyntaxHighlighter.VARIABLE),
            AttributesDescriptor("Variables//Property", SmartySyntaxHighlighter.PROPERTY),
            AttributesDescriptor("Variables//Reserved variable", SmartySyntaxHighlighter.RESERVED_VARIABLE),
            AttributesDescriptor("Variables//Config file variable", SmartySyntaxHighlighter.CONSTANT),

            AttributesDescriptor("Modifiers//Modifier name", SmartySyntaxHighlighter.MODIFIER),

            AttributesDescriptor("Functions//Call", SmartySyntaxHighlighter.FUNCTION_CALL),
            AttributesDescriptor(
                "Functions//Block or function declaration",
                SmartySyntaxHighlighter.FUNCTION_DECLARATION
            ),

            AttributesDescriptor("Operators", SmartySyntaxHighlighter.OPERATORS),
            AttributesDescriptor("Numbers", SmartySyntaxHighlighter.NUMBERS),
            AttributesDescriptor("Strings", SmartySyntaxHighlighter.STRING),
            AttributesDescriptor("Parentheses", SmartySyntaxHighlighter.PARENTHESES),
            AttributesDescriptor("Brackets", SmartySyntaxHighlighter.BRACKETS)
        )

        /**
         * The colours no lexer can decide on its own. A `$` or a `#` is enough to tell that a name
         * follows, which is why the lexer can colour those marks, but the name after them is an
         * `IDENTIFIER` like any other until the tree says otherwise.
         */
        val TAGS = mapOf(
            "var" to SmartySyntaxHighlighter.VARIABLE,
            "prop" to SmartySyntaxHighlighter.PROPERTY,
            "reserved" to SmartySyntaxHighlighter.RESERVED_VARIABLE,
            "config" to SmartySyntaxHighlighter.CONSTANT,
            "call" to SmartySyntaxHighlighter.FUNCTION_CALL,
            "decl" to SmartySyntaxHighlighter.FUNCTION_DECLARATION
        )

        /**
         * Valid Smarty that this plugin parses, so it can be pasted into a `.tpl` file and still be
         * a working template. Modifier names such as `escape` have a token of their own, which is
         * why they need no marker.
         */
        val DEMO_TEXT = """
            {* Comments never reach the browser: they are stripped when the template renders. *}

            {config_load file="colors.conf" section="setup"}

            <h1 style="color: {#<config>headlineColor</config>#}">{#<config>pageTitle</config>#}</h1>

            {if <call>count</call>(${'$'}<var>rows</var>) gt 0 and not ${'$'}<var>hideList</var>}
              <ul>
              {foreach ${'$'}<var>rows</var> as ${'$'}<var>row</var>}
                <li>{${'$'}<var>row</var>@<prop>index</prop>}:
                    {${'$'}<var>row</var>.<prop>title</prop>|escape:"html"|truncate:40:"..."}</li>
              {foreachelse}
                <li>{#<config>emptyMessage</config>#}</li>
              {/foreach}
              </ul>
              {* the first row again, by index *}
              {${'$'}<var>rows</var>[0].<prop>id</prop>}
            {/if}

            {block name="<decl>footer</decl>"}
              <p>{${'$'}<reserved>smarty</reserved>.<prop>now</prop>|date_format:"%d.%m.%Y"}</p>
              {include file="parts/footer.tpl" nocache}
            {/block}
        """.trimIndent()
    }
}
