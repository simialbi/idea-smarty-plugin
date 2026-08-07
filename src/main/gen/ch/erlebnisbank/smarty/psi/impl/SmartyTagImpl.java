// This is a generated file. Not intended for manual editing.
package ch.erlebnisbank.smarty.psi.impl;

import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.example.smarty.psi.*;

public class SmartyTagImpl extends ASTWrapperPsiElement implements SmartyTag {

  public SmartyTagImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull Visitor visitor) {
    visitor.visitSmartyTag(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof Visitor) accept((Visitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SmartyComment getSmartyComment() {
    return findChildByClass(SmartyComment.class);
  }

  @Override
  @Nullable
  public SmartyFunctionCall getSmartyFunctionCall() {
    return findChildByClass(SmartyFunctionCall.class);
  }

  @Override
  @Nullable
  public SmartyLiteralBlock getSmartyLiteralBlock() {
    return findChildByClass(SmartyLiteralBlock.class);
  }

  @Override
  @Nullable
  public SmartyNocacheBlock getSmartyNocacheBlock() {
    return findChildByClass(SmartyNocacheBlock.class);
  }

}
