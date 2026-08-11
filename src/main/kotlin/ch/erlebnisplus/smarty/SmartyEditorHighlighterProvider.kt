package ch.erlebnisplus.smarty

import ch.erlebnisplus.smarty.psi.SmartyTypes
import com.intellij.lang.Language
import com.intellij.lang.html.HTMLLanguage
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.editor.ex.util.LayerDescriptor
import com.intellij.openapi.editor.ex.util.LayeredLexerEditorHighlighter
import com.intellij.openapi.editor.highlighter.EditorHighlighter
import com.intellij.openapi.fileTypes.EditorHighlighterProvider
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.templateLanguages.TemplateDataHighlighterWrapper
import com.intellij.psi.templateLanguages.TemplateDataLanguageMappings

/**
 * Gives the editor a highlighter that colours markup as markup.
 *
 * Two PSI roots do *not* buy two sets of colours. The editor's lexer-based highlighter - the pass
 * that paints the file before any PSI exists, and the one that repaints while you type - runs a
 * single lexer, and for a `.tpl` file that lexer is the Smarty one, which sees the whole of
 * `<p class="a">` as one [SmartyTypes.TEXT] token. Without this extension point HTML would be
 * parsed and understood but shown in plain black.
 *
 * [LayeredLexerEditorHighlighter] fixes that by re-lexing the text of a chosen token with a second
 * highlighter, so the layer registered below hands every run of template data to whichever
 * highlighter the data language provides.
 */
internal class SmartyEditorHighlighterProvider : EditorHighlighterProvider {

    override fun getEditorHighlighter(
        project: Project?,
        fileType: FileType,
        virtualFile: VirtualFile?,
        colors: EditorColorsScheme,
    ): EditorHighlighter = SmartyEditorHighlighter(project, virtualFile, colors)
}

/**
 * Smarty colours as the base layer, the data language's colours inside every template-data token.
 */
private class SmartyEditorHighlighter(
    project: Project?,
    virtualFile: VirtualFile?,
    colors: EditorColorsScheme,
) : LayeredLexerEditorHighlighter(SmartySyntaxHighlighter(), colors) {

    init {
        val dataLanguage = dataLanguageOf(project, virtualFile)
        val dataHighlighter =
            SyntaxHighlighterFactory.getSyntaxHighlighter(dataLanguage, project, virtualFile)

        // The wrapper drops BAD_CHARACTER only. The data language is fed one run of template data
        // at a time, so a tag split across a Smarty expression - `<a href="{$url}">` is three runs
        // to it - reaches its lexer as fragments that do not lex cleanly. Painting those red would
        // put error colours on correct markup.
        registerLayer(
            SmartyTypes.TEXT,
            LayerDescriptor(TemplateDataHighlighterWrapper(dataHighlighter), ""),
        )
    }

    private companion object {

        /**
         * Mirrors `SmartyFileViewProvider.dataLanguageFor`, minus the substitution and nesting
         * guards: this decides colours, not PSI, and it has to answer before a project exists -
         * the highlighter is also built for files shown outside a project, where the per-file
         * mapping cannot be read.
         */
        fun dataLanguageOf(project: Project?, virtualFile: VirtualFile?): Language {
            if (project == null || virtualFile == null) return HTMLLanguage.INSTANCE
            return TemplateDataLanguageMappings.getInstance(project).getMapping(virtualFile)
                ?: HTMLLanguage.INSTANCE
        }
    }
}
