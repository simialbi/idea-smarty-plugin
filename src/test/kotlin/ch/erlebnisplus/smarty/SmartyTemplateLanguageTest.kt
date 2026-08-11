package ch.erlebnisplus.smarty

import ch.erlebnisplus.smarty.psi.SmartyTypes
import com.intellij.lang.html.HTMLLanguage
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.ex.util.LayeredLexerEditorHighlighter
import com.intellij.openapi.fileTypes.FileTypeEditorHighlighterProviders
import com.intellij.psi.PsiFile
import com.intellij.psi.templateLanguages.OuterLanguageElement
import com.intellij.psi.templateLanguages.TemplateLanguageFileViewProvider
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlTag

/**
 * That a `.tpl` file really is two files.
 *
 * Everything the HTML support gives us - highlighting, completion, structure, HTML's own formatter -
 * follows from one thing: the document carries a second PSI tree, built by HTML's parser over the
 * same characters, in which every `{...}` appears as an opaque fragment. These tests pin that tree
 * and the three registrations that produce it, because a mistake in any of them fails silently:
 * the plugin keeps working, just without any HTML support, and only a human looking at a template
 * would notice.
 */
class SmartyTemplateLanguageTest : SmartyTestCase() {

    // ---------------------------------------------------------------- the two roots

    fun testTemplateHasASmartyRootAndAnHtmlRoot() {
        val provider = viewProviderOf("<div>{\$user.name}</div>")

        assertEquals(SmartyLanguage.INSTANCE, provider.baseLanguage)
        assertEquals(HTMLLanguage.INSTANCE, provider.templateDataLanguage)
        assertEquals(
            setOf(SmartyLanguage.INSTANCE, HTMLLanguage.INSTANCE),
            provider.languages,
        )
        assertNotNull("no HTML root", provider.getPsi(HTMLLanguage.INSTANCE))
        assertNotNull("no Smarty root", provider.getPsi(SmartyLanguage.INSTANCE))
    }

    /** Both roots span the whole document - neither is a fragment of it. */
    fun testBothRootsCoverTheWholeFile() {
        val text = "<p>{if \$a}yes{/if}</p>"
        val provider = viewProviderOf(text)

        for (language in provider.languages) {
            val root = provider.getPsi(language)!!
            assertEquals(language.id, text, root.text)
        }
    }

    // ---------------------------------------------------------------- the HTML tree

    /** The point of the exercise: HTML's parser produces real elements, not a run of text. */
    fun testHtmlTreeHasRealTags() {
        val html = htmlRootOf("<ul class=\"list\">{foreach \$items as \$i}<li>{\$i}</li>{/foreach}</ul>")

        val tags = PsiTreeUtil.findChildrenOfType(html, XmlTag::class.java).map { it.name }
        assertEquals(listOf("ul", "li"), tags)
        assertEquals(
            "list",
            PsiTreeUtil.findChildOfType(html, XmlTag::class.java)!!.getAttributeValue("class"),
        )
    }

    /**
     * A Smarty tag is a hole in the HTML tree. It has to be an [OuterLanguageElement] and not just
     * unrecognised text, because that is what stops HTML from trying to make sense of the braces
     * and what lets the platform hand the caret to the right language.
     */
    fun testSmartyTagsAreOuterElementsInTheHtmlTree() {
        val text = "<div>{if \$a}x{/if}</div>"
        val html = htmlRootOf(text)

        val outer = PsiTreeUtil.findChildrenOfType(html, OuterLanguageElement::class.java)
        assertEquals(
            listOf("{if \$a}", "{/if}"),
            outer.map { (it as com.intellij.psi.PsiElement).text },
        )
    }

    /** And the mirror image: the markup is a hole in the Smarty tree. */
    fun testMarkupIsOneOpaqueTokenInTheSmartyTree() {
        val file = configure("<div>{\$a}</div>")
        val smarty = file.viewProvider.getPsi(SmartyLanguage.INSTANCE)!!

        val text = PsiTreeUtil.collectElements(smarty) {
            it.node?.elementType == SmartyTypes.TEXT
        }.map { it.text }
        assertEquals(listOf("<div>", "</div>"), text)
    }

    // ---------------------------------------------------------------- what the caret sees

    /**
     * `findElementAt` on the file picks a root by offset, and this is the method every feature goes
     * through. Inside a tag it has to answer Smarty, in the markup HTML - otherwise completion,
     * quick documentation and the rest fire for the wrong language.
     */
    fun testEachOffsetBelongsToOneLanguage() {
        //                     0123456789
        val file = configure("<b>{\$a}</b>")

        assertEquals("the tag name", HTMLLanguage.INSTANCE, rootLanguageAt(file, 1))
        assertEquals("inside the Smarty tag", SmartyLanguage.INSTANCE, rootLanguageAt(file, 4))
        assertEquals("the closing tag", HTMLLanguage.INSTANCE, rootLanguageAt(file, 9))
    }

    /** HTML completion has to reach a `.tpl` file, or none of this was worth doing. */
    fun testHtmlTagCompletionWorksInATemplate() {
        myFixture.configureByText("test.tpl", "{if \$a}<ta<caret>{/if}")

        val suggestions = myFixture.completeBasic().map { it.lookupString }

        assertTrue("no <table> among $suggestions", "table" in suggestions)
    }

    /** As does Smarty's own, right next to it. */
    fun testSmartyCompletionStillWorks() {
        myFixture.configureByText("test.tpl", "<div>{<caret>}</div>")

        val suggestions = myFixture.completeBasic().map { it.lookupString }

        assertTrue("no {foreach} among $suggestions", "foreach" in suggestions)
    }

    // ---------------------------------------------------------------- the editor

    /**
     * The highlighter is the one piece the two roots do *not* give us for free: without the
     * `editorHighlighterProvider` registration the HTML PSI is all there and the editor still paints
     * the markup in plain text, because it colours from a lexer rather than from the tree.
     */
    fun testTheEditorHighlighterIsLayered() {
        val file = myFixture.configureByText("test.tpl", "<b>x</b>").virtualFile
        val provider = FileTypeEditorHighlighterProviders.getInstance()
            .forFileType(SmartyFileType.INSTANCE)

        val highlighter = provider.getEditorHighlighter(
            project,
            SmartyFileType.INSTANCE,
            file,
            EditorColorsManager.getInstance().globalScheme,
        )

        assertInstanceOf(highlighter, LayeredLexerEditorHighlighter::class.java)
    }

    // ---------------------------------------------------------------- helpers

    private fun configure(text: String): PsiFile = myFixture.configureByText("test.tpl", text)

    private fun viewProviderOf(text: String): TemplateLanguageFileViewProvider =
        configure(text).viewProvider as TemplateLanguageFileViewProvider

    private fun htmlRootOf(text: String): PsiFile =
        viewProviderOf(text).getPsi(HTMLLanguage.INSTANCE)!!

    /**
     * The root the element at [offset] lives in, which is what `findElementAt` is really choosing.
     * Not the element's own language: HTML reuses XML's token types, so an HTML leaf reports XML.
     */
    private fun rootLanguageAt(file: PsiFile, offset: Int) =
        file.findElementAt(offset)!!.containingFile.language
}
