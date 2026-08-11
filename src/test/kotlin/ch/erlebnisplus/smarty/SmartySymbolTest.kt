package ch.erlebnisplus.smarty

import com.intellij.navigation.NavigationItem
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.Processor
import com.intellij.util.indexing.FindSymbolParameters

/**
 * Coverage of Navigate | Symbol. The contributor is driven directly rather than through the
 * chooser popup: the popup adds pattern matching and sorting of its own, and what has to hold
 * here is that the contributor offers the right names and the right elements behind them.
 */
class SmartySymbolTest : SmartyTestCase() {

    fun testContributesBlockAndFunctionNames() {
        myFixture.addFileToProject("layout.tpl", "{block name=\"content\"}{/block}")
        myFixture.addFileToProject("macros.tpl", "{function name=\"renderBox\"}{/function}")

        val names = names()

        assertContainsElements(names, "content", "renderBox")
    }

    /** The bare spelling declares a name just as the attribute one does. */
    fun testContributesBareDeclaration() {
        myFixture.addFileToProject("layout.tpl", "{block content}{/block}")

        assertContainsElements(names(), "content")
    }

    /** A declaration is nested several rules deep, never a direct child of the file. */
    fun testFindsNestedDeclaration() {
        myFixture.addFileToProject(
            "layout.tpl",
            """
            {if ${'$'}showSidebar}
              {block name="sidebar"}{/block}
            {/if}
            """.trimIndent()
        )

        assertContainsElements(names(), "sidebar")
    }

    fun testPlainTemplateContributesNothing() {
        myFixture.addFileToProject("static.tpl", "<h1>Header</h1>\n{include file=\"nav.tpl\"}")

        assertEmpty(names())
    }

    fun testResolvesNameToItsDeclaration() {
        myFixture.addFileToProject("layout.tpl", "{block name=\"content\"}{/block}")
        myFixture.addFileToProject("macros.tpl", "{function name=\"renderBox\"}{/function}")

        val items = itemsNamed("content")

        assertEquals(1, items.size)
        assertEquals("layout.tpl", items.single().containingFile.name)
    }

    /** Overriding a block declares the name twice, and both have to be offered. */
    fun testOverriddenBlockIsListedTwice() {
        myFixture.addFileToProject("layout.tpl", "{block name=\"content\"}{/block}")
        myFixture.addFileToProject(
            "page.tpl",
            "{extends file=\"layout.tpl\"}\n{block name=\"content\"}{/block}"
        )

        val files = itemsNamed("content").map { item -> item.containingFile.name }.toSet()

        assertEquals(setOf("layout.tpl", "page.tpl"), files)
    }

    fun testUnknownNameResolvesToNothing() {
        myFixture.addFileToProject("layout.tpl", "{block name=\"content\"}{/block}")

        assertEmpty(itemsNamed("sidebar"))
    }

    /** What the chooser renders for each entry: the name, the file it is in, and an icon. */
    fun testPresentationIdentifiesTheDeclaration() {
        myFixture.addFileToProject("layout.tpl", "{block name=\"content\"}{/block}")

        val presentation = checkNotNull(itemsNamed("content").single().presentation)

        assertEquals("content", presentation.presentableText)
        assertEquals("layout.tpl", presentation.locationString)
        assertNotNull(presentation.getIcon(false))
    }

    /** A processor that stops early has to be obeyed, or the chooser cannot cut a sweep short. */
    fun testStopsWhenTheProcessorIsDone() {
        myFixture.addFileToProject("layout.tpl", "{block name=\"content\"}{/block}")
        myFixture.addFileToProject("macros.tpl", "{function name=\"renderBox\"}{/function}")

        val seen = mutableListOf<String>()
        contributor.processNames(
            Processor { name -> seen.add(name); false },
            GlobalSearchScope.allScope(project),
            null
        )

        assertEquals(1, seen.size)
    }

    private val contributor = SmartyChooseByNameContributor()

    private fun names(): List<String> {
        val result = mutableListOf<String>()
        contributor.processNames(
            Processor { name -> result.add(name) },
            GlobalSearchScope.allScope(project),
            null
        )
        return result
    }

    private fun itemsNamed(name: String): List<NavigationItem> {
        val result = mutableListOf<NavigationItem>()
        contributor.processElementsWithName(
            name,
            Processor { item -> result.add(item) },
            FindSymbolParameters.simple(project, true)
        )
        return result
    }

    private val NavigationItem.containingFile
        get() = (this as com.intellij.psi.PsiElement).containingFile
}
