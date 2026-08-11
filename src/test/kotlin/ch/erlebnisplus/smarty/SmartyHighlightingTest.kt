package ch.erlebnisplus.smarty

import com.intellij.openapi.editor.colors.TextAttributesKey

/**
 * What [SmartyAnnotator] colours, as the editor sees it.
 *
 * Only the semantic half of the highlighting is visible here. Delimiters, keywords and the `$` are
 * coloured by [SmartySyntaxHighlighter] straight off the lexer, which needs no PSI and never fails;
 * everything in this file is driven by the parse tree instead, and the tests are the ones that
 * would have caught "only the dollar sign is coloured" - a broken parse tree leaves the lexical
 * colours in place and takes all of these away.
 */
class SmartyHighlightingTest : SmartyTestCase() {

    /**
     * The literal report: the name gets the variable colour and not just the `$`. The annotator
     * takes the `$` with it, which is why the expected range is the whole `$title` - it repaints
     * over the lexer's own colour for the `$` with the same key.
     */
    fun testEveryVariableNameIsColoured() = assertColoured(
        "{\$title|default:\"n/a\"} and {\$other}",
        SmartySyntaxHighlighter.VARIABLE,
        "\$title", "\$other"
    )

    /**
     * A tag the grammar cannot parse costs that tag and nothing else, so the variables behind it
     * still reach the annotator. Without the recovery rule in the grammar the rest of the file went
     * into one error element and `$after` had no `Variable` node at all.
     */
    fun testABadTagDoesNotUncolourTheRestOfTheFile() = assertColoured(
        "{qqq zzz}<p>a</p>{\$after}",
        SmartySyntaxHighlighter.VARIABLE,
        "\$after"
    )

    /**
     * And inside the bad tag itself the name is still coloured, without any PSI to go on: Smarty
     * allows no space after a `$`, so the leaf behind one is the variable name and can be nothing
     * else. This is the belt to the recovery rule's braces - it holds even for a tag no grammar
     * will ever know. The range is the name alone here: with no `Variable` node there is nothing
     * spanning the `$` as well, and the lexer has that one covered anyway.
     */
    fun testAVariableNameInsideABrokenTagIsColoured() = assertColoured(
        "{qqq \$inside zzz}",
        SmartySyntaxHighlighter.VARIABLE,
        "inside"
    )

    /** The `$smarty` super global is told apart from a template variable. */
    fun testTheSuperGlobalHasItsOwnColour() = assertColoured(
        "{\$smarty.get.page}",
        SmartySyntaxHighlighter.RESERVED_VARIABLE,
        "\$smarty"
    )

    /** A loop property is an access chain step, so it is coloured like `.property` is. */
    fun testALoopPropertyIsColouredLikeAProperty() = assertColoured(
        "{foreach \$rows as \$row}{\$row@index}{/foreach}",
        SmartySyntaxHighlighter.PROPERTY,
        "index"
    )

    /**
     * The text of every range the annotator coloured with [key], in document order. Annotations
     * carrying nothing but a colour are silent [com.intellij.lang.annotation.HighlightSeverity]
     * `INFORMATION` ones, which is what puts the key in `forcedTextAttributesKey`.
     */
    private fun assertColoured(text: String, key: TextAttributesKey, vararg expected: String) {
        myFixture.configureByText("test.tpl", text)

        val found = myFixture.doHighlighting()
            .filter { it.forcedTextAttributesKey === key }
            .sortedBy { it.startOffset }
            .map { text.substring(it.startOffset, it.endOffset) }

        assertEquals("ranges coloured ${key.externalName} in $text", expected.toList(), found)
    }
}
