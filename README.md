# Smarty Templating Engine

[![Build](https://github.com/simialbi/idea-smarty-plugin/actions/workflows/build.yml/badge.svg)](https://github.com/simialbi/idea-smarty-plugin/actions/workflows/build.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE.md)

Support for the [Smarty][smarty] template language in IntelliJ-based IDEs.

`.tpl` files are treated as what they are: a markup document with a second language woven through it. The IDE's own
HTML, CSS and JavaScript support keeps working, and everything inside
`{...}` is parsed, highlighted, completed and navigable as Smarty.

<!-- TODO: add a screenshot of a highlighted template here -->

## Features

### Editing

- **Two languages in one file.** A `.tpl` file gets an HTML tree and a Smarty tree. Markup is highlighted and completed
  by the bundled HTML support; Smarty tags are highlighted on top.
- **Syntax highlighting** driven by the lexer: delimiters, built-in tag names, modifiers, operators, numbers, strings,
  parentheses, brackets and comments.
- **Semantic highlighting** driven by the PSI: template variables, the `$smarty` super global, property and `->` access
  chains, method calls and static class members (`{$this->head()}`, `{DynamicModal::SIZE}`), modifier names, function
  calls, `{block}` and `{function}` declarations, and config variables written as `{#name#}`.
- **Colour settings page** at *Settings | Editor | Color Scheme | Smarty*, with a live preview and 15 separately
  configurable attributes.
- **Folding** for `{* multi-line comments *}`, `{literal}` and `{nocache}` blocks, and the body of every paired tag such
  as `{block}…{/block}` or `{foreach}…{/foreach}`.
- **Formatting** with indentation and spacing rules for Smarty tags that leave the surrounding markup indentation
  intact.
- **Commenting** with <kbd>Ctrl</kbd>+<kbd>/</kbd>, which wraps the selection in `{* … *}`.

### Code completion

Completion is context sensitive — what is offered depends on where the caret sits:

| At the caret | Suggestions                                           |
|--------------|-------------------------------------------------------|
| `{fo⎸`       | tag names: `foreach`, `for`, `function`, …            |
| `{/⎸`        | the block tags that are still open, innermost first   |
| `{include ⎸` | that tag's attributes: `file`, `assign`, `scope`, …   |
| `{$user\|⎸`  | modifiers: `upper`, `truncate`, `date_format`, …      |
| `{$⎸`        | the variables used or assigned in this template       |
| `{$smarty.⎸` | reserved sub-keys: `get`, `post`, `foreach`, `now`, … |

### Navigation and refactoring

- **Gutter icons** on `{include}`, `{extends}` and `{insert}` open the referenced template, and on `{block}` jump to the
  declarations of the same name in the templates the current one inherits from.
- **References** — <kbd>Ctrl</kbd>+<kbd>Click</kbd> a template path or a block name to jump to it.
- **Find Usages** for block and function declarations.
- **Rename** a `{block}` declaration in place — both the `{block name="x"}` and the bare
  `{block x}` form are rewritten, quotes included.
- **Go to Symbol** (<kbd>Ctrl</kbd>+<kbd>Alt</kbd>+<kbd>Shift</kbd>+<kbd>N</kbd>) lists the
  `{block}` and `{function}` declarations of the project.

### Problems reported in the editor

| Severity     | Reported for                                                                              |
|--------------|-------------------------------------------------------------------------------------------|
| Error        | an unexpected character inside a tag                                                      |
| Warning      | a template path that cannot be resolved; a modifier given more parameters than it accepts |
| Weak warning | an unknown modifier — one that must come from a Smarty plugin or a PHP function           |

## Requirements

| Product                | min Version                                                                                                       |
|------------------------|-------------------------------------------------------------------------------------------------------------------|
| IDE                    | IntelliJ IDEA — the plugin depends on the `com.intellij.java` module, so it does not load in PhpStorm or WebStorm |
| Declared compatibility | `since-build 262.9437.185` (see `src/main/resources/META-INF/plugin.xml`)                                         |
| Compile target         | IntelliJ Platform 2025.3.5 (see `build.gradle.kts`)                                                               |
| JDK for building       | 21, the version CI builds with                                                                                    |

> The declared `since-build` and the compile target are currently out of step. Keep the
> `intellijIdea(...)` version in `build.gradle.kts` and `<idea-version since-build="...">` aligned
> with the oldest IDE you actually want to support.

## Installation

The plugin is not on the JetBrains Marketplace yet. Build it and install the ZIP by hand:

```bash
./gradlew buildPlugin
```

The distribution lands in `build/distributions/`. In the IDE, go to *Settings | Plugins | ⚙ | Install Plugin from Disk…*
and pick that ZIP.

## Building from source

```bash
./gradlew build          # compile, run the tests, verify the plugin
./gradlew test           # tests only
./gradlew runIde         # start a sandbox IDE with the plugin loaded
./gradlew verifyPlugin   # IntelliJ Plugin Verifier
```

Three run configurations are checked in under `.run/` and show up in the IDE's run configuration list: **Run IDE with
Plugin**, **Run Tests** and **Run Verifications**.

### Regenerating the lexer and the parser

The lexer, parser and PSI classes under `src/main/gen` are generated from two sources and are **checked into the
repository**, so a regeneration always produces a reviewable diff. Regenerate whenever you touch `Smarty.flex` or
`Smarty.bnf`.

Inside the IDE (needs the *Grammar-Kit* plugin):

- right-click `src/main/kotlin/ch/erlebnisplus/smarty/Smarty.flex` → **Run JFlex Generator**
- right-click `src/main/kotlin/ch/erlebnisplus/smarty/Smarty.bnf` → **Generate Parser Code**

The lexer can also be regenerated from the command line with the JFlex jar and the platform's lexer skeleton, both of
which sit at the repository root:

```bash
java -jar jflex-1.10.17.jar \
  --skel idea-flex.skeleton \
  -d src/main/gen/ch/erlebnisplus/smarty \
  src/main/kotlin/ch/erlebnisplus/smarty/Smarty.flex
```

## Project layout

```
├── src/main/kotlin/ch/erlebnisplus/smarty/
│   ├── Smarty.flex                  lexer definition
│   ├── Smarty.bnf                   grammar; generates the parser and the PSI interfaces
│   ├── Smarty*.kt                   the plugin: highlighter, annotator, completion,
│   │                                references, folding, formatter, commenter, …
│   └── psi/                         token sets, built-in tag and modifier tables
│       └── impl/                    hand-written PSI helpers and mixins
├── src/main/gen/ch/erlebnisplus/smarty/
│   ├── SmartyLexer.java             generated by JFlex — do not edit
│   ├── parser/                      generated by Grammar-Kit — do not edit
│   └── psi/                         generated by Grammar-Kit — do not edit
├── src/main/resources/META-INF/plugin.xml
├── src/test/kotlin/ch/erlebnisplus/smarty/
├── jflex-1.10.17.jar, idea-flex.skeleton
└── .run/                            checked-in run configurations
```

## Tests

The suite runs on the platform test framework (`BasePlatformTestCase`) and covers parsing, references, Find Usages,
go-to-symbol, folding, formatting, operators, the commenter, the template-language layering and the colour settings
page.

```bash
./gradlew test
```

The colour settings page is self-checking: its tests assert that the preview sample parses without errors, that every
declared attribute is actually reachable in that sample, and that the sample uses no colour key that is not offered in
the settings dialog.

## Known limitations

The grammar does not yet cover everything Smarty allows. The following constructs parse as errors today:

- `auto_literal`, where a `{` followed by whitespace is output verbatim. Every `{` starts a tag as far as the lexer is
  concerned, so a lone brace in the markup — `body { color: red }` in an inline stylesheet, for instance — is read as
  Smarty.

A tag the grammar cannot parse costs that one tag and nothing more: the parser recovers at the next `{` or run of
markup, so the rest of the file is still parsed, highlighted and completed.

Other gaps:

- No element manipulator is registered, so renaming does not rewrite the string literals that point at a declaration —
  neither `{include file="…"}` when a template file is renamed, nor a
  `{block}` name in the templates that override it.
- `{call name="x"}` does not resolve to the matching `{function name="x"}`.
- There is no code style settings page, so the spacing options the formatter reads cannot be changed in the UI yet.

Contributions for any of these are welcome.

## Contributing

1. Fork and branch.
2. Change `Smarty.flex` or `Smarty.bnf` rather than the generated sources, then regenerate as described above and commit
   the generated diff along with your change.
3. Add a test — `src/test/kotlin/ch/erlebnisplus/smarty/` has an example for every extension point the plugin registers.
4. Keep `./gradlew build` green and add an entry to `CHANGELOG.md` under *Unreleased*.

## License

[MIT](LICENSE.md) © Erlebnisplus

[smarty]: https://smarty-php.github.io/smarty/stable/
