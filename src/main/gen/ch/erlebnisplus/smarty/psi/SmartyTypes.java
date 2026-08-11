// This is a generated file. Not intended for manual editing.
package ch.erlebnisplus.smarty.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import ch.erlebnisplus.smarty.psi.impl.*;

public interface SmartyTypes {

  IElementType APPEND_STATEMENT = new SmartyElementType("APPEND_STATEMENT");
  IElementType ARGUMENT_LIST = new SmartyElementType("ARGUMENT_LIST");
  IElementType ARRAY_ACCESS = new SmartyElementType("ARRAY_ACCESS");
  IElementType ARRAY_LITERAL = new SmartyElementType("ARRAY_LITERAL");
  IElementType ASSIGN_STATEMENT = new SmartyElementType("ASSIGN_STATEMENT");
  IElementType BLOCK_STATEMENT = new SmartyElementType("BLOCK_STATEMENT");
  IElementType BREAK_STATEMENT = new SmartyElementType("BREAK_STATEMENT");
  IElementType CALL_STATEMENT = new SmartyElementType("CALL_STATEMENT");
  IElementType CAPTURE_STATEMENT = new SmartyElementType("CAPTURE_STATEMENT");
  IElementType CASE_STATEMENT = new SmartyElementType("CASE_STATEMENT");
  IElementType CONFIG_LOAD_STATEMENT = new SmartyElementType("CONFIG_LOAD_STATEMENT");
  IElementType CONFIG_VARIABLE = new SmartyElementType("CONFIG_VARIABLE");
  IElementType CONTINUE_STATEMENT = new SmartyElementType("CONTINUE_STATEMENT");
  IElementType DEBUG_STATEMENT = new SmartyElementType("DEBUG_STATEMENT");
  IElementType ELSEIF_STATEMENT = new SmartyElementType("ELSEIF_STATEMENT");
  IElementType ELSE_STATEMENT = new SmartyElementType("ELSE_STATEMENT");
  IElementType EXPR = new SmartyElementType("EXPR");
  IElementType EXTENDS_STATEMENT = new SmartyElementType("EXTENDS_STATEMENT");
  IElementType FOREACHELSE_STATEMENT = new SmartyElementType("FOREACHELSE_STATEMENT");
  IElementType FOREACH_STATEMENT = new SmartyElementType("FOREACH_STATEMENT");
  IElementType FOR_STATEMENT = new SmartyElementType("FOR_STATEMENT");
  IElementType FUNCTION_BODY = new SmartyElementType("FUNCTION_BODY");
  IElementType FUNCTION_CALL = new SmartyElementType("FUNCTION_CALL");
  IElementType FUNCTION_STATEMENT = new SmartyElementType("FUNCTION_STATEMENT");
  IElementType IF_STATEMENT = new SmartyElementType("IF_STATEMENT");
  IElementType INCLUDE_STATEMENT = new SmartyElementType("INCLUDE_STATEMENT");
  IElementType INSERT_STATEMENT = new SmartyElementType("INSERT_STATEMENT");
  IElementType LDELIM_RDELIM_STATEMENT = new SmartyElementType("LDELIM_RDELIM_STATEMENT");
  IElementType LITERAL = new SmartyElementType("LITERAL");
  IElementType MEMBER_ACCESS = new SmartyElementType("MEMBER_ACCESS");
  IElementType MODIFIER = new SmartyElementType("MODIFIER");
  IElementType PLUGIN_CALL_STATEMENT = new SmartyElementType("PLUGIN_CALL_STATEMENT");
  IElementType SECTIONELSE_STATEMENT = new SmartyElementType("SECTIONELSE_STATEMENT");
  IElementType SECTION_STATEMENT = new SmartyElementType("SECTION_STATEMENT");
  IElementType SETFILTER_STATEMENT = new SmartyElementType("SETFILTER_STATEMENT");
  IElementType SMARTY_CLOSING_TAG = new SmartyElementType("SMARTY_CLOSING_TAG");
  IElementType SMARTY_FUNCTION_CALL = new SmartyElementType("SMARTY_FUNCTION_CALL");
  IElementType SMARTY_LITERAL_BLOCK = new SmartyElementType("SMARTY_LITERAL_BLOCK");
  IElementType SMARTY_NOCACHE_BLOCK = new SmartyElementType("SMARTY_NOCACHE_BLOCK");
  IElementType SMARTY_TAG = new SmartyElementType("SMARTY_TAG");
  IElementType STRIP_STATEMENT = new SmartyElementType("STRIP_STATEMENT");
  IElementType SWITCH_STATEMENT = new SmartyElementType("SWITCH_STATEMENT");
  IElementType TEXT_CONTENT = new SmartyElementType("TEXT_CONTENT");
  IElementType VARIABLE = new SmartyElementType("VARIABLE");
  IElementType VARIABLE_OUTPUT = new SmartyElementType("VARIABLE_OUTPUT");
  IElementType WHILE_STATEMENT = new SmartyElementType("WHILE_STATEMENT");

  IElementType AND = new SmartyTokenType("&&");
  IElementType AND_KEYWORD = new SmartyTokenType("and");
  IElementType APPEND = new SmartyTokenType("append");
  IElementType ARROW = new SmartyTokenType("->");
  IElementType AS = new SmartyTokenType("AS");
  IElementType ASSIGN = new SmartyTokenType("=");
  IElementType ASSIGN_KW = new SmartyTokenType("assign");
  IElementType AT = new SmartyTokenType("@");
  IElementType BAD_CHARACTER = new SmartyTokenType("bad_character");
  IElementType BLOCK = new SmartyTokenType("block");
  IElementType BREAK = new SmartyTokenType("break");
  IElementType CALL = new SmartyTokenType("call");
  IElementType CAPITALIZE = new SmartyTokenType("capitalize");
  IElementType CAPTURE = new SmartyTokenType("capture");
  IElementType CASE = new SmartyTokenType("case");
  IElementType CAT = new SmartyTokenType("cat");
  IElementType COLON = new SmartyTokenType(":");
  IElementType COMMA = new SmartyTokenType(",");
  IElementType COMMENT = new SmartyTokenType("comment");
  IElementType CONFIG_LOAD = new SmartyTokenType("config_load");
  IElementType CONTINUE = new SmartyTokenType("continue");
  IElementType COUNT_PARAGRAPHS = new SmartyTokenType("count_paragraphs");
  IElementType COUNT_SENTENCES = new SmartyTokenType("count_sentences");
  IElementType COUNT_WORDS = new SmartyTokenType("count_words");
  IElementType DATE_FORMAT = new SmartyTokenType("date_format");
  IElementType DEBUG = new SmartyTokenType("debug");
  IElementType DEFAULT = new SmartyTokenType("default");
  IElementType DIV = new SmartyTokenType("/");
  IElementType DIV_KEYWORD = new SmartyTokenType("div");
  IElementType DOLLAR = new SmartyTokenType("$");
  IElementType DOT = new SmartyTokenType(".");
  IElementType ELSE = new SmartyTokenType("else");
  IElementType ELSEIF = new SmartyTokenType("elseif");
  IElementType EQ = new SmartyTokenType("==");
  IElementType EQEQ = new SmartyTokenType("===");
  IElementType EQ_KEYWORD = new SmartyTokenType("eq");
  IElementType ESCAPE = new SmartyTokenType("escape");
  IElementType EXTENDS = new SmartyTokenType("extends");
  IElementType FALSE = new SmartyTokenType("false");
  IElementType FAT_ARROW = new SmartyTokenType("=>");
  IElementType FOR = new SmartyTokenType("for");
  IElementType FOREACH = new SmartyTokenType("foreach");
  IElementType FOREACHELSE = new SmartyTokenType("foreachelse");
  IElementType FROM_CHARSET = new SmartyTokenType("from_charset");
  IElementType FUNCTION = new SmartyTokenType("function");
  IElementType GE = new SmartyTokenType(">=");
  IElementType GE_KEYWORD = new SmartyTokenType("gte");
  IElementType GT = new SmartyTokenType(">");
  IElementType GT_KEYWORD = new SmartyTokenType("gt");
  IElementType HASH = new SmartyTokenType("#");
  IElementType IDENTIFIER = new SmartyTokenType("IDENTIFIER");
  IElementType IF = new SmartyTokenType("if");
  IElementType INCLUDE = new SmartyTokenType("include");
  IElementType INDENT = new SmartyTokenType("indent");
  IElementType INSERT = new SmartyTokenType("insert");
  IElementType LBRACKET = new SmartyTokenType("[");
  IElementType LDELIM = new SmartyTokenType("{");
  IElementType LDELIM_KW = new SmartyTokenType("ldelim");
  IElementType LE = new SmartyTokenType("<=");
  IElementType LE_KEYWORD = new SmartyTokenType("lte");
  IElementType LITERAL_KW = new SmartyTokenType("literal");
  IElementType LOWER = new SmartyTokenType("lower");
  IElementType LPAREN = new SmartyTokenType("(");
  IElementType LT = new SmartyTokenType("<");
  IElementType LT_KEYWORD = new SmartyTokenType("lt");
  IElementType MATCHES = new SmartyTokenType("matches");
  IElementType MINUS = new SmartyTokenType("-");
  IElementType MOD = new SmartyTokenType("%");
  IElementType MOD_KEYWORD = new SmartyTokenType("mod");
  IElementType MULT = new SmartyTokenType("*");
  IElementType NEQ = new SmartyTokenType("!=");
  IElementType NEQEQ = new SmartyTokenType("!==");
  IElementType NEQ_KEYWORD = new SmartyTokenType("neq");
  IElementType NL2BR = new SmartyTokenType("nl2br");
  IElementType NOCACHE = new SmartyTokenType("nocache");
  IElementType NOT = new SmartyTokenType("!");
  IElementType NOT_KEYWORD = new SmartyTokenType("not");
  IElementType NULL_LITERAL = new SmartyTokenType("null");
  IElementType NUMBER = new SmartyTokenType("NUMBER");
  IElementType OR = new SmartyTokenType("||");
  IElementType OR_KEYWORD = new SmartyTokenType("or");
  IElementType PIPE = new SmartyTokenType("|");
  IElementType PLUS = new SmartyTokenType("+");
  IElementType PREPEND = new SmartyTokenType("prepend");
  IElementType QUESTION = new SmartyTokenType("?");
  IElementType RBRACKET = new SmartyTokenType("]");
  IElementType RDELIM = new SmartyTokenType("}");
  IElementType RDELIM_KW = new SmartyTokenType("rdelim");
  IElementType REGEX_REPLACE = new SmartyTokenType("regex_replace");
  IElementType REPLACE = new SmartyTokenType("replace");
  IElementType RPAREN = new SmartyTokenType(")");
  IElementType SECTION = new SmartyTokenType("section");
  IElementType SECTIONELSE = new SmartyTokenType("sectionelse");
  IElementType SEMICOLON = new SmartyTokenType(";");
  IElementType SETFILTER = new SmartyTokenType("setfilter");
  IElementType SPACIFY = new SmartyTokenType("spacify");
  IElementType STRING = new SmartyTokenType("STRING");
  IElementType STRING_FORMAT = new SmartyTokenType("string_format");
  IElementType STRIP = new SmartyTokenType("strip");
  IElementType STRIP_TAGS = new SmartyTokenType("strip_tags");
  IElementType SWITCH = new SmartyTokenType("switch");
  IElementType TEXT = new SmartyTokenType("text");
  IElementType TO_CHARSET = new SmartyTokenType("to_charset");
  IElementType TRUE = new SmartyTokenType("true");
  IElementType TRUNCATE = new SmartyTokenType("truncate");
  IElementType UNESCAPE = new SmartyTokenType("unescape");
  IElementType UPPER = new SmartyTokenType("upper");
  IElementType WHILE = new SmartyTokenType("while");
  IElementType WORDWRAP = new SmartyTokenType("wordwrap");
  IElementType WS = new SmartyTokenType("ws");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == APPEND_STATEMENT) {
        return new AppendStatementImpl(node);
      }
      else if (type == ARGUMENT_LIST) {
        return new ArgumentListImpl(node);
      }
      else if (type == ARRAY_ACCESS) {
        return new ArrayAccessImpl(node);
      }
      else if (type == ARRAY_LITERAL) {
        return new ArrayLiteralImpl(node);
      }
      else if (type == ASSIGN_STATEMENT) {
        return new AssignStatementImpl(node);
      }
      else if (type == BLOCK_STATEMENT) {
        return new BlockStatementImpl(node);
      }
      else if (type == BREAK_STATEMENT) {
        return new BreakStatementImpl(node);
      }
      else if (type == CALL_STATEMENT) {
        return new CallStatementImpl(node);
      }
      else if (type == CAPTURE_STATEMENT) {
        return new CaptureStatementImpl(node);
      }
      else if (type == CASE_STATEMENT) {
        return new CaseStatementImpl(node);
      }
      else if (type == CONFIG_LOAD_STATEMENT) {
        return new ConfigLoadStatementImpl(node);
      }
      else if (type == CONFIG_VARIABLE) {
        return new ConfigVariableImpl(node);
      }
      else if (type == CONTINUE_STATEMENT) {
        return new ContinueStatementImpl(node);
      }
      else if (type == DEBUG_STATEMENT) {
        return new DebugStatementImpl(node);
      }
      else if (type == ELSEIF_STATEMENT) {
        return new ElseifStatementImpl(node);
      }
      else if (type == ELSE_STATEMENT) {
        return new ElseStatementImpl(node);
      }
      else if (type == EXPR) {
        return new ExprImpl(node);
      }
      else if (type == EXTENDS_STATEMENT) {
        return new ExtendsStatementImpl(node);
      }
      else if (type == FOREACHELSE_STATEMENT) {
        return new ForeachelseStatementImpl(node);
      }
      else if (type == FOREACH_STATEMENT) {
        return new ForeachStatementImpl(node);
      }
      else if (type == FOR_STATEMENT) {
        return new ForStatementImpl(node);
      }
      else if (type == FUNCTION_BODY) {
        return new FunctionBodyImpl(node);
      }
      else if (type == FUNCTION_CALL) {
        return new FunctionCallImpl(node);
      }
      else if (type == FUNCTION_STATEMENT) {
        return new FunctionStatementImpl(node);
      }
      else if (type == IF_STATEMENT) {
        return new IfStatementImpl(node);
      }
      else if (type == INCLUDE_STATEMENT) {
        return new IncludeStatementImpl(node);
      }
      else if (type == INSERT_STATEMENT) {
        return new InsertStatementImpl(node);
      }
      else if (type == LDELIM_RDELIM_STATEMENT) {
        return new LdelimRdelimStatementImpl(node);
      }
      else if (type == LITERAL) {
        return new LiteralImpl(node);
      }
      else if (type == MEMBER_ACCESS) {
        return new MemberAccessImpl(node);
      }
      else if (type == MODIFIER) {
        return new ModifierImpl(node);
      }
      else if (type == PLUGIN_CALL_STATEMENT) {
        return new PluginCallStatementImpl(node);
      }
      else if (type == SECTIONELSE_STATEMENT) {
        return new SectionelseStatementImpl(node);
      }
      else if (type == SECTION_STATEMENT) {
        return new SectionStatementImpl(node);
      }
      else if (type == SETFILTER_STATEMENT) {
        return new SetfilterStatementImpl(node);
      }
      else if (type == SMARTY_CLOSING_TAG) {
        return new SmartyClosingTagImpl(node);
      }
      else if (type == SMARTY_FUNCTION_CALL) {
        return new SmartyFunctionCallImpl(node);
      }
      else if (type == SMARTY_LITERAL_BLOCK) {
        return new SmartyLiteralBlockImpl(node);
      }
      else if (type == SMARTY_NOCACHE_BLOCK) {
        return new SmartyNocacheBlockImpl(node);
      }
      else if (type == SMARTY_TAG) {
        return new SmartyTagImpl(node);
      }
      else if (type == STRIP_STATEMENT) {
        return new StripStatementImpl(node);
      }
      else if (type == SWITCH_STATEMENT) {
        return new SwitchStatementImpl(node);
      }
      else if (type == TEXT_CONTENT) {
        return new TextContentImpl(node);
      }
      else if (type == VARIABLE) {
        return new VariableImpl(node);
      }
      else if (type == VARIABLE_OUTPUT) {
        return new VariableOutputImpl(node);
      }
      else if (type == WHILE_STATEMENT) {
        return new WhileStatementImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
