// This is a generated file. Not intended for manual editing.
package ch.erlebnisbank.smarty.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SmartyTag extends PsiElement {

  @Nullable
  SmartyComment getSmartyComment();

  @Nullable
  SmartyFunctionCall getSmartyFunctionCall();

  @Nullable
  SmartyLiteralBlock getSmartyLiteralBlock();

  @Nullable
  SmartyNocacheBlock getSmartyNocacheBlock();

}
