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

public class ModifierImpl extends ASTWrapperPsiElement implements Modifier {

  public ModifierImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull Visitor visitor) {
    visitor.visitModifier(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof Visitor) accept((Visitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<ArrayAccess> getArrayAccessList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ArrayAccess.class);
  }

  @Override
  @NotNull
  public List<ConfigVariable> getConfigVariableList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ConfigVariable.class);
  }

  @Override
  @NotNull
  public List<Expr> getExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, Expr.class);
  }

  @Override
  @NotNull
  public List<FunctionCall> getFunctionCallList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, FunctionCall.class);
  }

  @Override
  @NotNull
  public List<Literal> getLiteralList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, Literal.class);
  }

  @Override
  @NotNull
  public List<MemberAccess> getMemberAccessList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MemberAccess.class);
  }

  @Override
  @NotNull
  public List<Variable> getVariableList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, Variable.class);
  }

  @Override
  @NotNull
  public String getModifierName() {
    return SmartyPsiImplUtil.getModifierName(this);
  }

  @Override
  @NotNull
  public String[] getModifierParams() {
    return SmartyPsiImplUtil.getModifierParams(this);
  }

}
