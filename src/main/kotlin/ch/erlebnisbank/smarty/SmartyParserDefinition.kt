package ch.erlebnisbank.smarty

import ch.erlebnisbank.smarty.parser.SmartyParser
import ch.erlebnisbank.smarty.psi.SmartyFile
import ch.erlebnisbank.smarty.psi.SmartyTokenSets
import ch.erlebnisbank.smarty.psi.SmartyTypes
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet

class SmartyParserDefinition : ParserDefinition {

    override fun createLexer(project: Project?): Lexer = SmartyLexerAdapter()

    override fun createParser(project: Project?): PsiParser = SmartyParser()

    override fun getFileNodeType(): IFileElementType = FILE

    override fun getWhitespaceTokens(): TokenSet = SmartyTokenSets.WHITE_SPACES

    override fun getCommentTokens(): TokenSet = SmartyTokenSets.COMMENTS

    override fun getStringLiteralElements(): TokenSet = SmartyTokenSets.STRINGS

    override fun createElement(node: ASTNode): PsiElement = SmartyTypes.Factory.createElement(node)

    override fun createFile(viewProvider: FileViewProvider): PsiFile = SmartyFile(viewProvider)

    companion object {
        /**
         * Must be a single instance: the platform compares file node types by identity when it
         * decides whether a cached PSI tree can be reused.
         */
        private val FILE = IFileElementType(SmartyLanguage.INSTANCE)
    }
}
