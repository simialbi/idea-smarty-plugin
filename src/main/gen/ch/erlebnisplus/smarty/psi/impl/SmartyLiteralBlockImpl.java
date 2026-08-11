// This is a generated file. Not intended for manual editing.
package ch.erlebnisplus.smarty.psi.impl;

import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import ch.erlebnisplus.smarty.psi.*;

public class SmartyLiteralBlockImpl extends ASTWrapperPsiElement implements SmartyLiteralBlock {

  public SmartyLiteralBlockImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull Visitor visitor) {
    visitor.visitSmartyLiteralBlock(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof Visitor) accept((Visitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public String getLiteralContent() {
    return SmartyPsiImplUtil.getLiteralContent(this);
  }

}
