// This is a generated file. Not intended for manual editing.
package ch.erlebnisbank.smarty.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;

import static ch.erlebnisbank.smarty.psi.SmartyTypes.*;
import static ch.erlebnisbank.smarty.psi.SmartyTypes.*;
//import static ch.erlebnisbank.smarty.parser.SmartyParserUtil.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;

import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;
//import com.intellij.lang.PsiBuilder.Error;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class SmartyParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return root_rule(b, l + 1);
  }

  /* ********************************************************** */
  // LBRACKET [array_element_list] RBRACKET
  public static boolean ARRAY_LITERAL(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ARRAY_LITERAL")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ARRAY_LITERAL, "<array literal>");
    r = consumeToken(b, LBRACKET);
    p = r; // pin = 1
    r = r && report_error_(b, ARRAY_LITERAL_1(b, l + 1));
    r = p && consumeToken(b, RBRACKET) && r;
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // [array_element_list]
  private static boolean ARRAY_LITERAL_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ARRAY_LITERAL_1")) return false;
    array_element_list(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'true' | 'false' | 'yes' | 'no' | 'on' | 'off'
  static boolean BOOLEAN(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BOOLEAN")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, "true");
    if (!r) r = consumeToken(b, "false");
    if (!r) r = consumeToken(b, "yes");
    if (!r) r = consumeToken(b, "no");
    if (!r) r = consumeToken(b, "on");
    if (!r) r = consumeToken(b, "off");
    exit_section_(b, l, m, r, false, SmartyParser::not_pipe_or_rbrace);
    return r;
  }

  /* ********************************************************** */
  // (IDENTIFIER | variable) (',' (IDENTIFIER | variable))*
  static boolean DEBUG_VARS(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "DEBUG_VARS")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = DEBUG_VARS_0(b, l + 1);
    p = r; // pin = 1
    r = r && DEBUG_VARS_1(b, l + 1);
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // IDENTIFIER | variable
  private static boolean DEBUG_VARS_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "DEBUG_VARS_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    if (!r) r = variable(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (',' (IDENTIFIER | variable))*
  private static boolean DEBUG_VARS_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "DEBUG_VARS_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!DEBUG_VARS_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "DEBUG_VARS_1", c)) break;
    }
    return true;
  }

  // ',' (IDENTIFIER | variable)
  private static boolean DEBUG_VARS_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "DEBUG_VARS_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && DEBUG_VARS_1_0_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // IDENTIFIER | variable
  private static boolean DEBUG_VARS_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "DEBUG_VARS_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    if (!r) r = variable(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LPAREN param_list RPAREN
  static boolean FUNCTION_PARAMS(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FUNCTION_PARAMS")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, LPAREN);
    p = r; // pin = 1
    r = r && report_error_(b, param_list(b, l + 1));
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  /* ********************************************************** */
  // 'null'
  static boolean NULL_LITERAL(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "NULL_LITERAL")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, "null");
    exit_section_(b, l, m, r, false, SmartyParser::not_pipe_or_rbrace);
    return r;
  }

  /* ********************************************************** */
  // multiplicative_expr ((PLUS | MINUS) multiplicative_expr)*
  static boolean additive_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "additive_expr")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null, "<expression>");
    r = multiplicative_expr(b, l + 1);
    p = r; // pin = 1
    r = r && additive_expr_1(b, l + 1);
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // ((PLUS | MINUS) multiplicative_expr)*
  private static boolean additive_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "additive_expr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!additive_expr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "additive_expr_1", c)) break;
    }
    return true;
  }

  // (PLUS | MINUS) multiplicative_expr
  private static boolean additive_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "additive_expr_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = additive_expr_1_0_0(b, l + 1);
    p = r; // pin = 1
    r = r && multiplicative_expr(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // PLUS | MINUS
  private static boolean additive_expr_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "additive_expr_1_0_0")) return false;
    boolean r;
    r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, MINUS);
    return r;
  }

  /* ********************************************************** */
  // APPEND variable '=' expr assign_clause*
  public static boolean append_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "append_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, APPEND_STATEMENT, "<append statement>");
    r = consumeToken(b, APPEND);
    p = r; // pin = 1
    r = r && report_error_(b, variable(b, l + 1));
    r = p && report_error_(b, consumeToken(b, ASSIGN)) && r;
    r = p && report_error_(b, expr(b, l + 1)) && r;
    r = p && append_statement_4(b, l + 1) && r;
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // assign_clause*
  private static boolean append_statement_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "append_statement_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!assign_clause(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "append_statement_4", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // named_argument (',' named_argument)*
  //     | positional_argument (',' positional_argument)*
  public static boolean argument_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument_list")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ARGUMENT_LIST, "<argument list>");
    r = argument_list_0(b, l + 1);
    if (!r) r = argument_list_1(b, l + 1);
    exit_section_(b, l, m, r, false, SmartyParser::not_pipe_or_rbrace);
    return r;
  }

  // named_argument (',' named_argument)*
  private static boolean argument_list_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument_list_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = named_argument(b, l + 1);
    p = r; // pin = 1
    r = r && argument_list_0_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (',' named_argument)*
  private static boolean argument_list_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument_list_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!argument_list_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "argument_list_0_1", c)) break;
    }
    return true;
  }

  // ',' named_argument
  private static boolean argument_list_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument_list_0_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && named_argument(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // positional_argument (',' positional_argument)*
  private static boolean argument_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument_list_1")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = positional_argument(b, l + 1);
    p = r; // pin = 1
    r = r && argument_list_1_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (',' positional_argument)*
  private static boolean argument_list_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument_list_1_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!argument_list_1_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "argument_list_1_1", c)) break;
    }
    return true;
  }

  // ',' positional_argument
  private static boolean argument_list_1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument_list_1_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && positional_argument(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // LBRACKET array_index RBRACKET
  public static boolean array_access(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_access")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ARRAY_ACCESS, "<array access>");
    r = consumeToken(b, LBRACKET);
    p = r; // pin = 1
    r = r && report_error_(b, array_index(b, l + 1));
    r = p && consumeToken(b, RBRACKET) && r;
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  /* ********************************************************** */
  // (expr '=>')? expr
  static boolean array_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_element")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = array_element_0(b, l + 1);
    p = r; // pin = 1
    r = r && expr(b, l + 1);
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // (expr '=>')?
  private static boolean array_element_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_element_0")) return false;
    array_element_0_0(b, l + 1);
    return true;
  }

  // expr '=>'
  private static boolean array_element_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_element_0_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = expr(b, l + 1);
    p = r; // pin = 1
    r = r && consumeToken(b, "=>");
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // array_element (',' array_element)* [COMMA]
  static boolean array_element_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_element_list")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = array_element(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, array_element_list_1(b, l + 1));
    r = p && array_element_list_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // (',' array_element)*
  private static boolean array_element_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_element_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!array_element_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "array_element_list_1", c)) break;
    }
    return true;
  }

  // ',' array_element
  private static boolean array_element_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_element_list_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && array_element(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [COMMA]
  private static boolean array_element_list_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_element_list_2")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // expr
  //     | IDENTIFIER
  //     | NUMBER
  static boolean array_index(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_index")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = expr(b, l + 1);
    if (!r) r = consumeToken(b, IDENTIFIER);
    if (!r) r = consumeToken(b, NUMBER);
    exit_section_(b, l, m, r, false, SmartyParser::not_pipe_or_rbrace);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER '=' expr
  static boolean assign_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assign_clause")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeTokens(b, 1, IDENTIFIER, ASSIGN);
    p = r; // pin = 1
    r = r && expr(b, l + 1);
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  /* ********************************************************** */
  // assign_clause (',' assign_clause)*
  static boolean assign_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assign_list")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = assign_clause(b, l + 1);
    p = r; // pin = 1
    r = r && assign_list_1(b, l + 1);
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // (',' assign_clause)*
  private static boolean assign_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assign_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!assign_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "assign_list_1", c)) break;
    }
    return true;
  }

  // ',' assign_clause
  private static boolean assign_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assign_list_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && assign_clause(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // ASSIGN_KW variable '=' expr assign_clause*
  public static boolean assign_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assign_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ASSIGN_STATEMENT, "<assign statement>");
    r = consumeToken(b, ASSIGN_KW);
    p = r; // pin = 1
    r = r && report_error_(b, variable(b, l + 1));
    r = p && report_error_(b, consumeToken(b, ASSIGN)) && r;
    r = p && report_error_(b, expr(b, l + 1)) && r;
    r = p && assign_statement_4(b, l + 1) && r;
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // assign_clause*
  private static boolean assign_statement_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assign_statement_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!assign_clause(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "assign_statement_4", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // variable ASSIGN expr
  static boolean assignment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = variable(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, ASSIGN));
    r = p && expr(b, l + 1) && r;
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  /* ********************************************************** */
  // BLOCK IDENTIFIER [APPEND | PREPEND]
  public static boolean block_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, BLOCK_STATEMENT, "<block statement>");
    r = consumeTokens(b, 1, BLOCK, IDENTIFIER);
    p = r; // pin = 1
    r = r && block_statement_2(b, l + 1);
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // [APPEND | PREPEND]
  private static boolean block_statement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_statement_2")) return false;
    block_statement_2_0(b, l + 1);
    return true;
  }

  // APPEND | PREPEND
  private static boolean block_statement_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_statement_2_0")) return false;
    boolean r;
    r = consumeToken(b, APPEND);
    if (!r) r = consumeToken(b, PREPEND);
    return r;
  }

  /* ********************************************************** */
  // BREAK [NUMBER]
  public static boolean break_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "break_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, BREAK_STATEMENT, "<break statement>");
    r = consumeToken(b, BREAK);
    p = r; // pin = 1
    r = r && break_statement_1(b, l + 1);
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // [NUMBER]
  private static boolean break_statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "break_statement_1")) return false;
    consumeToken(b, NUMBER);
    return true;
  }

  /* ********************************************************** */
  // LPAREN [assign_list] RPAREN
  static boolean call_arguments(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "call_arguments")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, LPAREN);
    p = r; // pin = 1
    r = r && report_error_(b, call_arguments_1(b, l + 1));
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // [assign_list]
  private static boolean call_arguments_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "call_arguments_1")) return false;
    assign_list(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // CALL template_reference [call_arguments]
  public static boolean call_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "call_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CALL_STATEMENT, "<call statement>");
    r = consumeToken(b, CALL);
    p = r; // pin = 1
    r = r && report_error_(b, template_reference(b, l + 1));
    r = p && call_statement_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // [call_arguments]
  private static boolean call_statement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "call_statement_2")) return false;
    call_arguments(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // CAPTURE variable assign_clause*
  public static boolean capture_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "capture_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CAPTURE_STATEMENT, "<capture statement>");
    r = consumeToken(b, CAPTURE);
    p = r; // pin = 1
    r = r && report_error_(b, variable(b, l + 1));
    r = p && capture_statement_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // assign_clause*
  private static boolean capture_statement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "capture_statement_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!assign_clause(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "capture_statement_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // CASE case_value
  public static boolean case_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CASE_STATEMENT, "<case statement>");
    r = consumeToken(b, CASE);
    p = r; // pin = 1
    r = r && case_value(b, l + 1);
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  /* ********************************************************** */
  // expr | DEFAULT
  static boolean case_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = expr(b, l + 1);
    if (!r) r = consumeToken(b, DEFAULT);
    exit_section_(b, l, m, r, false, SmartyParser::not_pipe_or_rbrace);
    return r;
  }

  /* ********************************************************** */
  // !(LCOMMENT_END) .
  static boolean comment_content(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comment_content")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, LCOMMENT_END);
    exit_section_(b, l, m, r, false, SmartyParser::not_pipe_or_rbrace);
    return r;
  }

  /* ********************************************************** */
  // additive_expr ((EQ | NEQ | EQEQ | NEQEQ | LT | GT | LE | GE) additive_expr)*
  static boolean comparison_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comparison_expr")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null, "<expression>");
    r = additive_expr(b, l + 1);
    p = r; // pin = 1
    r = r && comparison_expr_1(b, l + 1);
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // ((EQ | NEQ | EQEQ | NEQEQ | LT | GT | LE | GE) additive_expr)*
  private static boolean comparison_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comparison_expr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!comparison_expr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "comparison_expr_1", c)) break;
    }
    return true;
  }

  // (EQ | NEQ | EQEQ | NEQEQ | LT | GT | LE | GE) additive_expr
  private static boolean comparison_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comparison_expr_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = comparison_expr_1_0_0(b, l + 1);
    p = r; // pin = 1
    r = r && additive_expr(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // EQ | NEQ | EQEQ | NEQEQ | LT | GT | LE | GE
  private static boolean comparison_expr_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comparison_expr_1_0_0")) return false;
    boolean r;
    r = consumeToken(b, EQ);
    if (!r) r = consumeToken(b, NEQ);
    if (!r) r = consumeToken(b, EQEQ);
    if (!r) r = consumeToken(b, NEQEQ);
    if (!r) r = consumeToken(b, LT);
    if (!r) r = consumeToken(b, GT);
    if (!r) r = consumeToken(b, LE);
    if (!r) r = consumeToken(b, GE);
    return r;
  }

  /* ********************************************************** */
  // CONFIG_LOAD STRING [assign_list]
  public static boolean config_load_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "config_load_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CONFIG_LOAD_STATEMENT, "<config load statement>");
    r = consumeTokens(b, 1, CONFIG_LOAD, STRING);
    p = r; // pin = 1
    r = r && config_load_statement_2(b, l + 1);
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // [assign_list]
  private static boolean config_load_statement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "config_load_statement_2")) return false;
    assign_list(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // AT IDENTIFIER
  public static boolean config_variable(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "config_variable")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CONFIG_VARIABLE, "<config variable>");
    r = consumeTokens(b, 1, AT, IDENTIFIER);
    p = r; // pin = 1
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  /* ********************************************************** */
  // CONTINUE [NUMBER]
  public static boolean continue_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "continue_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CONTINUE_STATEMENT, "<continue statement>");
    r = consumeToken(b, CONTINUE);
    p = r; // pin = 1
    r = r && continue_statement_1(b, l + 1);
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // [NUMBER]
  private static boolean continue_statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "continue_statement_1")) return false;
    consumeToken(b, NUMBER);
    return true;
  }

  /* ********************************************************** */
  // DEBUG [DEBUG_VARS]
  public static boolean debug_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "debug_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, DEBUG_STATEMENT, "<debug statement>");
    r = consumeToken(b, DEBUG);
    p = r; // pin = 1
    r = r && debug_statement_1(b, l + 1);
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // [DEBUG_VARS]
  private static boolean debug_statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "debug_statement_1")) return false;
    DEBUG_VARS(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // ELSE
  public static boolean else_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "else_statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ELSE_STATEMENT, "<else statement>");
    r = consumeToken(b, ELSE);
    exit_section_(b, l, m, r, false, SmartyParser::not_pipe_or_rbrace);
    return r;
  }

  /* ********************************************************** */
  // ELSEIF expr assign_clause*
  public static boolean elseif_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "elseif_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ELSEIF_STATEMENT, "<elseif statement>");
    r = consumeToken(b, ELSEIF);
    p = r; // pin = 1
    r = r && report_error_(b, expr(b, l + 1));
    r = p && elseif_statement_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // assign_clause*
  private static boolean elseif_statement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "elseif_statement_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!assign_clause(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "elseif_statement_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // ternary_expr
  public static boolean expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, EXPR, "<expr>");
    r = ternary_expr(b, l + 1);
    exit_section_(b, l, m, r, false, SmartyParser::not_pipe_or_rbrace);
    return r;
  }

  /* ********************************************************** */
  // EXTENDS STRING [assign_list]
  public static boolean extends_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "extends_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, EXTENDS_STATEMENT, "<extends statement>");
    r = consumeTokens(b, 1, EXTENDS, STRING);
    p = r; // pin = 1
    r = r && extends_statement_2(b, l + 1);
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // [assign_list]
  private static boolean extends_statement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "extends_statement_2")) return false;
    assign_list(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // expr
  static boolean for_condition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_condition")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = expr(b, l + 1);
    exit_section_(b, l, m, r, false, SmartyParser::not_pipe_or_rbrace);
    return r;
  }

  /* ********************************************************** */
  // assignment
  static boolean for_increment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_increment")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = assignment(b, l + 1);
    exit_section_(b, l, m, r, false, SmartyParser::not_pipe_or_rbrace);
    return r;
  }

  /* ********************************************************** */
  // assignment
  static boolean for_init(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_init")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = assignment(b, l + 1);
    exit_section_(b, l, m, r, false, SmartyParser::not_pipe_or_rbrace);
    return r;
  }

  /* ********************************************************** */
  // FOR for_init ';' for_condition ';' for_increment assign_clause*
  public static boolean for_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FOR_STATEMENT, "<for loop>");
    r = consumeToken(b, FOR);
    p = r; // pin = 1
    r = r && report_error_(b, for_init(b, l + 1));
    r = p && report_error_(b, consumeToken(b, SEMICOLON)) && r;
    r = p && report_error_(b, for_condition(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, SEMICOLON)) && r;
    r = p && report_error_(b, for_increment(b, l + 1)) && r;
    r = p && for_statement_6(b, l + 1) && r;
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // assign_clause*
  private static boolean for_statement_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_statement_6")) return false;
    while (true) {
      int c = current_position_(b);
      if (!assign_clause(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "for_statement_6", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // FOREACH expression AS dollar_var assign_clause* '}' template_item* '{' '/' FOREACH
  public static boolean foreach_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FOREACH_STATEMENT, "<foreach loop>");
    r = consumeTokens(b, 1, FOREACH, EXPRESSION, AS, DOLLAR_VAR);
    p = r; // pin = 1
    r = r && report_error_(b, foreach_statement_4(b, l + 1));
    r = p && report_error_(b, consumeToken(b, RDELIM)) && r;
    r = p && report_error_(b, foreach_statement_6(b, l + 1)) && r;
    r = p && report_error_(b, consumeTokens(b, -1, LDELIM, DIV, FOREACH)) && r;
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // assign_clause*
  private static boolean foreach_statement_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_statement_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!assign_clause(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "foreach_statement_4", c)) break;
    }
    return true;
  }

  // template_item*
  private static boolean foreach_statement_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_statement_6")) return false;
    while (true) {
      int c = current_position_(b);
      if (!template_item(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "foreach_statement_6", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // FOREACHELSE
  public static boolean foreachelse_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreachelse_statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FOREACHELSE_STATEMENT, "<foreachelse statement>");
    r = consumeToken(b, FOREACHELSE);
    exit_section_(b, l, m, r, false, SmartyParser::not_pipe_or_rbrace);
    return r;
  }

  /* ********************************************************** */
  // if_statement
  //     | elseif_statement
  //     | else_statement
  //     | foreach_statement
  //     | foreachelse_statement
  //     | for_statement
  //     | while_statement
  //     | section_statement
  //     | sectionelse_statement
  //     | switch_statement
  //     | case_statement
  //     | break_statement
  //     | continue_statement
  //     | block_statement
  //     | function_statement
  //     | call_statement
  //     | extends_statement
  //     | include_statement
  //     | insert_statement
  //     | assign_statement
  //     | append_statement
  //     | capture_statement
  //     | config_load_statement
  //     | debug_statement
  //     | setfilter_statement
  //     | ldelim_rdelim_statement
  //     | variable_output
  public static boolean function_body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_body")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_BODY, "<function body>");
    r = if_statement(b, l + 1);
    if (!r) r = elseif_statement(b, l + 1);
    if (!r) r = else_statement(b, l + 1);
    if (!r) r = foreach_statement(b, l + 1);
    if (!r) r = foreachelse_statement(b, l + 1);
    if (!r) r = for_statement(b, l + 1);
    if (!r) r = while_statement(b, l + 1);
    if (!r) r = section_statement(b, l + 1);
    if (!r) r = sectionelse_statement(b, l + 1);
    if (!r) r = switch_statement(b, l + 1);
    if (!r) r = case_statement(b, l + 1);
    if (!r) r = break_statement(b, l + 1);
    if (!r) r = continue_statement(b, l + 1);
    if (!r) r = block_statement(b, l + 1);
    if (!r) r = function_statement(b, l + 1);
    if (!r) r = call_statement(b, l + 1);
    if (!r) r = extends_statement(b, l + 1);
    if (!r) r = include_statement(b, l + 1);
    if (!r) r = insert_statement(b, l + 1);
    if (!r) r = assign_statement(b, l + 1);
    if (!r) r = append_statement(b, l + 1);
    if (!r) r = capture_statement(b, l + 1);
    if (!r) r = config_load_statement(b, l + 1);
    if (!r) r = debug_statement(b, l + 1);
    if (!r) r = setfilter_statement(b, l + 1);
    if (!r) r = ldelim_rdelim_statement(b, l + 1);
    if (!r) r = variable_output(b, l + 1);
    exit_section_(b, l, m, r, false, SmartyParser::not_pipe_or_rbrace);
    return r;
  }

  /* ********************************************************** */
  // function_name LPAREN [argument_list] RPAREN
  public static boolean function_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_CALL, "<function call>");
    r = function_name(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, LPAREN));
    r = p && report_error_(b, function_call_2(b, l + 1)) && r;
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // [argument_list]
  private static boolean function_call_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call_2")) return false;
    argument_list(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // IDENTIFIER
  static boolean function_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_name")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, l, m, r, false, SmartyParser::not_pipe_or_rbrace);
    return r;
  }

  /* ********************************************************** */
  // FUNCTION IDENTIFIER [FUNCTION_PARAMS]
  public static boolean function_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_STATEMENT, "<function statement>");
    r = consumeTokens(b, 1, FUNCTION, IDENTIFIER);
    p = r; // pin = 1
    r = r && function_statement_2(b, l + 1);
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // [FUNCTION_PARAMS]
  private static boolean function_statement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_statement_2")) return false;
    FUNCTION_PARAMS(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // html_tag
  public static boolean html_content(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "html_content")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, HTML_CONTENT, "<html content>");
    r = consumeToken(b, HTML_TAG);
    exit_section_(b, l, m, r, false, SmartyParser::not_pipe_or_rbrace);
    return r;
  }

  /* ********************************************************** */
  // IF expr assign_clause*
  public static boolean if_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, IF_STATEMENT, "<if statement>");
    r = consumeToken(b, IF);
    p = r; // pin = 1
    r = r && report_error_(b, expr(b, l + 1));
    r = p && if_statement_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // assign_clause*
  private static boolean if_statement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_statement_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!assign_clause(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "if_statement_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // INCLUDE STRING [assign_list] [NOCACHE]
  public static boolean include_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "include_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, INCLUDE_STATEMENT, "<include statement>");
    r = consumeTokens(b, 1, INCLUDE, STRING);
    p = r; // pin = 1
    r = r && report_error_(b, include_statement_2(b, l + 1));
    r = p && include_statement_3(b, l + 1) && r;
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // [assign_list]
  private static boolean include_statement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "include_statement_2")) return false;
    assign_list(b, l + 1);
    return true;
  }

  // [NOCACHE]
  private static boolean include_statement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "include_statement_3")) return false;
    consumeToken(b, NOCACHE);
    return true;
  }

  /* ********************************************************** */
  // INSERT STRING [assign_list]
  public static boolean insert_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "insert_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, INSERT_STATEMENT, "<insert statement>");
    r = consumeTokens(b, 1, INSERT, STRING);
    p = r; // pin = 1
    r = r && insert_statement_2(b, l + 1);
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // [assign_list]
  private static boolean insert_statement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "insert_statement_2")) return false;
    assign_list(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // LDELIM_KW | RDELIM_KW
  public static boolean ldelim_rdelim_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ldelim_rdelim_statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LDELIM_RDELIM_STATEMENT, "<ldelim rdelim statement>");
    r = consumeToken(b, LDELIM_KW);
    if (!r) r = consumeToken(b, RDELIM_KW);
    exit_section_(b, l, m, r, false, SmartyParser::not_pipe_or_rbrace);
    return r;
  }

  /* ********************************************************** */
  // NUMBER
  //     | STRING
  //     | BOOLEAN
  //     | NULL_LITERAL
  //     | ARRAY_LITERAL
  public static boolean literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "literal")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LITERAL, "<literal>");
    r = consumeToken(b, NUMBER);
    if (!r) r = consumeToken(b, STRING);
    if (!r) r = BOOLEAN(b, l + 1);
    if (!r) r = NULL_LITERAL(b, l + 1);
    if (!r) r = ARRAY_LITERAL(b, l + 1);
    exit_section_(b, l, m, r, false, SmartyParser::not_pipe_or_rbrace);
    return r;
  }

  /* ********************************************************** */
  // !'{' .
  static boolean literal_content(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "literal_content")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, LDELIM);
    exit_section_(b, l, m, r, false, SmartyParser::not_pipe_or_rbrace);
    return r;
  }

  /* ********************************************************** */
  // logical_not_expr ((AND | AND_KEYWORD) logical_not_expr)*
  static boolean logical_and_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logical_and_expr")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null, "<expression>");
    r = logical_not_expr(b, l + 1);
    p = r; // pin = 1
    r = r && logical_and_expr_1(b, l + 1);
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // ((AND | AND_KEYWORD) logical_not_expr)*
  private static boolean logical_and_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logical_and_expr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!logical_and_expr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "logical_and_expr_1", c)) break;
    }
    return true;
  }

  // (AND | AND_KEYWORD) logical_not_expr
  private static boolean logical_and_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logical_and_expr_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = logical_and_expr_1_0_0(b, l + 1);
    p = r; // pin = 1
    r = r && logical_not_expr(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // AND | AND_KEYWORD
  private static boolean logical_and_expr_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logical_and_expr_1_0_0")) return false;
    boolean r;
    r = consumeToken(b, AND);
    if (!r) r = consumeToken(b, AND_KEYWORD);
    return r;
  }

  /* ********************************************************** */
  // ((NOT | NOT_KEYWORD) logical_not_expr)
  //     | comparison_expr
  static boolean logical_not_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logical_not_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, null, "<expression>");
    r = logical_not_expr_0(b, l + 1);
    if (!r) r = comparison_expr(b, l + 1);
    exit_section_(b, l, m, r, false, SmartyParser::not_pipe_or_rbrace);
    return r;
  }

  // (NOT | NOT_KEYWORD) logical_not_expr
  private static boolean logical_not_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logical_not_expr_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = logical_not_expr_0_0(b, l + 1);
    p = r; // pin = 1
    r = r && logical_not_expr(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // NOT | NOT_KEYWORD
  private static boolean logical_not_expr_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logical_not_expr_0_0")) return false;
    boolean r;
    r = consumeToken(b, NOT);
    if (!r) r = consumeToken(b, NOT_KEYWORD);
    return r;
  }

  /* ********************************************************** */
  // logical_and_expr ((OR | OR_KEYWORD) logical_and_expr)*
  static boolean logical_or_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logical_or_expr")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null, "<expression>");
    r = logical_and_expr(b, l + 1);
    p = r; // pin = 1
    r = r && logical_or_expr_1(b, l + 1);
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // ((OR | OR_KEYWORD) logical_and_expr)*
  private static boolean logical_or_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logical_or_expr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!logical_or_expr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "logical_or_expr_1", c)) break;
    }
    return true;
  }

  // (OR | OR_KEYWORD) logical_and_expr
  private static boolean logical_or_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logical_or_expr_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = logical_or_expr_1_0_0(b, l + 1);
    p = r; // pin = 1
    r = r && logical_and_expr(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // OR | OR_KEYWORD
  private static boolean logical_or_expr_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logical_or_expr_1_0_0")) return false;
    boolean r;
    r = consumeToken(b, OR);
    if (!r) r = consumeToken(b, OR_KEYWORD);
    return r;
  }

  /* ********************************************************** */
  // DOT IDENTIFIER
  //     | ARROW IDENTIFIER
  //     | array_access
  public static boolean member_access(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "member_access")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MEMBER_ACCESS, "<member access>");
    r = parseTokens(b, 1, DOT, IDENTIFIER);
    if (!r) r = parseTokens(b, 1, ARROW, IDENTIFIER);
    if (!r) r = array_access(b, l + 1);
    exit_section_(b, l, m, r, false, SmartyParser::not_pipe_or_rbrace);
    return r;
  }

  /* ********************************************************** */
  // PIPE modifier_name [modifier_arguments]
  public static boolean modifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "modifier")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, MODIFIER, "<modifier>");
    r = consumeToken(b, PIPE);
    p = r; // pin = 1
    r = r && report_error_(b, modifier_name(b, l + 1));
    r = p && modifier_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // [modifier_arguments]
  private static boolean modifier_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "modifier_2")) return false;
    modifier_arguments(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // COLON modifier_param (',' modifier_param)*
  static boolean modifier_arguments(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "modifier_arguments")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COLON);
    p = r; // pin = 1
    r = r && report_error_(b, modifier_param(b, l + 1));
    r = p && modifier_arguments_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // (',' modifier_param)*
  private static boolean modifier_arguments_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "modifier_arguments_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!modifier_arguments_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "modifier_arguments_2", c)) break;
    }
    return true;
  }

  // ',' modifier_param
  private static boolean modifier_arguments_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "modifier_arguments_2_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && modifier_param(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // modifier+
  public static boolean modifier_chain(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "modifier_chain")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MODIFIER_CHAIN, "<modifier chain>");
    r = modifier(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!modifier(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "modifier_chain", c)) break;
    }
    exit_section_(b, l, m, r, false, SmartyParser::not_pipe_or_rbrace);
    return r;
  }

  /* ********************************************************** */
  // modifier (',' modifier)*
  static boolean modifier_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "modifier_list")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = modifier(b, l + 1);
    p = r; // pin = 1
    r = r && modifier_list_1(b, l + 1);
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // (',' modifier)*
  private static boolean modifier_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "modifier_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!modifier_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "modifier_list_1", c)) break;
    }
    return true;
  }

  // ',' modifier
  private static boolean modifier_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "modifier_list_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && modifier(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // UPPER | LOWER | CAPITALIZE | CAT | COUNT_PARAGRAPHS | COUNT_SENTENCES
  //     | COUNT_WORDS | DATE_FORMAT | ESCAPE | FROM_CHARSET | INDENT
  //     | NL2BR | REGEX_REPLACE | REPLACE | SPACIFY | STRING_FORMAT
  //     | STRIP_TAGS | TO_CHARSET | TRUNCATE | UNESCAPE | WORDWRAP | DEFAULT_MOD
  //     | IDENTIFIER
  static boolean modifier_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "modifier_name")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, UPPER);
    if (!r) r = consumeToken(b, LOWER);
    if (!r) r = consumeToken(b, CAPITALIZE);
    if (!r) r = consumeToken(b, CAT);
    if (!r) r = consumeToken(b, COUNT_PARAGRAPHS);
    if (!r) r = consumeToken(b, COUNT_SENTENCES);
    if (!r) r = consumeToken(b, COUNT_WORDS);
    if (!r) r = consumeToken(b, DATE_FORMAT);
    if (!r) r = consumeToken(b, ESCAPE);
    if (!r) r = consumeToken(b, FROM_CHARSET);
    if (!r) r = consumeToken(b, INDENT);
    if (!r) r = consumeToken(b, NL2BR);
    if (!r) r = consumeToken(b, REGEX_REPLACE);
    if (!r) r = consumeToken(b, REPLACE);
    if (!r) r = consumeToken(b, SPACIFY);
    if (!r) r = consumeToken(b, STRING_FORMAT);
    if (!r) r = consumeToken(b, STRIP_TAGS);
    if (!r) r = consumeToken(b, TO_CHARSET);
    if (!r) r = consumeToken(b, TRUNCATE);
    if (!r) r = consumeToken(b, UNESCAPE);
    if (!r) r = consumeToken(b, WORDWRAP);
    if (!r) r = consumeToken(b, DEFAULT_MOD);
    if (!r) r = consumeToken(b, IDENTIFIER);
    exit_section_(b, l, m, r, false, SmartyParser::not_pipe_or_rbrace);
    return r;
  }

  /* ********************************************************** */
  // expr
  static boolean modifier_param(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "modifier_param")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = expr(b, l + 1);
    exit_section_(b, l, m, r, false, SmartyParser::not_pipe_or_rbrace);
    return r;
  }

  /* ********************************************************** */
  // unary_expr ((MULT | DIV | MOD | MOD_KEYWORD | DIV_KEYWORD) unary_expr)*
  static boolean multiplicative_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multiplicative_expr")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null, "<expression>");
    r = unary_expr(b, l + 1);
    p = r; // pin = 1
    r = r && multiplicative_expr_1(b, l + 1);
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // ((MULT | DIV | MOD | MOD_KEYWORD | DIV_KEYWORD) unary_expr)*
  private static boolean multiplicative_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multiplicative_expr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!multiplicative_expr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "multiplicative_expr_1", c)) break;
    }
    return true;
  }

  // (MULT | DIV | MOD | MOD_KEYWORD | DIV_KEYWORD) unary_expr
  private static boolean multiplicative_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multiplicative_expr_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = multiplicative_expr_1_0_0(b, l + 1);
    p = r; // pin = 1
    r = r && unary_expr(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // MULT | DIV | MOD | MOD_KEYWORD | DIV_KEYWORD
  private static boolean multiplicative_expr_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multiplicative_expr_1_0_0")) return false;
    boolean r;
    r = consumeToken(b, MULT);
    if (!r) r = consumeToken(b, DIV);
    if (!r) r = consumeToken(b, MOD);
    if (!r) r = consumeToken(b, MOD_KEYWORD);
    if (!r) r = consumeToken(b, DIV_KEYWORD);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER '=' expr
  static boolean named_argument(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "named_argument")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeTokens(b, 1, IDENTIFIER, ASSIGN);
    p = r; // pin = 1
    r = r && expr(b, l + 1);
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  /* ********************************************************** */
  // !'{' .
  static boolean nocache_content(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "nocache_content")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, LDELIM);
    exit_section_(b, l, m, r, false, SmartyParser::not_pipe_or_rbrace);
    return r;
  }

  /* ********************************************************** */
  // !(PIPE | RDELIM)
  static boolean not_pipe_or_rbrace(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "not_pipe_or_rbrace")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !not_pipe_or_rbrace_0(b, l + 1);
    exit_section_(b, l, m, r, false, SmartyParser::not_pipe_or_rbrace);
    return r;
  }

  // PIPE | RDELIM
  private static boolean not_pipe_or_rbrace_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "not_pipe_or_rbrace_0")) return false;
    boolean r;
    r = consumeToken(b, PIPE);
    if (!r) r = consumeToken(b, RDELIM);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER (',' IDENTIFIER)*
  static boolean param_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "param_list")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, IDENTIFIER);
    p = r; // pin = 1
    r = r && param_list_1(b, l + 1);
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // (',' IDENTIFIER)*
  private static boolean param_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "param_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!param_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "param_list_1", c)) break;
    }
    return true;
  }

  // ',' IDENTIFIER
  private static boolean param_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "param_list_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeTokens(b, 1, COMMA, IDENTIFIER);
    p = r; // pin = 1
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // expr
  static boolean positional_argument(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "positional_argument")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = expr(b, l + 1);
    exit_section_(b, l, m, r, false, SmartyParser::not_pipe_or_rbrace);
    return r;
  }

  /* ********************************************************** */
  // primary_expr (postfix_op)*
  static boolean postfix_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "postfix_expr")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null, "<expression>");
    r = primary_expr(b, l + 1);
    p = r; // pin = 1
    r = r && postfix_expr_1(b, l + 1);
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // (postfix_op)*
  private static boolean postfix_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "postfix_expr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!postfix_expr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "postfix_expr_1", c)) break;
    }
    return true;
  }

  // (postfix_op)
  private static boolean postfix_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "postfix_expr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = postfix_op(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // member_access
  //     | array_access
  //     | method_call
  static boolean postfix_op(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "postfix_op")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = member_access(b, l + 1);
    if (!r) r = array_access(b, l + 1);
    if (!r) r = consumeToken(b, METHOD_CALL);
    exit_section_(b, l, m, r, false, SmartyParser::not_pipe_or_rbrace);
    return r;
  }

  /* ********************************************************** */
  // variable
  //     | literal
  //     | function_call
  //     | config_variable
  //     | LPAREN expr RPAREN
  //     | IDENTIFIER
  static boolean primary_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primary_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, null, "<expression>");
    r = variable(b, l + 1);
    if (!r) r = literal(b, l + 1);
    if (!r) r = function_call(b, l + 1);
    if (!r) r = config_variable(b, l + 1);
    if (!r) r = primary_expr_4(b, l + 1);
    if (!r) r = consumeToken(b, IDENTIFIER);
    exit_section_(b, l, m, r, false, SmartyParser::not_pipe_or_rbrace);
    return r;
  }

  // LPAREN expr RPAREN
  private static boolean primary_expr_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primary_expr_4")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, LPAREN);
    p = r; // pin = 1
    r = r && report_error_(b, expr(b, l + 1));
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // template_item*
  static boolean root_rule(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "root_rule")) return false;
    Marker m = enter_section_(b, l, _NONE_);
    while (true) {
      int c = current_position_(b);
      if (!template_item(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "root_rule", c)) break;
    }
    exit_section_(b, l, m, true, false, SmartyParser::not_pipe_or_rbrace);
    return true;
  }

  /* ********************************************************** */
  // SECTION IDENTIFIER assign_clause*
  public static boolean section_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "section_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SECTION_STATEMENT, "<section loop>");
    r = consumeTokens(b, 1, SECTION, IDENTIFIER);
    p = r; // pin = 1
    r = r && section_statement_2(b, l + 1);
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // assign_clause*
  private static boolean section_statement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "section_statement_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!assign_clause(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "section_statement_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // SECTIONELSE
  public static boolean sectionelse_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sectionelse_statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SECTIONELSE_STATEMENT, "<sectionelse statement>");
    r = consumeToken(b, SECTIONELSE);
    exit_section_(b, l, m, r, false, SmartyParser::not_pipe_or_rbrace);
    return r;
  }

  /* ********************************************************** */
  // SETFILTER modifier_list
  public static boolean setfilter_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "setfilter_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SETFILTER_STATEMENT, "<setfilter statement>");
    r = consumeToken(b, SETFILTER);
    p = r; // pin = 1
    r = r && modifier_list(b, l + 1);
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  /* ********************************************************** */
  // LCOMMENT_START comment_content* LCOMMENT_END
  public static boolean smarty_comment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "smarty_comment")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SMARTY_COMMENT, "<smarty comment>");
    r = consumeToken(b, LCOMMENT_START);
    p = r; // pin = 1
    r = r && report_error_(b, smarty_comment_1(b, l + 1));
    r = p && consumeToken(b, LCOMMENT_END) && r;
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // comment_content*
  private static boolean smarty_comment_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "smarty_comment_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!comment_content(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "smarty_comment_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // '{' function_body '}'
  public static boolean smarty_function_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "smarty_function_call")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SMARTY_FUNCTION_CALL, "<smarty function call>");
    r = consumeToken(b, LDELIM);
    p = r; // pin = 1
    r = r && report_error_(b, function_body(b, l + 1));
    r = p && consumeToken(b, RDELIM) && r;
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  /* ********************************************************** */
  // '{' LITERAL '}' literal_content* '{' '/' LITERAL '}'
  public static boolean smarty_literal_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "smarty_literal_block")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SMARTY_LITERAL_BLOCK, "<smarty literal block>");
    r = consumeTokens(b, 1, LDELIM, LITERAL, RDELIM);
    p = r; // pin = 1
    r = r && report_error_(b, smarty_literal_block_3(b, l + 1));
    r = p && report_error_(b, consumeTokens(b, -1, LDELIM, DIV, LITERAL, RDELIM)) && r;
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // literal_content*
  private static boolean smarty_literal_block_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "smarty_literal_block_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!literal_content(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "smarty_literal_block_3", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // '{' NOCACHE '}' nocache_content* '{' '/' NOCACHE '}'
  public static boolean smarty_nocache_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "smarty_nocache_block")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SMARTY_NOCACHE_BLOCK, "<smarty nocache block>");
    r = consumeTokens(b, 1, LDELIM, NOCACHE, RDELIM);
    p = r; // pin = 1
    r = r && report_error_(b, smarty_nocache_block_3(b, l + 1));
    r = p && report_error_(b, consumeTokens(b, -1, LDELIM, DIV, NOCACHE, RDELIM)) && r;
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // nocache_content*
  private static boolean smarty_nocache_block_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "smarty_nocache_block_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!nocache_content(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "smarty_nocache_block_3", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // smarty_comment
  //     | smarty_function_call
  //     | smarty_literal_block
  //     | smarty_nocache_block
  public static boolean smarty_tag(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "smarty_tag")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SMARTY_TAG, "<smarty tag>");
    r = smarty_comment(b, l + 1);
    if (!r) r = smarty_function_call(b, l + 1);
    if (!r) r = smarty_literal_block(b, l + 1);
    if (!r) r = smarty_nocache_block(b, l + 1);
    exit_section_(b, l, m, r, false, SmartyParser::not_pipe_or_rbrace);
    return r;
  }

  /* ********************************************************** */
  // SWITCH expr
  public static boolean switch_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SWITCH_STATEMENT, "<switch statement>");
    r = consumeToken(b, SWITCH);
    p = r; // pin = 1
    r = r && expr(b, l + 1);
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  /* ********************************************************** */
  // smarty_tag
  //     | text_content
  //     | html_content
  static boolean template_item(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "template_item")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = smarty_tag(b, l + 1);
    if (!r) r = text_content(b, l + 1);
    if (!r) r = html_content(b, l + 1);
    exit_section_(b, l, m, r, false, SmartyParser::not_pipe_or_rbrace);
    return r;
  }

  /* ********************************************************** */
  // (IDENTIFIER | STRING) ('.' IDENTIFIER)*
  static boolean template_reference(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "template_reference")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = template_reference_0(b, l + 1);
    p = r; // pin = 1
    r = r && template_reference_1(b, l + 1);
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // IDENTIFIER | STRING
  private static boolean template_reference_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "template_reference_0")) return false;
    boolean r;
    r = consumeToken(b, IDENTIFIER);
    if (!r) r = consumeToken(b, STRING);
    return r;
  }

  // ('.' IDENTIFIER)*
  private static boolean template_reference_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "template_reference_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!template_reference_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "template_reference_1", c)) break;
    }
    return true;
  }

  // '.' IDENTIFIER
  private static boolean template_reference_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "template_reference_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeTokens(b, 1, DOT, IDENTIFIER);
    p = r; // pin = 1
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // logical_or_expr (QUESTION expr COLON expr)?
  static boolean ternary_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ternary_expr")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null, "<expression>");
    r = logical_or_expr(b, l + 1);
    p = r; // pin = 1
    r = r && ternary_expr_1(b, l + 1);
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // (QUESTION expr COLON expr)?
  private static boolean ternary_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ternary_expr_1")) return false;
    ternary_expr_1_0(b, l + 1);
    return true;
  }

  // QUESTION expr COLON expr
  private static boolean ternary_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ternary_expr_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, QUESTION);
    p = r; // pin = 1
    r = r && report_error_(b, expr(b, l + 1));
    r = p && report_error_(b, consumeToken(b, COLON)) && r;
    r = p && expr(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // ~
  public static boolean text_content(PsiBuilder b, int l) {
    Marker m = enter_section_(b, l, _NONE_, TEXT_CONTENT, null);
    exit_section_(b, l, m, true, false, SmartyParser::not_pipe_or_rbrace);
    return true;
  }

  /* ********************************************************** */
  // ((PLUS | MINUS | NOT | NOT_KEYWORD) unary_expr)
  //     | postfix_expr
  static boolean unary_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, null, "<expression>");
    r = unary_expr_0(b, l + 1);
    if (!r) r = postfix_expr(b, l + 1);
    exit_section_(b, l, m, r, false, SmartyParser::not_pipe_or_rbrace);
    return r;
  }

  // (PLUS | MINUS | NOT | NOT_KEYWORD) unary_expr
  private static boolean unary_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_expr_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = unary_expr_0_0(b, l + 1);
    p = r; // pin = 1
    r = r && unary_expr(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // PLUS | MINUS | NOT | NOT_KEYWORD
  private static boolean unary_expr_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_expr_0_0")) return false;
    boolean r;
    r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, MINUS);
    if (!r) r = consumeToken(b, NOT);
    if (!r) r = consumeToken(b, NOT_KEYWORD);
    return r;
  }

  /* ********************************************************** */
  // DOLLAR variable_name
  public static boolean variable(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, VARIABLE, "<variable>");
    r = consumeToken(b, DOLLAR);
    p = r; // pin = 1
    r = r && variable_name(b, l + 1);
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  /* ********************************************************** */
  // IDENTIFIER member_access*
  static boolean variable_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_name")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, IDENTIFIER);
    p = r; // pin = 1
    r = r && variable_name_1(b, l + 1);
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // member_access*
  private static boolean variable_name_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_name_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!member_access(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "variable_name_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // expression modifier_chain?
  public static boolean variable_output(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_output")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, VARIABLE_OUTPUT, "<variable output>");
    r = consumeToken(b, EXPRESSION);
    p = r; // pin = 1
    r = r && variable_output_1(b, l + 1);
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // modifier_chain?
  private static boolean variable_output_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_output_1")) return false;
    modifier_chain(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // WHILE expr assign_clause*
  public static boolean while_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "while_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, WHILE_STATEMENT, "<while loop>");
    r = consumeToken(b, WHILE);
    p = r; // pin = 1
    r = r && report_error_(b, expr(b, l + 1));
    r = p && while_statement_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, SmartyParser::not_pipe_or_rbrace);
    return r || p;
  }

  // assign_clause*
  private static boolean while_statement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "while_statement_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!assign_clause(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "while_statement_2", c)) break;
    }
    return true;
  }

}
