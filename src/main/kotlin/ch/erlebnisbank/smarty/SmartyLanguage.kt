package ch.erlebnisbank.smarty

import com.intellij.lang.Language
import com.intellij.psi.templateLanguages.TemplateLanguage

/**
 * [TemplateLanguage] is a marker with no methods, and it is what makes a `.tpl` file two files.
 *
 * It tells the platform that a Smarty document is a host for a second language rather than a
 * self-contained one: the Template Data Languages settings page offers the file type, the
 * *Change Template Data Language* action appears on it, and a nested Smarty file - a `.tpl`
 * chosen as the data language of another `.tpl` - is refused instead of recursing.
 */
class SmartyLanguage private constructor() : Language("Smarty"), TemplateLanguage {
    companion object {
        @JvmStatic
        val INSTANCE = SmartyLanguage()
    }
}
