package ch.erlebnisplus.smarty

import ch.erlebnisplus.smarty.psi.BlockStatement
import ch.erlebnisplus.smarty.psi.SmartyUtil
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.PsiPolyVariantReferenceBase
import com.intellij.psi.ResolveResult
import com.intellij.psi.impl.source.tree.Factory
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
    PsiPolyVariantReferenceBase<PsiElement>(element, rangeInElement, true) {

    /**
     * Rewrites the part of the leaf this reference covers.
     *
     * `PsiReferenceBase` would delegate this to an `ElementManipulator`, but a manipulator can
     * only be registered for a PSI class, and Smarty leaves are plain platform leaves shared
     * with every other language - registering for those would be far too broad. Rebuilding the
     * single leaf here keeps the change scoped to Smarty.
     */
    override fun handleElementRename(newElementName: String): PsiElement = replaceRangeWith(newElementName)

    /**
     * Rewrites the covered range by rebuilding the single leaf that holds it. The reference is
     * anchored on the statement, so the leaf has to be located first.
     */
    protected fun replaceRangeWith(text: String): PsiElement {
        val host = element
        val absolute = rangeInElement.shiftRight(host.textRange.startOffset)

        val leaf = host.containingFile?.findElementAt(absolute.startOffset) ?: return host
        val withinLeaf = absolute.shiftLeft(leaf.textRange.startOffset)
        if (withinLeaf.endOffset > leaf.textLength) return host

        val replacement = Factory.createSingleLeafElement(
            leaf.node.elementType,
            withinLeaf.replace(leaf.text, text),
            null,
            leaf.manager
        )
        leaf.node.treeParent.replaceChild(leaf.node, replacement)
        return host
    }
}

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

    /**
     * Renaming a template hands over the bare file name, but the reference holds a path. The
     * directory is kept so that `{include file="parts/header.tpl"}` becomes
     * `parts/footer.tpl` rather than a broken `footer.tpl`.
     */
    override fun handleElementRename(newElementName: String): PsiElement {
        val directory = path.substringBeforeLast('/', "")
        return replaceRangeWith(if (directory.isEmpty()) newElementName else "$directory/$newElementName")
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
