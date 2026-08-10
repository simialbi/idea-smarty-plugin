package ch.erlebnisbank.smarty

import ch.erlebnisbank.smarty.psi.BlockStatement
import ch.erlebnisbank.smarty.psi.SmartyFile
import ch.erlebnisbank.smarty.psi.SmartyNamedElement
import ch.erlebnisbank.smarty.psi.SmartyTokenSets
import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.tree.TokenSet

/**
 * Makes Find Usages work for Smarty declarations - the block a child template overrides, and the
 * template an `{include}` or `{extends}` points at.
 *
 * The provider supplies the word scanner the platform uses to index every word in every file
 * together with its context. Resolving alone is not enough: without the index, Find Usages has
 * no candidate set to check references against.
 *
 * Read more: https://plugins.jetbrains.com/docs/intellij/find-usages-provider.html
 */
class SmartyFindUsagesProvider : FindUsagesProvider {

    /**
     * [DefaultWordsScanner] is used rather than a hand-written scanner because the platform
     * guarantees it is thread-safe.
     *
     * The literal token set is empty on purpose. Smarty writes its declarations quoted -
     * `{block name="content"}` - so strings are scanned as identifiers instead, see
     * [SmartyTokenSets.IDENTIFIERS]. Handing them over as literals would file the names under
     * `IN_STRINGS`, which the default reference search skips, and Find Usages would quietly
     * miss every override written in the common syntax.
     */
    override fun getWordsScanner(): WordsScanner = DefaultWordsScanner(
        SmartyLexerAdapter(),
        SmartyTokenSets.IDENTIFIERS,
        SmartyTokenSets.COMMENTS,
        TokenSet.EMPTY
    )

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean = psiElement is PsiNamedElement

    override fun getHelpId(psiElement: PsiElement): String? = null

    /** The category shown in the Find Usages dialog and as the group header in the tool window. */
    override fun getType(element: PsiElement): String = when (element) {
        is BlockStatement -> "block"
        is SmartyFile -> "template"
        else -> ""
    }

    /** The name shown in the dialog title, as in "Find Usages of block content". */
    override fun getDescriptiveName(element: PsiElement): String = when (element) {
        is SmartyNamedElement -> element.name.orEmpty()
        is SmartyFile -> element.name
        else -> ""
    }

    /** The label of the node in the usages tree. */
    override fun getNodeText(element: PsiElement, useFullName: Boolean): String = when (element) {
        is BlockStatement -> element.name.orEmpty()
        is SmartyFile -> if (useFullName) element.virtualFile?.path ?: element.name else element.name
        else -> ""
    }
}
