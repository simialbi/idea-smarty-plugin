package ch.erlebnisbank.smarty

import com.intellij.formatting.Alignment
import com.intellij.formatting.Wrap
import com.intellij.formatting.templateLanguages.DataLanguageBlockWrapper
import com.intellij.formatting.templateLanguages.TemplateLanguageBlock
import com.intellij.formatting.templateLanguages.TemplateLanguageFormattingModelBuilder
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CodeStyleSettings

/**
 * Entry point for `Code | Reformat Code` in a `.tpl` file, registered as `com.intellij.lang.formatter`.
 *
 * The base class does the part that only a template language needs: it asks the data language for
 * *its* formatting model, unpacks that model's blocks into [DataLanguageBlockWrapper]s and passes
 * them in here as foreign children. [SmartyBlock] then merges them with the Smarty blocks by
 * offset, so one block tree spans both roots and `Reformat Code` indents markup and tags together.
 *
 * `dontFormatMyModel()` is inherited as `true`. It is a statement about the other direction: if
 * some other template language ever named Smarty as *its* data language, that language should
 * format the file, not this builder.
 */
internal class SmartyFormattingModelBuilder : TemplateLanguageFormattingModelBuilder() {

    override fun createTemplateLanguageBlock(
        node: ASTNode,
        wrap: Wrap?,
        alignment: Alignment?,
        foreignChildren: MutableList<DataLanguageBlockWrapper>?,
        codeStyleSettings: CodeStyleSettings,
    ): TemplateLanguageBlock =
        SmartyBlock(node, wrap, alignment, this, codeStyleSettings, foreignChildren)
}
