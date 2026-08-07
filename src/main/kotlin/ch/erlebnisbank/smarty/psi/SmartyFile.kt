package ch.erlebnisbank.smarty.psi

import ch.erlebnisbank.smarty.SmartyFileType
import ch.erlebnisbank.smarty.SmartyLanguage
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class SmartyFile : PsiFileBase {
    constructor(fileViewProvider: FileViewProvider) : super(fileViewProvider, SmartyLanguage.INSTANCE)

    override fun getFileType(): FileType {
        return SmartyFileType.INSTANCE;
    }

    override fun toString(): String {
        return "Smarty template file";
    }
}
