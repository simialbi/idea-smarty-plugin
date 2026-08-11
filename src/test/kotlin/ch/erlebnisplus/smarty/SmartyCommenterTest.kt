package ch.erlebnisplus.smarty

import com.intellij.codeInsight.generation.actions.CommentByBlockCommentAction
import com.intellij.codeInsight.generation.actions.CommentByLineCommentAction

/**
 * What the two comment actions do in a template.
 *
 * Smarty has one comment form, `{* ... *}`, and [SmartyCommenter] is a handful of constants - so
 * most of what is worth testing is not the constants but the platform behaviour they select. Three
 * things in particular: that the line action works at all in a language with no line comment, that
 * a delimiter caught inside a new comment does not break out of it, and that a `.tpl` file uses
 * Smarty's comment in a tag and HTML's in the markup.
 */
class SmartyCommenterTest : SmartyTestCase() {

    // ---------------------------------------------------------------- the plain cases

    /**
     * Smarty has no line comment, so the line action falls back to wrapping the line in the block
     * form. Without that fallback the plugin would answer `null` and the keystroke would do nothing.
     */
    fun testLineCommentUsesTheBlockForm() {
        configure("{<caret>\$user.name}")

        commentLine()
        assertText("{*{\$user.name}*}")

        commentLine()
        assertText("{\$user.name}")
    }

    fun testBlockCommentWrapsTheSelection() {
        configure("<p><selection>{\$a}{\$b}</selection></p>")

        commentBlock()
        assertText("<p>{*{\$a}{\$b}*}</p>")

        commentBlock()
        assertText("<p>{\$a}{\$b}</p>")
    }

    /**
     * Several lines at once are one comment, not one per line. A selection that covers whole lines
     * gets the opening delimiter on a line of its own, which is the platform's doing rather than
     * this plugin's.
     */
    fun testBlockCommentSpansSeveralLines() {
        configure("<selection>{if \$a}\n  <b>x</b>\n{/if}</selection>")

        commentBlock()
        assertText("{*\n{if \$a}\n  <b>x</b>\n{/if}*}\n")
    }

    // ---------------------------------------------------------------- delimiters in the payload

    /**
     * The case [SmartyCommenter] is careful about. `*}` closes a comment at the first occurrence,
     * so wrapping a region that already holds one would leave the tail of that region rendering.
     * The platform avoids it by splitting the comment in two, which it only does because the
     * commenter answers `null` for the commented-delimiter forms.
     *
     * Every character of the original has to end up inside one of the two comments; nothing may be
     * left between them.
     */
    fun testCommentingSurvivesAStrayTerminator() {
        configure("<selection><p>*}</p></selection>")

        commentBlock()
        assertText("{*\n<p>*}{*\n</p>*}\n")

        // Uncommenting does not give the `*}` back, and cannot: the split turned it into a real
        // terminator, and Smarty has no escaped form to tell one apart from the other. Commenting
        // is safe - nothing renders - but this particular round trip loses a character. Answering
        // the commented-delimiter methods with `{*`/`*}` loses it too *and* leaves markup showing
        // in between, so this is the better of the two, not a regression from it.
        commentBlock()
        assertText("<p>\n</p>\n")
    }

    /**
     * And the same for a comment inside the region, which is the nesting Smarty cannot express. The
     * inner comment is left as it stands and the region is commented around it, so the only thing
     * outside a comment afterwards is a line break.
     */
    fun testCommentingAnExistingCommentSplitsIt() {
        configure("<selection><p>{* inner *}</p></selection>")

        commentBlock()
        assertText("{*\n<p>*}\n{* inner *}{*\n</p>*}\n")
    }

    // ---------------------------------------------------------------- the other language

    /**
     * A `.tpl` file has an HTML root too, but the markup still gets Smarty's comment rather than
     * `<!-- -->`. That is deliberate on the platform's side - `getCommenter` swaps the template
     * data language for the base language whenever it lands on the former - and it is the better
     * answer here: `{* ... *}` is dropped when the template is rendered, where an HTML comment
     * would be shipped to the browser and shown in *View Source*.
     */
    fun testMarkupIsCommentedWithSmartysComment() {
        configure("<div><caret>text</div>")

        commentLine()
        assertText("{*<div>text</div>*}")

        commentLine()
        assertText("<div>text</div>")
    }

    private fun configure(text: String) = myFixture.configureByText("test.tpl", text)

    /** The whole document, which is what these actions rewrite. */
    private fun assertText(expected: String) =
        assertEquals(expected, myFixture.editor.document.text)

    private fun commentLine() =
        CommentByLineCommentAction().actionPerformedImpl(project, myFixture.editor)

    private fun commentBlock() =
        CommentByBlockCommentAction().actionPerformedImpl(project, myFixture.editor)
}
