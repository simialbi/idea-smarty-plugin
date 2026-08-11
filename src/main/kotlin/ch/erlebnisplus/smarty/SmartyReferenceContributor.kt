package ch.erlebnisplus.smarty

import ch.erlebnisplus.smarty.psi.BlockStatement
import ch.erlebnisplus.smarty.psi.ExtendsStatement
import ch.erlebnisplus.smarty.psi.IncludeStatement
import ch.erlebnisplus.smarty.psi.InsertStatement
import ch.erlebnisplus.smarty.psi.SmartyUtil
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.PsiReferenceRegistrar
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

        // Registered on the statements rather than on the string or identifier inside them.
        // Contributed references only reach elements that ask the registry for them, which the
        // platform's leaves do not do - see SmartyReferenceHostImpl. Anchoring on the statement
        // also covers both spellings at once, {include file="x.tpl"} and {include "x.tpl"}.
        for (statement in HOSTS) {
            registrar.registerReferenceProvider(PlatformPatterns.psiElement(statement), provider)
        }
    }

    private companion object {
        private val HOSTS = listOf(
            IncludeStatement::class.java,
            ExtendsStatement::class.java,
            InsertStatement::class.java,
            BlockStatement::class.java
        )
    }
}

private class SmartyReferenceProvider : PsiReferenceProvider() {

    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> =
        when (element) {
            is BlockStatement -> blockReference(element, element)
            else -> templateReference(element, element)
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
     * Narrowing the range to just the path or the name is what keeps Ctrl+Click and Rename off
     * the rest of the tag: in `{include file="x.tpl" title="Hello"}` only `x.tpl` is covered.
     *
     * @return the relative range, or `null` when the range is not inside this element
     */
    private fun rangeIn(element: PsiElement, absolute: TextRange): TextRange? {
        val relative = absolute.shiftLeft(element.textRange.startOffset)
        if (relative.startOffset < 0 || relative.endOffset > element.textLength) return null
        return relative
    }
}
