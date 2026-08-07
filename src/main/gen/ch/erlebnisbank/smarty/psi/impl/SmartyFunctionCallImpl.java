// This is a generated file. Not intended for manual editing.
package ch.erlebnisbank.smarty.psi.impl;

import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.example.smarty.psi.*;

public class SmartyFunctionCallImpl extends ASTWrapperPsiElement implements SmartyFunctionCall {

  public SmartyFunctionCallImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull Visitor visitor) {
    visitor.visitSmartyFunctionCall(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof Visitor) accept((Visitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public FunctionBody getFunctionBody() {
    return findChildByClass(FunctionBody.class);
  }

}
