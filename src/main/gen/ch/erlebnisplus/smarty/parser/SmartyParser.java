// This is a generated file. Not intended for manual editing.
package ch.erlebnisplus.smarty.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static ch.erlebnisplus.smarty.psi.SmartyTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class SmartyParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType root_, PsiBuilder builder_) {
    parseLight(root_, builder_);
    return builder_.getTreeBuilt();
  }

  public void parseLight(IElementType root_, PsiBuilder builder_) {
    boolean result_;
    builder_ = adapt_builder_(root_, builder_, this, null);
    Marker marker_ = enter_section_(builder_, 0, _COLLAPSE_, null);
    result_ = parse_root_(root_, builder_);
    exit_section_(builder_, 0, marker_, root_, result_, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType root_, PsiBuilder builder_) {
    return parse_root_(root_, builder_, 0);
  }

  static boolean parse_root_(IElementType root_, PsiBuilder builder_, int level_) {
    return root_rule(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // LBRACKET [array_element_list] RBRACKET
  public static boolean ARRAY_LITERAL(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ARRAY_LITERAL")) return false;
    if (!nextTokenIs(builder_, LBRACKET)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LBRACKET);
    result_ = result_ && ARRAY_LITERAL_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACKET);
    exit_section_(builder_, marker_, ARRAY_LITERAL, result_);
    return result_;
  }

  // [array_element_list]
  private static boolean ARRAY_LITERAL_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ARRAY_LITERAL_1")) return false;
    array_element_list(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // TRUE | FALSE
  static boolean BOOLEAN(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "BOOLEAN")) return false;
    if (!nextTokenIs(builder_, "", FALSE, TRUE)) return false;
    boolean result_;
    result_ = consumeToken(builder_, TRUE);
    if (!result_) result_ = consumeToken(builder_, FALSE);
    return result_;
  }

  /* ********************************************************** */
  // (IDENTIFIER | variable) (',' (IDENTIFIER | variable))*
  static boolean DEBUG_VARS(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "DEBUG_VARS")) return false;
    if (!nextTokenIs(builder_, "", DOLLAR, IDENTIFIER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = DEBUG_VARS_0(builder_, level_ + 1);
    result_ = result_ && DEBUG_VARS_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // IDENTIFIER | variable
  private static boolean DEBUG_VARS_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "DEBUG_VARS_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = variable(builder_, level_ + 1);
    return result_;
  }

  // (',' (IDENTIFIER | variable))*
  private static boolean DEBUG_VARS_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "DEBUG_VARS_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!DEBUG_VARS_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "DEBUG_VARS_1", pos_)) break;
    }
    return true;
  }

  // ',' (IDENTIFIER | variable)
  private static boolean DEBUG_VARS_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "DEBUG_VARS_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && DEBUG_VARS_1_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // IDENTIFIER | variable
  private static boolean DEBUG_VARS_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "DEBUG_VARS_1_0_1")) return false;
    boolean result_;
    result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = variable(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // LPAREN param_list RPAREN
  static boolean FUNCTION_PARAMS(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "FUNCTION_PARAMS")) return false;
    if (!nextTokenIs(builder_, LPAREN)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LPAREN);
    result_ = result_ && param_list(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAREN);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // multiplicative_expr ((PLUS | MINUS) multiplicative_expr)*
  static boolean additive_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "additive_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null, "<expression>");
    result_ = multiplicative_expr(builder_, level_ + 1);
    result_ = result_ && additive_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // ((PLUS | MINUS) multiplicative_expr)*
  private static boolean additive_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "additive_expr_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!additive_expr_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "additive_expr_1", pos_)) break;
    }
    return true;
  }

  // (PLUS | MINUS) multiplicative_expr
  private static boolean additive_expr_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "additive_expr_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = additive_expr_1_0_0(builder_, level_ + 1);
    result_ = result_ && multiplicative_expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // PLUS | MINUS
  private static boolean additive_expr_1_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "additive_expr_1_0_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, PLUS);
    if (!result_) result_ = consumeToken(builder_, MINUS);
    return result_;
  }

  /* ********************************************************** */
  // APPEND variable '=' expr assign_clause*
  public static boolean append_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "append_statement")) return false;
    if (!nextTokenIs(builder_, "<append statement>", APPEND)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, APPEND_STATEMENT, "<append statement>");
    result_ = consumeToken(builder_, APPEND);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, variable(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, ASSIGN)) && result_;
    result_ = pinned_ && report_error_(builder_, expr(builder_, level_ + 1)) && result_;
    result_ = pinned_ && append_statement_4(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // assign_clause*
  private static boolean append_statement_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "append_statement_4")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!assign_clause(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "append_statement_4", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // named_argument (',' named_argument)*
  //     | positional_argument (',' positional_argument)*
  public static boolean argument_list(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "argument_list")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ARGUMENT_LIST, "<argument list>");
    result_ = argument_list_0(builder_, level_ + 1);
    if (!result_) result_ = argument_list_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // named_argument (',' named_argument)*
  private static boolean argument_list_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "argument_list_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = named_argument(builder_, level_ + 1);
    result_ = result_ && argument_list_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (',' named_argument)*
  private static boolean argument_list_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "argument_list_0_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!argument_list_0_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "argument_list_0_1", pos_)) break;
    }
    return true;
  }

  // ',' named_argument
  private static boolean argument_list_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "argument_list_0_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && named_argument(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // positional_argument (',' positional_argument)*
  private static boolean argument_list_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "argument_list_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = positional_argument(builder_, level_ + 1);
    result_ = result_ && argument_list_1_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (',' positional_argument)*
  private static boolean argument_list_1_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "argument_list_1_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!argument_list_1_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "argument_list_1_1", pos_)) break;
    }
    return true;
  }

  // ',' positional_argument
  private static boolean argument_list_1_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "argument_list_1_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && positional_argument(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // LBRACKET array_index RBRACKET
  public static boolean array_access(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_access")) return false;
    if (!nextTokenIs(builder_, LBRACKET)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LBRACKET);
    result_ = result_ && array_index(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACKET);
    exit_section_(builder_, marker_, ARRAY_ACCESS, result_);
    return result_;
  }

  /* ********************************************************** */
  // (expr '=>')? expr
  static boolean array_element(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_element")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = array_element_0(builder_, level_ + 1);
    result_ = result_ && expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (expr '=>')?
  private static boolean array_element_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_element_0")) return false;
    array_element_0_0(builder_, level_ + 1);
    return true;
  }

  // expr '=>'
  private static boolean array_element_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_element_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = expr(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, FAT_ARROW);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // array_element (',' array_element)* [COMMA]
  static boolean array_element_list(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_element_list")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = array_element(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, array_element_list_1(builder_, level_ + 1));
    result_ = pinned_ && array_element_list_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // (',' array_element)*
  private static boolean array_element_list_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_element_list_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!array_element_list_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "array_element_list_1", pos_)) break;
    }
    return true;
  }

  // ',' array_element
  private static boolean array_element_list_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_element_list_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && array_element(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [COMMA]
  private static boolean array_element_list_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_element_list_2")) return false;
    consumeToken(builder_, COMMA);
    return true;
  }

  /* ********************************************************** */
  // expr
  //     | IDENTIFIER
  //     | NUMBER
  static boolean array_index(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_index")) return false;
    boolean result_;
    result_ = expr(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = consumeToken(builder_, NUMBER);
    return result_;
  }

  /* ********************************************************** */
  // IDENTIFIER '=' expr
  static boolean assign_clause(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "assign_clause")) return false;
    if (!nextTokenIs(builder_, IDENTIFIER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, IDENTIFIER, ASSIGN);
    result_ = result_ && expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // assign_clause (',' assign_clause)*
  static boolean assign_list(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "assign_list")) return false;
    if (!nextTokenIs(builder_, IDENTIFIER)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = assign_clause(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && assign_list_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // (',' assign_clause)*
  private static boolean assign_list_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "assign_list_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!assign_list_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "assign_list_1", pos_)) break;
    }
    return true;
  }

  // ',' assign_clause
  private static boolean assign_list_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "assign_list_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && assign_clause(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // ASSIGN_KW variable '=' expr assign_clause*
  public static boolean assign_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "assign_statement")) return false;
    if (!nextTokenIs(builder_, "<assign statement>", ASSIGN_KW)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ASSIGN_STATEMENT, "<assign statement>");
    result_ = consumeToken(builder_, ASSIGN_KW);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, variable(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, ASSIGN)) && result_;
    result_ = pinned_ && report_error_(builder_, expr(builder_, level_ + 1)) && result_;
    result_ = pinned_ && assign_statement_4(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // assign_clause*
  private static boolean assign_statement_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "assign_statement_4")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!assign_clause(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "assign_statement_4", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // variable ASSIGN expr
  static boolean assignment(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "assignment")) return false;
    if (!nextTokenIs(builder_, DOLLAR)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = variable(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, ASSIGN);
    result_ = result_ && expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // BLOCK declaration_name [APPEND | PREPEND] assign_clause*
  public static boolean block_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block_statement")) return false;
    if (!nextTokenIs(builder_, BLOCK)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, BLOCK_STATEMENT, null);
    result_ = consumeToken(builder_, BLOCK);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, declaration_name(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, block_statement_2(builder_, level_ + 1)) && result_;
    result_ = pinned_ && block_statement_3(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [APPEND | PREPEND]
  private static boolean block_statement_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block_statement_2")) return false;
    block_statement_2_0(builder_, level_ + 1);
    return true;
  }

  // APPEND | PREPEND
  private static boolean block_statement_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block_statement_2_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, APPEND);
    if (!result_) result_ = consumeToken(builder_, PREPEND);
    return result_;
  }

  // assign_clause*
  private static boolean block_statement_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block_statement_3")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!assign_clause(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "block_statement_3", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // BREAK [NUMBER]
  public static boolean break_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "break_statement")) return false;
    if (!nextTokenIs(builder_, BREAK)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, BREAK);
    result_ = result_ && break_statement_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, BREAK_STATEMENT, result_);
    return result_;
  }

  // [NUMBER]
  private static boolean break_statement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "break_statement_1")) return false;
    consumeToken(builder_, NUMBER);
    return true;
  }

  /* ********************************************************** */
  // LPAREN [assign_list] RPAREN
  static boolean call_arguments(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "call_arguments")) return false;
    if (!nextTokenIs(builder_, LPAREN)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LPAREN);
    result_ = result_ && call_arguments_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAREN);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [assign_list]
  private static boolean call_arguments_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "call_arguments_1")) return false;
    assign_list(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // CALL template_reference [call_arguments]
  public static boolean call_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "call_statement")) return false;
    if (!nextTokenIs(builder_, CALL)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CALL_STATEMENT, null);
    result_ = consumeToken(builder_, CALL);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, template_reference(builder_, level_ + 1));
    result_ = pinned_ && call_statement_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [call_arguments]
  private static boolean call_statement_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "call_statement_2")) return false;
    call_arguments(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // CAPTURE variable assign_clause*
  public static boolean capture_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "capture_statement")) return false;
    if (!nextTokenIs(builder_, "<capture statement>", CAPTURE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CAPTURE_STATEMENT, "<capture statement>");
    result_ = consumeToken(builder_, CAPTURE);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, variable(builder_, level_ + 1));
    result_ = pinned_ && capture_statement_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // assign_clause*
  private static boolean capture_statement_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "capture_statement_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!assign_clause(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "capture_statement_2", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // CASE case_value
  public static boolean case_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "case_statement")) return false;
    if (!nextTokenIs(builder_, CASE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CASE_STATEMENT, null);
    result_ = consumeToken(builder_, CASE);
    pinned_ = result_; // pin = 1
    result_ = result_ && case_value(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // expr | DEFAULT
  static boolean case_value(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "case_value")) return false;
    boolean result_;
    result_ = expr(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, DEFAULT);
    return result_;
  }

  /* ********************************************************** */
  // IF | FOREACH | FOR | WHILE | SECTION | SWITCH | BLOCK | FUNCTION
  //     | CAPTURE | STRIP | SETFILTER | NOCACHE | LITERAL_KW
  static boolean closing_keyword(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "closing_keyword")) return false;
    boolean result_;
    result_ = consumeToken(builder_, IF);
    if (!result_) result_ = consumeToken(builder_, FOREACH);
    if (!result_) result_ = consumeToken(builder_, FOR);
    if (!result_) result_ = consumeToken(builder_, WHILE);
    if (!result_) result_ = consumeToken(builder_, SECTION);
    if (!result_) result_ = consumeToken(builder_, SWITCH);
    if (!result_) result_ = consumeToken(builder_, BLOCK);
    if (!result_) result_ = consumeToken(builder_, FUNCTION);
    if (!result_) result_ = consumeToken(builder_, CAPTURE);
    if (!result_) result_ = consumeToken(builder_, STRIP);
    if (!result_) result_ = consumeToken(builder_, SETFILTER);
    if (!result_) result_ = consumeToken(builder_, NOCACHE);
    if (!result_) result_ = consumeToken(builder_, LITERAL_KW);
    return result_;
  }

  /* ********************************************************** */
  // additive_expr (comparison_operator additive_expr)*
  static boolean comparison_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "comparison_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null, "<expression>");
    result_ = additive_expr(builder_, level_ + 1);
    result_ = result_ && comparison_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (comparison_operator additive_expr)*
  private static boolean comparison_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "comparison_expr_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!comparison_expr_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "comparison_expr_1", pos_)) break;
    }
    return true;
  }

  // comparison_operator additive_expr
  private static boolean comparison_expr_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "comparison_expr_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = comparison_operator(builder_, level_ + 1);
    result_ = result_ && additive_expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // EQ | NEQ | EQEQ | NEQEQ | LT | GT | LE | GE
  //     | EQ_KEYWORD | NEQ_KEYWORD | LT_KEYWORD | GT_KEYWORD | LE_KEYWORD | GE_KEYWORD
  //     | MATCHES
  static boolean comparison_operator(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "comparison_operator")) return false;
    boolean result_;
    result_ = consumeToken(builder_, EQ);
    if (!result_) result_ = consumeToken(builder_, NEQ);
    if (!result_) result_ = consumeToken(builder_, EQEQ);
    if (!result_) result_ = consumeToken(builder_, NEQEQ);
    if (!result_) result_ = consumeToken(builder_, LT);
    if (!result_) result_ = consumeToken(builder_, GT);
    if (!result_) result_ = consumeToken(builder_, LE);
    if (!result_) result_ = consumeToken(builder_, GE);
    if (!result_) result_ = consumeToken(builder_, EQ_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, NEQ_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, LT_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, GT_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, LE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, GE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, MATCHES);
    return result_;
  }

  /* ********************************************************** */
  // CONFIG_LOAD [STRING] (assign_clause | section_attribute)*
  public static boolean config_load_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "config_load_statement")) return false;
    if (!nextTokenIs(builder_, CONFIG_LOAD)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CONFIG_LOAD_STATEMENT, null);
    result_ = consumeToken(builder_, CONFIG_LOAD);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, config_load_statement_1(builder_, level_ + 1));
    result_ = pinned_ && config_load_statement_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [STRING]
  private static boolean config_load_statement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "config_load_statement_1")) return false;
    consumeToken(builder_, STRING);
    return true;
  }

  // (assign_clause | section_attribute)*
  private static boolean config_load_statement_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "config_load_statement_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!config_load_statement_2_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "config_load_statement_2", pos_)) break;
    }
    return true;
  }

  // assign_clause | section_attribute
  private static boolean config_load_statement_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "config_load_statement_2_0")) return false;
    boolean result_;
    result_ = assign_clause(builder_, level_ + 1);
    if (!result_) result_ = section_attribute(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // HASH config_variable_name HASH
  public static boolean config_variable(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "config_variable")) return false;
    if (!nextTokenIs(builder_, HASH)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CONFIG_VARIABLE, null);
    result_ = consumeToken(builder_, HASH);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, config_variable_name(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, HASH) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // IDENTIFIER
  //     | variable
  static boolean config_variable_name(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "config_variable_name")) return false;
    if (!nextTokenIs(builder_, "", DOLLAR, IDENTIFIER)) return false;
    boolean result_;
    result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = variable(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // CONTINUE [NUMBER]
  public static boolean continue_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "continue_statement")) return false;
    if (!nextTokenIs(builder_, CONTINUE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, CONTINUE);
    result_ = result_ && continue_statement_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, CONTINUE_STATEMENT, result_);
    return result_;
  }

  // [NUMBER]
  private static boolean continue_statement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "continue_statement_1")) return false;
    consumeToken(builder_, NUMBER);
    return true;
  }

  /* ********************************************************** */
  // DEBUG [DEBUG_VARS]
  public static boolean debug_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "debug_statement")) return false;
    if (!nextTokenIs(builder_, DEBUG)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, DEBUG_STATEMENT, null);
    result_ = consumeToken(builder_, DEBUG);
    pinned_ = result_; // pin = 1
    result_ = result_ && debug_statement_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [DEBUG_VARS]
  private static boolean debug_statement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "debug_statement_1")) return false;
    DEBUG_VARS(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // (IDENTIFIER '=' name_value) | name_value
  static boolean declaration_name(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "declaration_name")) return false;
    if (!nextTokenIs(builder_, "", IDENTIFIER, STRING)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = declaration_name_0(builder_, level_ + 1);
    if (!result_) result_ = name_value(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // IDENTIFIER '=' name_value
  private static boolean declaration_name_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "declaration_name_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, IDENTIFIER, ASSIGN);
    result_ = result_ && name_value(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // ELSE
  public static boolean else_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "else_statement")) return false;
    if (!nextTokenIs(builder_, ELSE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, ELSE);
    exit_section_(builder_, marker_, ELSE_STATEMENT, result_);
    return result_;
  }

  /* ********************************************************** */
  // ELSEIF expr assign_clause*
  public static boolean elseif_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "elseif_statement")) return false;
    if (!nextTokenIs(builder_, ELSEIF)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ELSEIF_STATEMENT, null);
    result_ = consumeToken(builder_, ELSEIF);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, expr(builder_, level_ + 1));
    result_ = pinned_ && elseif_statement_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // assign_clause*
  private static boolean elseif_statement_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "elseif_statement_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!assign_clause(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "elseif_statement_2", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // ternary_expr
  public static boolean expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, EXPR, "<expr>");
    result_ = ternary_expr(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // EXTENDS [STRING] assign_clause*
  public static boolean extends_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "extends_statement")) return false;
    if (!nextTokenIs(builder_, EXTENDS)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, EXTENDS_STATEMENT, null);
    result_ = consumeToken(builder_, EXTENDS);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, extends_statement_1(builder_, level_ + 1));
    result_ = pinned_ && extends_statement_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [STRING]
  private static boolean extends_statement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "extends_statement_1")) return false;
    consumeToken(builder_, STRING);
    return true;
  }

  // assign_clause*
  private static boolean extends_statement_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "extends_statement_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!assign_clause(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "extends_statement_2", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // expr
  static boolean for_condition(PsiBuilder builder_, int level_) {
    return expr(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // assignment
  static boolean for_increment(PsiBuilder builder_, int level_) {
    return assignment(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // assignment
  static boolean for_init(PsiBuilder builder_, int level_) {
    return assignment(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // FOR for_init ';' for_condition ';' for_increment assign_clause*
  public static boolean for_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_statement")) return false;
    if (!nextTokenIs(builder_, "<for loop>", FOR)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FOR_STATEMENT, "<for loop>");
    result_ = consumeToken(builder_, FOR);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, for_init(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, SEMICOLON)) && result_;
    result_ = pinned_ && report_error_(builder_, for_condition(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, SEMICOLON)) && result_;
    result_ = pinned_ && report_error_(builder_, for_increment(builder_, level_ + 1)) && result_;
    result_ = pinned_ && for_statement_6(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // assign_clause*
  private static boolean for_statement_6(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_statement_6")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!assign_clause(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "for_statement_6", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // FOREACH [expr AS variable [FAT_ARROW variable]] assign_clause*
  public static boolean foreach_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_statement")) return false;
    if (!nextTokenIs(builder_, "<foreach loop>", FOREACH)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FOREACH_STATEMENT, "<foreach loop>");
    result_ = consumeToken(builder_, FOREACH);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, foreach_statement_1(builder_, level_ + 1));
    result_ = pinned_ && foreach_statement_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [expr AS variable [FAT_ARROW variable]]
  private static boolean foreach_statement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_statement_1")) return false;
    foreach_statement_1_0(builder_, level_ + 1);
    return true;
  }

  // expr AS variable [FAT_ARROW variable]
  private static boolean foreach_statement_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_statement_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = expr(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, AS);
    result_ = result_ && variable(builder_, level_ + 1);
    result_ = result_ && foreach_statement_1_0_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [FAT_ARROW variable]
  private static boolean foreach_statement_1_0_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_statement_1_0_3")) return false;
    foreach_statement_1_0_3_0(builder_, level_ + 1);
    return true;
  }

  // FAT_ARROW variable
  private static boolean foreach_statement_1_0_3_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_statement_1_0_3_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, FAT_ARROW);
    result_ = result_ && variable(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // assign_clause*
  private static boolean foreach_statement_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_statement_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!assign_clause(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "foreach_statement_2", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // FOREACHELSE
  public static boolean foreachelse_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreachelse_statement")) return false;
    if (!nextTokenIs(builder_, FOREACHELSE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, FOREACHELSE);
    exit_section_(builder_, marker_, FOREACHELSE_STATEMENT, result_);
    return result_;
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
  public static boolean function_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_body")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FUNCTION_BODY, "<function body>");
    result_ = if_statement(builder_, level_ + 1);
    if (!result_) result_ = elseif_statement(builder_, level_ + 1);
    if (!result_) result_ = else_statement(builder_, level_ + 1);
    if (!result_) result_ = foreach_statement(builder_, level_ + 1);
    if (!result_) result_ = foreachelse_statement(builder_, level_ + 1);
    if (!result_) result_ = for_statement(builder_, level_ + 1);
    if (!result_) result_ = while_statement(builder_, level_ + 1);
    if (!result_) result_ = section_statement(builder_, level_ + 1);
    if (!result_) result_ = sectionelse_statement(builder_, level_ + 1);
    if (!result_) result_ = switch_statement(builder_, level_ + 1);
    if (!result_) result_ = case_statement(builder_, level_ + 1);
    if (!result_) result_ = break_statement(builder_, level_ + 1);
    if (!result_) result_ = continue_statement(builder_, level_ + 1);
    if (!result_) result_ = block_statement(builder_, level_ + 1);
    if (!result_) result_ = function_statement(builder_, level_ + 1);
    if (!result_) result_ = call_statement(builder_, level_ + 1);
    if (!result_) result_ = extends_statement(builder_, level_ + 1);
    if (!result_) result_ = include_statement(builder_, level_ + 1);
    if (!result_) result_ = insert_statement(builder_, level_ + 1);
    if (!result_) result_ = assign_statement(builder_, level_ + 1);
    if (!result_) result_ = append_statement(builder_, level_ + 1);
    if (!result_) result_ = capture_statement(builder_, level_ + 1);
    if (!result_) result_ = config_load_statement(builder_, level_ + 1);
    if (!result_) result_ = debug_statement(builder_, level_ + 1);
    if (!result_) result_ = setfilter_statement(builder_, level_ + 1);
    if (!result_) result_ = ldelim_rdelim_statement(builder_, level_ + 1);
    if (!result_) result_ = variable_output(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // function_name LPAREN [argument_list] RPAREN
  public static boolean function_call(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_call")) return false;
    if (!nextTokenIs(builder_, IDENTIFIER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = function_name(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, LPAREN);
    result_ = result_ && function_call_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAREN);
    exit_section_(builder_, marker_, FUNCTION_CALL, result_);
    return result_;
  }

  // [argument_list]
  private static boolean function_call_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_call_2")) return false;
    argument_list(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // IDENTIFIER
  static boolean function_name(PsiBuilder builder_, int level_) {
    return consumeToken(builder_, IDENTIFIER);
  }

  /* ********************************************************** */
  // FUNCTION declaration_name [FUNCTION_PARAMS] assign_clause*
  public static boolean function_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_statement")) return false;
    if (!nextTokenIs(builder_, FUNCTION)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FUNCTION_STATEMENT, null);
    result_ = consumeToken(builder_, FUNCTION);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, declaration_name(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, function_statement_2(builder_, level_ + 1)) && result_;
    result_ = pinned_ && function_statement_3(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [FUNCTION_PARAMS]
  private static boolean function_statement_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_statement_2")) return false;
    FUNCTION_PARAMS(builder_, level_ + 1);
    return true;
  }

  // assign_clause*
  private static boolean function_statement_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_statement_3")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!assign_clause(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "function_statement_3", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // IF expr assign_clause*
  public static boolean if_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "if_statement")) return false;
    if (!nextTokenIs(builder_, IF)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, IF_STATEMENT, null);
    result_ = consumeToken(builder_, IF);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, expr(builder_, level_ + 1));
    result_ = pinned_ && if_statement_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // assign_clause*
  private static boolean if_statement_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "if_statement_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!assign_clause(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "if_statement_2", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // INCLUDE [STRING] assign_clause* [NOCACHE]
  public static boolean include_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "include_statement")) return false;
    if (!nextTokenIs(builder_, INCLUDE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, INCLUDE_STATEMENT, null);
    result_ = consumeToken(builder_, INCLUDE);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, include_statement_1(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, include_statement_2(builder_, level_ + 1)) && result_;
    result_ = pinned_ && include_statement_3(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [STRING]
  private static boolean include_statement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "include_statement_1")) return false;
    consumeToken(builder_, STRING);
    return true;
  }

  // assign_clause*
  private static boolean include_statement_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "include_statement_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!assign_clause(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "include_statement_2", pos_)) break;
    }
    return true;
  }

  // [NOCACHE]
  private static boolean include_statement_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "include_statement_3")) return false;
    consumeToken(builder_, NOCACHE);
    return true;
  }

  /* ********************************************************** */
  // INSERT [STRING | IDENTIFIER] assign_clause*
  public static boolean insert_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "insert_statement")) return false;
    if (!nextTokenIs(builder_, INSERT)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, INSERT_STATEMENT, null);
    result_ = consumeToken(builder_, INSERT);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, insert_statement_1(builder_, level_ + 1));
    result_ = pinned_ && insert_statement_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [STRING | IDENTIFIER]
  private static boolean insert_statement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "insert_statement_1")) return false;
    insert_statement_1_0(builder_, level_ + 1);
    return true;
  }

  // STRING | IDENTIFIER
  private static boolean insert_statement_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "insert_statement_1_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = consumeToken(builder_, IDENTIFIER);
    return result_;
  }

  // assign_clause*
  private static boolean insert_statement_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "insert_statement_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!assign_clause(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "insert_statement_2", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // LDELIM_KW | RDELIM_KW
  public static boolean ldelim_rdelim_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ldelim_rdelim_statement")) return false;
    if (!nextTokenIs(builder_, "<ldelim rdelim statement>", LDELIM_KW, RDELIM_KW)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, LDELIM_RDELIM_STATEMENT, "<ldelim rdelim statement>");
    result_ = consumeToken(builder_, LDELIM_KW);
    if (!result_) result_ = consumeToken(builder_, RDELIM_KW);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // NUMBER
  //     | STRING
  //     | BOOLEAN
  //     | NULL_LITERAL
  //     | ARRAY_LITERAL
  public static boolean literal(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "literal")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, LITERAL, "<literal>");
    result_ = consumeToken(builder_, NUMBER);
    if (!result_) result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = BOOLEAN(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, NULL_LITERAL);
    if (!result_) result_ = ARRAY_LITERAL(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // logical_not_expr ((AND | AND_KEYWORD) logical_not_expr)*
  static boolean logical_and_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "logical_and_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null, "<expression>");
    result_ = logical_not_expr(builder_, level_ + 1);
    result_ = result_ && logical_and_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // ((AND | AND_KEYWORD) logical_not_expr)*
  private static boolean logical_and_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "logical_and_expr_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!logical_and_expr_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "logical_and_expr_1", pos_)) break;
    }
    return true;
  }

  // (AND | AND_KEYWORD) logical_not_expr
  private static boolean logical_and_expr_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "logical_and_expr_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = logical_and_expr_1_0_0(builder_, level_ + 1);
    result_ = result_ && logical_not_expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // AND | AND_KEYWORD
  private static boolean logical_and_expr_1_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "logical_and_expr_1_0_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, AND);
    if (!result_) result_ = consumeToken(builder_, AND_KEYWORD);
    return result_;
  }

  /* ********************************************************** */
  // ((NOT | NOT_KEYWORD) logical_not_expr)
  //     | comparison_expr
  static boolean logical_not_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "logical_not_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null, "<expression>");
    result_ = logical_not_expr_0(builder_, level_ + 1);
    if (!result_) result_ = comparison_expr(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (NOT | NOT_KEYWORD) logical_not_expr
  private static boolean logical_not_expr_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "logical_not_expr_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = logical_not_expr_0_0(builder_, level_ + 1);
    result_ = result_ && logical_not_expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // NOT | NOT_KEYWORD
  private static boolean logical_not_expr_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "logical_not_expr_0_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, NOT);
    if (!result_) result_ = consumeToken(builder_, NOT_KEYWORD);
    return result_;
  }

  /* ********************************************************** */
  // logical_and_expr ((OR | OR_KEYWORD) logical_and_expr)*
  static boolean logical_or_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "logical_or_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null, "<expression>");
    result_ = logical_and_expr(builder_, level_ + 1);
    result_ = result_ && logical_or_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // ((OR | OR_KEYWORD) logical_and_expr)*
  private static boolean logical_or_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "logical_or_expr_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!logical_or_expr_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "logical_or_expr_1", pos_)) break;
    }
    return true;
  }

  // (OR | OR_KEYWORD) logical_and_expr
  private static boolean logical_or_expr_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "logical_or_expr_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = logical_or_expr_1_0_0(builder_, level_ + 1);
    result_ = result_ && logical_and_expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // OR | OR_KEYWORD
  private static boolean logical_or_expr_1_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "logical_or_expr_1_0_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, OR);
    if (!result_) result_ = consumeToken(builder_, OR_KEYWORD);
    return result_;
  }

  /* ********************************************************** */
  // DOT IDENTIFIER
  //     | ARROW IDENTIFIER
  //     | array_access
  public static boolean member_access(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "member_access")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, MEMBER_ACCESS, "<member access>");
    result_ = parseTokens(builder_, 0, DOT, IDENTIFIER);
    if (!result_) result_ = parseTokens(builder_, 0, ARROW, IDENTIFIER);
    if (!result_) result_ = array_access(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // PIPE modifier_name [modifier_arguments]
  public static boolean modifier(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "modifier")) return false;
    if (!nextTokenIs(builder_, PIPE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, MODIFIER, null);
    result_ = consumeToken(builder_, PIPE);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, modifier_name(builder_, level_ + 1));
    result_ = pinned_ && modifier_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [modifier_arguments]
  private static boolean modifier_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "modifier_2")) return false;
    modifier_arguments(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // (COLON modifier_param)+
  static boolean modifier_arguments(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "modifier_arguments")) return false;
    if (!nextTokenIs(builder_, COLON)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = modifier_arguments_0(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!modifier_arguments_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "modifier_arguments", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // COLON modifier_param
  private static boolean modifier_arguments_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "modifier_arguments_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COLON);
    result_ = result_ && modifier_param(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // modifier+
  public static boolean modifier_chain(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "modifier_chain")) return false;
    if (!nextTokenIs(builder_, PIPE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = modifier(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!modifier(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "modifier_chain", pos_)) break;
    }
    exit_section_(builder_, marker_, MODIFIER_CHAIN, result_);
    return result_;
  }

  /* ********************************************************** */
  // modifier (',' modifier)*
  static boolean modifier_list(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "modifier_list")) return false;
    if (!nextTokenIs(builder_, PIPE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = modifier(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && modifier_list_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // (',' modifier)*
  private static boolean modifier_list_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "modifier_list_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!modifier_list_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "modifier_list_1", pos_)) break;
    }
    return true;
  }

  // ',' modifier
  private static boolean modifier_list_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "modifier_list_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && modifier(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // UPPER | LOWER | CAPITALIZE | CAT | COUNT_PARAGRAPHS | COUNT_SENTENCES
  //     | COUNT_WORDS | DATE_FORMAT | ESCAPE | FROM_CHARSET | INDENT
  //     | NL2BR | REGEX_REPLACE | REPLACE | SPACIFY | STRING_FORMAT
  //     | STRIP_TAGS | TO_CHARSET | TRUNCATE | UNESCAPE | WORDWRAP | DEFAULT_MOD
  //     | IDENTIFIER
  static boolean modifier_name(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "modifier_name")) return false;
    boolean result_;
    result_ = consumeToken(builder_, UPPER);
    if (!result_) result_ = consumeToken(builder_, LOWER);
    if (!result_) result_ = consumeToken(builder_, CAPITALIZE);
    if (!result_) result_ = consumeToken(builder_, CAT);
    if (!result_) result_ = consumeToken(builder_, COUNT_PARAGRAPHS);
    if (!result_) result_ = consumeToken(builder_, COUNT_SENTENCES);
    if (!result_) result_ = consumeToken(builder_, COUNT_WORDS);
    if (!result_) result_ = consumeToken(builder_, DATE_FORMAT);
    if (!result_) result_ = consumeToken(builder_, ESCAPE);
    if (!result_) result_ = consumeToken(builder_, FROM_CHARSET);
    if (!result_) result_ = consumeToken(builder_, INDENT);
    if (!result_) result_ = consumeToken(builder_, NL2BR);
    if (!result_) result_ = consumeToken(builder_, REGEX_REPLACE);
    if (!result_) result_ = consumeToken(builder_, REPLACE);
    if (!result_) result_ = consumeToken(builder_, SPACIFY);
    if (!result_) result_ = consumeToken(builder_, STRING_FORMAT);
    if (!result_) result_ = consumeToken(builder_, STRIP_TAGS);
    if (!result_) result_ = consumeToken(builder_, TO_CHARSET);
    if (!result_) result_ = consumeToken(builder_, TRUNCATE);
    if (!result_) result_ = consumeToken(builder_, UNESCAPE);
    if (!result_) result_ = consumeToken(builder_, WORDWRAP);
    if (!result_) result_ = consumeToken(builder_, DEFAULT_MOD);
    if (!result_) result_ = consumeToken(builder_, IDENTIFIER);
    return result_;
  }

  /* ********************************************************** */
  // expr
  static boolean modifier_param(PsiBuilder builder_, int level_) {
    return expr(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // unary_expr ((MULT | DIV | MOD | MOD_KEYWORD | DIV_KEYWORD) unary_expr)*
  static boolean multiplicative_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "multiplicative_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null, "<expression>");
    result_ = unary_expr(builder_, level_ + 1);
    result_ = result_ && multiplicative_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // ((MULT | DIV | MOD | MOD_KEYWORD | DIV_KEYWORD) unary_expr)*
  private static boolean multiplicative_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "multiplicative_expr_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!multiplicative_expr_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "multiplicative_expr_1", pos_)) break;
    }
    return true;
  }

  // (MULT | DIV | MOD | MOD_KEYWORD | DIV_KEYWORD) unary_expr
  private static boolean multiplicative_expr_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "multiplicative_expr_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = multiplicative_expr_1_0_0(builder_, level_ + 1);
    result_ = result_ && unary_expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // MULT | DIV | MOD | MOD_KEYWORD | DIV_KEYWORD
  private static boolean multiplicative_expr_1_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "multiplicative_expr_1_0_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, MULT);
    if (!result_) result_ = consumeToken(builder_, DIV);
    if (!result_) result_ = consumeToken(builder_, MOD);
    if (!result_) result_ = consumeToken(builder_, MOD_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, DIV_KEYWORD);
    return result_;
  }

  /* ********************************************************** */
  // IDENTIFIER | STRING
  static boolean name_value(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "name_value")) return false;
    if (!nextTokenIs(builder_, "", IDENTIFIER, STRING)) return false;
    boolean result_;
    result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = consumeToken(builder_, STRING);
    return result_;
  }

  /* ********************************************************** */
  // IDENTIFIER '=' expr
  static boolean named_argument(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "named_argument")) return false;
    if (!nextTokenIs(builder_, IDENTIFIER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, IDENTIFIER, ASSIGN);
    result_ = result_ && expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // !(PIPE | RDELIM)
  static boolean not_pipe_or_rbrace(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "not_pipe_or_rbrace")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !not_pipe_or_rbrace_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // PIPE | RDELIM
  private static boolean not_pipe_or_rbrace_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "not_pipe_or_rbrace_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, PIPE);
    if (!result_) result_ = consumeToken(builder_, RDELIM);
    return result_;
  }

  /* ********************************************************** */
  // IDENTIFIER (',' IDENTIFIER)*
  static boolean param_list(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "param_list")) return false;
    if (!nextTokenIs(builder_, IDENTIFIER)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, IDENTIFIER);
    pinned_ = result_; // pin = 1
    result_ = result_ && param_list_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // (',' IDENTIFIER)*
  private static boolean param_list_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "param_list_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!param_list_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "param_list_1", pos_)) break;
    }
    return true;
  }

  // ',' IDENTIFIER
  private static boolean param_list_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "param_list_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, COMMA, IDENTIFIER);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // expr
  static boolean positional_argument(PsiBuilder builder_, int level_) {
    return expr(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // primary_expr (postfix_op)*
  static boolean postfix_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "postfix_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null, "<expression>");
    result_ = primary_expr(builder_, level_ + 1);
    result_ = result_ && postfix_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (postfix_op)*
  private static boolean postfix_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "postfix_expr_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!postfix_expr_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "postfix_expr_1", pos_)) break;
    }
    return true;
  }

  // (postfix_op)
  private static boolean postfix_expr_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "postfix_expr_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = postfix_op(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // member_access
  //     | array_access
  //     | method_call
  static boolean postfix_op(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "postfix_op")) return false;
    boolean result_;
    result_ = member_access(builder_, level_ + 1);
    if (!result_) result_ = array_access(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, METHOD_CALL);
    return result_;
  }

  /* ********************************************************** */
  // variable
  //     | literal
  //     | function_call
  //     | config_variable
  //     | LPAREN expr RPAREN
  //     | IDENTIFIER
  static boolean primary_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "primary_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null, "<expression>");
    result_ = variable(builder_, level_ + 1);
    if (!result_) result_ = literal(builder_, level_ + 1);
    if (!result_) result_ = function_call(builder_, level_ + 1);
    if (!result_) result_ = config_variable(builder_, level_ + 1);
    if (!result_) result_ = primary_expr_4(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, IDENTIFIER);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // LPAREN expr RPAREN
  private static boolean primary_expr_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "primary_expr_4")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LPAREN);
    result_ = result_ && expr(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAREN);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // template_item*
  static boolean root_rule(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "root_rule")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!template_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "root_rule", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // SECTION '=' expr
  static boolean section_attribute(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "section_attribute")) return false;
    if (!nextTokenIs(builder_, SECTION)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, SECTION, ASSIGN);
    result_ = result_ && expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // SECTION IDENTIFIER assign_clause*
  public static boolean section_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "section_statement")) return false;
    if (!nextTokenIs(builder_, "<section loop>", SECTION)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SECTION_STATEMENT, "<section loop>");
    result_ = consumeTokens(builder_, 1, SECTION, IDENTIFIER);
    pinned_ = result_; // pin = 1
    result_ = result_ && section_statement_2(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // assign_clause*
  private static boolean section_statement_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "section_statement_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!assign_clause(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "section_statement_2", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // SECTIONELSE
  public static boolean sectionelse_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sectionelse_statement")) return false;
    if (!nextTokenIs(builder_, SECTIONELSE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, SECTIONELSE);
    exit_section_(builder_, marker_, SECTIONELSE_STATEMENT, result_);
    return result_;
  }

  /* ********************************************************** */
  // SETFILTER modifier_list
  public static boolean setfilter_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "setfilter_statement")) return false;
    if (!nextTokenIs(builder_, SETFILTER)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SETFILTER_STATEMENT, null);
    result_ = consumeToken(builder_, SETFILTER);
    pinned_ = result_; // pin = 1
    result_ = result_ && modifier_list(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // '{' '/' closing_keyword '}'
  public static boolean smarty_closing_tag(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "smarty_closing_tag")) return false;
    if (!nextTokenIs(builder_, LDELIM)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SMARTY_CLOSING_TAG, null);
    result_ = consumeTokens(builder_, 2, LDELIM, DIV);
    pinned_ = result_; // pin = 2
    result_ = result_ && report_error_(builder_, closing_keyword(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, RDELIM) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // '{' function_body '}'
  public static boolean smarty_function_call(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "smarty_function_call")) return false;
    if (!nextTokenIs(builder_, LDELIM)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SMARTY_FUNCTION_CALL, null);
    result_ = consumeToken(builder_, LDELIM);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, function_body(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, RDELIM) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // '{' LITERAL_KW '}' TEXT* '{' '/' LITERAL_KW '}'
  public static boolean smarty_literal_block(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "smarty_literal_block")) return false;
    if (!nextTokenIs(builder_, LDELIM)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SMARTY_LITERAL_BLOCK, null);
    result_ = consumeTokens(builder_, 2, LDELIM, LITERAL_KW, RDELIM);
    pinned_ = result_; // pin = 2
    result_ = result_ && report_error_(builder_, smarty_literal_block_3(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, consumeTokens(builder_, -1, LDELIM, DIV, LITERAL_KW, RDELIM)) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // TEXT*
  private static boolean smarty_literal_block_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "smarty_literal_block_3")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!consumeToken(builder_, TEXT)) break;
      if (!empty_element_parsed_guard_(builder_, "smarty_literal_block_3", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // '{' NOCACHE '}' TEXT* '{' '/' NOCACHE '}'
  public static boolean smarty_nocache_block(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "smarty_nocache_block")) return false;
    if (!nextTokenIs(builder_, LDELIM)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SMARTY_NOCACHE_BLOCK, null);
    result_ = consumeTokens(builder_, 2, LDELIM, NOCACHE, RDELIM);
    pinned_ = result_; // pin = 2
    result_ = result_ && report_error_(builder_, smarty_nocache_block_3(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, consumeTokens(builder_, -1, LDELIM, DIV, NOCACHE, RDELIM)) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // TEXT*
  private static boolean smarty_nocache_block_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "smarty_nocache_block_3")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!consumeToken(builder_, TEXT)) break;
      if (!empty_element_parsed_guard_(builder_, "smarty_nocache_block_3", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // smarty_literal_block
  //     | smarty_nocache_block
  //     | smarty_closing_tag
  //     | smarty_function_call
  public static boolean smarty_tag(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "smarty_tag")) return false;
    if (!nextTokenIs(builder_, LDELIM)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = smarty_literal_block(builder_, level_ + 1);
    if (!result_) result_ = smarty_nocache_block(builder_, level_ + 1);
    if (!result_) result_ = smarty_closing_tag(builder_, level_ + 1);
    if (!result_) result_ = smarty_function_call(builder_, level_ + 1);
    exit_section_(builder_, marker_, SMARTY_TAG, result_);
    return result_;
  }

  /* ********************************************************** */
  // SWITCH expr
  public static boolean switch_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switch_statement")) return false;
    if (!nextTokenIs(builder_, SWITCH)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SWITCH_STATEMENT, null);
    result_ = consumeToken(builder_, SWITCH);
    pinned_ = result_; // pin = 1
    result_ = result_ && expr(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // smarty_tag
  //     | text_content
  static boolean template_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "template_item")) return false;
    if (!nextTokenIs(builder_, "", LDELIM, TEXT)) return false;
    boolean result_;
    result_ = smarty_tag(builder_, level_ + 1);
    if (!result_) result_ = text_content(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // (IDENTIFIER | STRING) ('.' IDENTIFIER)*
  static boolean template_reference(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "template_reference")) return false;
    if (!nextTokenIs(builder_, "", IDENTIFIER, STRING)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = template_reference_0(builder_, level_ + 1);
    result_ = result_ && template_reference_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // IDENTIFIER | STRING
  private static boolean template_reference_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "template_reference_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = consumeToken(builder_, STRING);
    return result_;
  }

  // ('.' IDENTIFIER)*
  private static boolean template_reference_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "template_reference_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!template_reference_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "template_reference_1", pos_)) break;
    }
    return true;
  }

  // '.' IDENTIFIER
  private static boolean template_reference_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "template_reference_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, DOT, IDENTIFIER);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // logical_or_expr (QUESTION expr COLON expr)?
  static boolean ternary_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ternary_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null, "<expression>");
    result_ = logical_or_expr(builder_, level_ + 1);
    result_ = result_ && ternary_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (QUESTION expr COLON expr)?
  private static boolean ternary_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ternary_expr_1")) return false;
    ternary_expr_1_0(builder_, level_ + 1);
    return true;
  }

  // QUESTION expr COLON expr
  private static boolean ternary_expr_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ternary_expr_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, QUESTION);
    result_ = result_ && expr(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, COLON);
    result_ = result_ && expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // TEXT+
  public static boolean text_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "text_content")) return false;
    if (!nextTokenIs(builder_, TEXT)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, TEXT);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!consumeToken(builder_, TEXT)) break;
      if (!empty_element_parsed_guard_(builder_, "text_content", pos_)) break;
    }
    exit_section_(builder_, marker_, TEXT_CONTENT, result_);
    return result_;
  }

  /* ********************************************************** */
  // ((PLUS | MINUS | NOT | NOT_KEYWORD) unary_expr)
  //     | postfix_expr
  static boolean unary_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unary_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null, "<expression>");
    result_ = unary_expr_0(builder_, level_ + 1);
    if (!result_) result_ = postfix_expr(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (PLUS | MINUS | NOT | NOT_KEYWORD) unary_expr
  private static boolean unary_expr_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unary_expr_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = unary_expr_0_0(builder_, level_ + 1);
    result_ = result_ && unary_expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // PLUS | MINUS | NOT | NOT_KEYWORD
  private static boolean unary_expr_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unary_expr_0_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, PLUS);
    if (!result_) result_ = consumeToken(builder_, MINUS);
    if (!result_) result_ = consumeToken(builder_, NOT);
    if (!result_) result_ = consumeToken(builder_, NOT_KEYWORD);
    return result_;
  }

  /* ********************************************************** */
  // DOLLAR variable_name
  public static boolean variable(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable")) return false;
    if (!nextTokenIs(builder_, DOLLAR)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, DOLLAR);
    result_ = result_ && variable_name(builder_, level_ + 1);
    exit_section_(builder_, marker_, VARIABLE, result_);
    return result_;
  }

  /* ********************************************************** */
  // IDENTIFIER member_access*
  static boolean variable_name(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_name")) return false;
    if (!nextTokenIs(builder_, IDENTIFIER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, IDENTIFIER);
    result_ = result_ && variable_name_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // member_access*
  private static boolean variable_name_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_name_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!member_access(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "variable_name_1", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // expr modifier_chain?
  public static boolean variable_output(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_output")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, VARIABLE_OUTPUT, "<variable output>");
    result_ = expr(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && variable_output_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // modifier_chain?
  private static boolean variable_output_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_output_1")) return false;
    modifier_chain(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // WHILE expr assign_clause*
  public static boolean while_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "while_statement")) return false;
    if (!nextTokenIs(builder_, "<while loop>", WHILE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, WHILE_STATEMENT, "<while loop>");
    result_ = consumeToken(builder_, WHILE);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, expr(builder_, level_ + 1));
    result_ = pinned_ && while_statement_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // assign_clause*
  private static boolean while_statement_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "while_statement_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!assign_clause(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "while_statement_2", pos_)) break;
    }
    return true;
  }

}
