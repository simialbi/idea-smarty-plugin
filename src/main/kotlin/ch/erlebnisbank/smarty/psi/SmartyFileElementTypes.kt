package ch.erlebnisbank.smarty.psi

import ch.erlebnisbank.smarty.SmartyLanguage
import com.intellij.psi.templateLanguages.TemplateDataElementType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.OuterLanguageElementType

/**
 * The two element types that make a `.tpl` file hold two syntax trees at once.
 *
 * A template language does not parse the data language itself. Instead the document is parsed
 * twice: once by the Smarty parser, which sees the markup as [SmartyTypes.TEXT], and once by the
 * data language's parser, which is handed a copy of the document with every Smarty tag blanked
 * out. Each tree covers the whole file, and where one tree has real nodes the other has
 * placeholders. `SmartyFileViewProvider` is what puts the two roots on one document.
 */
object SmartyFileElementTypes {

    /**
     * The placeholder the *data* tree gets for every range that belongs to Smarty.
     *
     * [OuterLanguageElementType] is not just a name: it implements `ILeafElementType` and builds
     * an `OuterLanguageElementImpl`, which is the PSI class the platform recognises as "this is
     * somebody else's text". Handing a plain [IElementType] here would log an error and fall back
     * to constructing that class by hand.
     *
     * The language is Smarty rather than the data language on purpose - the fragment *is* Smarty,
     * it just sits inside the HTML tree.
     */
    val OUTER_ELEMENT_TYPE: IElementType =
        OuterLanguageElementType("SMARTY_FRAGMENT", SmartyLanguage.INSTANCE)

    /**
     * The content element type of the data language's file, set in `SmartyFileViewProvider`.
     *
     * It is a lazy-parseable node: when the data tree is first needed, the platform re-runs the
     * Smarty *lexer* over the document and splits it in two - every [SmartyTypes.TEXT] token is
     * copied through to the data language verbatim, everything else becomes an
     * [OUTER_ELEMENT_TYPE] placeholder. That token-level split is the whole reason the lexer emits
     * a single token type for template data: two types would leave half the markup outside.
     *
     * One instance is enough for any data language. The base class reads the language off the
     * view provider, so switching a file from HTML to XML needs no second element type.
     */
    val TEMPLATE_DATA: TemplateDataElementType = TemplateDataElementType(
        "SMARTY_TEMPLATE_DATA",
        SmartyLanguage.INSTANCE,
        SmartyTypes.TEXT,
        OUTER_ELEMENT_TYPE,
    )
}
