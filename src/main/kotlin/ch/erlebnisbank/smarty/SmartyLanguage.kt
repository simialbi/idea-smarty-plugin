package ch.erlebnisbank.smarty

import com.intellij.lang.Language

class SmartyLanguage private constructor() : Language("Smarty") {
    companion object {
        @JvmStatic
        val INSTANCE = SmartyLanguage()
    }
}
