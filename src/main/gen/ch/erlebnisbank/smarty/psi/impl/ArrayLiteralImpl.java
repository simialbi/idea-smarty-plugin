// This is a generated file. Not intended for manual editing.
package ch.erlebnisbank.smarty.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.example.smarty.psi.*;

public class ArrayLiteralImpl extends ASTWrapperPsiElement implements ArrayLiteral {

  public ArrayLiteralImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull Visitor visitor) {
    visitor.visitArrayLiteral(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof Visitor) accept((Visitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<Expr> getExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, Expr.class);
  }

}
