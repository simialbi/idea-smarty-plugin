package ch.erlebnisplus.smarty

import ch.erlebnisplus.smarty.psi.SmartyNamedElement
import com.intellij.lang.refactoring.RefactoringSupportProvider
import com.intellij.psi.PsiElement

/**
 * Enables in-place rename for Smarty declarations - renaming inside the editor rather than
 * through the dialog. Resolving alone does not switch this on; it has to be stated explicitly.
 *
 * Read more: https://plugins.jetbrains.com/docs/intellij/reference-contributor.html
 */
class SmartyRefactoringSupportProvider : RefactoringSupportProvider() {

    override fun isMemberInplaceRenameAvailable(elementToRename: PsiElement, context: PsiElement?): Boolean =
        elementToRename is SmartyNamedElement
}
