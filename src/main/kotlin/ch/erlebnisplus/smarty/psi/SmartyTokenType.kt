package ch.erlebnisplus.smarty.psi

import ch.erlebnisplus.smarty.SmartyLanguage
import com.intellij.psi.tree.IElementType

/**
 * The element type of a Smarty token.
 *
 * The constants themselves are not declared here: they are generated into [SmartyTypes] from the
 * `tokens` block of `Smarty.bnf`, so that the lexer and the parser share one token family. A
 * second, hand-written set would produce distinct [IElementType] instances and the parser would
 * never match what the lexer emits.
 */
class SmartyTokenType(debugName: String) : IElementType(debugName, SmartyLanguage.INSTANCE) {

    override fun toString(): String = "SmartyTokenType." + super.toString()
}
