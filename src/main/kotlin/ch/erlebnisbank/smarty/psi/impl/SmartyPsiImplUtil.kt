package ch.erlebnisbank.smarty.psi.impl

import ch.erlebnisbank.smarty.psi.*
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

class SmartyPsiImplUtil private constructor() {
    companion object {
        // ========================================================================
        // VARIABLE METHODS
        // ========================================================================

        /**
         * Gets the name of a variable (without the $).
         */
        @JvmStatic
        fun getName(element: Variable): String? {
            val node = element.node.findChildByType(SmartyTypes.DOLLAR_VAR)
                ?: element.node.findChildByType(SmartyTypes.VARIABLE)
            val text = node?.text ?: element.text
            if (text.startsWith("$")) {
                // Remove leading $ and extract the identifier
                return text.substring(1).split("[", ".").firstOrNull()
            }
            return text.split("[", ".").firstOrNull()
        }

        /**
         * Checks if the variable has array access (e.g., $var['key']).
         */
        @JvmStatic
        fun hasArrayAccess(element: Variable): Boolean {
            return element.node.findChildByType(SmartyTypes.LBRACKET) != null
        }

        /**
         * Gets array indices from variable access.
         */
        @JvmStatic
        fun getArrayIndices(element: Variable): Array<String> {
            val indices = mutableListOf<String>()
            var node: ASTNode? = element.node.firstChildNode

            while (node != null) {
                if (node.elementType === SmartyTypes.LBRACKET) {
                    var nextNode = node.treeNext
                    while (nextNode != null && nextNode.elementType !== SmartyTypes.RBRACKET) {
                        if (nextNode.elementType === SmartyTypes.IDENTIFIER ||
                            nextNode.elementType === SmartyTypes.STRING ||
                            nextNode.elementType === SmartyTypes.NUMBER
                        ) {
                            indices.add(nextNode.text)
                        }
                        nextNode = nextNode.treeNext
                    }
                }
                node = node.treeNext
            }

            return indices.toTypedArray()
        }

        /**
         * Gets property names from dot notation (e.g., $obj.property.nested).
         */
        @JvmStatic
        fun getPropertyChain(element: Variable): Array<String> {
            val properties = mutableListOf<String>()
            var node: ASTNode? = element.node.firstChildNode
            var afterDot = false

            while (node != null) {
                if (node.elementType === SmartyTypes.DOT) {
                    afterDot = true
                } else if (afterDot && node.elementType === SmartyTypes.IDENTIFIER) {
                    properties.add(node.text)
                    afterDot = false
                }
                node = node.treeNext
            }

            return properties.toTypedArray()
        }

        // ========================================================================
        // EXPRESSION METHODS
        // ========================================================================

        /**
         * Gets the operator of a binary expression.
         */
        @JvmStatic
        fun getOperator(element: Expr): String? {
            var node: ASTNode? = element.node.firstChildNode

            while (node != null) {
                val type = node.elementType
                if (type === SmartyTypes.EQ || type === SmartyTypes.NEQ ||
                    type === SmartyTypes.LT || type === SmartyTypes.GT ||
                    type === SmartyTypes.LE || type === SmartyTypes.GE ||
                    type === SmartyTypes.AND || type === SmartyTypes.OR ||
                    type === SmartyTypes.PLUS || type === SmartyTypes.MINUS ||
                    type === SmartyTypes.MULT || type === SmartyTypes.DIV ||
                    type === SmartyTypes.MOD
                ) {
                    return node.text
                }
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
                    type === SmartyTokenType.TRUE ||
                    type === SmartyTokenType.FALSE ||
                    type === SmartyTokenType.NULL_LITERAL
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
                    node.elementType !== SmartyTokenType.WS
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
         * Gets the target variable for assignment.
         */
        @JvmStatic
        fun getAssignTarget(element: AssignStatement): String {
            val `var` = PsiTreeUtil.findChildOfType(element, Variable::class.java)
            return if (`var` != null) getName(`var`) ?: "" else ""
        }

        /**
         * Gets the value expression from assignment.
         */
        @JvmStatic
        fun getAssignValue(element: AssignStatement): Expr? {
            val exprs = PsiTreeUtil.getChildrenOfType(element, Expr::class.java)
            return if (exprs != null && exprs.isNotEmpty()) exprs[0] else null
        }

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
                if (type !== SmartyTypes.LITERAL &&
                    type !== SmartyTypes.LDELIM &&
                    type !== SmartyTypes.RDELIM &&
                    type !== SmartyTokenType.FORWARD_SLASH
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

        /**
         * Checks if element is within a comment block.
         */
        @JvmStatic
        fun isInComment(element: PsiElement): Boolean {
            var parent = element.parent
            while (parent != null) {
                if (parent is SmartyComment) {
                    return true
                }
                parent = parent.parent
            }
            return false
        }

        /**
         * Gets the parent Smarty tag containing this element.
         */
        @JvmStatic
        fun getParentTag(element: PsiElement): SmartyTag? {
            return PsiTreeUtil.getParentOfType(element, SmartyTag::class.java)
        }
    }
}
