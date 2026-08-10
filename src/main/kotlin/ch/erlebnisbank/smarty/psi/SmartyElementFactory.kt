package ch.erlebnisbank.smarty.psi

import ch.erlebnisbank.smarty.SmartyFileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.PsiTreeUtil

/**
 * Builds Smarty PSI from text.
 *
 * Rename cannot edit a leaf in place - PSI is immutable - so `setName` parses a throwaway
 * template holding the new name and swaps the resulting node in. Everything here therefore
 * depends on `SmartyParserDefinition` being registered.
 */
object SmartyElementFactory {

    /** Parses [text] as a Smarty template that is never written to disk. */
    fun createFile(project: Project, text: String): SmartyFile =
        PsiFileFactory.getInstance(project)
            .createFileFromText(DUMMY_FILE_NAME, SmartyFileType.INSTANCE, text) as SmartyFile

    /**
     * Builds a `{block name=…}` whose name node can be lifted out as a replacement.
     *
     * @param nameLiteral the name exactly as it should appear in the source - bare for
     *        `{block name=content}`, quoted for `{block name="content"}`
     */
    fun createBlockStatement(project: Project, nameLiteral: String): BlockStatement? =
        PsiTreeUtil.findChildOfType(
            createFile(project, "{block name=$nameLiteral}"),
            BlockStatement::class.java
        )

    private const val DUMMY_FILE_NAME = "dummy.tpl"
}
