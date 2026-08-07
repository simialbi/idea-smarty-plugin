package ch.erlebnisbank.smarty

import ch.erlebnisbank.smarty.psi.SmartyTypes
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType

class SmartySyntaxHighlighter : SyntaxHighlighterBase() {
    companion object {
        val OPERATORS: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "SMARTY_SEPARATOR",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )
        val KEY: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "SMARTY_KEY",
            DefaultLanguageHighlighterColors.KEYWORD
        )
        val VALUE: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "SMARTY_VALUE",
            DefaultLanguageHighlighterColors.STRING
        )
        val VARIABLE: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "SMARTY_VARIABLE",
            DefaultLanguageHighlighterColors.LOCAL_VARIABLE
        )
        val COMMENT: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "SMARTY_COMMENT",
            DefaultLanguageHighlighterColors.BLOCK_COMMENT
        )
        val CONSTANT: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "SMARTY_CONSTANT",
            DefaultLanguageHighlighterColors.CONSTANT
        )
        val PARENTHESES: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "SMARTY_PARENTHESES",
            DefaultLanguageHighlighterColors.PARENTHESES
        )
        val BRACKETS: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "SMARTY_BRACKETS",
            DefaultLanguageHighlighterColors.PARENTHESES
        )
        val NUMBERS: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "SMARTY_NUMBERS",
            DefaultLanguageHighlighterColors.NUMBER
        )
        val STRING: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "SMARTY_STRING",
            DefaultLanguageHighlighterColors.STRING
        )

        private val OPERATOR_KEYS = arrayOf(OPERATORS)
        private val KEY_KEYS = arrayOf(KEY)
        private val VARIABLE_KEYS = arrayOf(VARIABLE)
        private val COMMENT_KEYS = arrayOf(COMMENT)
        private val EMPTY_KEYS = emptyArray<TextAttributesKey>()
        private val CONSTANT_KEYS = arrayOf(CONSTANT)
        private val PARENTHESE_KEYS = arrayOf(PARENTHESES)
        private val BRACKET_KEYS = arrayOf(BRACKETS)
        private val NUMBERS_KEYS = arrayOf(NUMBERS)
        private val STRING_KEYS = arrayOf(STRING)
    }

    override fun getHighlightingLexer(): Lexer {
        return SmartyLexerAdapter()
    }

    override fun getTokenHighlights(p0: IElementType?): Array<TextAttributesKey> {
        return when (p0) {
            SmartyTypes.IF, SmartyTypes.ELSE, SmartyTypes.ELSEIF,
            SmartyTypes.FOR, SmartyTypes.FOREACH, SmartyTypes.FOREACHELSE, SmartyTypes.WHILE, SmartyTypes.SECTION, SmartyTypes.SECTIONELSE,
            SmartyTypes.SWITCH, SmartyTypes.CASE, SmartyTypes.DEFAULT, SmartyTypes.BREAK, SmartyTypes.CONTINUE
                -> KEY_KEYS

            SmartyTypes.LPAREN, SmartyTypes.RPAREN
                -> PARENTHESE_KEYS

            SmartyTypes.LBRACKET, SmartyTypes.RBRACKET
                -> BRACKET_KEYS

            SmartyTypes.AS, SmartyTypes.AND, SmartyTypes.OR, SmartyTypes.NOT, SmartyTypes.EQ, SmartyTypes.NEQ, SmartyTypes.EQEQ,
            SmartyTypes.NEQEQ, SmartyTypes.GT, SmartyTypes.LT, SmartyTypes.LE, SmartyTypes.GE, SmartyTypes.PLUS, SmartyTypes.MINUS,
            SmartyTypes.DIV, SmartyTypes.MOD, SmartyTypes.MULT
                -> OPERATOR_KEYS

            SmartyTypes.CONFIG_VARIABLE -> CONSTANT_KEYS
            SmartyTypes.VARIABLE -> VARIABLE_KEYS
            SmartyTypes.COMMENT -> COMMENT_KEYS
            SmartyTypes.NUMBER -> NUMBERS_KEYS
            SmartyTypes.STRING -> STRING_KEYS
//            SmartyTypes.BAD_CHARACTER -> BAD_CHAR_KEYS
            else -> EMPTY_KEYS
        }
    }
}
