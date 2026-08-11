// This is a generated file. Not intended for manual editing.
package ch.erlebnisplus.smarty.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SmartyTag extends PsiElement {

  @Nullable
  SmartyClosingTag getSmartyClosingTag();

  @Nullable
  SmartyFunctionCall getSmartyFunctionCall();

  @Nullable
  SmartyLiteralBlock getSmartyLiteralBlock();

  @Nullable
  SmartyNocacheBlock getSmartyNocacheBlock();

}
