package ch.erlebnisplus.smarty.psi.impl

import ch.erlebnisplus.smarty.psi.SmartyNamedElement
import com.intellij.lang.ASTNode

/**
 * Base class of every named Smarty element, used as the `mixin=` of the corresponding rules in
 * `Smarty.bnf`. Grammar-Kit generates the concrete impl to extend this, so it must keep the
 * single `ASTNode` constructor.
 *
 * Extends [SmartyReferenceHostImpl] because a declaration is also a reference host: a
 * `{block name="content"}` in a child template points at the block it overrides.
 */
abstract class SmartyNamedElementImpl(node: ASTNode) :
    SmartyReferenceHostImpl(node), SmartyNamedElement
