package ch.erlebnisbank.smarty;

import ch.erlebnisbank.smarty.psi.SmartyTokenType;
import ch.erlebnisbank.smarty.psi.SmartyTypes;
import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

%%

%class SmartyLexer
%implements FlexLexer
%public
%final
%unicode
%line
%column
%caseless
%function advance
%type IElementType
%eof{  return;
%eof}

// ============================================================================
// Macro Definitions
// ============================================================================

LineTerminator       = \r\n|\r|\n
WhiteSpace           = [ \t\f]
Identifier           = [a-zA-Z_][a-zA-Z0-9_]*
Variable             = \$[a-zA-Z_][a-zA-Z0-9_]*(\.[a-zA-Z_][a-zA-Z0-9_]*|\[[^\]]*\])*
Number               = [0-9]+(\.[0-9]+)?([eE][+-]?[0-9]+)?
String               = \"([^\"\\\\]|\\\\.)*\"|\'([^\'\\\\]|\\\\.)*\'

// ============================================================================
// Lexical States
// ============================================================================

%state IN_SMARTY_TAG
%state IN_SMARTY_COMMENT
%state IN_LITERAL_BLOCK
%state IN_NOCACHE_BLOCK

%%

// ============================================================================
// YYINITIAL STATE - Outside Smarty Tags
// ============================================================================

<YYINITIAL> {
    // Opening comment block
    "{*"                           {
                                     yybegin(IN_SMARTY_COMMENT);
                                     return SmartyTokenType.LCOMMENT_START;
                                   }

    // Opening regular Smarty tag
    "{"                            {
                                     yybegin(IN_SMARTY_TAG);
                                     return SmartyTokenType.LDELIM;
                                   }

    // HTML tags and text content
    "<"[^>]+">"                    { return SmartyTokenType.HTML_TAG; }

    // Text content (everything else)
    [^{<]+                         { return SmartyTokenType.TEXT; }

    // Single characters that aren't special
    .                              { return SmartyTokenType.TEXT; }

    {LineTerminator}               { return SmartyTokenType.TEXT; }
}

// ============================================================================
// SMARTY COMMENT STATE
// ============================================================================

<IN_SMARTY_COMMENT> {
    "*}"                           {
                                     yybegin(YYINITIAL);
                                     return SmartyTokenType.LCOMMENT_END;
                                   }

    [^*]+                          { return SmartyTokenType.TEXT; }
    "*"                            { return SmartyTokenType.TEXT; }
    {LineTerminator}               { return SmartyTokenType.TEXT; }
}

// ============================================================================
// SMARTY TAG STATE - Main tokenization happens here
// ============================================================================

<IN_SMARTY_TAG> {
    // Closing tag
    "}"                            {
                                     yybegin(YYINITIAL);
                                     return SmartyTokenType.RDELIM;
                                   }

    // CONTROL KEYWORDS
    "if"                           { return SmartyTokenType.IF; }
    "else"                         { return SmartyTokenType.ELSE; }
    "elseif"                       { return SmartyTokenType.ELSEIF; }
    "foreach"                      { return SmartyTokenType.FOREACH; }
    "foreachelse"                  { return SmartyTokenType.FOREACHELSE; }
    "for"                          { return SmartyTokenType.FOR; }
    "while"                        { return SmartyTokenType.WHILE; }
    "section"                      { return SmartyTokenType.SECTION; }
    "sectionelse"                  { return SmartyTokenType.SECTIONELSE; }
    "switch"                       { return SmartyTokenType.SWITCH; }
    "case"                         { return SmartyTokenType.CASE; }
    "default"                      { return SmartyTokenType.DEFAULT; }
    "break"                        { return SmartyTokenType.BREAK; }
    "continue"                     { return SmartyTokenType.CONTINUE; }

    // BLOCK & FUNCTION KEYWORDS
    "block"                        { return SmartyTokenType.BLOCK; }
    "function"                     { return SmartyTokenType.FUNCTION; }
    "call"                         { return SmartyTokenType.CALL; }
    "extends"                      { return SmartyTokenType.EXTENDS; }
    "include"                      { return SmartyTokenType.INCLUDE; }
    "insert"                       { return SmartyTokenType.INSERT; }

    // VARIABLE & STATE MANAGEMENT KEYWORDS
    "assign"                       { return SmartyTokenType.ASSIGN_KW; }
    "append"                       { return SmartyTokenType.APPEND; }
    "capture"                      { return SmartyTokenType.CAPTURE; }
    "config_load"                  { return SmartyTokenType.CONFIG_LOAD; }
    "debug"                        { return SmartyTokenType.DEBUG; }
    "literal"                      { return SmartyTokenType.LITERAL; }
    "nocache"                      { return SmartyTokenType.NOCACHE; }
    "setfilter"                    { return SmartyTokenType.SETFILTER; }
    "strip"                        { return SmartyTokenType.STRIP; }
    "ldelim"                       { return SmartyTokenType.LDELIM_KW; }
    "rdelim"                       { return SmartyTokenType.RDELIM_KW; }

    // MODIFIERS / FILTERS
    "upper"                        { return SmartyTokenType.UPPER; }
    "lower"                        { return SmartyTokenType.LOWER; }
    "capitalize"                   { return SmartyTokenType.CAPITALIZE; }
    "cat"                          { return SmartyTokenType.CAT; }
    "count_paragraphs"             { return SmartyTokenType.COUNT_PARAGRAPHS; }
    "count_sentences"              { return SmartyTokenType.COUNT_SENTENCES; }
    "count_words"                  { return SmartyTokenType.COUNT_WORDS; }
    "date_format"                  { return SmartyTokenType.DATE_FORMAT; }
    "escape"                       { return SmartyTokenType.ESCAPE; }
    "from_charset"                 { return SmartyTokenType.FROM_CHARSET; }
    "indent"                       { return SmartyTokenType.INDENT; }
    "nl2br"                        { return SmartyTokenType.NL2BR; }
    "regex_replace"                { return SmartyTokenType.REGEX_REPLACE; }
    "replace"                      { return SmartyTokenType.REPLACE; }
    "spacify"                      { return SmartyTokenType.SPACIFY; }
    "string_format"                { return SmartyTokenType.STRING_FORMAT; }
    "strip_tags"                   { return SmartyTokenType.STRIP_TAGS; }
    "to_charset"                   { return SmartyTokenType.TO_CHARSET; }
    "truncate"                     { return SmartyTokenType.TRUNCATE; }
    "unescape"                     { return SmartyTokenType.UNESCAPE; }
    "wordwrap"                     { return SmartyTokenType.WORDWRAP; }
    "default"                      { return SmartyTokenType.DEFAULT_MOD; }

    // BOOLEAN & NULL LITERALS
    "true"|"yes"|"on"              { return SmartyTokenType.TRUE; }
    "false"|"no"|"off"             { return SmartyTokenType.FALSE; }
    "null"                         { return SmartyTokenType.NULL_LITERAL; }

    // AS KEYWORD (for foreach)
    "as"                           { return SmartyTokenType.AS; }

    // OPERATORS - LOGICAL
    "&&"|"and"                     { return SmartyTokenType.AND; }
    "||"|"or"                      { return SmartyTokenType.OR; }
    "!"|"not"                      { return SmartyTokenType.NOT; }

    // OPERATORS - COMPARISON
    "=="                           { return SmartyTokenType.EQ; }
    "!="                           { return SmartyTokenType.NEQ; }
    "==="                          { return SmartyTokenType.EQEQ; }
    "!=="                          { return SmartyTokenType.NEQEQ; }
    "<="                           { return SmartyTokenType.LE; }
    ">="                           { return SmartyTokenType.GE; }
    "<"                            { return SmartyTokenType.LT; }
    ">"                            { return SmartyTokenType.GT; }

    // OPERATORS - ARITHMETIC
    "+"                            { return SmartyTokenType.PLUS; }
    "-"                            { return SmartyTokenType.MINUS; }
    "*"                            { return SmartyTokenType.MULT; }
    "/"                            { return SmartyTokenType.DIV; }
    "%"|"mod"                      { return SmartyTokenType.MOD; }
    "div"                          { return SmartyTokenType.DIV_KEYWORD; }

    // OPERATORS - SPECIAL
    "="                            { return SmartyTokenType.ASSIGN; }
    "|"                            { return SmartyTokenType.PIPE; }
    "."                            { return SmartyTokenType.DOT; }
    "->"                           { return SmartyTokenType.ARROW; }
    ":"                            { return SmartyTokenType.COLON; }
    "?"                            { return SmartyTokenType.QUESTION; }
    ";"                            { return SmartyTokenType.SEMICOLON; }

    // BRACKETS
    "("                            { return SmartyTokenType.LPAREN; }
    ")"                            { return SmartyTokenType.RPAREN; }
    "["                            { return SmartyTokenType.LBRACKET; }
    "]"                            { return SmartyTokenType.RBRACKET; }

    // SEPARATORS
    ","                            { return SmartyTokenType.COMMA; }

    // SPECIAL
    "@"                            { return SmartyTokenType.AT; }
    "$"                            { return SmartyTokenType.DOLLAR; }
    "#"                            { return SmartyTokenType.HASH; }
    "=>"                           { return SmartyTokenType.FAT_ARROW; }
    "/"                            { return SmartyTokenType.FORWARD_SLASH; }

    // VARIABLES
    {Variable}                     { return SmartyTokenType.VARIABLE; }

    // NUMBERS
    {Number}                       { return SmartyTokenType.NUMBER; }

    // STRINGS
    {String}                       { return SmartyTokenType.STRING; }

    // IDENTIFIERS
    {Identifier}                   { return SmartyTokenType.IDENTIFIER; }

    // WHITESPACE
    {WhiteSpace}+                  { return SmartyTokenType.WS; }
    {LineTerminator}               { return SmartyTokenType.WS; }

    // CATCH-ALL for unknown characters
    .                              { return SmartyTokenType.BAD_CHARACTER; }
}

// ============================================================================
// EOF
// ============================================================================

<<EOF>> { return null; }
