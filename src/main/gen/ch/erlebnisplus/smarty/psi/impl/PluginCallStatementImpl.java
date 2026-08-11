// This is a generated file. Not intended for manual editing.
package ch.erlebnisplus.smarty.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static ch.erlebnisplus.smarty.psi.SmartyTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import ch.erlebnisplus.smarty.psi.*;

public class PluginCallStatementImpl extends ASTWrapperPsiElement implements PluginCallStatement {

  public PluginCallStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull Visitor visitor) {
    visitor.visitPluginCallStatement(this);
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
