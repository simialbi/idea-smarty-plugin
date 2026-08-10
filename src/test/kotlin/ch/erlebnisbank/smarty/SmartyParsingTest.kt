package ch.erlebnisbank.smarty

import ch.erlebnisbank.smarty.psi.SmartyTypes
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.impl.DebugUtil
import com.intellij.psi.util.PsiTreeUtil

/**
 * Checks that real templates reach the PSI without parse errors. These are the first tests the
 * project has, and they are the gate for everything built on the PSI - the annotator, the line
 * markers, completion and the references.
 */
class SmartyParsingTest : SmartyTestCase() {

    fun testPlainMarkup() = assertParses("<h1>Hello</h1>\n<p>Some text.</p>\n")

    fun testComment() = assertParses("{* just a comment *}")

    fun testVariableOutput() = assertParses("{\$user}")

    fun testVariableWithModifier() = assertParses("{\$user.name|upper}")

    /**
     * Modifier parameters are colon separated, one colon each:
     * https://smarty-php.github.io/smarty/stable/designers/language-modifiers/language-modifier-truncate
     */
    fun testModifierParameters() = assertParses(
        "{\$text|truncate:30}" +
                "{\$text|truncate:30:\"...\":true}" +
                "{\$text|truncate:\$len:\$suffix|escape:\"html\":\"UTF-8\"}" +
                "{\$date|date_format:\"%d.%m.%Y\"}"
    )

    fun testInclude() = assertParses("{include file=\"parts/header.tpl\"}")

    fun testBlockWithNameAttribute() = assertParses("{block name=\"content\"}")

    fun testBareBlock() = assertParses("{block content}")

    fun testIf() = assertParses("{if \$user}<b>yes</b>{/if}")

    fun testSymbolicComparisons() = assertParses(
        "{if \$a == \$b || \$a != \$c && \$a === \$d && \$a !== \$e}x{/if}" +
                "{if \$a < \$b && \$a > \$c && \$a <= \$d && \$a >= \$e}y{/if}"
    )

    /**
     * The textual equivalents documented at
     * https://smarty-php.github.io/smarty/stable/designers/language-basic-syntax/language-syntax-operators
     */
    fun testTextualComparisons() = assertParses(
        "{if \$a eq \$b}1{/if}" +
                "{if \$a ne \$b}2{/if}" +
                "{if \$a neq \$b}3{/if}" +
                "{if \$a lt \$b}4{/if}" +
                "{if \$a gt \$b}5{/if}" +
                "{if \$a le \$b}6{/if}" +
                "{if \$a lte \$b}7{/if}" +
                "{if \$a ge \$b}8{/if}" +
                "{if \$a gte \$b}9{/if}"
    )

    /** `matches` takes a regex on the right, so the pattern brings `/`, `^`, `$` and `+` with it. */
    fun testMatches() = assertParses(
        "{if \"hello\" matches \"/^[a-z]+\$/\"}1{/if}" +
                "{if \$email matches \"/^[^@]+@[^@]+\\.[^@]+\$/\"}2{/if}" +
                "{if \$password matches \$pattern}3{/if}" +
                "{if \$name matches \"/^[a-z]+\$/\" && \$name neq \"\"}4{/if}" +
                "{if \"HELLO\" matches \"/hello/i\"}5{/if}"
    )

    fun testTextualLogicalOperators() = assertParses("{if not \$a and \$b or \$c}x{/if}")

    fun testTextualModulo() = assertParses("{if \$a mod 2 eq 0}even{/if}")

    fun testForeach() = assertParses("{foreach \$items as \$item}{\$item}{/foreach}")

    fun testMixedTemplate() = assertParses(
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
     * A regex pattern is mostly backslashes, so a single-backslash escape has to stay inside the
     * string token instead of ending it. The lexer used to require two backslashes, which made
     * every pattern on the operator documentation page unlexable.
     */
    fun testEscapesStayInsideStrings() {
        assertOneStringToken("\"/^[^@]+@[^@]+\\.[^@]+\$/\"")
        assertOneStringToken("\"/^(?=.*\\d).{8,}\$/\"")
        assertOneStringToken("\"a \\\" quote\"")
        assertOneStringToken("'it\\'s'")
        assertOneStringToken("\"back\\\\slash\"")
    }

    /** Lexes the literal inside a tag and expects the whole of it to be a single STRING. */
    private fun assertOneStringToken(literal: String) {
        val file = myFixture.configureByText("test.tpl", "{if \$a matches $literal}x{/if}")
        val strings = PsiTreeUtil
            .collectElements(file) { it.firstChild == null && it.node.elementType === SmartyTypes.STRING }
            .map { it.text }

        assertEquals("$literal should lex as one string", listOf(literal), strings)
    }

    private fun assertParses(text: String) {
        val file = myFixture.configureByText("test.tpl", text)
        val error = PsiTreeUtil.findChildOfType(file, PsiErrorElement::class.java)

        if (error != null) {
            fail(
                "Parse error: ${error.errorDescription}\n" +
                        "at offset ${error.textRange.startOffset} of:\n$text\n\n" +
                        DebugUtil.psiToString(file, true)
            )
        }
    }
}
