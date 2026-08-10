package ch.erlebnisbank.smarty

import ch.erlebnisbank.smarty.psi.BlockStatement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil

/**
 * End to end coverage of Find Usages: the word scanner has to index the declaration names, and
 * the references from [SmartyReferenceContributor] have to confirm the candidates it finds.
 */
class SmartyFindUsagesTest : SmartyTestCase() {

    fun testFindsBlockOverride() {
        val layout = myFixture.addFileToProject("layout.tpl", "{block name=\"content\"}{/block}")
        myFixture.addFileToProject(
            "page.tpl",
            "{extends file=\"layout.tpl\"}\n{block name=\"content\"}{/block}"
        )

        val usages = myFixture.findUsages(blockOf(layout))

        assertEquals("expected the child block to be a usage", 1, usages.size)
        assertEquals("page.tpl", usages.first().file?.name)
    }

    /** The bare spelling is indexed as an identifier, the quoted one as a literal. */
    fun testFindsBareBlockOverride() {
        val layout = myFixture.addFileToProject("layout.tpl", "{block content}{/block}")
        myFixture.addFileToProject("page.tpl", "{extends file=\"layout.tpl\"}\n{block content}{/block}")

        val usages = myFixture.findUsages(blockOf(layout))

        assertEquals(1, usages.size)
    }

    fun testUnrelatedBlockIsNotAUsage() {
        val layout = myFixture.addFileToProject("layout.tpl", "{block name=\"content\"}{/block}")
        myFixture.addFileToProject(
            "page.tpl",
            "{extends file=\"layout.tpl\"}\n{block name=\"sidebar\"}{/block}"
        )

        assertEmpty(myFixture.findUsages(blockOf(layout)))
    }

    fun testFindsTemplateInclude() {
        val header = myFixture.addFileToProject("header.tpl", "<h1>Header</h1>")
        myFixture.addFileToProject("page.tpl", "{include file=\"header.tpl\"}")

        val usages = myFixture.findUsages(header)

        assertEquals("expected the include to be a usage", 1, usages.size)
        assertEquals("page.tpl", usages.first().file?.name)
    }

    fun testDescriptionsAreReadable() {
        val layout = myFixture.addFileToProject("layout.tpl", "{block name=\"content\"}{/block}")
        val provider = SmartyFindUsagesProvider()
        val block = blockOf(layout)

        assertTrue(provider.canFindUsagesFor(block))
        assertEquals("block", provider.getType(block))
        assertEquals("content", provider.getDescriptiveName(block))
        assertEquals("template", provider.getType(layout))
        assertEquals("layout.tpl", provider.getDescriptiveName(layout))
    }

    private fun blockOf(file: PsiFile): BlockStatement =
        checkNotNull(PsiTreeUtil.findChildOfType(file, BlockStatement::class.java)) {
            "no block statement in:\n${file.text}"
        }
}
