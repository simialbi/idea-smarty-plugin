// This is a generated file. Not intended for manual editing.
package ch.erlebnisbank.smarty.psi;

import java.util.List;
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
