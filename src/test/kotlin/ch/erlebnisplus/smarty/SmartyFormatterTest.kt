package ch.erlebnisplus.smarty

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleManager

/**
 * What `Reformat Code` does to a template, and - at least as important - what it does not.
 *
 * Two formatters are at work. Inside a `{...}` tag it is this plugin's, and [SmartySpacingRules]
 * decides every space. Outside a tag it is the data language's - HTML by default - reached through
 * the block merge described in [SmartyBlock]; the Smarty tags are indented along with the markup
 * they stand in. What no formatter may touch is the two verbatim blocks and the text a template
 * prints as-is; the tests below that assert something is unchanged are guarding those.
 */
class SmartyFormatterTest : SmartyTestCase() {

    // ---------------------------------------------------------------- operators

    fun testSymbolicOperatorsGetSpaces() = assertFormatted(
        "{if \$a==\$b&&\$c<\$d}x{/if}",
        "{if \$a == \$b && \$c < \$d}x{/if}"
    )

    /** `$a eq $b` glued together would lex as one identifier, so this one is correctness. */
    fun testTextualOperatorsKeepTheirSpaces() = assertUnchanged(
        "{if \$a eq \$b and not \$c}x{/if}"
    )

    fun testMatchesKeepsItsSpaces() = assertUnchanged(
        "{if \$email matches \"/^[^@]+@[^@]+\$/\"}x{/if}"
    )

    fun testArithmeticGetsSpaces() = assertFormatted(
        "{assign \$x=1+2*3}",
        "{assign \$x = 1 + 2 * 3}"
    )

    /** A leading `-` is a sign, not a subtraction, and must stay glued to its number. */
    fun testSignStaysGluedToItsNumber() = assertUnchanged(
        "{if \$a > -1 && -2 < \$b}x{/if}"
    )

    fun testNotStaysGluedToItsOperand() = assertFormatted(
        "{if ! \$a}x{/if}",
        "{if !\$a}x{/if}"
    )

    fun testTernaryGetsSpaces() = assertFormatted(
        "{\$a?1:2}",
        "{\$a ? 1 : 2}"
    )

    // ---------------------------------------------------------------- tags

    fun testDelimitersHugTheirContents() = assertFormatted(
        "{ if \$a }x{ /if }",
        "{if \$a}x{/if}"
    )

    fun testRunsOfSpacesCollapse() = assertFormatted(
        "{if     \$a}x{/if}",
        "{if \$a}x{/if}"
    )

    /** `=` between an attribute and its value is written tight. */
    fun testAttributeAssignmentIsTight() = assertFormatted(
        "{include file = \"parts/header.tpl\" nocache}",
        "{include file=\"parts/header.tpl\" nocache}"
    )

    /** The same token as an assignment gets spaces. */
    fun testVariableAssignmentGetsSpaces() = assertFormatted(
        "{assign \$total=0}",
        "{assign \$total = 0}"
    )

    fun testForeachHeader() = assertFormatted(
        "{foreach \$items as \$key=>\$value}{\$value}{/foreach}",
        "{foreach \$items as \$key => \$value}{\$value}{/foreach}"
    )

    fun testBareTagsAreLeftAlone() = assertUnchanged("{\$user}{else}{/if}")

    // ---------------------------------------------------------------- expressions

    fun testModifierChainsAreTight() = assertFormatted(
        "{\$title | truncate : 30 : \"...\" | escape}",
        "{\$title|truncate:30:\"...\"|escape}"
    )

    fun testMemberAccessIsTight() = assertFormatted(
        "{\$user . name}{\$rows [0]}",
        "{\$user.name}{\$rows[0]}"
    )

    /**
     * `@` is tight on both sides but for two different reasons: in front of a loop property it is
     * the third separator of an access chain, and in front of a modifier name it marks the modifier
     * as applying to the array rather than to each element.
     */
    fun testLoopPropertiesAndArrayModifiersAreTight() = assertFormatted(
        "{\$row @ index}{\$rows | @ count}",
        "{\$row@index}{\$rows|@count}"
    )

    /** A config variable's hash marks hug its key, the way a `$` hugs a variable name. */
    fun testConfigVariablesAreTight() = assertFormatted(
        "{# pageTitle #}{\$a|cat:# suffix #}",
        "{#pageTitle#}{\$a|cat:#suffix#}"
    )

    fun testCallsHugTheirParentheses() = assertFormatted(
        "{count ( \$items ,\$other )}",
        "{count(\$items, \$other)}"
    )

    fun testGroupingParenthesesAreTight() = assertFormatted(
        "{if ( \$a || \$b ) && \$c}x{/if}",
        "{if (\$a || \$b) && \$c}x{/if}"
    )

    // ---------------------------------------------------------------- markup

    /**
     * The markup is indented by the data language, and the tags come along.
     *
     * `<ul>` and the two `{block}` tags are at the top level of the document, so nothing encloses
     * them and they stay at column zero. `{foreach}` is inside `<ul>` as far as the HTML tree is
     * concerned, so it takes one step of indent exactly like `<li>` does.
     *
     * That `<li>` lands *beside* `{foreach}` rather than inside it is the one gap: the Smarty
     * grammar matches `{foreach}` and `{/foreach}` as two flat siblings, so no PSI node spans the
     * body of a block and the engine has no range to indent. Nesting blocks in the grammar is what
     * would change this, and nothing else here.
     */
    fun testMarkupIsIndentedByTheDataLanguage() = assertFormatted(
        """
        {extends file="layout.tpl"}
        {block name="content"}
                <ul>
          {foreach ${'$'}items as ${'$'}item}
                      <li>{${'$'}item.title|escape}</li>
          {/foreach}
              </ul>
        {/block}
        """.trimIndent(),
        """
        {extends file="layout.tpl"}
        {block name="content"}
        <ul>
            {foreach ${'$'}items as ${'$'}item}
            <li>{${'$'}item.title|escape}</li>
            {/foreach}
        </ul>
        {/block}
        """.trimIndent()
    )

    /**
     * A tag inside an element whose whitespace is content. `<pre>` renders every space it is given,
     * and HTML alone would keep the run intact by giving it to a single block; the block merge takes
     * that block apart to make room for `{$code}`, leaving the whitespace unowned and so fair game
     * for the default re-indent. [SmartyBlock] answers read-only spacing at both boundaries to
     * prevent that - the five spaces on each side are output, not layout.
     */
    fun testWhitespaceInsidePreIsPreserved() = assertUnchanged(
        "<pre>\n     {\$code}     \n</pre>"
    )

    /**
     * A tag inside an element HTML does not indent the children of.
     *
     * `html`, `body`, `thead`, `tbody` and `tfoot` are listed under *Code Style | HTML | Other | Do
     * not indent children of*, so `<tr>` stays level with `<tbody>`. [SmartyBlock] used to assume a
     * normal indent for every top-level Smarty item and pushed `{foreach}` one step past the `<tr>`
     * it generates; it now asks the enclosing markup block how deep its children go. The `{foreach}`
     * landing beside `<tr>` rather than around it is the flat-blocks gap of
     * [testMarkupIsIndentedByTheDataLanguage], not this one.
     */
    fun testTagsStayLevelWithMarkupThatIsNotIndented() = assertFormatted(
        """
        <table>
        <tbody>
        {foreach ${'$'}rows as ${'$'}row}
        <tr><td>{${'$'}row}</td></tr>
        {/foreach}
        </tbody>
        </table>
        """.trimIndent(),
        """
        <table>
            <tbody>
            {foreach ${'$'}rows as ${'$'}row}
            <tr>
                <td>{${'$'}row}</td>
            </tr>
            {/foreach}
            </tbody>
        </table>
        """.trimIndent()
    )

    /**
     * A comment between two runs of markup, which is the case that used to throw.
     *
     * The parser skips comment tokens, so a `text_content ::= TEXT+` spanning the runs on both
     * sides made the `PsiComment` an interior leaf of a composite that [SmartyBlock] drops in favour
     * of the HTML blocks - leaving no block over the comment at all ("nonempty text is not covered
     * by block"). In the IDE that was a logged error and a formatter that carried on with a hole in
     * its tree, which is the mangled indentation this fixes.
     */
    fun testACommentInsideMarkupIsIndentedLikeTheMarkup() = assertFormatted(
        "<div>\n{* note *}\n<p>x</p>\n</div>",
        "<div>\n    {* note *}\n    <p>x</p>\n</div>"
    )

    // ---------------------------------------------------------------- what stays untouched

    /**
     * No braces in the payload: the lexer declares an `IN_LITERAL_BLOCK` state but never enters
     * it, so a `{` inside a `{literal}` block still opens a Smarty tag. Until that is fixed the
     * block only holds together for brace-free content.
     */
    fun testLiteralBlockContentsSurvive() = assertUnchanged(
        "{literal}<script>var a = 1 ;   b == c</script>{/literal}"
    )

    fun testCommentsSurvive() = assertUnchanged("{* keep   these   spaces *}\n{\$a}")

    /** Reformatting an already formatted file has to be a no-op. */
    fun testReformatIsIdempotent() {
        val file = reformat(
            "{ if \$a==\$b }{\$x | escape}{ /if }{include file = \"a.tpl\"}{assign \$y=1}"
        )
        val once = file.text

        WriteCommandAction.runWriteCommandAction(project) {
            CodeStyleManager.getInstance(project).reformat(file)
        }

        assertEquals("second reformat changed the file", once, file.text)
    }

    private fun assertFormatted(before: String, after: String) =
        assertEquals(after, reformat(before).text)

    private fun assertUnchanged(text: String) = assertFormatted(text, text)

    private fun reformat(text: String): PsiFile {
        val file = myFixture.configureByText("test.tpl", text)
        WriteCommandAction.runWriteCommandAction(project) {
            CodeStyleManager.getInstance(project).reformat(file)
        }
        return file
    }
}
