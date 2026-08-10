// This is a generated file. Not intended for manual editing.
package ch.erlebnisbank.smarty.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ForeachStatement extends PsiElement {

  @NotNull
  List<Expr> getExprList();

  @NotNull
  List<Variable> getVariableList();

  @Nullable
  String getForeachVariable();

  @Nullable
  Expr getForeachItems();

  @Nullable
  String getForeachValueVar();

  @Nullable
  String getForeachKeyVar();

}
