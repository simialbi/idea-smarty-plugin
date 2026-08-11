package ch.erlebnisplus.smarty.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry

/**
 * Base class for the elements a [com.intellij.psi.PsiReferenceContributor] may attach references
 * to, used as the `mixin=` of the corresponding rules in `Smarty.bnf`.
 *
 * Contributed references are not picked up automatically: the default `getReferences()` only
 * returns whatever `getReference()` yields, so an element has to ask the registry itself. The
 * platform's own PSI does exactly this - `PsiLiteralExpressionImpl` is what makes the reference
 * contributor work in the SDK tutorial - and Smarty needs the same opt-in.
 */
abstract class SmartyReferenceHostImpl(node: ASTNode) : ASTWrapperPsiElement(node) {

    override fun getReferences(): Array<PsiReference> =
        ReferenceProvidersRegistry.getReferencesFromProviders(this)
}
