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
  public List<Variable> getVariableList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, Variable.class);
  }

  @Override
  @Nullable
  public String getForeachVariable() {
    return SmartyPsiImplUtil.getForeachVariable(this);
  }

  @Override
  @Nullable
  public Expr getForeachItems() {
    return SmartyPsiImplUtil.getForeachItems(this);
  }

  @Override
  @Nullable
  public String getForeachValueVar() {
    return SmartyPsiImplUtil.getForeachValueVar(this);
  }

  @Override
  @Nullable
  public String getForeachKeyVar() {
    return SmartyPsiImplUtil.getForeachKeyVar(this);
  }

}
