package ch.erlebnisplus.smarty.psi.impl

import ch.erlebnisplus.smarty.psi.*
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import javax.swing.Icon


class SmartyPsiImplUtil private constructor() {
    companion object {
        // ========================================================================
        // NAMED ELEMENT METHODS
        // ========================================================================

        /**
         * The declared name of a block, with the quotes stripped.
         *
         * Backs [SmartyNamedElement]; wired up through `methods=[getName …]` on
         * `block_statement` in `Smarty.bnf`.
         */
        @JvmStatic
        fun getName(element: BlockStatement): String? = declaredName(element)

        /**
         * The node holding the block name, which is what Rename highlights and replaces.
         *
         * For `{block name="content"}` that is the value behind the `=`, not the `name`
         * attribute keyword in front of it; for the bare `{block content}` it is the identifier
         * itself.
         */
        @JvmStatic
        fun getNameIdentifier(element: BlockStatement): PsiElement? = nameIdentifier(element)

        /**
         * Renames a block by swapping its name node for one parsed from a throwaway template -
         * PSI leaves cannot be edited in place.
         */
        @JvmStatic
        fun setName(element: BlockStatement, newName: String): PsiElement = rename(element, newName)

        /** How a block is listed in Navigate | Symbol and in the Structure tool window. */
        @JvmStatic
        fun getPresentation(element: BlockStatement): ItemPresentation =
            presentation(element, AllIcons.Nodes.Method)

        /**
         * `{function}` declares a name the same way `{block}` does - through `declaration_name`
         * in `Smarty.bnf` - so the four helpers below are the block ones applied to the other
         * rule.
         */
        @JvmStatic
        fun getName(element: FunctionStatement): String? = declaredName(element)

        @JvmStatic
        fun getNameIdentifier(element: FunctionStatement): PsiElement? = nameIdentifier(element)

        @JvmStatic
        fun setName(element: FunctionStatement, newName: String): PsiElement = rename(element, newName)

        /** A template function is callable, so it gets the function icon rather than the block one. */
        @JvmStatic
        fun getPresentation(element: FunctionStatement): ItemPresentation =
            presentation(element, AllIcons.Nodes.Function)

        private fun declaredName(element: PsiElement): String? {
            val identifier = nameIdentifier(element) ?: return null
            return StringUtil.unquoteString(identifier.text)
        }

        private fun nameIdentifier(element: PsiElement): PsiElement? {
            val parts = meaningfulChildren(element)
            val index = parts.indexOfFirst(::isNameNode)
            if (index < 0) return null

            if (parts.getOrNull(index + 1)?.node?.elementType === SmartyTypes.ASSIGN) {
                return parts.getOrNull(index + 2)?.takeIf(::isNameNode)
            }
            return parts[index]
        }

        /**
         * The replacement keeps the spelling of the original: a quoted name stays quoted, and a
         * bare one is quoted only if the new name would not lex as an identifier.
         *
         * The name node is lifted out of a throwaway `{block}` whatever the declaration is - it
         * is a bare `IDENTIFIER` or `STRING` leaf, and a block is the cheapest way to parse one.
         */
        private fun rename(element: PsiElement, newName: String): PsiElement {
            val identifier = nameIdentifier(element) ?: return element

            val quote = identifier.node.elementType === SmartyTypes.STRING ||
                    !BARE_NAME.matches(newName)
            val literal = if (quote) "\"" + StringUtil.escapeStringCharacters(newName) + "\"" else newName

            val created = SmartyElementFactory.createBlockStatement(element.project, literal)
            val replacement = created?.let { nameIdentifier(it) } ?: return element

            element.node.replaceChild(identifier.node, replacement.node)
            return element
        }

        /**
         * Reads the declaration live rather than capturing its name, so that an entry already
         * shown in the chooser keeps up with an edit.
         */
        private fun presentation(element: PsiElement, icon: Icon): ItemPresentation =
            object : ItemPresentation {
                override fun getPresentableText(): String? = declaredName(element)
                override fun getLocationString(): String? = element.containingFile?.name
                override fun getIcon(unused: Boolean): Icon = icon
            }

        private fun isNameNode(element: PsiElement): Boolean {
            val type = element.node.elementType
            return type === SmartyTypes.IDENTIFIER || type === SmartyTypes.STRING
        }

        /** The children of [element] without whitespace, so positions can be compared. */
        private fun meaningfulChildren(element: PsiElement): List<PsiElement> {
            val result = mutableListOf<PsiElement>()
            var child = element.firstChild

            while (child != null) {
                if (child !is PsiWhiteSpace && child.node.elementType !== SmartyTypes.WS) {
                    result.add(child)
                }
                child = child.nextSibling
            }

            return result
        }

        private val BARE_NAME = Regex("""[a-zA-Z_]\w*""")

        // ========================================================================
        // VARIABLE METHODS
        // ========================================================================

        /**
         * The name of a variable, without the `$`.
         *
         * `variable ::= DOLLAR IDENTIFIER member_access*`, so the name is simply the first
         * identifier; everything after it belongs to the access chain.
         */
        @JvmStatic
        fun getName(element: Variable): String? =
            element.node.findChildByType(SmartyTypes.IDENTIFIER)?.text

        /** Whether the variable is indexed, as in `$var['key']`. */
        @JvmStatic
        fun hasArrayAccess(element: Variable): Boolean =
            PsiTreeUtil.findChildOfType(element, ArrayAccess::class.java) != null

        /** The index expressions of `$var['key'][0]`, in source order. */
        @JvmStatic
        fun getArrayIndices(element: Variable): Array<String> =
            PsiTreeUtil.findChildrenOfType(element, ArrayAccess::class.java)
                .map { access ->
                    access.text.removePrefix("[").removeSuffix("]").trim()
                }
                .toTypedArray()

        /** The property names of `$obj.property->nested`, in source order. */
        @JvmStatic
        fun getPropertyChain(element: Variable): Array<String> =
            PsiTreeUtil.findChildrenOfType(element, MemberAccess::class.java)
                .mapNotNull { access -> access.node.findChildByType(SmartyTypes.IDENTIFIER)?.text }
                .toTypedArray()

        // ========================================================================
        // EXPRESSION METHODS
        // ========================================================================

        /**
         * The operator of a binary expression, as written - so `eq` for `{if $a eq $b}` and
         * `==` for `{if $a == $b}`.
         */
        @JvmStatic
        fun getOperator(element: Expr): String? {
            var node: ASTNode? = element.node.firstChildNode

            while (node != null) {
                if (SmartyTokenSets.OPERATORS.contains(node.elementType)) return node.text
                node = node.treeNext
            }

            return null
        }

        /**
         * Checks if the expression is a literal value.
         */
        @JvmStatic
        fun isLiteral(element: Expr): Boolean {
            val child = element.firstChild ?: return false

            val type = child.node.elementType
            return type === SmartyTypes.NUMBER ||
                    type === SmartyTypes.STRING ||
                    type === SmartyTypes.TRUE ||
                    type === SmartyTypes.FALSE ||
                    type === SmartyTypes.NULL_LITERAL
        }

        // ========================================================================
        // FUNCTION CALL METHODS
        // ========================================================================

        /**
         * Gets the name of a function call.
         */
        @JvmStatic
        fun getFunctionName(element: FunctionCall): String {
            val node = element.node.findChildByType(SmartyTypes.IDENTIFIER)
            return node?.text ?: ""
        }

        /**
         * Gets all argument values from a function call.
         */
        @JvmStatic
        fun getFunctionArguments(element: FunctionCall): Array<String> {
            val args = mutableListOf<String>()
            var node: ASTNode? = element.node.firstChildNode
            var inArgs = false

            while (node != null) {
                if (node.elementType === SmartyTypes.LPAREN) {
                    inArgs = true
                } else if (node.elementType === SmartyTypes.RPAREN) {
                    break
                } else if (inArgs && node.elementType !== SmartyTypes.COMMA &&
                    node.elementType !== SmartyTypes.WS
                ) {
                    args.add(node.text)
                }
                node = node.treeNext
            }

            return args.toTypedArray()
        }

        /**
         * Checks if function is a built-in Smarty function.
         */
        @JvmStatic
        fun isBuiltinFunction(element: FunctionCall): Boolean {
            val name = getFunctionName(element)
            return isBuiltinFunction(name)
        }

        @JvmStatic
        private fun isBuiltinFunction(name: String): Boolean {
            val builtins = arrayOf(
                "assign", "append", "capture", "config_load", "debug", "extends",
                "foreach", "for", "function", "if", "include", "insert", "literal",
                "nocache", "section", "setfilter", "strip", "while", "block", "call"
            )

            for (builtin in builtins) {
                if (builtin.equals(name, ignoreCase = true)) {
                    return true
                }
            }
            return false
        }

        // ========================================================================
        // MODIFIER METHODS
        // ========================================================================

        /**
         * Gets the name of a modifier.
         */
        @JvmStatic
        fun getModifierName(element: Modifier): String {
            var node = element.node.findChildByType(SmartyTypes.IDENTIFIER)
            if (node == null) {
                // Check for built-in modifiers
                node = element.node.firstChildNode
                while (node != null) {
                    val type = node.elementType
                    if (type === SmartyTypes.UPPER || type === SmartyTypes.LOWER ||
                        type === SmartyTypes.CAPITALIZE || type === SmartyTypes.ESCAPE ||
                        type === SmartyTypes.DATE_FORMAT || type === SmartyTypes.TRUNCATE
                    ) {
                        return node.text
                    }
                    node = node.treeNext
                }
            }
            return node?.text ?: ""
        }

        /**
         * Gets modifier parameters.
         */
        @JvmStatic
        fun getModifierParams(element: Modifier): Array<String> {
            val params = mutableListOf<String>()
            var node = element.node.findChildByType(SmartyTypes.COLON)

            if (node != null) {
                node = node.treeNext
                while (node != null && node.elementType !== SmartyTypes.PIPE) {
                    if (node.elementType === SmartyTypes.STRING ||
                        node.elementType === SmartyTypes.IDENTIFIER ||
                        node.elementType === SmartyTypes.VARIABLE
                    ) {
                        params.add(node.text)
                    }
                    node = node.treeNext
                }
            }

            return params.toTypedArray()
        }

        // ========================================================================
        // CONTROL STRUCTURE METHODS
        // ========================================================================

        /**
         * Gets the condition expression from an if/elseif statement.
         */
        @JvmStatic
        fun getCondition(element: PsiElement): Expr? {
            return PsiTreeUtil.findChildOfType(element, Expr::class.java)
        }

        /**
         * Gets loop variable from foreach statement.
         */
        @JvmStatic
        fun getForeachVariable(element: ForeachStatement): String? {
            val `var` = PsiTreeUtil.findChildOfType(element, Variable::class.java)
            return if (`var` != null) getName(`var`) else null
        }

        /**
         * Gets items expression from foreach statement.
         */
        @JvmStatic
        fun getForeachItems(element: ForeachStatement): Expr? {
            val exprs = PsiTreeUtil.getChildrenOfType(element, Expr::class.java)
            return if (exprs != null && exprs.isNotEmpty()) exprs[0] else null
        }

        /**
         * Gets the value variable from foreach (e.g., $item in foreach $items as $item).
         */
        @JvmStatic
        fun getForeachValueVar(element: ForeachStatement): String? {
            val vars = PsiTreeUtil.getChildrenOfType(element, Variable::class.java)
            return if (vars != null && vars.isNotEmpty()) getName(vars[0]) else null
        }

        /**
         * Gets the key variable from foreach (e.g., $key in foreach $items as $key => $value).
         */
        @JvmStatic
        fun getForeachKeyVar(element: ForeachStatement): String? {
            val vars = PsiTreeUtil.getChildrenOfType(element, Variable::class.java)
            return if (vars != null && vars.size > 1) getName(vars[1]) else null
        }

        // ========================================================================
        // ASSIGN/APPEND METHODS
        // ========================================================================

        /**
         * The name of the variable an `{assign}` or `{append}` writes to, without the `$`.
         *
         * Both spellings are read: the shorthand `{assign $total = 0}` keeps its target in a
         * [Variable] of its own, the classic `{assign var="total" value=0}` in the `var`
         * attribute. Telling them apart matters - looking for the first variable anywhere below
         * the tag would answer `y` for `{assign var="x" value=$y}`.
         */
        @JvmStatic
        fun getAssignTarget(element: PsiElement): String {
            shorthandTarget(element)?.let { return getName(it) ?: "" }

            val value = attributeValue(element, VAR_ATTRIBUTE) ?: return ""
            return StringUtil.unquoteString(value.text)
        }

        /**
         * The expression being assigned - `$y` in both `{assign $x = $y}` and
         * `{assign var="x" value=$y}`.
         */
        @JvmStatic
        fun getAssignValue(element: PsiElement): Expr? {
            if (shorthandTarget(element) != null) {
                return PsiTreeUtil.getChildOfType(element, Expr::class.java)
            }
            return attributeValue(element, VALUE_ATTRIBUTE)
        }

        /**
         * The `$x` of the shorthand form. Only a direct child counts: in the classic form every
         * variable belongs to an attribute value and sits one level deeper.
         */
        private fun shorthandTarget(element: PsiElement): Variable? =
            PsiTreeUtil.getChildOfType(element, Variable::class.java)

        /**
         * The value of one attribute of a tag written in the classic form.
         *
         * `attribute_clause` is private in the grammar, so the attributes are flat in the tree -
         * a name leaf, `=`, an expression, repeated. The name of an attribute is therefore
         * whichever leaf came last before its value.
         */
        private fun attributeValue(element: PsiElement, name: String): Expr? {
            var attribute: String? = null

            for (child in meaningfulChildren(element)) {
                when {
                    child is Expr -> {
                        if (attribute == name) return child
                        attribute = null
                    }

                    child.node.elementType === SmartyTypes.ASSIGN -> {}

                    // The lexer is caseless, so `VAR=` names the same attribute as `var=`.
                    else -> attribute = child.text.lowercase()
                }
            }

            return null
        }

        private const val VAR_ATTRIBUTE = "var"
        private const val VALUE_ATTRIBUTE = "value"

        // ========================================================================
        // INCLUDE/EXTEND METHODS
        // ========================================================================

        /**
         * Gets the template path from include/extend statements.
         */
        @JvmStatic
        fun getTemplatePath(element: PsiElement): String {
            val node = element.node.findChildByType(SmartyTypes.STRING)
            if (node != null) {
                val text = node.text
                // Remove surrounding quotes
                return if (text.length > 2) text.substring(1, text.length - 1) else ""
            }
            return ""
        }

        /**
         * Gets all named parameters from include/extend.
         */
        @JvmStatic
        fun getTemplateParams(element: PsiElement): Map<String, String> {
            val params = mutableMapOf<String, String>()
            val vars = PsiTreeUtil.getChildrenOfType(element, Variable::class.java)

            if (vars != null) {
                for (`var` in vars) {
                    // Extract parameter assignments
                    val parent = `var`.parent
                    if (parent != null) {
                        val assignNode = parent.node.findChildByType(SmartyTypes.ASSIGN)
                        if (assignNode != null) {
                            val varName = getName(`var`)
                            if (varName != null) {
                                params[varName] = ""
                            }
                        }
                    }
                }
            }

            return params
        }

        // ========================================================================
        // LITERAL & NOCACHE METHODS
        // ========================================================================

        /**
         * Gets the raw content of a literal block.
         */
        @JvmStatic
        fun getLiteralContent(element: SmartyLiteralBlock): String {
            var node: ASTNode? = element.node.firstChildNode
            val content = StringBuilder()

            while (node != null) {
                val type = node.elementType
                if (type !== SmartyTypes.LITERAL_KW &&
                    type !== SmartyTypes.LDELIM &&
                    type !== SmartyTypes.RDELIM &&
                    type !== SmartyTypes.DIV
                ) {
                    content.append(node.text)
                }
                node = node.treeNext
            }

            return content.toString()
        }

        // ========================================================================
        // UTILITY METHODS
        // ========================================================================

        /**
         * Gets the full text of an element with proper formatting.
         */
        @JvmStatic
        fun getFullText(element: PsiElement): String {
            return element.text
        }

        /** Whether the element is part of a `{* ... *}` comment. */
        @JvmStatic
        fun isInComment(element: PsiElement): Boolean =
            element is PsiComment || PsiTreeUtil.getParentOfType(element, PsiComment::class.java) != null

        /**
         * Gets the parent Smarty tag containing this element.
         */
        @JvmStatic
        fun getParentTag(element: PsiElement): SmartyTag? {
            return PsiTreeUtil.getParentOfType(element, SmartyTag::class.java)
        }
    }
}
