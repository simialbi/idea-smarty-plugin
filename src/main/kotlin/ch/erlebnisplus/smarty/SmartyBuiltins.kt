package ch.erlebnisplus.smarty

/**
 * The Smarty vocabulary: built-in tags with their attributes, the built-in modifiers and the
 * `$smarty` super global. Shared by [SmartyAnnotator] and [SmartyCompletionContributor] so
 * that both agree on what counts as built-in.
 *
 * See https://www.smarty.net/docs/en/
 */
object SmartyBuiltins {

    /**
     * A built-in Smarty tag.
     *
     * @property description short explanation shown as the type text of a lookup element
     * @property block whether the tag has to be closed with `{/tag}`
     * @property attributes the attributes the tag accepts
     */
    data class Tag(
        val description: String,
        val block: Boolean = false,
        val attributes: List<String> = emptyList()
    )

    /** Smarty super globals, see https://www.smarty.net/docs/en/language.variables.smarty.tpl */
    val RESERVED_VARIABLES: Set<String> = setOf("smarty")

    /** The keys available on `$smarty`. */
    val SMARTY_PROPERTIES: List<String> = listOf(
        "block", "capture", "config", "const", "cookies", "current_dir", "env", "foreach",
        "get", "ldelim", "now", "post", "rdelim", "request", "section", "server", "session",
        "template", "template_object", "version"
    )

    /** Built-in modifiers mapped to the maximum number of parameters they accept. */
    val MODIFIERS: Map<String, Int> = mapOf(
        "capitalize" to 2,
        "cat" to 1,
        "count_characters" to 1,
        "count_paragraphs" to 0,
        "count_sentences" to 0,
        "count_words" to 0,
        "date_format" to 2,
        "default" to 1,
        "escape" to 3,
        "from_charset" to 1,
        "indent" to 2,
        "lower" to 0,
        "nl2br" to 0,
        "regex_replace" to 2,
        "replace" to 2,
        "spacify" to 1,
        "string_format" to 1,
        "strip" to 1,
        "strip_tags" to 1,
        "to_charset" to 1,
        "truncate" to 4,
        "unescape" to 2,
        "upper" to 0,
        "wordwrap" to 3
    )

    /** Built-in tags and the functions bundled with Smarty. */
    val TAGS: Map<String, Tag> = mapOf(
        // Variables and output
        "assign" to Tag("assign a variable", attributes = listOf("var", "value", "scope", "nocache")),
        "append" to Tag("append to an array", attributes = listOf("var", "value", "index", "scope")),
        "capture" to Tag(
            "capture output", block = true,
            attributes = listOf("name", "assign", "append", "scope")
        ),
        "config_load" to Tag("load a config file", attributes = listOf("file", "section", "scope")),
        "eval" to Tag("evaluate a variable as a template", attributes = listOf("var", "assign")),

        // Conditionals
        "if" to Tag("conditional", block = true),
        "elseif" to Tag("alternative condition"),
        "else" to Tag("fallback branch"),
        "switch" to Tag("multi way branch", block = true),
        "case" to Tag("switch branch"),
        "default" to Tag("switch fallback branch"),

        // Loops
        "foreach" to Tag(
            "loop over an array", block = true,
            attributes = listOf("from", "item", "key", "name", "nocache")
        ),
        "foreachelse" to Tag("empty foreach branch"),
        "for" to Tag("counting loop", block = true, attributes = listOf("max", "step")),
        "while" to Tag("conditional loop", block = true),
        "section" to Tag(
            "indexed loop", block = true,
            attributes = listOf("name", "loop", "start", "step", "max", "show")
        ),
        "sectionelse" to Tag("empty section branch"),
        "break" to Tag("leave the loop"),
        "continue" to Tag("skip to the next iteration"),

        // Templates
        "include" to Tag(
            "include a template",
            attributes = listOf("file", "assign", "cache_lifetime", "compile_id", "cache_id", "scope", "nocache")
        ),
        "extends" to Tag("inherit from a template", attributes = listOf("file")),
        "block" to Tag(
            "inheritance block", block = true,
            attributes = listOf("name", "append", "prepend", "hide", "nocache")
        ),
        "insert" to Tag("include uncached output", attributes = listOf("name", "assign", "script")),
        "function" to Tag("define a template function", block = true, attributes = listOf("name")),
        "call" to Tag("call a template function", attributes = listOf("name", "assign", "nocache")),
        "fetch" to Tag("fetch a file or URL", attributes = listOf("file", "assign")),

        // Output control
        "literal" to Tag("output verbatim", block = true),
        "nocache" to Tag("disable caching", block = true),
        "strip" to Tag("strip redundant whitespace", block = true),
        "setfilter" to Tag("set the output filter chain", block = true),
        "ldelim" to Tag("output the left delimiter"),
        "rdelim" to Tag("output the right delimiter"),
        "debug" to Tag("show the debug console", attributes = listOf("output")),

        // Bundled functions
        "counter" to Tag(
            "print a running count",
            attributes = listOf("name", "start", "skip", "direction", "print", "assign")
        ),
        "cycle" to Tag(
            "cycle through values",
            attributes = listOf("name", "values", "print", "advance", "delimiter", "reset", "assign")
        ),
        "mailto" to Tag(
            "obfuscated mailto link",
            attributes = listOf("address", "text", "encode", "cc", "bcc", "subject", "extra")
        ),
        "math" to Tag("evaluate an equation", attributes = listOf("equation", "format", "var", "assign")),
        "textformat" to Tag(
            "reformat a text block", block = true,
            attributes = listOf("style", "indent", "indent_first", "indent_char", "wrap", "wrap_char", "wrap_cut", "assign")
        ),
        "html_checkboxes" to Tag(
            "checkbox group",
            attributes = listOf("name", "values", "output", "selected", "options", "separator", "labels", "assign")
        ),
        "html_image" to Tag(
            "image tag with dimensions",
            attributes = listOf("file", "height", "width", "alt", "href", "basedir", "path_prefix")
        ),
        "html_options" to Tag(
            "select options",
            attributes = listOf("name", "values", "output", "selected", "options")
        ),
        "html_radios" to Tag(
            "radio button group",
            attributes = listOf("name", "values", "output", "selected", "options", "separator", "labels", "assign")
        ),
        "html_select_date" to Tag(
            "date selection boxes",
            attributes = listOf("prefix", "time", "start_year", "end_year", "display_days", "display_months", "display_years", "field_array", "field_order")
        ),
        "html_select_time" to Tag(
            "time selection boxes",
            attributes = listOf("prefix", "time", "display_hours", "display_minutes", "display_seconds", "display_meridian", "use_24_hours", "field_array")
        ),
        "html_table" to Tag(
            "render an array as a table",
            attributes = listOf("loop", "cols", "rows", "inner", "caption", "table_attr", "th_attr", "tr_attr", "td_attr", "hdir", "vdir")
        )
    )

    /** The tags that have to be closed with `{/tag}`. */
    val BLOCK_TAGS: Set<String> = TAGS.filterValues { it.block }.keys
}
