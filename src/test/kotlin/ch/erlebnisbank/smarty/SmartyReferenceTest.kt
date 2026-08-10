package ch.erlebnisbank.smarty

import ch.erlebnisbank.smarty.psi.BlockStatement
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil

/**
 * Covers what the reference contributor and the named elements are for: resolving, renaming and
 * keeping both sides of an inheritance pair in step.
 */
class SmartyReferenceTest : SmartyTestCase() {

    // ========================================================================
    // TEMPLATE REFERENCES
    // ========================================================================

    fun testIncludeResolvesToTemplate() {
        val header = myFixture.addFileToProject("parts/header.tpl", "<h1>Header</h1>")
        val page = myFixture.addFileToProject("page.tpl", "{include file=\"parts/header.tpl\"}")

        val target = page.findReferenceAt(page.text.indexOf("parts/"))?.resolve()

        assertEquals(header, target)
    }

    fun testUnresolvableTemplateHasNoTarget() {
        myFixture.configureByText("page.tpl", "{include file=\"nope<caret>.tpl\"}")

        assertNull(myFixture.getReferenceAtCaretPosition()?.resolve())
    }

    fun testDynamicPathGetsNoReference() {
        myFixture.configureByText("page.tpl", "{include file=\"\$dir<caret>/header.tpl\"}")

        assertNull(myFixture.getReferenceAtCaretPosition()?.resolve())
    }

    // ========================================================================
    // BLOCK REFERENCES
    // ========================================================================

    fun testBlockResolvesToOverriddenBlock() {
        myFixture.addFileToProject("layout.tpl", "{block name=\"content\"}{/block}")
        myFixture.configureByText(
            "page.tpl",
            "{extends file=\"layout.tpl\"}\n{block name=\"con<caret>tent\"}{/block}"
        )

        val target = myFixture.getReferenceAtCaretPosition()?.resolve()

        assertTrue("expected a block, got $target", target is BlockStatement)
        assertEquals("layout.tpl", target!!.containingFile.name)
    }

    // ========================================================================
    // NAMED ELEMENTS
    // ========================================================================

    fun testGetNameReadsTheAttributeForm() {
        val file = myFixture.configureByText("page.tpl", "{block name=\"content\"}{/block}")

        assertEquals("content", blockOf(file).name)
    }

    fun testGetNameReadsTheBareForm() {
        val file = myFixture.configureByText("page.tpl", "{block content}{/block}")

        assertEquals("content", blockOf(file).name)
    }

    fun testSetNameKeepsTheQuotes() {
        val file = myFixture.configureByText("page.tpl", "{block name=\"content\"}{/block}")

        rename(file, "main")

        assertEquals("{block name=\"main\"}{/block}", file.text)
    }

    fun testSetNameKeepsTheBareForm() {
        val file = myFixture.configureByText("page.tpl", "{block content}{/block}")

        rename(file, "main")

        assertEquals("{block main}{/block}", file.text)
    }

    /** A name that would not lex as an identifier has to be quoted, even if the original was not. */
    fun testSetNameQuotesWhenItHasTo() {
        val file = myFixture.configureByText("page.tpl", "{block content}{/block}")

        rename(file, "main-content")

        assertEquals("{block \"main-content\"}{/block}", file.text)
    }

    private fun blockOf(file: PsiFile): BlockStatement =
        checkNotNull(PsiTreeUtil.findChildOfType(file, BlockStatement::class.java)) {
            "no block statement in:\n${file.text}"
        }

    private fun rename(file: PsiFile, newName: String) {
        WriteCommandAction.runWriteCommandAction(project) { blockOf(file).setName(newName) }
    }
}
