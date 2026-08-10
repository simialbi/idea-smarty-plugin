package ch.erlebnisbank.smarty

import ch.erlebnisbank.smarty.psi.BlockStatement
import ch.erlebnisbank.smarty.psi.ExtendsStatement
import ch.erlebnisbank.smarty.psi.IncludeStatement
import ch.erlebnisbank.smarty.psi.InsertStatement
import ch.erlebnisbank.smarty.psi.SmartyTypes
import ch.erlebnisbank.smarty.psi.SmartyUtil
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.PsiReferenceRegistrar
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext

/**
 * Contributes the references of a Smarty template, which is what turns Ctrl+Click, Find Usages
 * and reference completion on:
 *
 * ```
 * {include file="parts/header.tpl"}   -> the template file
 * {extends file="layout.tpl"}         -> the template file
 * {block name="content"}              -> the blocks it overrides in the inherited templates
 * ```
 *
 * Read more: https://plugins.jetbrains.com/docs/intellij/reference-contributor.html
 */
class SmartyReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        val provider = SmartyReferenceProvider()

        // A path or a block name is written either quoted - {include file="x.tpl"} - or bare -
        // {block content} - so the provider has to see both token types.
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(SmartyTypes.STRING), provider)
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(SmartyTypes.IDENTIFIER), provider)
    }
}

private class SmartyReferenceProvider : PsiReferenceProvider() {

    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        val statement = PsiTreeUtil.getParentOfType(
            element,
            IncludeStatement::class.java,
            ExtendsStatement::class.java,
            InsertStatement::class.java,
            BlockStatement::class.java
        ) ?: return PsiReference.EMPTY_ARRAY

        return when (statement) {
            is BlockStatement -> blockReference(element, statement)
            else -> templateReference(element, statement)
        }
    }

    private fun templateReference(element: PsiElement, statement: PsiElement): Array<PsiReference> {
        val template = SmartyUtil.findTemplatePath(statement) ?: return PsiReference.EMPTY_ARRAY
        val range = rangeIn(element, template.range) ?: return PsiReference.EMPTY_ARRAY
        return arrayOf(SmartyTemplateReference(element, range, template.value))
    }

    private fun blockReference(element: PsiElement, statement: BlockStatement): Array<PsiReference> {
        val declaration = SmartyUtil.findDeclarationName(statement) ?: return PsiReference.EMPTY_ARRAY
        val range = rangeIn(element, declaration.range) ?: return PsiReference.EMPTY_ARRAY
        return arrayOf(SmartyBlockReference(element, range, declaration.value))
    }

    /**
     * Converts an absolute range into one relative to [element], which is what
     * `PsiReferenceBase` expects.
     *
     * This is also what keeps the reference off the wrong string: the provider is called for
     * every string of the statement, but only the one actually holding the path or the name
     * contains the range, so `title` in `{include file="x.tpl" title="Hello"}` is skipped.
     *
     * @return the relative range, or `null` when the range is not inside this element
     */
    private fun rangeIn(element: PsiElement, absolute: TextRange): TextRange? {
        val relative = absolute.shiftLeft(element.textRange.startOffset)
        if (relative.startOffset < 0 || relative.endOffset > element.textLength) return null
        return relative
    }
}
