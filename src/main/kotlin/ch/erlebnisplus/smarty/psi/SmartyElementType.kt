package ch.erlebnisplus.smarty.psi

import ch.erlebnisplus.smarty.SmartyLanguage
import com.intellij.psi.tree.IElementType

class SmartyElementType : IElementType {
    constructor(debugName: String) : super(debugName, SmartyLanguage.INSTANCE)
}
