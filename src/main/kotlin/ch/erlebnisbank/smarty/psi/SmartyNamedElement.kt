package ch.erlebnisbank.smarty.psi

import com.intellij.navigation.NavigationItem
import com.intellij.psi.PsiNameIdentifierOwner

/**
 * A Smarty element that declares a name and can therefore be renamed, referenced, found by
 * Find Usages and jumped to from Navigate | Symbol.
 *
 * A rule opts in through the `implements=` attribute in `Smarty.bnf`; the shared behaviour comes
 * from [ch.erlebnisbank.smarty.psi.impl.SmartyNamedElementImpl], wired in with `mixin=`. The
 * members of [PsiNameIdentifierOwner] and [NavigationItem] are generated onto the PSI interface
 * from `methods=[getName setName getNameIdentifier getPresentation]` and delegate to
 * [ch.erlebnisbank.smarty.psi.impl.SmartyPsiImplUtil].
 *
 * [NavigationItem] is what [ch.erlebnisbank.smarty.SmartyChooseByNameContributor] hands to the
 * platform. The generated implementations satisfy it through `ASTWrapperPsiElement` anyway;
 * naming it here states the contract instead of leaving the contributor to cast.
 *
 * Read more: https://plugins.jetbrains.com/docs/intellij/reference-contributor.html
 */
interface SmartyNamedElement : PsiNameIdentifierOwner, NavigationItem
