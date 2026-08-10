// This is a generated file. Not intended for manual editing.
package ch.erlebnisbank.smarty.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static ch.erlebnisbank.smarty.psi.SmartyTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import ch.erlebnisbank.smarty.psi.*;

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
  public SmartyClosingTag getSmartyClosingTag() {
    return findChildByClass(SmartyClosingTag.class);
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
