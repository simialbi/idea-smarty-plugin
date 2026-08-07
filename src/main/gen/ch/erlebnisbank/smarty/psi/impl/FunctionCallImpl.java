// This is a generated file. Not intended for manual editing.
package ch.erlebnisbank.smarty.psi.impl;

import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.example.smarty.psi.*;

public class FunctionCallImpl extends ASTWrapperPsiElement implements FunctionCall {

  public FunctionCallImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull Visitor visitor) {
    visitor.visitFunctionCall(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof Visitor) accept((Visitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public ArgumentList getArgumentList() {
    return findChildByClass(ArgumentList.class);
  }

}
