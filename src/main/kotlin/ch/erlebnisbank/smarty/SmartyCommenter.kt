package ch.erlebnisbank.smarty

import com.intellij.lang.Commenter

/**
 * `{* ... *}` is the only comment Smarty has.
 *
 * There is no line comment, and `null` is the right answer rather than a missing feature: the line
 * comment action falls back to the block form when a language has no line prefix, so *Comment with
 * Line Comment* still works and wraps the line in `{*` and `*}`.
 *
 * The two *commented* prefixes are `null` for a reason worth spelling out, because answering them
 * with the real delimiters looks harmless and is not. They tell the platform how to rewrite a
 * delimiter that ends up nested inside a new comment, and Smarty has no way to write one: the lexer
 * matches `{*` up to the **first** `*}`, so a stray `*}` anywhere in a region being commented would
 * close the new comment early and leave the rest of the region rendering as markup. Answering
 * `null` makes the platform split the comment around such a delimiter instead of escaping it, which
 * is the one strategy that works here - `<p>*}</p>` is commented as `{*<p>*}{*</p>*}`, two comments
 * with nothing left visible. Answering `{*`/`*}` asks for an escape that is a no-op, and the
 * corruption goes through unnoticed.
 */
class SmartyCommenter : Commenter {

    override fun getLineCommentPrefix(): String? = null

    override fun getBlockCommentPrefix(): String = "{*"

    override fun getBlockCommentSuffix(): String = "*}"

    /** Deliberately not `{*` - see the class comment. */
    override fun getCommentedBlockCommentPrefix(): String? = null

    /** Deliberately not `*}` - see the class comment. */
    override fun getCommentedBlockCommentSuffix(): String? = null
}
