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

public class StripStatementImpl extends ASTWrapperPsiElement implements StripStatement {

  public StripStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull Visitor visitor) {
    visitor.visitStripStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof Visitor) accept((Visitor)visitor);
    else super.accept(visitor);
  }

}
