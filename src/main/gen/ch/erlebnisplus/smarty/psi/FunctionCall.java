// This is a generated file. Not intended for manual editing.
package ch.erlebnisplus.smarty.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface FunctionCall extends PsiElement {

  @Nullable
  ArgumentList getArgumentList();

  @NotNull
  String getFunctionName();

  @NotNull
  String[] getFunctionArguments();

  boolean isBuiltinFunction();

}
