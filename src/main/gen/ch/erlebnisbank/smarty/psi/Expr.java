// This is a generated file. Not intended for manual editing.
package ch.erlebnisbank.smarty.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface Expr extends PsiElement {

  @NotNull
  List<ArrayAccess> getArrayAccessList();

  @NotNull
  List<ConfigVariable> getConfigVariableList();

  @NotNull
  List<Expr> getExprList();

  @NotNull
  List<FunctionCall> getFunctionCallList();

  @NotNull
  List<Literal> getLiteralList();

  @NotNull
  List<MemberAccess> getMemberAccessList();

  @NotNull
  List<Variable> getVariableList();

}
