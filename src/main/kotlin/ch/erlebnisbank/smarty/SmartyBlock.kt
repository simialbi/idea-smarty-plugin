package ch.erlebnisbank.smarty

import ch.erlebnisbank.smarty.psi.SmartyTokenSets
import ch.erlebnisbank.smarty.psi.SmartyTypes
import com.intellij.formatting.Block
import com.intellij.formatting.ChildAttributes
import com.intellij.formatting.Indent
import com.intellij.formatting.Spacing
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiFile
import com.intellij.psi.formatter.common.AbstractBlock
import com.intellij.psi.tree.TokenSet

/**
 * One node of the formatting tree.
 *
 * The blocks mirror the PSI one to one, minus the whitespace, with two exceptions that are worth
 * knowing about before reading the rest:
 *
 * **The formatter cannot re-indent a template body.** Outside a `{...}` tag the lexer produces
 * [SmartyTypes.TEXT] for everything, newlines included, because that text is output rather than
 * layout. The formatting engine only ever rewrites whitespace *between* blocks, so there is
 * physically nothing for it to touch between `{if}` and the markup it guards. That is a
 * limitation - `Reformat Code` will not line up nested tags - but it is also the reason a
 * reformat can never change a byte of what the template prints. Indenting block bodies would
 * need the lexer to hand layout whitespace to the parser separately from output text, and would
 * then have to reckon with the fact that the grammar matches `{/if}` flat, so no PSI node spans
 * a block.
 *
 * **`{literal}` and `{nocache}` blocks are atomic.** Their point is that their contents are
 * passed through untouched, so they report themselves as leaves and the engine leaves the whole
 * range alone.
 */
internal class SmartyBlock(
    node: ASTNode,
    private val indent: Indent,
    private val rules: SmartySpacingRules,
) : AbstractBlock(node, null, null) {

    override fun buildChildren(): List<Block> {
        if (isAtomic()) return emptyList()

        val childIndent = childIndent()
        val blocks = ArrayList<Block>()
        var child = myNode.firstChildNode
        while (child != null) {
            if (!isSkipped(child)) {
                blocks.add(SmartyBlock(child, childIndent, rules))
            }
            child = child.treeNext
        }
        return blocks
    }

    override fun getIndent(): Indent = indent

    override fun getSpacing(child1: Block?, child2: Block): Spacing? = rules.spacing(this, child1, child2)

    override fun isLeaf(): Boolean = isAtomic() || myNode.firstChildNode == null

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes =
        ChildAttributes(Indent.getNoneIndent(), null)

    /**
     * Template items sit at column zero as far as the formatter is concerned - the text before
     * them is not its whitespace to move. Inside a tag a continuation indent is what a wrapped
     * argument list wants.
     */
    private fun childIndent(): Indent =
        if (myNode.psi is PsiFile) Indent.getNoneIndent() else Indent.getContinuationWithoutFirstIndent()

    private fun isAtomic(): Boolean = ATOMIC.contains(myNode.elementType)

    private fun isSkipped(child: ASTNode): Boolean =
        SmartyTokenSets.WHITE_SPACES.contains(child.elementType) || child.textRange.isEmpty

    private companion object {

        /**
         * Nodes the formatter treats as one opaque run of text: the two verbatim blocks, plus
         * the text and markup nodes, which hold no whitespace tokens to begin with.
         */
        val ATOMIC: TokenSet = TokenSet.create(
            SmartyTypes.SMARTY_LITERAL_BLOCK,
            SmartyTypes.SMARTY_NOCACHE_BLOCK,
            SmartyTypes.TEXT_CONTENT,
            SmartyTypes.HTML_CONTENT
        )
    }
}
