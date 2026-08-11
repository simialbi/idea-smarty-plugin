package ch.erlebnisplus.smarty

import com.intellij.lang.Language
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.FileViewProvider
import com.intellij.psi.FileViewProviderFactory
import com.intellij.psi.PsiManager

/**
 * Registered under `lang.fileViewProviderFactory` for Smarty, and the only thing that makes the
 * platform build a [SmartyFileViewProvider] instead of the single-root default.
 *
 * The `language` argument is the language the caller asked for and is ignored: which roots a `.tpl`
 * file has is decided by the file, not by the caller, and the provider works that out for itself.
 */
internal class SmartyFileViewProviderFactory : FileViewProviderFactory {

    override fun createFileViewProvider(
        file: VirtualFile,
        language: Language?,
        manager: PsiManager,
        eventSystemEnabled: Boolean,
    ): FileViewProvider = SmartyFileViewProvider(manager, file, eventSystemEnabled)
}
