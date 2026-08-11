package ch.erlebnisbank.smarty

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.fileTypes.TemplateLanguageFileType
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.util.NlsSafe
import org.jetbrains.annotations.NonNls
import javax.swing.Icon

/**
 * [TemplateLanguageFileType] is the file-type half of the [SmartyLanguage] marker. The settings
 * pages and actions that let a user pick the data language of a file key off the file type, not
 * off the language, so both markers have to be there for `.tpl` to show up in them.
 */
class SmartyFileType private constructor() : LanguageFileType(SmartyLanguage.INSTANCE), TemplateLanguageFileType {
    companion object {
        @JvmStatic
        val INSTANCE = SmartyFileType()
    }

    override fun getName(): @NonNls String {
        return "Smarty Template"
    }

    override fun getDescription(): @NlsContexts.Label String {
        return "Smarty is a template engine for PHP, facilitating the separation of presentation (HTML/CSS) from application logic. This implies that PHP code is application logic, and is separated from the presentation."
    }

    override fun getDefaultExtension(): @NlsSafe String {
        return "tpl"
    }

    override fun getIcon(): Icon {
        return SmartyIcons.FILE
    }
}
