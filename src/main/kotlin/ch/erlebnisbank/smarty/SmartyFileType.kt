package ch.erlebnisbank.smarty

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.util.NlsSafe
import org.jetbrains.annotations.NonNls
import javax.swing.Icon

class SmartyFileType private constructor() : LanguageFileType(SmartyLanguage.INSTANCE) {
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
