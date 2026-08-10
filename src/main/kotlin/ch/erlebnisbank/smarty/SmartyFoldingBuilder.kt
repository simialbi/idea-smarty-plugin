package ch.erlebnisbank.smarty

import ch.erlebnisbank.smarty.psi.FunctionBody
import ch.erlebnisbank.smarty.psi.SmartyClosingTag
import ch.erlebnisbank.smarty.psi.SmartyFunctionCall
import ch.erlebnisbank.smarty.psi.SmartyLiteralBlock
import ch.erlebnisbank.smarty.psi.SmartyNocacheBlock
import ch.erlebnisbank.smarty.psi.SmartyTag
import ch.erlebnisbank.smarty.psi.SmartyTokenSets
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil

/**
 * Collapses the parts of a template that hide detail: multi-line comments, verbatim blocks, and
 * the body of every paired tag such as `{block}…{/block}` or `{foreach}…{/foreach}`.
 *
 * Pairs have to be reconstructed here. The grammar matches closing tags flat - see
 * `smarty_closing_tag` in `Smarty.bnf` - so no single PSI node spans an opening tag and its
 * counterpart, and a stack is used to match them the way the parser would if it nested them.
 *
 * Read more: https://plugins.jetbrains.com/docs/intellij/folding-builder.html
 */
class SmartyFoldingBuilder : FoldingBuilderEx(), DumbAware {

    override fun buildFoldRegions(
        root: PsiElement,
        document: Document,
        quick: Boolean
    ): Array<FoldingDescriptor> {
        val descriptors = mutableListOf<FoldingDescriptor>()

        // Opening tags still waiting for their {/tag}, innermost last.
        val unclosed = ArrayDeque<Pair<IElementType, PsiElement>>()

        // Comments are not part of the grammar: the parser skips comment tokens, so the platform
        // hands them over as PsiComment leaves rather than as a rule match.
        for (comment in PsiTreeUtil.findChildrenOfType(root, PsiComment::class.java)) {
            fold(descriptors, document, comment, comment.textRange, "{*...*}")
        }

        for (tag in PsiTreeUtil.findChildrenOfType(root, SmartyTag::class.java)) {
            when (val content = tag.firstChild) {
                is SmartyLiteralBlock ->
                    fold(descriptors, document, content, content.textRange, "{literal}...{/literal}")

                is SmartyNocacheBlock ->
                    fold(descriptors, document, content, content.textRange, "{nocache}...{/nocache}")

                is SmartyClosingTag -> closePair(descriptors, document, unclosed, content, tag)

                is SmartyFunctionCall -> openingKeyword(content)?.let { unclosed.addLast(it to tag) }
            }
        }

        return descriptors.toTypedArray()
    }

    /**
     * Folds everything between an opening tag and its counterpart, leaving both tags on screen so
     * that `{block name="content"}...{/block}` still reads.
     *
     * Unclosed tags in between are discarded rather than kept waiting: a template being typed is
     * routinely unbalanced, and matching the nearest opener of the same kind keeps the regions
     * properly nested, which the platform requires.
     */
    private fun closePair(
        descriptors: MutableList<FoldingDescriptor>,
        document: Document,
        unclosed: ArrayDeque<Pair<IElementType, PsiElement>>,
        closing: SmartyClosingTag,
        closingTag: PsiElement
    ) {
        val keyword = closingKeyword(closing) ?: return

        val index = unclosed.indexOfLast { (opener, _) -> opener === keyword }
        if (index < 0) return

        val (_, openingTag) = unclosed[index]
        while (unclosed.size > index) unclosed.removeLast()

        val body = TextRange(openingTag.textRange.endOffset, closingTag.textRange.startOffset)
        fold(descriptors, document, PsiTreeUtil.findCommonParent(openingTag, closingTag), body, "...")
    }

    /**
     * @return the keyword a tag opens with, or `null` when it is not a tag that has to be closed
     */
    private fun openingKeyword(call: SmartyFunctionCall): IElementType? {
        val body = PsiTreeUtil.findChildOfType(call, FunctionBody::class.java) ?: return null
        val statement = body.firstChild ?: return null

        val keyword = PsiTreeUtil.getDeepestFirst(statement).node.elementType
        return keyword.takeIf { SmartyTokenSets.BLOCK_OPENERS.contains(it) }
    }

    private fun closingKeyword(closing: SmartyClosingTag): IElementType? {
        var child = closing.firstChild

        while (child != null) {
            val type = child.node.elementType
            if (SmartyTokenSets.BLOCK_OPENERS.contains(type)) return type
            child = child.nextSibling
        }

        return null
    }

    /** Records a region, skipping anything that would not actually hide a line. */
    private fun fold(
        descriptors: MutableList<FoldingDescriptor>,
        document: Document,
        anchor: PsiElement?,
        range: TextRange,
        placeholder: String
    ) {
        if (anchor == null || range.isEmpty) return
        if (range.endOffset > document.textLength) return
        if (document.getLineNumber(range.startOffset) == document.getLineNumber(range.endOffset)) return

        descriptors.add(FoldingDescriptor(anchor.node, range, null, placeholder))
    }

    /**
     * Every region carries its own placeholder, so this is only a fallback. Several regions can
     * share an anchor - the pairs are all anchored on their common parent - which makes a
     * per-node placeholder impossible to compute here anyway.
     */
    override fun getPlaceholderText(node: ASTNode): String = "..."

    /** Structural folding, so nothing starts collapsed. */
    override fun isCollapsedByDefault(node: ASTNode): Boolean = false
}
