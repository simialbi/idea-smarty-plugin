package ch.erlebnisplus.smarty

import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.psi.PsiFile

/**
 * Folding is asserted on the descriptors rather than through the editor, so the exact range and
 * placeholder of each region are pinned down.
 */
class SmartyFoldingTest : SmartyTestCase() {

    fun testFoldsMultiLineComment() {
        val regions = foldingOf(
            """
            {*
              a note
            *}
            """.trimIndent()
        )

        assertEquals(1, regions.size)
        assertEquals("{*...*}", regions.single().placeholderText)
    }

    fun testIgnoresSingleLineComment() {
        assertEmpty(foldingOf("{* a note *}"))
    }

    fun testFoldsBlockBody() {
        val text = "{block name=\"content\"}\n  <p>Hello</p>\n{/block}"

        val region = foldingOf(text).single()

        assertEquals("...", region.placeholderText)
        // Both tags stay visible; only what sits between them folds away.
        assertEquals("\n  <p>Hello</p>\n", text.substring(region.range.startOffset, region.range.endOffset))
    }

    fun testFoldsNestedBlocks() {
        val regions = foldingOf(
            """
            {block name="content"}
              {foreach ${'$'}items as ${'$'}item}
                <li>{${'$'}item}</li>
              {/foreach}
            {/block}
            """.trimIndent()
        )

        assertEquals(2, regions.size)
        // Properly nested: the outer region has to contain the inner one.
        val (outer, inner) = regions.sortedByDescending { it.range.length }
        assertTrue("outer should contain inner", outer.range.contains(inner.range))
    }

    fun testIgnoresSingleLineBlock() = assertEmpty(foldingOf("{block name=\"x\"}hi{/block}"))

    fun testIgnoresEmptyBody() = assertEmpty(foldingOf("{block name=\"x\"}{/block}"))

    /** An `{if}` closed by the wrong tag must not produce a region. */
    fun testIgnoresMismatchedTags() {
        assertEmpty(foldingOf("{if \$a}\n  text\n{/foreach}"))
    }

    fun testFoldsLiteralBlock() {
        val regions = foldingOf("{literal}\n  <b>as is</b>\n{/literal}")

        assertEquals("{literal}...{/literal}", regions.single().placeholderText)
    }

    fun testElseIsNotAnOpeningTag() {
        val regions = foldingOf("{if \$a}\n  yes\n{else}\n  no\n{/if}")

        assertEquals("only the if body folds", 1, regions.size)
    }

    private fun foldingOf(text: String): List<FoldingDescriptor> {
        val file: PsiFile = myFixture.configureByText("test.tpl", text)
        val document = checkNotNull(myFixture.getDocument(file))

        return SmartyFoldingBuilder().buildFoldRegions(file, document, false).toList()
    }
}
