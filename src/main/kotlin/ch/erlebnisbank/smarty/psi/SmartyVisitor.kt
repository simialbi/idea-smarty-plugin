// This is a generated file. Not intended for manual editing.
package ch.erlebnisbank.smarty.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor

open class SmartyVisitor : PsiElementVisitor() {
    fun visitProperty(o: SmartyProperty) {
        visitNamedElement(o)
    }

    fun visitNamedElement(o: SmartyNamedElement) {
        visitPsiElement(o)
    }

    fun visitPsiElement(o: PsiElement) {
        visitElement(o)
    }
}
