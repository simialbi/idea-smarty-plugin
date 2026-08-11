package ch.erlebnisplus.smarty

import ch.erlebnisplus.smarty.psi.SmartyTokenSets
import ch.erlebnisplus.smarty.psi.SmartyTypes
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet

/**
 * Everything that can be coloured from a single token, without a PSI tree.
 *
 * This runs on the lexer alone, which makes it the fast path - it is what paints a file while it is
 * still being typed and what paints the preview on [SmartyColorSettingsPage]. Whatever needs the
 * tree instead lives in [SmartyAnnotator]; the keys are declared here for both, so that a colour
 * scheme has one entry per concept rather than one per producer.
 *
 * The external names (`SMARTY_*`) are what a saved colour scheme stores, so they stay even where the
 * Kotlin name has moved on - [OPERATORS] is `SMARTY_SEPARATOR` because that is what it was called
 * when the first scheme was written.
 */
class SmartySyntaxHighlighter : SyntaxHighlighterBase() {

    override fun getHighlightingLexer(): Lexer = SmartyLexerAdapter()

    override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> = when {
        tokenType == null -> EMPTY_KEYS

        // Before the keywords: `eq`, `and` and `mod` are words that get operator tokens, and they
        // have to look like the symbols they stand for.
        SmartyTokenSets.OPERATORS.contains(tokenType) -> OPERATOR_KEYS
        SEPARATORS.contains(tokenType) -> OPERATOR_KEYS

        KEYWORDS.contains(tokenType) -> KEYWORD_KEYS
        MODIFIER_NAMES.contains(tokenType) -> MODIFIER_KEYS
        TAG_DELIMITERS.contains(tokenType) -> DELIMITER_KEYS
        PARENTHESIS_TOKENS.contains(tokenType) -> PARENTHESE_KEYS
        BRACKET_TOKENS.contains(tokenType) -> BRACKET_KEYS

        // A `$` and the `#` marks of a config variable belong to the name they introduce, the way
        // the annotator colours those names once there is a tree to ask.
        tokenType === SmartyTypes.DOLLAR -> VARIABLE_KEYS
        tokenType === SmartyTypes.HASH -> CONSTANT_KEYS

        tokenType === SmartyTypes.COMMENT -> COMMENT_KEYS
        tokenType === SmartyTypes.NUMBER -> NUMBERS_KEYS
        tokenType === SmartyTypes.STRING -> STRING_KEYS

        // The lexer has a bad-character token of its own, so the platform's handling of
        // `TokenType.BAD_CHARACTER` never sees it. The annotator reports the error on top.
        tokenType === SmartyTypes.BAD_CHARACTER -> BAD_CHARACTER_KEYS

        else -> EMPTY_KEYS
    }

    companion object {

        // ====================================================================
        // TAGS
        // ====================================================================

        /** The `{` and `}` around every tag. */
        val DELIMITERS: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "SMARTY_DELIMITERS",
            DefaultLanguageHighlighterColors.BRACES
        )

        /** The name of a builtin tag, and `true`, `false` and `null`. */
        val KEYWORD: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "SMARTY_KEY",
            DefaultLanguageHighlighterColors.KEYWORD
        )

        // ====================================================================
        // VARIABLES
        // ====================================================================

        val VARIABLE: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "SMARTY_VARIABLE",
            DefaultLanguageHighlighterColors.LOCAL_VARIABLE
        )

        /** The `.name` and `->name` steps of an access chain. */
        val PROPERTY: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "SMARTY_PROPERTY",
            DefaultLanguageHighlighterColors.INSTANCE_FIELD
        )

        /** `$smarty` and the other variables Smarty reserves for itself. */
        val RESERVED_VARIABLE: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "SMARTY_RESERVED_VARIABLE",
            DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL
        )

        /**
         * A config variable, `{#pageTitle#}`. Its value comes from the file a `{config_load}` read
         * and cannot change while the template renders, which is why it is coloured as a constant.
         */
        val CONSTANT: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "SMARTY_CONSTANT",
            DefaultLanguageHighlighterColors.CONSTANT
        )

        // ====================================================================
        // MODIFIERS AND FUNCTIONS
        // ====================================================================

        /** The name after a `|`. Its own key because Smarty treats modifiers as their own concept. */
        val MODIFIER: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "SMARTY_MODIFIER",
            DefaultLanguageHighlighterColors.INSTANCE_METHOD
        )

        val FUNCTION_CALL: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "SMARTY_FUNCTION_CALL",
            DefaultLanguageHighlighterColors.FUNCTION_CALL
        )

        /** The name a `{block}` or `{function}` declares. */
        val FUNCTION_DECLARATION: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "SMARTY_FUNCTION_DECLARATION",
            DefaultLanguageHighlighterColors.FUNCTION_DECLARATION
        )

        // ====================================================================
        // EXPRESSIONS
        // ====================================================================

        val OPERATORS: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "SMARTY_SEPARATOR",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )

        val NUMBERS: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "SMARTY_NUMBERS",
            DefaultLanguageHighlighterColors.NUMBER
        )

        val STRING: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "SMARTY_STRING",
            DefaultLanguageHighlighterColors.STRING
        )

        val PARENTHESES: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "SMARTY_PARENTHESES",
            DefaultLanguageHighlighterColors.PARENTHESES
        )

        val BRACKETS: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "SMARTY_BRACKETS",
            DefaultLanguageHighlighterColors.BRACKETS
        )

        val COMMENT: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "SMARTY_COMMENT",
            DefaultLanguageHighlighterColors.BLOCK_COMMENT
        )

        // ====================================================================
        // TOKEN GROUPS
        // ====================================================================

        /**
         * Every word that opens or continues a builtin tag, plus the three literals.
         *
         * `literal`, `nocache`, `ldelim` and `rdelim` are in here although the grammar takes those
         * as whole blocks: a token is all this class sees, and it is a keyword either way.
         */
        private val KEYWORDS = TokenSet.create(
            // control flow
            SmartyTypes.IF, SmartyTypes.ELSE, SmartyTypes.ELSEIF,
            SmartyTypes.FOREACH, SmartyTypes.FOREACHELSE, SmartyTypes.FOR, SmartyTypes.WHILE,
            SmartyTypes.SECTION, SmartyTypes.SECTIONELSE,
            SmartyTypes.SWITCH, SmartyTypes.CASE, SmartyTypes.DEFAULT,
            SmartyTypes.BREAK, SmartyTypes.CONTINUE,
            // blocks and functions
            SmartyTypes.BLOCK, SmartyTypes.FUNCTION, SmartyTypes.CALL,
            SmartyTypes.EXTENDS, SmartyTypes.INCLUDE, SmartyTypes.INSERT,
            // variables and state
            SmartyTypes.ASSIGN_KW, SmartyTypes.APPEND, SmartyTypes.PREPEND, SmartyTypes.CAPTURE,
            SmartyTypes.CONFIG_LOAD, SmartyTypes.DEBUG, SmartyTypes.LITERAL_KW,
            SmartyTypes.NOCACHE, SmartyTypes.SETFILTER, SmartyTypes.STRIP,
            SmartyTypes.LDELIM_KW, SmartyTypes.RDELIM_KW,
            // `as` reads as a keyword in `{foreach $rows as $row}`, not as an operator
            SmartyTypes.AS,
            // literals
            SmartyTypes.TRUE, SmartyTypes.FALSE, SmartyTypes.NULL_LITERAL
        )

        /**
         * The modifier names the lexer has a token for.
         *
         * Any PHP function may be used as a modifier, so this is a head start rather than the whole
         * list - [SmartyAnnotator] colours the rest from [SmartyBuiltins] and warns about the names
         * it does not know. `default` is missing because the lexer hands `default` to the `switch`
         * keyword, so `{$x|default:"n/a"}` is coloured by the annotator rather than from here.
         */
        private val MODIFIER_NAMES = TokenSet.create(
            SmartyTypes.UPPER, SmartyTypes.LOWER, SmartyTypes.CAPITALIZE, SmartyTypes.CAT,
            SmartyTypes.COUNT_PARAGRAPHS, SmartyTypes.COUNT_SENTENCES, SmartyTypes.COUNT_WORDS,
            SmartyTypes.DATE_FORMAT, SmartyTypes.ESCAPE, SmartyTypes.FROM_CHARSET,
            SmartyTypes.INDENT, SmartyTypes.NL2BR, SmartyTypes.REGEX_REPLACE, SmartyTypes.REPLACE,
            SmartyTypes.SPACIFY, SmartyTypes.STRING_FORMAT, SmartyTypes.STRIP_TAGS,
            SmartyTypes.TO_CHARSET, SmartyTypes.TRUNCATE, SmartyTypes.UNESCAPE,
            SmartyTypes.WORDWRAP
        )

        /**
         * The punctuation that joins one part of a tag to the next: the `|` and `:` of a modifier
         * chain, the `.`, `->` and `@` of an access chain, the `=` of an attribute. They read as
         * operators - which is what the key was named after - and go unnoticed without a colour.
         */
        private val SEPARATORS = TokenSet.create(
            SmartyTypes.PIPE, SmartyTypes.COLON, SmartyTypes.DOT, SmartyTypes.ARROW,
            SmartyTypes.AT, SmartyTypes.COMMA, SmartyTypes.SEMICOLON, SmartyTypes.QUESTION,
            SmartyTypes.ASSIGN, SmartyTypes.FAT_ARROW
        )

        private val TAG_DELIMITERS = TokenSet.create(SmartyTypes.LDELIM, SmartyTypes.RDELIM)
        private val PARENTHESIS_TOKENS = TokenSet.create(SmartyTypes.LPAREN, SmartyTypes.RPAREN)
        private val BRACKET_TOKENS = TokenSet.create(SmartyTypes.LBRACKET, SmartyTypes.RBRACKET)

        private val EMPTY_KEYS = emptyArray<TextAttributesKey>()
        private val DELIMITER_KEYS = arrayOf(DELIMITERS)
        private val KEYWORD_KEYS = arrayOf(KEYWORD)
        private val MODIFIER_KEYS = arrayOf(MODIFIER)
        private val OPERATOR_KEYS = arrayOf(OPERATORS)
        private val VARIABLE_KEYS = arrayOf(VARIABLE)
        private val CONSTANT_KEYS = arrayOf(CONSTANT)
        private val COMMENT_KEYS = arrayOf(COMMENT)
        private val PARENTHESE_KEYS = arrayOf(PARENTHESES)
        private val BRACKET_KEYS = arrayOf(BRACKETS)
        private val NUMBERS_KEYS = arrayOf(NUMBERS)
        private val STRING_KEYS = arrayOf(STRING)
        private val BAD_CHARACTER_KEYS = arrayOf(HighlighterColors.BAD_CHARACTER)
    }
}
