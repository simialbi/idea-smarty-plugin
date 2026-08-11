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

public class FunctionBodyImpl extends ASTWrapperPsiElement implements FunctionBody {

  public FunctionBodyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull Visitor visitor) {
    visitor.visitFunctionBody(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof Visitor) accept((Visitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public AppendStatement getAppendStatement() {
    return findChildByClass(AppendStatement.class);
  }

  @Override
  @Nullable
  public AssignStatement getAssignStatement() {
    return findChildByClass(AssignStatement.class);
  }

  @Override
  @Nullable
  public BlockStatement getBlockStatement() {
    return findChildByClass(BlockStatement.class);
  }

  @Override
  @Nullable
  public BreakStatement getBreakStatement() {
    return findChildByClass(BreakStatement.class);
  }

  @Override
  @Nullable
  public CallStatement getCallStatement() {
    return findChildByClass(CallStatement.class);
  }

  @Override
  @Nullable
  public CaptureStatement getCaptureStatement() {
    return findChildByClass(CaptureStatement.class);
  }

  @Override
  @Nullable
  public CaseStatement getCaseStatement() {
    return findChildByClass(CaseStatement.class);
  }

  @Override
  @Nullable
  public ConfigLoadStatement getConfigLoadStatement() {
    return findChildByClass(ConfigLoadStatement.class);
  }

  @Override
  @Nullable
  public ContinueStatement getContinueStatement() {
    return findChildByClass(ContinueStatement.class);
  }

  @Override
  @Nullable
  public DebugStatement getDebugStatement() {
    return findChildByClass(DebugStatement.class);
  }

  @Override
  @Nullable
  public ElseStatement getElseStatement() {
    return findChildByClass(ElseStatement.class);
  }

  @Override
  @Nullable
  public ElseifStatement getElseifStatement() {
    return findChildByClass(ElseifStatement.class);
  }

  @Override
  @Nullable
  public ExtendsStatement getExtendsStatement() {
    return findChildByClass(ExtendsStatement.class);
  }

  @Override
  @Nullable
  public ForStatement getForStatement() {
    return findChildByClass(ForStatement.class);
  }

  @Override
  @Nullable
  public ForeachStatement getForeachStatement() {
    return findChildByClass(ForeachStatement.class);
  }

  @Override
  @Nullable
  public ForeachelseStatement getForeachelseStatement() {
    return findChildByClass(ForeachelseStatement.class);
  }

  @Override
  @Nullable
  public FunctionStatement getFunctionStatement() {
    return findChildByClass(FunctionStatement.class);
  }

  @Override
  @Nullable
  public IfStatement getIfStatement() {
    return findChildByClass(IfStatement.class);
  }

  @Override
  @Nullable
  public IncludeStatement getIncludeStatement() {
    return findChildByClass(IncludeStatement.class);
  }

  @Override
  @Nullable
  public InsertStatement getInsertStatement() {
    return findChildByClass(InsertStatement.class);
  }

  @Override
  @Nullable
  public LdelimRdelimStatement getLdelimRdelimStatement() {
    return findChildByClass(LdelimRdelimStatement.class);
  }

  @Override
  @Nullable
  public PluginCallStatement getPluginCallStatement() {
    return findChildByClass(PluginCallStatement.class);
  }

  @Override
  @Nullable
  public SectionStatement getSectionStatement() {
    return findChildByClass(SectionStatement.class);
  }

  @Override
  @Nullable
  public SectionelseStatement getSectionelseStatement() {
    return findChildByClass(SectionelseStatement.class);
  }

  @Override
  @Nullable
  public SetfilterStatement getSetfilterStatement() {
    return findChildByClass(SetfilterStatement.class);
  }

  @Override
  @Nullable
  public StripStatement getStripStatement() {
    return findChildByClass(StripStatement.class);
  }

  @Override
  @Nullable
  public SwitchStatement getSwitchStatement() {
    return findChildByClass(SwitchStatement.class);
  }

  @Override
  @Nullable
  public VariableOutput getVariableOutput() {
    return findChildByClass(VariableOutput.class);
  }

  @Override
  @Nullable
  public WhileStatement getWhileStatement() {
    return findChildByClass(WhileStatement.class);
  }

}
