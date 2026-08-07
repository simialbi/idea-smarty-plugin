package ch.erlebnisbank.smarty.psi

import ch.erlebnisbank.smarty.SmartyLanguage
import com.intellij.psi.tree.IElementType

class SmartyTokenType : IElementType {
    constructor(debugName: String) : super(debugName, SmartyLanguage.INSTANCE);

    override fun toString(): String {
        return "SmartyTokenType" + super.toString();
    }

    companion object {
        // ========================================================================
        // DELIMITERS
        // ========================================================================
        val LDELIM: SmartyTokenType = SmartyTokenType("LDELIM");
        val RDELIM: SmartyTokenType = SmartyTokenType("RDELIM");
        val LCOMMENT_START: SmartyTokenType = SmartyTokenType("LCOMMENT_START");
        val LCOMMENT_END: SmartyTokenType = SmartyTokenType("LCOMMENT_END");

        // ========================================================================
        // CONTROL KEYWORDS
        // ========================================================================
        val IF: SmartyTokenType = SmartyTokenType("IF");
        val ELSE: SmartyTokenType = SmartyTokenType("ELSE");
        val ELSEIF: SmartyTokenType = SmartyTokenType("ELSEIF");
        val FOREACH: SmartyTokenType = SmartyTokenType("FOREACH");
        val FOREACHELSE: SmartyTokenType = SmartyTokenType("FOREACHELSE");
        val FOR: SmartyTokenType = SmartyTokenType("FOR");
        val WHILE: SmartyTokenType = SmartyTokenType("WHILE");
        val SECTION: SmartyTokenType = SmartyTokenType("SECTION");
        val SECTIONELSE: SmartyTokenType = SmartyTokenType("SECTIONELSE");
        val SWITCH: SmartyTokenType = SmartyTokenType("SWITCH");
        val CASE: SmartyTokenType = SmartyTokenType("CASE");
        val DEFAULT: SmartyTokenType = SmartyTokenType("DEFAULT");
        val BREAK: SmartyTokenType = SmartyTokenType("BREAK");
        val CONTINUE: SmartyTokenType = SmartyTokenType("CONTINUE");

        // ========================================================================
        // BLOCK & FUNCTION KEYWORDS
        // ========================================================================
        val BLOCK: SmartyTokenType = SmartyTokenType("BLOCK");
        val FUNCTION: SmartyTokenType = SmartyTokenType("FUNCTION");
        val CALL: SmartyTokenType = SmartyTokenType("CALL");
        val EXTENDS: SmartyTokenType = SmartyTokenType("EXTENDS");
        val INCLUDE: SmartyTokenType = SmartyTokenType("INCLUDE");
        val INSERT: SmartyTokenType = SmartyTokenType("INSERT");

        // ========================================================================
        // VARIABLE & STATE MANAGEMENT KEYWORDS
        // ========================================================================
        val ASSIGN_KW: SmartyTokenType = SmartyTokenType("ASSIGN_KW");
        val APPEND: SmartyTokenType = SmartyTokenType("APPEND");
        val CAPTURE: SmartyTokenType = SmartyTokenType("CAPTURE");
        val CONFIG_LOAD: SmartyTokenType = SmartyTokenType("CONFIG_LOAD");
        val DEBUG: SmartyTokenType = SmartyTokenType("DEBUG");
        val LITERAL: SmartyTokenType = SmartyTokenType("LITERAL");
        val NOCACHE: SmartyTokenType = SmartyTokenType("NOCACHE");
        val SETFILTER: SmartyTokenType = SmartyTokenType("SETFILTER");
        val STRIP: SmartyTokenType = SmartyTokenType("STRIP");
        val LDELIM_KW: SmartyTokenType = SmartyTokenType("LDELIM_KW");
        val RDELIM_KW: SmartyTokenType = SmartyTokenType("RDELIM_KW");

        // ========================================================================
        // MODIFIERS / FILTERS
        // ========================================================================
        val UPPER: SmartyTokenType = SmartyTokenType("UPPER");
        val LOWER: SmartyTokenType = SmartyTokenType("LOWER");
        val CAPITALIZE: SmartyTokenType = SmartyTokenType("CAPITALIZE");
        val CAT: SmartyTokenType = SmartyTokenType("CAT");
        val COUNT_PARAGRAPHS: SmartyTokenType = SmartyTokenType("COUNT_PARAGRAPHS");
        val COUNT_SENTENCES: SmartyTokenType = SmartyTokenType("COUNT_SENTENCES");
        val COUNT_WORDS: SmartyTokenType = SmartyTokenType("COUNT_WORDS");
        val DATE_FORMAT: SmartyTokenType = SmartyTokenType("DATE_FORMAT");
        val ESCAPE: SmartyTokenType = SmartyTokenType("ESCAPE");
        val FROM_CHARSET: SmartyTokenType = SmartyTokenType("FROM_CHARSET");
        val INDENT: SmartyTokenType = SmartyTokenType("INDENT");
        val NL2BR: SmartyTokenType = SmartyTokenType("NL2BR");
        val REGEX_REPLACE: SmartyTokenType = SmartyTokenType("REGEX_REPLACE");
        val REPLACE: SmartyTokenType = SmartyTokenType("REPLACE");
        val SPACIFY: SmartyTokenType = SmartyTokenType("SPACIFY");
        val STRING_FORMAT: SmartyTokenType = SmartyTokenType("STRING_FORMAT");
        val STRIP_TAGS: SmartyTokenType = SmartyTokenType("STRIP_TAGS");
        val TO_CHARSET: SmartyTokenType = SmartyTokenType("TO_CHARSET");
        val TRUNCATE: SmartyTokenType = SmartyTokenType("TRUNCATE");
        val UNESCAPE: SmartyTokenType = SmartyTokenType("UNESCAPE");
        val WORDWRAP: SmartyTokenType = SmartyTokenType("WORDWRAP");
        val DEFAULT_MOD: SmartyTokenType = SmartyTokenType("DEFAULT_MOD");

        // ========================================================================
        // BOOLEAN & NULL LITERALS
        // ========================================================================
        val TRUE: SmartyTokenType = SmartyTokenType("TRUE");
        val FALSE: SmartyTokenType = SmartyTokenType("FALSE");
        val NULL_LITERAL: SmartyTokenType = SmartyTokenType("NULL_LITERAL");

        // ========================================================================
        // AS KEYWORD (for foreach)
        // ========================================================================
        val AS: SmartyTokenType = SmartyTokenType("AS");

        // ========================================================================
        // OPERATORS - LOGICAL
        // ========================================================================
        val AND: SmartyTokenType = SmartyTokenType("AND");
        val OR: SmartyTokenType = SmartyTokenType("OR");
        val NOT: SmartyTokenType = SmartyTokenType("NOT");

        // ========================================================================
        // OPERATORS - COMPARISON
        // ========================================================================
        val EQ: SmartyTokenType = SmartyTokenType("EQ");
        val NEQ: SmartyTokenType = SmartyTokenType("NEQ");
        val EQEQ: SmartyTokenType = SmartyTokenType("EQEQ");
        val NEQEQ: SmartyTokenType = SmartyTokenType("NEQEQ");
        val LT: SmartyTokenType = SmartyTokenType("LT");
        val GT: SmartyTokenType = SmartyTokenType("GT");
        val LE: SmartyTokenType = SmartyTokenType("LE");
        val GE: SmartyTokenType = SmartyTokenType("GE");

        // ========================================================================
        // OPERATORS - ARITHMETIC
        // ========================================================================
        val PLUS: SmartyTokenType = SmartyTokenType("PLUS");
        val MINUS: SmartyTokenType = SmartyTokenType("MINUS");
        val MULT: SmartyTokenType = SmartyTokenType("MULT");
        val DIV: SmartyTokenType = SmartyTokenType("DIV");
        val MOD: SmartyTokenType = SmartyTokenType("MOD");
        val DIV_KEYWORD: SmartyTokenType = SmartyTokenType("DIV_KEYWORD");

        // ========================================================================
        // OPERATORS - SPECIAL
        // ========================================================================
        val ASSIGN: SmartyTokenType = SmartyTokenType("ASSIGN");
        val PIPE: SmartyTokenType = SmartyTokenType("PIPE");
        val DOT: SmartyTokenType = SmartyTokenType("DOT");
        val ARROW: SmartyTokenType = SmartyTokenType("ARROW");
        val COLON: SmartyTokenType = SmartyTokenType("COLON");
        val QUESTION: SmartyTokenType = SmartyTokenType("QUESTION");
        val SEMICOLON: SmartyTokenType = SmartyTokenType("SEMICOLON");

        // ========================================================================
        // BRACKETS
        // ========================================================================
        val LPAREN: SmartyTokenType = SmartyTokenType("LPAREN");
        val RPAREN: SmartyTokenType = SmartyTokenType("RPAREN");
        val LBRACKET: SmartyTokenType = SmartyTokenType("LBRACKET");
        val RBRACKET: SmartyTokenType = SmartyTokenType("RBRACKET");

        // ========================================================================
        // SEPARATORS
        // ========================================================================
        val COMMA: SmartyTokenType = SmartyTokenType("COMMA");

        // ========================================================================
        // SPECIAL
        // ========================================================================
        val AT: SmartyTokenType = SmartyTokenType("AT");
        val DOLLAR: SmartyTokenType = SmartyTokenType("DOLLAR");
        val HASH: SmartyTokenType = SmartyTokenType("HASH");
        val FAT_ARROW: SmartyTokenType = SmartyTokenType("FAT_ARROW");
        val FORWARD_SLASH: SmartyTokenType = SmartyTokenType("FORWARD_SLASH");

        // ========================================================================
        // VARIABLES
        // ========================================================================
        val VARIABLE: SmartyTokenType = SmartyTokenType("VARIABLE");

        // ========================================================================
        // NUMBERS
        // ========================================================================
        val NUMBER: SmartyTokenType = SmartyTokenType("NUMBER");

        // ========================================================================
        // STRINGS
        // ========================================================================
        val STRING: SmartyTokenType = SmartyTokenType("STRING");

        // ========================================================================
        // WHITESPACE
        // ========================================================================
        val WS: SmartyTokenType = SmartyTokenType("WS");

        // ========================================================================
        // CATCH-ALL for unknown characters
        // ========================================================================
        val BAD_CHARACTER: SmartyTokenType = SmartyTokenType("BAD_CHARACTER");
    }

}
