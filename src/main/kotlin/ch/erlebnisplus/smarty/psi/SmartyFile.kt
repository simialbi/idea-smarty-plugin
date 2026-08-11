package ch.erlebnisplus.smarty.psi

import ch.erlebnisplus.smarty.SmartyFileType
import ch.erlebnisplus.smarty.SmartyLanguage
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class SmartyFile : PsiFileBase {
    constructor(fileViewProvider: FileViewProvider) : super(fileViewProvider, SmartyLanguage.INSTANCE)

    override fun getFileType(): FileType {
        return SmartyFileType.INSTANCE
    }

    override fun toString(): String {
        return "Smarty template file"
    }
}
