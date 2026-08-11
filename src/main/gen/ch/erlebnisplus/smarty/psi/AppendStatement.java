// This is a generated file. Not intended for manual editing.
package ch.erlebnisplus.smarty.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface AppendStatement extends PsiElement {

  @NotNull
  List<Expr> getExprList();

  @Nullable
  Variable getVariable();

  @NotNull
  String getAssignTarget();

  @Nullable
  Expr getAssignValue();

}
