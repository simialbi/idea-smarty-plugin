package ch.erlebnisbank.smarty

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleManager

/**
 * What `Reformat Code` does to a template, and - at least as important - what it does not.
 *
 * The formatter only ever rewrites whitespace inside a `{...}` tag, because outside a tag the
 * lexer produces text tokens rather than whitespace. Every test that asserts something is left
 * unchanged is guarding that property.
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

    fun testCallsHugTheirParentheses() = assertFormatted(
        "{count ( \$items ,\$other )}",
        "{count(\$items, \$other)}"
    )

    fun testGroupingParenthesesAreTight() = assertFormatted(
        "{if ( \$a || \$b ) && \$c}x{/if}",
        "{if (\$a || \$b) && \$c}x{/if}"
    )

    // ---------------------------------------------------------------- what stays untouched

    /**
     * The whole point of the design: whitespace outside a tag is template output, and the
     * formatter has no whitespace tokens out there to rewrite.
     */
    fun testTemplateTextIsNeverTouched() = assertUnchanged(
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
