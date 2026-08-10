package ch.erlebnisbank.smarty

import ch.erlebnisbank.smarty.psi.SmartyTokenSets
import ch.erlebnisbank.smarty.psi.SmartyTypes
import com.intellij.psi.tree.IElementType
import com.intellij.testFramework.fixtures.BasePlatformTestCase

/**
 * The textual operator forms, as documented at
 * https://smarty-php.github.io/smarty/stable/designers/language-basic-syntax/language-syntax-operators
 *
 * Lexer level on purpose: a parsing test would also pass if an alias fell through to
 * `IDENTIFIER` and the grammar happened to tolerate it, so the token itself is asserted.
 */
class SmartyOperatorTest : BasePlatformTestCase() {

    fun testComparisonAliases() {
        assertOperator("eq", SmartyTypes.EQ_KEYWORD)
        assertOperator("ne", SmartyTypes.NEQ_KEYWORD)
        assertOperator("neq", SmartyTypes.NEQ_KEYWORD)
        assertOperator("lt", SmartyTypes.LT_KEYWORD)
        assertOperator("gt", SmartyTypes.GT_KEYWORD)
        assertOperator("le", SmartyTypes.LE_KEYWORD)
        assertOperator("lte", SmartyTypes.LE_KEYWORD)
        assertOperator("ge", SmartyTypes.GE_KEYWORD)
        assertOperator("gte", SmartyTypes.GE_KEYWORD)
    }

    /** `matches` is the one comparison operator Smarty spells only as a word. */
    fun testMatches() {
        assertOperator("matches", SmartyTypes.MATCHES)
    }

    fun testSymbolsAreUnchanged() {
        assertOperator("==", SmartyTypes.EQ)
        assertOperator("!=", SmartyTypes.NEQ)
        assertOperator("===", SmartyTypes.EQEQ)
        assertOperator("!==", SmartyTypes.NEQEQ)
        assertOperator("<", SmartyTypes.LT)
        assertOperator(">", SmartyTypes.GT)
        assertOperator("<=", SmartyTypes.LE)
        assertOperator(">=", SmartyTypes.GE)
    }

    /** These were declared in the grammar but folded into the symbols by the lexer. */
    fun testLogicalAndModuloAliases() {
        assertOperator("and", SmartyTypes.AND_KEYWORD)
        assertOperator("or", SmartyTypes.OR_KEYWORD)
        assertOperator("not", SmartyTypes.NOT_KEYWORD)
        assertOperator("mod", SmartyTypes.MOD_KEYWORD)
        assertOperator("&&", SmartyTypes.AND)
        assertOperator("||", SmartyTypes.OR)
        assertOperator("%", SmartyTypes.MOD)
    }

    /** The aliases have to be coloured like the symbols they stand for. */
    fun testAliasesAreHighlightedAsOperators() {
        val highlighter = SmartySyntaxHighlighter()

        val aliases = listOf("eq", "neq", "lt", "gt", "lte", "gte", "and", "or", "not", "mod", "matches")
        for (alias in aliases) {
            val keys = highlighter.getTokenHighlights(tokenFor(alias))
            assertEquals(
                "$alias should be highlighted as an operator",
                listOf(SmartySyntaxHighlighter.OPERATORS),
                keys.toList()
            )
        }
    }

    private fun assertOperator(text: String, expected: IElementType) {
        val actual = tokenFor(text)
        assertEquals("wrong token for '$text'", expected, actual)
        assertTrue("'$text' should be in the operator set", SmartyTokenSets.OPERATORS.contains(actual))
    }

    /** Lexes `{if $a <text> $b}` and returns the token produced for `text`. */
    private fun tokenFor(text: String): IElementType {
        val source = "{if \$a $text \$b}"
        val lexer = SmartyLexerAdapter()
        lexer.start(source)

        val start = source.indexOf(text, "{if \$a".length)
        while (lexer.tokenType != null) {
            if (lexer.tokenStart == start && lexer.tokenEnd == start + text.length) {
                return checkNotNull(lexer.tokenType)
            }
            lexer.advance()
        }
        throw AssertionError("no token covering '$text' in $source")
    }
}
