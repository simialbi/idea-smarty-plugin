package ch.erlebnisplus.smarty

import ch.erlebnisplus.smarty.psi.ConfigVariable
import ch.erlebnisplus.smarty.psi.Modifier
import ch.erlebnisplus.smarty.psi.SmartyTypes
import ch.erlebnisplus.smarty.psi.TextContent
import ch.erlebnisplus.smarty.psi.Variable
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.PsiFile
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

    // ---------------------------------------------------------------- config variables

    /**
     * A config variable is the one kind Smarty writes without a `$`. It reads a key from the file
     * a `{config_load}` pulled in, and everything an ordinary variable can carry it can carry too:
     * an index, a modifier chain, a place in a condition or an attribute value.
     */
    fun testConfigVariable() = assertConfigVariables("{#pageTitle#}", "#pageTitle#")

    /**
     * The reason the name gets a lexer state of its own. `default`, `section` and `include` are
     * ordinary config keys and perfectly ordinary Smarty keywords; without the switch they would
     * lex as those keywords and the tag would not parse.
     */
    fun testConfigVariableNamedLikeAKeyword() =
        assertConfigVariables(
            "{#default#}{#section#}{#include#}{#if#}",
            "#default#", "#section#", "#include#", "#if#"
        )

    /** The indirect form: the key is whatever the variable holds. */
    fun testConfigVariableFromAVariable() = assertConfigVariables("{#\$key#}", "#\$key#")

    /**
     * The variable's *own* name may be a keyword too, and it reaches the parser as that keyword's
     * token rather than as an identifier. Only the key position gets a lexer state of its own, so
     * this is the grammar's business, not the lexer's.
     */
    fun testConfigVariableFromAVariableNamedLikeAKeyword() =
        assertConfigVariables("{#\$section#}", "#\$section#")

    fun testConfigVariableWithIndex() = assertParses("{#rows#[0]}")

    fun testConfigVariableWithModifier() = assertParses("{#pageTitle#|escape:\"html\"}")

    fun testConfigVariableInACondition() = assertParses("{if #showFooter# eq \"yes\"}x{/if}")

    fun testConfigVariableInAnAttributeValue() =
        assertConfigVariables("<body bgcolor=\"{#bodyBgColor#}\">", "#bodyBgColor#")

    /** `{config_load}` reads the file the keys come from, and was already parsing. */
    fun testConfigLoadThenUse() = assertParses(
        "{config_load file=\"colors.conf\" section=\"setup\"}\n<p>{#pageTitle#}</p>"
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

    /**
     * A tag inside an HTML attribute value is a tag. The lexer used to match markup with
     * `"<"[^>]+">"`, which does not exclude `{`, so the whole of `<a href="{$url}">` collapsed
     * into one opaque token and the expression in the attribute never reached the parser.
     */
    fun testTagsInsideAttributeValues() {
        assertVariables("""<a href="{${'$'}url}">x</a>""", "${'$'}url")
        assertVariables(
            """<img src="{${'$'}base}/{${'$'}file|escape}" alt="{${'$'}alt}">""",
            "${'$'}base", "${'$'}file", "${'$'}alt"
        )
        assertVariables("""<li class="{if ${'$'}active}on{/if}">""", "${'$'}active")
    }

    // ---------------------------------------------------------------- everyday Smarty

    /**
     * Each of these was rejected, and a single rejected tag used to cost the whole file: the root
     * loop stops at the first token it cannot place and swallows everything after it, so no
     * `Variable` node past that point exists for the annotator to colour. They are the constructs
     * measured while tracking down "only the dollar sign is coloured", one test each.
     */
    fun testSectionWithAttributes() = assertParses("{section name=foo loop=\$bar}{/section}")

    /** The bare form has to keep working, and it is the reason the attribute form is tried first. */
    fun testBareSectionStillParses() = assertParses("{section rows}{/section}")

    fun testIncludeWithAssign() = assertParses("{include file=\"a.tpl\" assign=\"out\"}")

    fun testStrip() = assertParses("{strip}<p>{\$a}</p>{/strip}")

    fun testCaptureWithName() = assertParses("{capture name=\"x\"}y{/capture}")

    /** `{capture $x}` and `{capture}` both stay valid alongside the attribute form. */
    fun testCaptureShorthandAndBare() = assertParses("{capture \$x}y{/capture}{capture}z{/capture}")

    /** An access chain step may be a variable, and `config` is a reserved sub-key, not a keyword. */
    fun testIndirectAccessChain() = assertParses("{\$smarty.config.\$foo}{\$smarty.capture.default}")

    /** `@index` is the loop property of the current `{foreach}` item. */
    fun testLoopProperty() = assertVariables(
        "{foreach \$rows as \$row}{\$row@index}{/foreach}",
        "\$rows", "\$row", "\$row@index"
    )

    /** `@` in front of a modifier applies it to the array rather than to each element. */
    fun testArrayModifier() = assertParses("{\$rows|@count}")

    /** A plugin name is resolved at render time, so any word followed by attributes is a tag. */
    fun testPluginCall() = assertParses("{html_options values=\$a selected=\$b}{mailto address=\$to}")

    /**
     * Modifiers bind to the operand, one chain link at a time. With the parameter as a full `expr`
     * the second link is swallowed into the first one's argument - `default` would be given
     * `("-"|escape)` - which wrecks the tree silently and makes the annotator's parameter count
     * fire on a perfectly good chain.
     */
    fun testAModifierChainDoesNotNestInItsArgument() {
        val file = myFixture.configureByText("test.tpl", "{\$a|default:\"-\"|escape}")
        val chain = PsiTreeUtil.findChildrenOfType(file, Modifier::class.java)

        assertEquals(
            "expected two sibling modifiers, got ${DebugUtil.psiToString(file, true)}",
            listOf("|default:\"-\"", "|escape"),
            chain.map { it.text }
        )
        assertEquals(2, chain.count { it.parent !is Modifier })
    }

    /**
     * The regression test for the report this all started with. One tag the grammar cannot parse
     * is one error, and everything behind it is still parsed - the markup is a node again and
     * `$after` is a `Variable` the annotator can colour.
     */
    fun testOneBadTagCostsOneTag() {
        val file = myFixture.configureByText("test.tpl", "{qqq zzz}<p>a</p>{\$after}")
        val tree = DebugUtil.psiToString(file, true)

        val errors = PsiTreeUtil.findChildrenOfType(file, PsiErrorElement::class.java)
        assertEquals("expected exactly one error in\n$tree", 1, errors.size)

        assertEquals(
            listOf("<p>a</p>"),
            PsiTreeUtil.findChildrenOfType(file, TextContent::class.java).map { it.text }
        )
        assertEquals(
            listOf("\$after"),
            PsiTreeUtil.findChildrenOfType(file, Variable::class.java).map { it.text }
        )
    }

    /**
     * A comment is a comment token, so the builder skips it and the platform hoists it out of
     * whatever node was open. It has to land at file level: inside a `TEXT_CONTENT` it would be an
     * interior leaf of a composite the formatter drops, and nothing would cover it - see
     * [SmartyFormatterTest.testACommentInsideMarkupIsIndentedLikeTheMarkup].
     */
    fun testACommentInsideMarkupIsAFileLevelNode() {
        val file = myFixture.configureByText("test.tpl", "<div>\n{* note *}\n<p>x</p>\n</div>\n")
        val comment = PsiTreeUtil.findChildOfType(file, PsiComment::class.java)

        assertNotNull("no comment in the tree", comment)
        assertTrue(
            "the comment is not a child of the file:\n${DebugUtil.psiToString(file, true)}",
            comment!!.parent is PsiFile
        )
    }

    /** Markup is template data, not structure: one contiguous run is one text node. */
    fun testMarkupIsOneTextRunBetweenTags() {
        val file = myFixture.configureByText("test.tpl", "<p>a</p>{\$x}<p>b</p>")
        val texts = PsiTreeUtil
            .findChildrenOfType(file, TextContent::class.java)
            .map { it.text }

        assertEquals(listOf("<p>a</p>", "<p>b</p>"), texts)
    }

    /** Collects every VARIABLE in the file, in document order, as `$name`. */
    private fun assertVariables(text: String, vararg expected: String) {
        val file = myFixture.configureByText("test.tpl", text)
        val error = PsiTreeUtil.findChildOfType(file, PsiErrorElement::class.java)
        assertNull("unexpected parse error in $text", error)

        val found = PsiTreeUtil
            .findChildrenOfType(file, Variable::class.java)
            .map { it.text }

        assertEquals(expected.toList(), found)
    }

    /** Collects every CONFIG_VARIABLE in the file, in document order, hash marks included. */
    private fun assertConfigVariables(text: String, vararg expected: String) {
        val file = myFixture.configureByText("test.tpl", text)
        val error = PsiTreeUtil.findChildOfType(file, PsiErrorElement::class.java)
        assertNull("unexpected parse error in $text", error)

        val found = PsiTreeUtil
            .findChildrenOfType(file, ConfigVariable::class.java)
            .map { it.text }

        assertEquals(expected.toList(), found)
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
