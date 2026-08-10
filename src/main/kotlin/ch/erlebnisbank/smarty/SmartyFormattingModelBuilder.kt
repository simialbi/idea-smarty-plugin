package ch.erlebnisbank.smarty

import com.intellij.formatting.FormattingContext
import com.intellij.formatting.FormattingModel
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.formatting.FormattingModelProvider
import com.intellij.formatting.Indent

/**
 * Entry point for `Code | Reformat Code` in a `.tpl` file, registered as `com.intellij.lang.formatter`.
 *
 * All the work is in [SmartyBlock] and [SmartySpacingRules]; both document what the formatter
 * does and, just as importantly, what it deliberately leaves alone.
 */
internal class SmartyFormattingModelBuilder : FormattingModelBuilder {

    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val settings = formattingContext.codeStyleSettings
        val root = SmartyBlock(
            formattingContext.node,
            Indent.getNoneIndent(),
            SmartySpacingRules(settings)
        )

        return FormattingModelProvider.createFormattingModelForPsiFile(
            formattingContext.containingFile,
            root,
            settings
        )
    }
}
