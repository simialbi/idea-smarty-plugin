package ch.erlebnisplus.smarty.psi

import ch.erlebnisplus.smarty.SmartyFileType
import ch.erlebnisplus.smarty.psi.impl.SmartyPsiImplUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import java.util.*
import java.util.stream.Collectors

/**
 * A piece of text a Smarty statement refers to - a template path or a declaration name -
 * together with its absolute range in the file it was read from.
 */
data class SmartyTextReference(val value: String, val range: TextRange)

class SmartyUtil private constructor() {
    companion object {
        // ========================================================================
        // VARIABLE SEARCH METHODS
        // ========================================================================

        /**
         * Searches the project for all variable declarations (assign statements).
         *
         * @param project current project
         * @return all assign statements in the project
         */
        @JvmStatic
        fun findAllAssignments(project: Project): List<AssignStatement> {
            val result = ArrayList<AssignStatement>()
            val virtualFiles = FileTypeIndex.getFiles(
                SmartyFileType.INSTANCE,
                GlobalSearchScope.allScope(project)
            )

            for (virtualFile in virtualFiles) {
                val smartyFile = PsiManager.getInstance(project).findFile(virtualFile) as? SmartyFile
                if (smartyFile != null) {
                    val assignments = PsiTreeUtil.getChildrenOfType(smartyFile, AssignStatement::class.java)
                    if (assignments != null) {
                        result.addAll(listOf(*assignments))
                    }
                }
            }
            return result
        }

        /**
         * Finds all assign statements for a specific variable name.
         *
         * @param project current project
         * @param variableName the variable name to search for
         * @return assign statements matching the variable name
         */
        @JvmStatic
        fun findAssignmentsByVariable(
            project: Project,
            variableName: String
        ): List<AssignStatement> {
            return findAllAssignments(project)
                .stream()
                .filter { assign -> variableName == SmartyPsiImplUtil.getAssignTarget(assign) }
                .collect(Collectors.toList())
        }

        /**
         * Finds all variable declarations in a file.
         *
         * @param file the Smarty file
         * @return all variables defined in the file
         */
        @JvmStatic
        fun findVariablesInFile(file: SmartyFile): List<Variable> {
            val result = ArrayList<Variable>()
            val variables = PsiTreeUtil.getChildrenOfType(file, Variable::class.java)
            if (variables != null) {
                result.addAll(listOf(*variables))
            }
            return result
        }

        /**
         * Gets all variables declared in foreach loops.
         *
         * @param project current project
         * @return all foreach loop variables
         */
        @JvmStatic
        fun findForeachVariables(project: Project): List<String> {
            val result = ArrayList<String>()
            val virtualFiles = FileTypeIndex.getFiles(
                SmartyFileType.INSTANCE,
                GlobalSearchScope.allScope(project)
            )

            for (virtualFile in virtualFiles) {
                val smartyFile = PsiManager.getInstance(project).findFile(virtualFile) as? SmartyFile
                if (smartyFile != null) {
                    val foreaches = PsiTreeUtil.getChildrenOfType(smartyFile, ForeachStatement::class.java)
                    if (foreaches != null) {
                        for (foreach in foreaches) {
                            val valueVar = SmartyPsiImplUtil.getForeachValueVar(foreach)
                            if (valueVar != null) {
                                result.add(valueVar)
                            }
                            val keyVar = SmartyPsiImplUtil.getForeachKeyVar(foreach)
                            if (keyVar != null) {
                                result.add(keyVar)
                            }
                        }
                    }
                }
            }
            return result
        }

        // ========================================================================
        // FUNCTION & MODIFIER SEARCH METHODS
        // ========================================================================

        /**
         * Searches for all function calls in the project.
         *
         * @param project current project
         * @return all function calls
         */
        @JvmStatic
        fun findAllFunctionCalls(project: Project): List<FunctionCall> {
            val result = ArrayList<FunctionCall>()
            val virtualFiles = FileTypeIndex.getFiles(
                SmartyFileType.INSTANCE,
                GlobalSearchScope.allScope(project)
            )

            for (virtualFile in virtualFiles) {
                val smartyFile = PsiManager.getInstance(project).findFile(virtualFile) as? SmartyFile
                if (smartyFile != null) {
                    val calls = PsiTreeUtil.getChildrenOfType(smartyFile, FunctionCall::class.java)
                    if (calls != null) {
                        result.addAll(listOf(*calls))
                    }
                }
            }
            return result
        }

        /**
         * Finds all calls to a specific function by name.
         *
         * @param project current project
         * @param functionName the function name to search for
         * @return all function calls matching the name
         */
        @JvmStatic
        fun findFunctionCallsByName(
            project: Project,
            functionName: String
        ): List<FunctionCall> {
            return findAllFunctionCalls(project)
                .stream()
                .filter { call -> functionName == SmartyPsiImplUtil.getFunctionName(call) }
                .collect(Collectors.toList())
        }

        /**
         * Finds all function calls in a specific file.
         *
         * @param file the Smarty file
         * @return all function calls in the file
         */
        @JvmStatic
        fun findFunctionCallsInFile(file: SmartyFile): List<FunctionCall> {
            val result = ArrayList<FunctionCall>()
            val calls = PsiTreeUtil.getChildrenOfType(file, FunctionCall::class.java)
            if (calls != null) {
                result.addAll(listOf(*calls))
            }
            return result
        }

        /**
         * Searches for all modifiers used in the project.
         *
         * @param project current project
         * @return all modifiers
         */
        @JvmStatic
        fun findAllModifiers(project: Project): List<Modifier> {
            val result = ArrayList<Modifier>()
            val virtualFiles = FileTypeIndex.getFiles(
                SmartyFileType.INSTANCE,
                GlobalSearchScope.allScope(project)
            )

            for (virtualFile in virtualFiles) {
                val smartyFile = PsiManager.getInstance(project).findFile(virtualFile) as? SmartyFile
                if (smartyFile != null) {
                    val modifiers = PsiTreeUtil.getChildrenOfType(smartyFile, Modifier::class.java)
                    if (modifiers != null) {
                        result.addAll(listOf(*modifiers))
                    }
                }
            }
            return result
        }

        /**
         * Finds all uses of a specific modifier.
         *
         * @param project current project
         * @param modifierName the modifier name to search for
         * @return all modifier uses
         */
        @JvmStatic
        fun findModifiersByName(
            project: Project,
            modifierName: String
        ): List<Modifier> {
            return findAllModifiers(project)
                .stream()
                .filter { mod -> modifierName == SmartyPsiImplUtil.getModifierName(mod) }
                .collect(Collectors.toList())
        }

        // ========================================================================
        // TEMPLATE & INCLUDE SEARCH METHODS
        // ========================================================================

        /**
         * Finds all include statements in the project.
         *
         * @param project current project
         * @return all include statements
         */
        @JvmStatic
        fun findAllIncludes(project: Project): List<IncludeStatement> {
            val result = ArrayList<IncludeStatement>()
            val virtualFiles = FileTypeIndex.getFiles(
                SmartyFileType.INSTANCE,
                GlobalSearchScope.allScope(project)
            )

            for (virtualFile in virtualFiles) {
                val smartyFile = PsiManager.getInstance(project).findFile(virtualFile) as? SmartyFile
                if (smartyFile != null) {
                    val includes = PsiTreeUtil.getChildrenOfType(smartyFile, IncludeStatement::class.java)
                    if (includes != null) {
                        result.addAll(listOf(*includes))
                    }
                }
            }
            return result
        }

        /**
         * Finds all extends statements in the project.
         *
         * @param project current project
         * @return all extends statements
         */
        @JvmStatic
        fun findAllExtends(project: Project): List<ExtendsStatement> {
            val result = ArrayList<ExtendsStatement>()
            val virtualFiles = FileTypeIndex.getFiles(
                SmartyFileType.INSTANCE,
                GlobalSearchScope.allScope(project)
            )

            for (virtualFile in virtualFiles) {
                val smartyFile = PsiManager.getInstance(project).findFile(virtualFile) as? SmartyFile
                if (smartyFile != null) {
                    val extends_ = PsiTreeUtil.getChildrenOfType(smartyFile, ExtendsStatement::class.java)
                    if (extends_ != null) {
                        result.addAll(listOf(*extends_))
                    }
                }
            }
            return result
        }

        /**
         * Finds all includes of a specific template.
         *
         * @param project current project
         * @param templatePath the template path to search for
         * @return all includes of that template
         */
        @JvmStatic
        fun findIncludesOfTemplate(
            project: Project,
            templatePath: String
        ): List<IncludeStatement> {
            return findAllIncludes(project)
                .stream()
                .filter { inc -> templatePath == SmartyPsiImplUtil.getTemplatePath(inc) }
                .collect(Collectors.toList())
        }

        // ========================================================================
        // TEMPLATE PATH RESOLUTION
        // ========================================================================

        /**
         * Reads the template path an `{include}`, `{extends}` or `{insert}` statement points
         * at. Both the `{include file="x.tpl"}` and the `{include "x.tpl"}` spelling are
         * understood.
         *
         * @param element the statement to read
         * @return the path and its range, or `null` when the path is dynamic (contains a
         *         variable) or uses a non file resource such as `string:` or `eval:`, both of
         *         which cannot be resolved statically
         */
        @JvmStatic
        fun findTemplatePath(element: PsiElement): SmartyTextReference? {
            val text = element.text
            val group = (TEMPLATE_FILE.find(text) ?: STRING_LITERAL.find(text))
                ?.groups?.get("path") ?: return null

            val raw = group.value.trim()
            if (raw.isEmpty() || DYNAMIC_PATH.containsMatchIn(raw)) return null

            val path = raw.removePrefix(FILE_RESOURCE)
            if (RESOURCE_PREFIX.containsMatchIn(path)) return null

            return SmartyTextReference(path, group.rangeIn(element.textRange.startOffset))
        }

        /**
         * Reads the name of a `{block}` or `{function}` declaration.
         *
         * @param element the declaration to read
         * @return the name and its range, or `null` when the declaration has no name
         */
        @JvmStatic
        fun findDeclarationName(element: PsiElement): SmartyTextReference? {
            val group = DECLARATION_NAME.find(element.text)?.groups?.get(1) ?: return null
            return SmartyTextReference(group.value, group.rangeIn(element.textRange.startOffset))
        }

        /**
         * Resolves a template path relative to the file containing [context] first, then
         * anywhere in the project - the Smarty template directories are configured in PHP and
         * are therefore unknown here.
         *
         * @param context an element of the referring template
         * @param path the template path to resolve
         * @return the referenced file, or `null` when nothing matches
         */
        @JvmStatic
        fun resolveTemplateFile(context: PsiElement, path: String): VirtualFile? {
            val relative = path.trimStart('/')
            if (relative.isEmpty()) return null

            val current = context.containingFile?.originalFile?.virtualFile
            current?.parent?.findFileByRelativePath(relative)?.let { return it }

            val name = relative.substringAfterLast('/')
            if (name.isEmpty()) return null

            return FilenameIndex
                .getVirtualFilesByName(name, GlobalSearchScope.allScope(context.project))
                .firstOrNull { candidate -> candidate.path.endsWith(relative) }
        }

        /**
         * Same as [resolveTemplateFile], but returns the PSI file so that it can be used as a
         * navigation target.
         */
        @JvmStatic
        fun resolveTemplate(context: PsiElement, path: String): PsiFile? {
            val file = resolveTemplateFile(context, path) ?: return null
            return PsiManager.getInstance(context.project).findFile(file)
        }

        /**
         * Walks the `{extends}` chain of a template, closest parent first. Already visited
         * files are skipped and the chain is cut off at [MAX_EXTENDS_DEPTH] so that a cyclic
         * or pathological setup still terminates.
         *
         * @param file the template to start from
         * @return the templates [file] inherits from
         */
        @JvmStatic
        fun findExtendedTemplates(file: PsiFile): List<PsiFile> {
            val result = ArrayList<PsiFile>()
            val visited = HashSet<VirtualFile>()
            file.originalFile.virtualFile?.let { visited.add(it) }

            var current: PsiFile? = file
            while (current != null && result.size < MAX_EXTENDS_DEPTH) {
                val statement = PsiTreeUtil.findChildOfType(current, ExtendsStatement::class.java) ?: break
                val path = findTemplatePath(statement) ?: break
                val parent = resolveTemplate(statement, path.value) ?: break

                val virtualFile = parent.originalFile.virtualFile
                if (virtualFile != null && !visited.add(virtualFile)) break

                result.add(parent)
                current = parent
            }

            return result
        }

        /**
         * Finds the `{block}` declarations of a template that carry a given name.
         *
         * @param file the template to search
         * @param name the block name to look for
         * @return the matching block declarations
         */
        @JvmStatic
        fun findBlocksByName(file: PsiFile, name: String): List<BlockStatement> {
            return PsiTreeUtil.findChildrenOfType(file, BlockStatement::class.java)
                .filter { block -> name == findDeclarationName(block)?.value }
        }

        private fun MatchGroup.rangeIn(offset: Int): TextRange =
            TextRange(offset + range.first, offset + range.last + 1)

        // ========================================================================
        // CONTROL STRUCTURE SEARCH METHODS
        // ========================================================================

        /**
         * Finds all if statements in the project.
         *
         * @param project current project
         * @return all if statements
         */
        @JvmStatic
        fun findAllIfStatements(project: Project): List<IfStatement> {
            val result = ArrayList<IfStatement>()
            val virtualFiles = FileTypeIndex.getFiles(
                SmartyFileType.INSTANCE,
                GlobalSearchScope.allScope(project)
            )

            for (virtualFile in virtualFiles) {
                val smartyFile = PsiManager.getInstance(project).findFile(virtualFile) as? SmartyFile
                if (smartyFile != null) {
                    val ifs = PsiTreeUtil.getChildrenOfType(smartyFile, IfStatement::class.java)
                    if (ifs != null) {
                        result.addAll(listOf(*ifs))
                    }
                }
            }
            return result
        }

        /**
         * Finds all foreach loops in the project.
         *
         * @param project current project
         * @return all foreach statements
         */
        @JvmStatic
        fun findAllForeachLoops(project: Project): List<ForeachStatement> {
            val result = ArrayList<ForeachStatement>()
            val virtualFiles = FileTypeIndex.getFiles(
                SmartyFileType.INSTANCE,
                GlobalSearchScope.allScope(project)
            )

            for (virtualFile in virtualFiles) {
                val smartyFile = PsiManager.getInstance(project).findFile(virtualFile) as? SmartyFile
                if (smartyFile != null) {
                    val foreaches = PsiTreeUtil.getChildrenOfType(smartyFile, ForeachStatement::class.java)
                    if (foreaches != null) {
                        result.addAll(listOf(*foreaches))
                    }
                }
            }
            return result
        }

        /**
         * Finds all block definitions in the project.
         *
         * @param project current project
         * @return all block statements
         */
        @JvmStatic
        fun findAllBlocks(project: Project): List<BlockStatement> =
            findAllBlocks(project, GlobalSearchScope.allScope(project))

        @JvmStatic
        fun findAllBlocks(project: Project, scope: GlobalSearchScope): List<BlockStatement> =
            findInTemplates(project, scope, BlockStatement::class.java)

        // ========================================================================
        // DECLARATION SEARCH METHODS
        // ========================================================================

        /**
         * Every declaration a template names - `{block}` and `{function}` - within [scope].
         *
         * This is what Navigate | Symbol lists, so it deliberately does not go through an index
         * of its own: Smarty templates are small and there is no stub index for them yet, and
         * the chooser only ever asks for one scope at a time.
         *
         * @param project current project
         * @param scope the files to search, as handed over by the chooser
         * @return all named declarations, in file order
         */
        @JvmStatic
        fun findDeclarations(project: Project, scope: GlobalSearchScope): List<SmartyNamedElement> =
            findInTemplates(project, scope, SmartyNamedElement::class.java)

        /**
         * The declarations carrying a given name.
         *
         * @param project current project
         * @param scope the files to search
         * @param name the declared name to match, without quotes
         * @return the matching declarations
         */
        @JvmStatic
        fun findDeclarationsByName(
            project: Project,
            scope: GlobalSearchScope,
            name: String
        ): List<SmartyNamedElement> =
            findDeclarations(project, scope).filter { declaration -> name == declaration.name }

        /**
         * Collects elements of one type out of every Smarty template in [scope].
         *
         * Descends the whole tree rather than looking at the file's own children: a statement
         * sits under `smarty_tag` → `smarty_function_call` → `function_body`, never directly
         * under the file.
         */
        private fun <T : PsiElement> findInTemplates(
            project: Project,
            scope: GlobalSearchScope,
            type: Class<T>
        ): List<T> {
            val result = ArrayList<T>()
            val manager = PsiManager.getInstance(project)

            for (virtualFile in FileTypeIndex.getFiles(SmartyFileType.INSTANCE, scope)) {
                val smartyFile = manager.findFile(virtualFile) as? SmartyFile ?: continue
                result.addAll(PsiTreeUtil.findChildrenOfType(smartyFile, type))
            }

            return result
        }

        // ========================================================================
        // DOCUMENTATION & COMMENT METHODS
        // ========================================================================

        /**
         * Attempts to collect any comment elements above a Smarty element.
         *
         * @param element the element to get documentation for
         * @return documentation comment text
         */
        @JvmStatic
        fun findDocumentationComment(element: PsiElement): String {
            val result = LinkedList<String>()
            var prevElement = element.prevSibling

            while (prevElement is PsiComment || prevElement is PsiWhiteSpace) {
                if (prevElement is PsiComment) {
                    val commentText = prevElement.text
                        .replaceFirst("^\\{\\*\\s*".toRegex(), "")
                        .replaceFirst("\\s*\\*\\}$".toRegex(), "")
                    result.add(commentText)
                }
                prevElement = prevElement.prevSibling
            }

            Collections.reverse(result)
            return java.lang.String.join("\n", result)
        }

        /**
         * Gets inline comment for an element (comment on same line after element).
         *
         * @param element the element to get comment for
         * @return inline comment text
         */
        @JvmStatic
        fun findInlineComment(element: PsiElement): String {
            var nextElement = element.nextSibling

            while (nextElement != null) {
                if (nextElement is PsiComment) {
                    return nextElement.text
                        .replaceFirst("^\\{\\*\\s*".toRegex(), "")
                        .replaceFirst("\\s*\\*\\}$".toRegex(), "")
                }
                if (nextElement is PsiWhiteSpace && nextElement.text.contains("\n")) {
                    break
                }
                nextElement = nextElement.nextSibling
            }

            return ""
        }

        // ========================================================================
        // FILE & SCOPE METHODS
        // ========================================================================

        /**
         * Gets all Smarty files in the project.
         *
         * @param project current project
         * @return all Smarty files
         */
        @JvmStatic
        fun getSmartyFiles(project: Project): Collection<VirtualFile> {
            return FileTypeIndex.getFiles(SmartyFileType.INSTANCE, GlobalSearchScope.allScope(project))
        }

        // ========================================================================
        // CONSTANTS
        // ========================================================================

        /** Upper bound for the `{extends}` chain, so that a cyclic setup cannot loop forever. */
        private const val MAX_EXTENDS_DEPTH = 16

        private const val FILE_RESOURCE = "file:"

        private val TEMPLATE_FILE =
            Regex("""\bfile\s*=\s*(?<quote>["'])(?<path>[^"']*)\k<quote>""", RegexOption.IGNORE_CASE)
        private val STRING_LITERAL = Regex("""(?<quote>["'])(?<path>[^"']*)\k<quote>""")
        private val DECLARATION_NAME =
            Regex("""^(?:block|function)\s+(?:name\s*=\s*)?["']?([a-zA-Z_]\w*)""", RegexOption.IGNORE_CASE)
        private val DYNAMIC_PATH = Regex("""[$`{]""")
        private val RESOURCE_PREFIX = Regex("""^[a-zA-Z]\w*:""")
    }
}
