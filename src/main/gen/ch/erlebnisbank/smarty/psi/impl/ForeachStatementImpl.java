// This is a generated file. Not intended for manual editing.
package ch.erlebnisbank.smarty.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.example.smarty.psi.*;

public class ForeachStatementImpl extends ASTWrapperPsiElement implements ForeachStatement {

  public ForeachStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull Visitor visitor) {
    visitor.visitForeachStatement(this);
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

  @Override
  @NotNull
  public List<HtmlContent> getHtmlContentList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HtmlContent.class);
  }

  @Override
  @NotNull
  public List<SmartyTag> getSmartyTagList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SmartyTag.class);
  }

  @Override
  @NotNull
  public List<TextContent> getTextContentList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TextContent.class);
  }

}
