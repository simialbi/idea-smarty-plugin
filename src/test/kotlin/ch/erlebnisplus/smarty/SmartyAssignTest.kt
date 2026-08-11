package ch.erlebnisplus.smarty

import ch.erlebnisplus.smarty.psi.AppendStatement
import ch.erlebnisplus.smarty.psi.AssignStatement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.DebugUtil
import com.intellij.psi.util.PsiTreeUtil

/**
 * `{assign}` and `{append}` are written either way round: the classic form names its parts,
 * `{assign var="total" value=0}`, and the shorthand `{assign $total = 0}` came with Smarty 3.
 * Both have to parse, and both have to answer the same questions about what is assigned to what.
 *
 * https://smarty-php.github.io/smarty/stable/designers/language-builtin-functions/language-function-assign
 * https://smarty-php.github.io/smarty/stable/designers/language-builtin-functions/language-function-append
 */
class SmartyAssignTest : SmartyTestCase() {

    // ========================================================================
    // PARSING
    // ========================================================================

    fun testShorthand() = assertParses("{assign \$total = 0}")

    fun testShorthandWithAttribute() = assertParses("{assign \$total = 0 scope=\"global\"}")

    fun testClassic() = assertParses("{assign var=\"total\" value=0}")

    fun testClassicWithSingleQuotes() = assertParses("{assign var='total' value='ten'}")

    /** Neither the name nor the value has to be quoted. */
    fun testClassicUnquoted() = assertParses("{assign var=total value=count}")

    fun testClassicWithExpression() = assertParses("{assign var=\"total\" value=\$count + 1}")

    fun testClassicWithScope() = assertParses("{assign var=\"total\" value=0 scope=\"global\"}")

    /** `nocache` is a keyword to the lexer, and valid both as a bare flag and as an attribute. */
    fun testNocacheFlag() = assertParses("{assign var=\"total\" value=0 nocache}")

    fun testNocacheAttribute() = assertParses("{assign var=\"total\" value=0 nocache=true}")

    fun testAppendShorthand() = assertParses("{append \$tags = \"red\"}")

    fun testAppendClassic() = assertParses("{append var=\"tags\" value=\"red\"}")

    /** The attribute `{append}` has and `{assign}` has not. */
    fun testAppendWithIndex() = assertParses("{append var=\"tags\" value=\"red\" index=\"colour\"}")

    fun testBothFormsInOneTemplate() = assertParses(
        "{assign var=\"total\" value=0}\n" +
                "{foreach \$rows as \$row}\n" +
                "  {assign \$total = \$total + \$row.amount}\n" +
                "  {append var=\"seen\" value=\$row.id index=\$row.key}\n" +
                "{/foreach}\n" +
                "<p>{\$total}</p>\n"
    )

    // ========================================================================
    // WHAT IS ASSIGNED TO WHAT
    // ========================================================================

    fun testShorthandTarget() = assertEquals("total", assign("{assign \$total = 0}").assignTarget)

    fun testClassicTarget() = assertEquals("total", assign("{assign var=\"total\" value=0}").assignTarget)

    fun testUnquotedTarget() = assertEquals("total", assign("{assign var=total value=0}").assignTarget)

    fun testCaselessAttributeNames() =
        assertEquals("total", assign("{assign VAR=\"total\" VALUE=0}").assignTarget)

    fun testAttributeOrderDoesNotMatter() =
        assertEquals("total", assign("{assign value=0 var=\"total\"}").assignTarget)

    /**
     * The regression this rule was written around: the first variable below the tag belongs to
     * the value, not to the target.
     */
    fun testVariableValueIsNotTheTarget() {
        val statement = assign("{assign var=\"x\" value=\$y}")

        assertEquals("x", statement.assignTarget)
        assertEquals("\$y", statement.assignValue?.text)
    }

    fun testShorthandValue() = assertEquals("5", assign("{assign \$x = 5}").assignValue?.text)

    fun testClassicValue() =
        assertEquals("\"red\"", assign("{assign var=\"x\" value=\"red\"}").assignValue?.text)

    fun testValueSurvivesFurtherAttributes() =
        assertEquals("0", assign("{assign var=\"x\" value=0 scope=\"global\" nocache}").assignValue?.text)

    /** A tag without a `var` names nothing; the accessor says so rather than guessing. */
    fun testMissingVar() {
        val statement = assign("{assign value=0}")

        assertEquals("", statement.assignTarget)
        assertEquals("0", statement.assignValue?.text)
    }

    fun testAppendTarget() = assertEquals("tags", append("{append var=\"tags\" value=\"red\"}").assignTarget)

    fun testAppendShorthandTarget() = assertEquals("tags", append("{append \$tags = \"red\"}").assignTarget)

    fun testAppendValue() =
        assertEquals("\"red\"", append("{append var=\"tags\" value=\"red\" index=1}").assignValue?.text)

    // ========================================================================
    // HELPERS
    // ========================================================================

    private fun assign(text: String): AssignStatement = statement(text, AssignStatement::class.java)

    private fun append(text: String): AppendStatement = statement(text, AppendStatement::class.java)

    private fun <T : PsiElement> statement(text: String, type: Class<T>): T {
        val file = myFixture.configureByText("test.tpl", text)
        assertNoErrors(file, text)

        val statement = PsiTreeUtil.findChildOfType(file, type)
        assertNotNull("no ${type.simpleName} in $text", statement)
        return statement!!
    }

    private fun assertParses(text: String) {
        assertNoErrors(myFixture.configureByText("test.tpl", text), text)
    }

    private fun assertNoErrors(file: PsiFile, text: String) {
        val error = PsiTreeUtil.findChildOfType(file, PsiErrorElement::class.java) ?: return

        fail(
            "Parse error: ${error.errorDescription}\n" +
                    "at offset ${error.textRange.startOffset} of:\n$text\n\n" +
                    DebugUtil.psiToString(file, true)
        )
    }
}
