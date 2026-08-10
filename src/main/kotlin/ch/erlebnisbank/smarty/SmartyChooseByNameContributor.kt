package ch.erlebnisbank.smarty

import ch.erlebnisbank.smarty.psi.SmartyUtil
import com.intellij.navigation.ChooseByNameContributorEx
import com.intellij.navigation.NavigationItem
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.Processor
import com.intellij.util.indexing.FindSymbolParameters
import com.intellij.util.indexing.IdFilter

/**
 * Lists the declarations of a Smarty template - `{block name="content"}` and
 * `{function name="box"}` - under Navigate | Symbol, so that they can be jumped to by name from
 * anywhere in the project.
 *
 * [ChooseByNameContributorEx] is implemented rather than the older `ChooseByNameContributor`
 * because its two methods stream through a [Processor]: the chooser stops asking as soon as the
 * user has typed enough to narrow the list, instead of every template being visited to build an
 * array that is then thrown away.
 *
 * Read more: https://plugins.jetbrains.com/docs/intellij/go-to-symbol-contributor.html
 */
class SmartyChooseByNameContributor : ChooseByNameContributorEx {

    /**
     * Feeds the chooser the names it matches the typed pattern against.
     *
     * [filter] is ignored: it narrows candidates by the file id of an index, and Smarty
     * declarations are not indexed - see [SmartyUtil.findDeclarations]. [scope] is honoured,
     * which is what decides whether "Include non-project items" widens the list.
     */
    override fun processNames(
        processor: Processor<in String>,
        scope: GlobalSearchScope,
        filter: IdFilter?
    ) {
        val project = scope.project ?: return

        for (declaration in SmartyUtil.findDeclarations(project, scope)) {
            val name = declaration.name ?: continue
            if (!processor.process(name)) return
        }
    }

    /**
     * Feeds the chooser the declarations behind one name. A name is rarely unique - a child
     * template overriding `{block name="content"}` declares the same name as its parent - so
     * every match is passed on and the chooser lists them side by side, told apart by the file
     * name in their presentation.
     */
    override fun processElementsWithName(
        name: String,
        processor: Processor<in NavigationItem>,
        parameters: FindSymbolParameters
    ) {
        val declarations = SmartyUtil.findDeclarationsByName(
            parameters.project,
            parameters.searchScope,
            name
        )

        for (declaration in declarations) {
            if (!processor.process(declaration)) return
        }
    }
}
