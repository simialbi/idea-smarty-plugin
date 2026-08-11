package ch.erlebnisbank.smarty;

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
Number               = [0-9]+(\.[0-9]+)?([eE][+-]?[0-9]+)?
// An escape is one backslash and the character after it. \\ is a single literal backslash in
// JFlex, so the escape alternative has to be \\[^] - the old \\\\. required *two* backslashes,
// which meant "\." never lexed as a string at all. Regex patterns are full of those, so the
// matches operator depends on this.
String               = \"([^\"\\]|\\[^])*\"|\'([^\'\\]|\\[^])*\'

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
    // A comment is a single token. The parser skips whatever the parser definition reports
    // as a comment, so a comment can never be assembled from a grammar rule; ~"*}" consumes
    // everything up to and including the terminator.
    "{*" ~"*}"                     { return SmartyTypes.COMMENT; }

    // Unterminated: swallow the rest of the file rather than dropping into tag lexing.
    "{*"                           {
                                     yybegin(IN_SMARTY_COMMENT);
                                     return SmartyTypes.COMMENT;
                                   }

    // Opening regular Smarty tag
    "{"                            {
                                     yybegin(IN_SMARTY_TAG);
                                     return SmartyTypes.LDELIM;
                                   }

    // Everything that is not a tag opener is template data - markup, text and the newlines
    // between them alike. It has to be a single token type, because that is how the platform
    // hands template data to a second language: TemplateDataElementType re-lexes the file and
    // partitions it by comparing each token against one content element type.
    //
    // This also replaced a separate "<"[^>]+">" rule for HTML tags. That regex did not exclude
    // "{", so the whole of <a href="{$url}"> lexed as one opaque tag token and every Smarty
    // expression written inside an HTML attribute was invisible to the parser.
    //
    // The previous "." and {LineTerminator} rules are gone with it: this rule matches newlines
    // and single characters too, and JFlex breaks equal-length ties by rule order, so they were
    // unreachable. Coverage stays total - "{" is claimed by the rules above, the rest by this one.
    [^{]+                          { return SmartyTypes.TEXT; }
}

// ============================================================================
// SMARTY COMMENT STATE
// ============================================================================

<IN_SMARTY_COMMENT> {
    [^]+                           { return SmartyTypes.COMMENT; }
}

// ============================================================================
// SMARTY TAG STATE - Main tokenization happens here
// ============================================================================

<IN_SMARTY_TAG> {
    // Closing tag
    "}"                            {
                                     yybegin(YYINITIAL);
                                     return SmartyTypes.RDELIM;
                                   }

    // CONTROL KEYWORDS
    "if"                           { return SmartyTypes.IF; }
    "else"                         { return SmartyTypes.ELSE; }
    "elseif"                       { return SmartyTypes.ELSEIF; }
    "foreach"                      { return SmartyTypes.FOREACH; }
    "foreachelse"                  { return SmartyTypes.FOREACHELSE; }
    "for"                          { return SmartyTypes.FOR; }
    "while"                        { return SmartyTypes.WHILE; }
    "section"                      { return SmartyTypes.SECTION; }
    "sectionelse"                  { return SmartyTypes.SECTIONELSE; }
    "switch"                       { return SmartyTypes.SWITCH; }
    "case"                         { return SmartyTypes.CASE; }
    "default"                      { return SmartyTypes.DEFAULT; }
    "break"                        { return SmartyTypes.BREAK; }
    "continue"                     { return SmartyTypes.CONTINUE; }

    // BLOCK & FUNCTION KEYWORDS
    "block"                        { return SmartyTypes.BLOCK; }
    "function"                     { return SmartyTypes.FUNCTION; }
    "call"                         { return SmartyTypes.CALL; }
    "extends"                      { return SmartyTypes.EXTENDS; }
    "include"                      { return SmartyTypes.INCLUDE; }
    "insert"                       { return SmartyTypes.INSERT; }

    // VARIABLE & STATE MANAGEMENT KEYWORDS
    "assign"                       { return SmartyTypes.ASSIGN_KW; }
    "append"                       { return SmartyTypes.APPEND; }
    "prepend"                      { return SmartyTypes.PREPEND; }
    "capture"                      { return SmartyTypes.CAPTURE; }
    "config_load"                  { return SmartyTypes.CONFIG_LOAD; }
    "debug"                        { return SmartyTypes.DEBUG; }
    "literal"                      { return SmartyTypes.LITERAL_KW; }
    "nocache"                      { return SmartyTypes.NOCACHE; }
    "setfilter"                    { return SmartyTypes.SETFILTER; }
    "strip"                        { return SmartyTypes.STRIP; }
    "ldelim"                       { return SmartyTypes.LDELIM_KW; }
    "rdelim"                       { return SmartyTypes.RDELIM_KW; }

    // MODIFIERS / FILTERS
    "upper"                        { return SmartyTypes.UPPER; }
    "lower"                        { return SmartyTypes.LOWER; }
    "capitalize"                   { return SmartyTypes.CAPITALIZE; }
    "cat"                          { return SmartyTypes.CAT; }
    "count_paragraphs"             { return SmartyTypes.COUNT_PARAGRAPHS; }
    "count_sentences"              { return SmartyTypes.COUNT_SENTENCES; }
    "count_words"                  { return SmartyTypes.COUNT_WORDS; }
    "date_format"                  { return SmartyTypes.DATE_FORMAT; }
    "escape"                       { return SmartyTypes.ESCAPE; }
    "from_charset"                 { return SmartyTypes.FROM_CHARSET; }
    "indent"                       { return SmartyTypes.INDENT; }
    "nl2br"                        { return SmartyTypes.NL2BR; }
    "regex_replace"                { return SmartyTypes.REGEX_REPLACE; }
    "replace"                      { return SmartyTypes.REPLACE; }
    "spacify"                      { return SmartyTypes.SPACIFY; }
    "string_format"                { return SmartyTypes.STRING_FORMAT; }
    "strip_tags"                   { return SmartyTypes.STRIP_TAGS; }
    "to_charset"                   { return SmartyTypes.TO_CHARSET; }
    "truncate"                     { return SmartyTypes.TRUNCATE; }
    "unescape"                     { return SmartyTypes.UNESCAPE; }
    "wordwrap"                     { return SmartyTypes.WORDWRAP; }

    // BOOLEAN & NULL LITERALS
    "true"|"yes"|"on"              { return SmartyTypes.TRUE; }
    "false"|"no"|"off"             { return SmartyTypes.FALSE; }
    "null"                         { return SmartyTypes.NULL_LITERAL; }

    // AS KEYWORD (for foreach)
    "as"                           { return SmartyTypes.AS; }

    // OPERATORS - LOGICAL
    // The textual forms keep their own token so that {if $a and $b} stays distinguishable from
    // {if $a && $b} in the PSI; the grammar accepts either.
    "&&"                           { return SmartyTypes.AND; }
    "and"                          { return SmartyTypes.AND_KEYWORD; }
    "||"                           { return SmartyTypes.OR; }
    "or"                           { return SmartyTypes.OR_KEYWORD; }
    "!"                            { return SmartyTypes.NOT; }
    "not"                          { return SmartyTypes.NOT_KEYWORD; }

    // OPERATORS - COMPARISON
    "=="                           { return SmartyTypes.EQ; }
    "!="                           { return SmartyTypes.NEQ; }
    "==="                          { return SmartyTypes.EQEQ; }
    "!=="                          { return SmartyTypes.NEQEQ; }
    "<="                           { return SmartyTypes.LE; }
    ">="                           { return SmartyTypes.GE; }
    "<"                            { return SmartyTypes.LT; }
    ">"                            { return SmartyTypes.GT; }

    // OPERATORS - COMPARISON, TEXTUAL FORMS
    "eq"                           { return SmartyTypes.EQ_KEYWORD; }
    "ne"|"neq"                     { return SmartyTypes.NEQ_KEYWORD; }
    "lt"                           { return SmartyTypes.LT_KEYWORD; }
    "gt"                           { return SmartyTypes.GT_KEYWORD; }
    "le"|"lte"                     { return SmartyTypes.LE_KEYWORD; }
    "ge"|"gte"                     { return SmartyTypes.GE_KEYWORD; }
    "matches"                      { return SmartyTypes.MATCHES; }

    // OPERATORS - ARITHMETIC
    "+"                            { return SmartyTypes.PLUS; }
    "-"                            { return SmartyTypes.MINUS; }
    "*"                            { return SmartyTypes.MULT; }
    "/"                            { return SmartyTypes.DIV; }
    "%"                            { return SmartyTypes.MOD; }
    "mod"                          { return SmartyTypes.MOD_KEYWORD; }
    "div"                          { return SmartyTypes.DIV_KEYWORD; }

    // OPERATORS - SPECIAL
    "="                            { return SmartyTypes.ASSIGN; }
    "|"                            { return SmartyTypes.PIPE; }
    "."                            { return SmartyTypes.DOT; }
    "->"                           { return SmartyTypes.ARROW; }
    ":"                            { return SmartyTypes.COLON; }
    "?"                            { return SmartyTypes.QUESTION; }
    ";"                            { return SmartyTypes.SEMICOLON; }

    // BRACKETS
    "("                            { return SmartyTypes.LPAREN; }
    ")"                            { return SmartyTypes.RPAREN; }
    "["                            { return SmartyTypes.LBRACKET; }
    "]"                            { return SmartyTypes.RBRACKET; }

    // SEPARATORS
    ","                            { return SmartyTypes.COMMA; }

    // SPECIAL
    "@"                            { return SmartyTypes.AT; }
    "$"                            { return SmartyTypes.DOLLAR; }
    "#"                            { return SmartyTypes.HASH; }
    "=>"                           { return SmartyTypes.FAT_ARROW; }

    // Variables are NOT lexed as one token: the grammar models them as
    // DOLLAR IDENTIFIER member_access*, which gives each property its own PSI node.

    // NUMBERS
    {Number}                       { return SmartyTypes.NUMBER; }

    // STRINGS
    {String}                       { return SmartyTypes.STRING; }

    // IDENTIFIERS
    {Identifier}                   { return SmartyTypes.IDENTIFIER; }

    // WHITESPACE
    {WhiteSpace}+                  { return SmartyTypes.WS; }
    {LineTerminator}               { return SmartyTypes.WS; }

    // CATCH-ALL for unknown characters
    .                              { return SmartyTypes.BAD_CHARACTER; }
}

// ============================================================================
// EOF
// ============================================================================

<<EOF>> { return null; }
