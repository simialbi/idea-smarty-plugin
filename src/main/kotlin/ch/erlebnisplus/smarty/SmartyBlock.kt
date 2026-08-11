package ch.erlebnisplus.smarty

import ch.erlebnisplus.smarty.psi.SmartyTokenSets
import ch.erlebnisplus.smarty.psi.SmartyTypes
import com.intellij.formatting.Alignment
import com.intellij.formatting.Block
import com.intellij.formatting.Indent
import com.intellij.formatting.Spacing
import com.intellij.formatting.Wrap
import com.intellij.formatting.templateLanguages.DataLanguageBlockWrapper
import com.intellij.formatting.templateLanguages.TemplateLanguageBlock
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.xml.HtmlCodeStyleSettings
import com.intellij.psi.templateLanguages.TemplateLanguageFileViewProvider
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlTag

/**
 * One node of the formatting tree, on either side of the language boundary.
 *
 * A `.tpl` file has two PSI roots and therefore two candidate block trees. The formatter runs one
 * tree, so [TemplateLanguageFormattingModelBuilder][com.intellij.formatting.templateLanguages.TemplateLanguageFormattingModelBuilder]
 * builds the data language's model first, wraps each of its blocks in a [DataLanguageBlockWrapper],
 * and hands them to the Smarty blocks as *foreign children*. The base class merges the two lists by
 * offset. The result is a single tree in which `<div>` and `{if}` are siblings, and that is what
 * lets `Reformat Code` indent a Smarty tag according to the markup around it.
 *
 * Two consequences of the merge that the rest of this class is mostly about:
 *
 * **Template data is claimed by whoever can say more about it.** The Smarty tree covers the markup
 * with [SmartyTypes.TEXT_CONTENT] nodes and the data tree covers the same ranges with real
 * elements. Building blocks for both would give the engine two blocks over one range, so
 * [shouldBuildBlockFor] drops the Smarty side whenever foreign children are present. With no data
 * language formatter - plain text, say - nothing is dropped and the old behaviour stands: the text
 * is one opaque leaf and a reformat cannot change a byte of it.
 *
 * **`{literal}` and `{nocache}` stay atomic.** Their whole point is that their contents are passed
 * through untouched, so they report themselves as leaves, which discards any foreign children
 * inside them along with the Smarty ones. Markup inside a `{literal}` is therefore parsed and
 * highlighted but never re-indented.
 */
internal class SmartyBlock(
    node: ASTNode,
    wrap: Wrap?,
    alignment: Alignment?,
    blockFactory: SmartyFormattingModelBuilder,
    private val settings: CodeStyleSettings,
    foreignChildren: List<DataLanguageBlockWrapper>?,
) : TemplateLanguageBlock(node, wrap, alignment, blockFactory, settings, foreignChildren) {

    /**
     * Built on demand, and only ever on a block that has at least two children to space apart -
     * which is a small minority of the tree. The rules compile a `SpacingBuilder`, so creating one
     * per leaf would be waste.
     */
    private val rules: SmartySpacingRules by lazy(LazyThreadSafetyMode.NONE) {
        SmartySpacingRules(settings)
    }

    /**
     * Whitespace is skipped by the base class already; this adds the two cases specific to a
     * template language.
     *
     * [SmartyTypes.WS] is the lexer's own whitespace token rather than the platform's, so it is
     * listed explicitly instead of trusting the generic check to recognise it. Template data is
     * dropped in favour of the data language's blocks - see the class comment.
     */
    override fun shouldBuildBlockFor(childNode: ASTNode): Boolean {
        if (SmartyTokenSets.WHITE_SPACES.contains(childNode.elementType)) return false
        return !TEMPLATE_DATA.contains(childNode.elementType) || foreignChildren.isNullOrEmpty()
    }

    /**
     * Only consulted by the base class's own [shouldBuildBlockFor], which is overridden above; it
     * is answered accurately regardless. The composite is the right answer rather than the
     * [SmartyTypes.TEXT] token: the grammar wraps every run of template data in one, so that is
     * what a child of the file node actually is.
     */
    override fun getTemplateTextElementType(): IElementType = SmartyTypes.TEXT_CONTENT

    /**
     * A Smarty tag is indented like the markup it stands in, and nothing else is indented at all.
     *
     * The depth test is the top-level case: a block whose grandparent is missing is a direct child
     * of the file, so no markup encloses it and there is no reason to move it. Anything deeper is
     * a child of one of the data language's blocks, and takes one step of indent inside it, exactly
     * as that language's own children do.
     *
     * Tokens *inside* a `{...}` fall to the last branch. Nothing wraps them today - every wrap is
     * [com.intellij.formatting.WrapType.NONE] - so the continuation indent only decides where a
     * hand-wrapped argument list lines up.
     */
    override fun getIndent(): Indent = when {
        parent?.parent == null -> Indent.getNoneIndent()
        isTemplateItem() -> Indent.getNormalIndent()
        else -> Indent.getContinuationWithoutFirstIndent()
    }

    override fun getChildIndent(): Indent =
        if (myNode.psi is PsiFile) Indent.getNoneIndent() else Indent.getNormalIndent()

    /**
     * Spacing between two Smarty blocks comes from [SmartySpacingRules]; between two blocks of the
     * data language it comes from that language, which the base class asks. The rules return `null`
     * for anything that is not a pair of Smarty blocks, so the two never fight.
     *
     * A tag next to markup is the third case and does not come through here at all: the two are
     * children of a data language block, which asks [getLeftNeighborSpacing] and
     * [getRightNeighborSpacing] instead.
     */
    override fun getSpacing(child1: Block?, child2: Block): Spacing? =
        rules.spacing(this, child1, child2) ?: super.getSpacing(child1, child2)

    /**
     * The two hooks for the third case: a Smarty block standing between blocks of the data
     * language. They are asked only when the common parent is a [DataLanguageBlockWrapper], which
     * is precisely the tag-next-to-markup situation, and the answer is cached per pair.
     *
     * Both return `null` almost always, which lets the engine do what it does to any unconstrained
     * gap containing a line break: re-indent the line after it and drop the spaces before it. That
     * is the behaviour [testMarkupIsIndentedByTheDataLanguage][SmartyFormatterTest] wants, and it
     * is why there is no rule here to tidy `{if $a}   <div>` down to one space - the gap has no
     * line break, so nothing touches it either way.
     *
     * Inside `<pre>` that default is wrong, because there the whitespace is content. The data
     * language knows which elements those are and normally keeps their contents intact by handing
     * the whole run to one block; the merge takes that block apart to make room for the Smarty
     * block, and the run stops being anybody's. Read-only spacing puts it back.
     */
    override fun getLeftNeighborSpacing(
        left: Block?,
        parent: DataLanguageBlockWrapper,
        childIndex: Int,
    ): Spacing? = readOnlyIfSignificantAt(left?.textRange?.endOffset ?: myNode.startOffset)

    override fun getRightNeighborSpacing(
        right: Block,
        parent: DataLanguageBlockWrapper,
        childIndex: Int,
    ): Spacing? = readOnlyIfSignificantAt(myNode.textRange.endOffset)

    private fun readOnlyIfSignificantAt(offset: Int): Spacing? =
        if (isWhitespaceSignificantAt(offset)) Spacing.getReadOnlySpacing() else null

    /**
     * Whether the whitespace at [offset] is rendered rather than ignored.
     *
     * The question is put to the *data* tree, because that is the tree with elements in it: the
     * offset is looked up in the other root of the same file and the ancestors of whatever is found
     * there are matched against the data language's own list of elements whose contents are kept -
     * HTML's `span,pre,textarea` by default, editable under *Code Style | HTML | Other*. A data
     * language with no markup yields no [XmlTag] and therefore `false`, which is the right answer.
     *
     * [offset] is the boundary between this block and its neighbour rather than this block's own
     * start, because a `{...}` shows up in the data tree as one opaque fragment that sits outside
     * the element structure and would find no enclosing tag. The whitespace next to it does not.
     */
    private fun isWhitespaceSignificantAt(offset: Int): Boolean {
        val names = settings
            .getCustomSettings(HtmlCodeStyleSettings::class.java)
            .HTML_KEEP_WHITESPACES_INSIDE
            ?.split(',')
            ?.map(String::trim)
            ?.filter(String::isNotEmpty)
            ?.takeIf(List<String>::isNotEmpty)
            ?: return false

        val provider = myNode.psi.containingFile?.viewProvider as? TemplateLanguageFileViewProvider
            ?: return false
        val dataRoot = provider.getPsi(provider.templateDataLanguage) ?: return false

        var tag = PsiTreeUtil.getParentOfType(dataRoot.findElementAt(offset), XmlTag::class.java)
        while (tag != null) {
            val name = tag.name
            if (names.any { it.equals(name, ignoreCase = true) }) return true
            tag = tag.parentTag
        }
        return false
    }

    override fun isLeaf(): Boolean = isAtomic() || super.isLeaf()

    private fun isAtomic(): Boolean = ATOMIC.contains(myNode.elementType)

    /** True for the things a template is a sequence of: tags, comments and runs of markup. */
    private fun isTemplateItem(): Boolean = myNode.treeParent?.psi is PsiFile

    private companion object {

        /**
         * Nodes the formatter treats as one opaque run of text: the two verbatim blocks, plus a
         * run of template data, which holds no whitespace tokens to begin with and is only ever
         * built as a block when the data language contributed nothing.
         */
        val ATOMIC: TokenSet = TokenSet.create(
            SmartyTypes.SMARTY_LITERAL_BLOCK,
            SmartyTypes.SMARTY_NOCACHE_BLOCK,
            SmartyTypes.TEXT_CONTENT,
        )

        /**
         * What the data language is given instead. The composite is what the file node has as a
         * child; the bare token is what the verbatim blocks hold.
         */
        val TEMPLATE_DATA: TokenSet = TokenSet.create(SmartyTypes.TEXT_CONTENT, SmartyTypes.TEXT)
    }
}
