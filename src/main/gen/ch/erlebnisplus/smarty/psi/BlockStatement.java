// This is a generated file. Not intended for manual editing.
package ch.erlebnisplus.smarty.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.navigation.ItemPresentation;

public interface BlockStatement extends SmartyNamedElement {

  @NotNull
  List<Expr> getExprList();

  @Nullable
  String getName();

  @NotNull
  PsiElement setName(@NotNull String p1);

  @Nullable
  PsiElement getNameIdentifier();

  @NotNull
  ItemPresentation getPresentation();

}
