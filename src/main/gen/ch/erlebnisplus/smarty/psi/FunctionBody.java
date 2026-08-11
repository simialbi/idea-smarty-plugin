// This is a generated file. Not intended for manual editing.
package ch.erlebnisplus.smarty.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface FunctionBody extends PsiElement {

  @Nullable
  AppendStatement getAppendStatement();

  @Nullable
  AssignStatement getAssignStatement();

  @Nullable
  BlockStatement getBlockStatement();

  @Nullable
  BreakStatement getBreakStatement();

  @Nullable
  CallStatement getCallStatement();

  @Nullable
  CaptureStatement getCaptureStatement();

  @Nullable
  CaseStatement getCaseStatement();

  @Nullable
  ConfigLoadStatement getConfigLoadStatement();

  @Nullable
  ContinueStatement getContinueStatement();

  @Nullable
  DebugStatement getDebugStatement();

  @Nullable
  ElseStatement getElseStatement();

  @Nullable
  ElseifStatement getElseifStatement();

  @Nullable
  ExtendsStatement getExtendsStatement();

  @Nullable
  ForStatement getForStatement();

  @Nullable
  ForeachStatement getForeachStatement();

  @Nullable
  ForeachelseStatement getForeachelseStatement();

  @Nullable
  FunctionStatement getFunctionStatement();

  @Nullable
  IfStatement getIfStatement();

  @Nullable
  IncludeStatement getIncludeStatement();

  @Nullable
  InsertStatement getInsertStatement();

  @Nullable
  LdelimRdelimStatement getLdelimRdelimStatement();

  @Nullable
  PluginCallStatement getPluginCallStatement();

  @Nullable
  SectionStatement getSectionStatement();

  @Nullable
  SectionelseStatement getSectionelseStatement();

  @Nullable
  SetfilterStatement getSetfilterStatement();

  @Nullable
  StripStatement getStripStatement();

  @Nullable
  SwitchStatement getSwitchStatement();

  @Nullable
  VariableOutput getVariableOutput();

  @Nullable
  WhileStatement getWhileStatement();

}
