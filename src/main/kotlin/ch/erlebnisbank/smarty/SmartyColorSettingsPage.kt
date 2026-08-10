package ch.erlebnisbank.smarty

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import com.intellij.openapi.util.NlsContexts
import org.jetbrains.annotations.NonNls
import javax.swing.Icon

class SmartyColorSettingsPage : ColorSettingsPage {
    companion object {
        private val DESCRIPTORS: Array<AttributesDescriptor> = arrayOf<AttributesDescriptor>(
            AttributesDescriptor("Operators", SmartySyntaxHighlighter.OPERATORS),
            AttributesDescriptor("Keys", SmartySyntaxHighlighter.KEY),
            AttributesDescriptor("Variables", SmartySyntaxHighlighter.VARIABLE),
            AttributesDescriptor("Comments", SmartySyntaxHighlighter.COMMENT),
            AttributesDescriptor("Constants", SmartySyntaxHighlighter.CONSTANT),
            AttributesDescriptor("Parentheses", SmartySyntaxHighlighter.PARENTHESES),
            AttributesDescriptor("Brackets", SmartySyntaxHighlighter.BRACKETS),
            AttributesDescriptor("Numbers", SmartySyntaxHighlighter.NUMBERS),
            AttributesDescriptor("Strings", SmartySyntaxHighlighter.STRING)
        )
    }

    override fun getIcon(): Icon {
        return SmartyIcons.FILE;
    }

    override fun getHighlighter(): SyntaxHighlighter {
        return SmartySyntaxHighlighter()
    }

    override fun getDemoText(): @NonNls String {
        return """
            {* replace each carriage return, tab and new line with a space *}

            {${'$'}articleTitle}
            {${'$'}articleTitle|regex_replace:"/[\r\t\n]/":" "}

            {config_load file="colors.conf"}

            {include file="header.tpl"}

            {if ${'$'}logged_in}
                Welcome, <span style="color:{#fontColor#}">{${'$'}name}!</span>
            {else}
                hi, {${'$'}name}
            {/if}
            
            {foreach ${'$'}res as ${'$'}r} 
                {if ${'$'}r@index eq 3}
                    {continue}
                {/if}
                {${'$'}r.id} 
                {${'$'}r.name}
            {foreachelse}
              .. no results .. 
            {/foreach}
            
            {${'$'}myVar|regex_replace:"/foo/":"bar"}

            {include file="footer.tpl"}
        """;
    }

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String?, TextAttributesKey?>? {
        return null;
    }

    override fun getAttributeDescriptors(): Array<out AttributesDescriptor?> {
        return DESCRIPTORS;
    }

    override fun getColorDescriptors(): Array<out ColorDescriptor?> {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    override fun getDisplayName(): @NlsContexts.ConfigurableName String {
        return "Smarty";
    }
}
