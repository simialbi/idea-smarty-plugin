package ch.erlebnisbank.smarty

import ch.erlebnisbank.smarty.psi.SmartyFileElementTypes
import com.intellij.lang.Language
import com.intellij.lang.LanguageParserDefinitions
import com.intellij.lang.html.HTMLLanguage
import com.intellij.openapi.fileTypes.PlainTextLanguage
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.LanguageSubstitutors
import com.intellij.psi.MultiplePsiFilesPerDocumentFileViewProvider
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.source.PsiFileImpl
import com.intellij.psi.templateLanguages.ConfigurableTemplateLanguageFileViewProvider
import com.intellij.psi.templateLanguages.TemplateDataLanguageMappings
import com.intellij.psi.templateLanguages.TemplateLanguage

/**
 * Puts two PSI roots on one `.tpl` document: the Smarty tree and the data language's tree.
 *
 * This is the class that turns everything JetBrains ships for HTML into features of this plugin.
 * The plugin contributes no HTML support of its own; it hands the markup to the HTML parser, which
 * brings its own completion, inspections, structure view, brace matching and rename with it.
 *
 * Both trees span the whole file. The Smarty tree sees the markup as one
 * [ch.erlebnisbank.smarty.psi.SmartyTypes.TEXT] token per run; the data tree sees every Smarty tag
 * as an outer-language placeholder. `findElementAt` picks between them, which is why the caret
 * decides whether Smarty completion or HTML completion fires.
 *
 * Extending [ConfigurableTemplateLanguageFileViewProvider] rather than plain
 * `TemplateLanguageFileViewProvider` is what allows the user to change the data language per file
 * or per pattern; the choice is read back below through [TemplateDataLanguageMappings].
 */
internal class SmartyFileViewProvider private constructor(
    manager: PsiManager,
    virtualFile: VirtualFile,
    physical: Boolean,
    private val dataLanguage: Language,
) : MultiplePsiFilesPerDocumentFileViewProvider(manager, virtualFile, physical),
    ConfigurableTemplateLanguageFileViewProvider {

    constructor(manager: PsiManager, virtualFile: VirtualFile, physical: Boolean) :
            this(manager, virtualFile, physical, dataLanguageFor(manager, virtualFile))

    /**
     * A file whose data language is Smarty itself has one root, not two - see [dataLanguageFor]
     * for why that case is reachable at all.
     */
    private val allLanguages: Set<Language> =
        if (dataLanguage == SmartyLanguage.INSTANCE) setOf(SmartyLanguage.INSTANCE)
        else setOf(SmartyLanguage.INSTANCE, dataLanguage)

    override fun getBaseLanguage(): Language = SmartyLanguage.INSTANCE

    override fun getTemplateDataLanguage(): Language = dataLanguage

    override fun getLanguages(): Set<Language> = allLanguages

    override fun cloneInner(copy: VirtualFile): SmartyFileViewProvider =
        SmartyFileViewProvider(manager, copy, false, dataLanguage)

    /**
     * Builds one of the two roots.
     *
     * The Smarty root is an ordinary file from the parser definition. The data root is the same
     * thing with its content element type swapped for
     * [SmartyFileElementTypes.TEMPLATE_DATA] - that single assignment is what makes the data
     * parser receive a blanked-out copy of the document instead of the raw text, and it is the
     * only place the two element types from that object are used.
     */
    override fun createFile(lang: Language): PsiFile? {
        if (lang != baseLanguage && lang != templateDataLanguage) return null

        val parserDefinition = LanguageParserDefinitions.INSTANCE.forLanguage(lang) ?: return null
        val file = parserDefinition.createFile(this)
        if (lang == templateDataLanguage) {
            (file as PsiFileImpl).contentElementType = SmartyFileElementTypes.TEMPLATE_DATA
        }
        return file
    }

    private companion object {

        /**
         * What the markup in this particular file is.
         *
         * HTML is the default because that is what Smarty is used for, but nothing here is
         * HTML-specific: point a `.tpl` at XML, JSON or plain text in *Settings | Languages &
         * Frameworks | Template Data Languages* and the second root changes with it.
         *
         * Two guards:
         *
         * - a data language that is itself a [TemplateLanguage] is refused. Nesting one template
         *   language inside another would ask the platform to blank out the same document twice,
         *   so the platform's own providers substitute plain text there and so does this one.
         * - the result goes through [LanguageSubstitutors], which is how other plugins redirect a
         *   language for a given file - a substituted data language has to be resolved before the
         *   root is created, because the root is cached under it.
         */
        fun dataLanguageFor(manager: PsiManager, virtualFile: VirtualFile): Language {
            val mapped = TemplateDataLanguageMappings.getInstance(manager.project)
                .getMapping(virtualFile) ?: HTMLLanguage.INSTANCE

            if (mapped is TemplateLanguage) return PlainTextLanguage.INSTANCE

            return LanguageSubstitutors.getInstance()
                .substituteLanguage(mapped, virtualFile, manager.project)
        }
    }
}
