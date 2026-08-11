package ch.erlebnisplus.smarty

import com.intellij.lexer.FlexAdapter

class SmartyLexerAdapter : FlexAdapter(SmartyLexer(null)) {
}
