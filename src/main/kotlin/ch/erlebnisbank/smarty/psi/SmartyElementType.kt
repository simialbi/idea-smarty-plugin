package ch.erlebnisbank.smarty.psi

import ch.erlebnisbank.smarty.SmartyLanguage
import com.intellij.psi.tree.IElementType

class SmartyElementType : IElementType {
    constructor(debugName: String) : super(debugName, SmartyLanguage.INSTANCE);
}
