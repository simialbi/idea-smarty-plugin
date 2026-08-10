package ch.erlebnisbank.smarty

import ch.erlebnisbank.smarty.psi.BlockStatement
import ch.erlebnisbank.smarty.psi.ExtendsStatement
import ch.erlebnisbank.smarty.psi.IncludeStatement
import ch.erlebnisbank.smarty.psi.InsertStatement
import ch.erlebnisbank.smarty.psi.SmartyUtil
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import javax.swing.Icon

/**
 * Puts navigation icons into the gutter of Smarty templates:
 *
 * - `{include}`, `{extends}` and `{insert}` get an icon that opens the referenced template.
 * - `{block}` gets an icon that jumps to the declarations of the same name in the templates
 *   the current one inherits from.
 *
 * Markers are only ever created for leaf elements, as required by the platform: a marker on
 * a composite element is removed by the visible-range phase of `LineMarkersPass` and added
 * back by the whole-file phase whenever the element is only partially visible, which makes
 * the gutter icon blink. The keyword opening the statement is used as the anchor, and the
 * statement itself is reached through its parent.
 *
 * Read more: https://plugins.jetbrains.com/docs/intellij/line-marker-provider.html
 */
class SmartyLineMarkerProvider : RelatedItemLineMarkerProvider() {

    /** Shown in Settings | Editor | General | Gutter Icons, where these icons can be turned off. */
    override fun getName(): String = "Smarty template navigation"

    override fun getIcon(): Icon = SmartyIcons.FILE

    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>
    ) {
        // Leaf elements only, and only the keyword the statement starts with, so that a
        // statement contributes at most one marker.
        if (element.firstChild != null) return

        val statement = element.parent ?: return
        if (!isLeadingKeyword(element, statement)) return

        when (statement) {
            is IncludeStatement, is ExtendsStatement, is InsertStatement ->
                collectTemplateMarker(element, statement, result)

            is BlockStatement -> collectBlockMarker(element, statement, result)
        }
    }

    /**
     * Adds a marker navigating to the template a statement references. Statements whose target
     * is dynamic or unresolvable get no marker - those are reported by [SmartyAnnotator].
     */
    private fun collectTemplateMarker(
        anchor: PsiElement,
        statement: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>
    ) {
        val template = SmartyUtil.findTemplatePath(statement) ?: return
        val target = SmartyUtil.resolveTemplate(statement, template.value) ?: return

        val builder = NavigationGutterIconBuilder.create(SmartyIcons.FILE)
            .setTargets(target)
            .setTooltipText("Navigate to template '${template.value}'")
            .setPopupTitle("Smarty Template")
        result.add(builder.createLineMarkerInfo(anchor))
    }

    /**
     * Adds a marker navigating to the blocks of the same name in the inherited templates, the
     * ones this declaration overrides.
     */
    private fun collectBlockMarker(
        anchor: PsiElement,
        statement: BlockStatement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>
    ) {
        val name = SmartyUtil.findDeclarationName(statement)?.value ?: return
        val file = statement.containingFile?.originalFile ?: return

        val overridden = SmartyUtil.findExtendedTemplates(file)
            .flatMap { parent -> SmartyUtil.findBlocksByName(parent, name) }
        if (overridden.isEmpty()) return

        val builder = NavigationGutterIconBuilder.create(AllIcons.Gutter.OverridingMethod)
            .setTargets(overridden)
            .setTooltipText("Overrides block '$name'")
            .setPopupTitle("Overridden Blocks")
        result.add(builder.createLineMarkerInfo(anchor))
    }

    /**
     * Checks whether [element] is the first meaningful child of [statement], that is the
     * keyword the statement is written with.
     */
    private fun isLeadingKeyword(element: PsiElement, statement: PsiElement): Boolean {
        var child = statement.firstChild
        while (child != null) {
            if (child.text.isNotBlank()) return child === element
            child = child.nextSibling
        }
        return false
    }
}
