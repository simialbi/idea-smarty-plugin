package ch.erlebnisbank.smarty

import ch.erlebnisbank.smarty.psi.BlockStatement
import ch.erlebnisbank.smarty.psi.SmartyUtil
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.PsiPolyVariantReferenceBase
import com.intellij.psi.ResolveResult
import com.intellij.psi.util.PsiTreeUtil

/**
 * Base class of the Smarty references.
 *
 * A single name can resolve to more than one declaration - a block may be overridden along a
 * whole inheritance chain - so the references are poly variant.
 *
 * They are also soft. Smarty looks templates up through the template directories configured in
 * PHP, which the IDE cannot see, so a path that does not resolve here is not necessarily wrong.
 * [SmartyAnnotator] reports those as a warning of its own; marking the reference soft keeps the
 * platform from reporting the same thing a second time as an error.
 */
abstract class SmartyReference(element: PsiElement, rangeInElement: TextRange) :
    PsiPolyVariantReferenceBase<PsiElement>(element, rangeInElement, true)

/**
 * The path of an `{include}`, `{extends}` or `{insert}`, resolving to the referenced template.
 *
 * @property path the template path, without the quotes and without a `file:` prefix
 */
class SmartyTemplateReference(
    element: PsiElement,
    rangeInElement: TextRange,
    private val path: String
) : SmartyReference(element, rangeInElement) {

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val target = SmartyUtil.resolveTemplate(element, path) ?: return ResolveResult.EMPTY_ARRAY
        return arrayOf<ResolveResult>(PsiElementResolveResult(target))
    }

    /** Completes a path inside the quotes with the templates of the project. */
    override fun getVariants(): Array<Any> {
        val directory = element.containingFile?.originalFile?.virtualFile?.parent

        val variants: List<Any> = SmartyUtil.getSmartyFiles(element.project).map { file ->
            // Prefer a path relative to the referring template, that is how it has to be written.
            val relative = directory?.let { VfsUtilCore.getRelativePath(file, it) } ?: file.name
            LookupElementBuilder.create(relative)
                .withIcon(AllIcons.FileTypes.Text)
                .withTypeText(file.parent?.name, true)
        }

        return variants.toTypedArray()
    }
}

/**
 * The name of a `{block}`, resolving to the blocks of the same name in the templates the
 * current one inherits from - the declarations this block overrides.
 *
 * @property name the block name
 */
class SmartyBlockReference(
    element: PsiElement,
    rangeInElement: TextRange,
    private val name: String
) : SmartyReference(element, rangeInElement) {

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val file = element.containingFile?.originalFile ?: return ResolveResult.EMPTY_ARRAY

        val results: List<ResolveResult> = SmartyUtil.findExtendedTemplates(file)
            .flatMap { template -> SmartyUtil.findBlocksByName(template, name) }
            .map { block -> PsiElementResolveResult(block) }

        return results.toTypedArray()
    }

    /** Completes a block name with the blocks that are available to override. */
    override fun getVariants(): Array<Any> {
        val file = element.containingFile?.originalFile ?: return emptyArray()

        val variants: List<Any> = SmartyUtil.findExtendedTemplates(file)
            .flatMap { template -> PsiTreeUtil.findChildrenOfType(template, BlockStatement::class.java) }
            .mapNotNull { block ->
                val declaration = SmartyUtil.findDeclarationName(block) ?: return@mapNotNull null
                LookupElementBuilder.create(declaration.value)
                    .withIcon(AllIcons.Nodes.Function)
                    .withTypeText(block.containingFile?.name, true)
            }

        return variants.toTypedArray()
    }
}
