package ch.erlebnisbank.smarty

import com.intellij.application.options.colors.highlighting.HighlightData
import com.intellij.application.options.colors.highlighting.HighlightsExtractor
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.util.PsiTreeUtil

/**
 * The colour settings page, checked against its own sample.
 *
 * Two things can go wrong here and neither shows up anywhere else. A marker that is not in the tag
 * map is not stripped, so `<var>` appears verbatim in the preview; and a descriptor that the sample
 * never reaches is a colour nobody can see before they pick it. Both are asserted below, using the
 * platform's own extractor so that the preview is measured the way it is built.
 */
class SmartyColorSettingsPageTest : SmartyTestCase() {

    fun testTheSampleIsATemplateThatParses() {
        val sample = strippedSample()
        val file = myFixture.configureByText("demo.tpl", sample)
        val error = PsiTreeUtil.findChildOfType(file, PsiErrorElement::class.java)

        assertNull(
            "the sample has to be valid Smarty - ${error?.errorDescription} at offset " +
                    "${error?.textRange?.startOffset} of:\n$sample",
            error
        )
    }

    /** Every marker is a tag the platform knows, so nothing of the markup survives into the view. */
    fun testEveryMarkerIsStripped() {
        val sample = strippedSample()
        val leftover = Regex("</?[a-zA-Z_]\\w*>").findAll(sample)
            .map { it.value }
            .filter { it.trim('<', '/', '>') in PAGE.additionalHighlightingTagToDescriptorMap }
            .toList()

        assertEquals("markers left in the sample", emptyList<String>(), leftover)
    }

    fun testEveryMarkerHasADescriptor() {
        val declared = descriptorKeys()
        for ((tag, key) in PAGE.additionalHighlightingTagToDescriptorMap) {
            assertTrue("<$tag> uses ${key.externalName}, which has no descriptor", key in declared)
        }
    }

    /**
     * The other direction, and the one that keeps the page honest: every colour it offers has to be
     * visible in the sample, whether the lexer produces it or a marker does.
     */
    fun testTheSampleReachesEveryDescriptor() {
        val shown = lexerKeys() + markerKeys()
        val missing = descriptorKeys().filterNot { it in shown }.map { it.externalName }

        assertEquals("descriptors the sample never shows", emptyList<String>(), missing)
    }

    /** And nothing is coloured that cannot be configured from this page. */
    fun testTheSampleShowsNothingUndeclared() {
        val declared = descriptorKeys()
        val extra = (lexerKeys() + markerKeys()).filterNot { it in declared }.map { it.externalName }

        assertEquals("keys used by the sample but not listed", emptyList<String>(), extra)
    }

    fun testDescriptorNamesAreUnique() {
        val names = PAGE.attributeDescriptors.map { it.displayName }
        assertEquals(names.distinct(), names)
    }

    // ---------------------------------------------------------------- helpers

    private fun descriptorKeys(): Set<TextAttributesKey> =
        PAGE.attributeDescriptors.map { it.key }.toSet()

    /** The sample as the preview shows it: markers removed, everything else left alone. */
    private fun strippedSample(): String =
        HighlightsExtractor(PAGE.additionalHighlightingTagToDescriptorMap)
            .extractHighlights(PAGE.demoText, mutableListOf<HighlightData>())

    private fun markerKeys(): Set<TextAttributesKey> {
        val highlights = mutableListOf<HighlightData>()
        HighlightsExtractor(PAGE.additionalHighlightingTagToDescriptorMap)
            .extractHighlights(PAGE.demoText, highlights)

        assertTrue("no marker was recognised at all", highlights.isNotEmpty())
        for (highlight in highlights) {
            assertTrue(
                "${highlight.highlightKey.externalName} covers an empty range",
                highlight.endOffset > highlight.startOffset
            )
        }
        return highlights.map { it.highlightKey }.toSet()
    }

    /** What the lexer alone makes of the sample - the only thing the preview runs. */
    private fun lexerKeys(): Set<TextAttributesKey> {
        val highlighter = SmartySyntaxHighlighter()
        val lexer = SmartyLexerAdapter()
        val keys = mutableSetOf<TextAttributesKey>()

        lexer.start(strippedSample())
        while (lexer.tokenType != null) {
            keys.addAll(highlighter.getTokenHighlights(lexer.tokenType))
            lexer.advance()
        }
        return keys
    }

    private companion object {
        private val PAGE = SmartyColorSettingsPage()
    }
}
