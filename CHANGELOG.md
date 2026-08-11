<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Smarty Changelog

## [Unreleased]

### Added

- Method calls and static class members: `{$this->head()}`, `{Foo::bar($x)}` and `{DynamicModal::SIZE}` parse, format
  and are highlighted. The method name is coloured like a call, a `::` member like a constant.
- Per-tag error recovery: a tag the grammar cannot parse costs that one tag, and the rest of the file is still parsed,
  highlighted and completed.
- The Smarty that templates are actually written in: `{section name=… loop=…}`, `{include … assign=…}`, `{strip}`,
  `{capture name=…}`, modifiers on any value, keyword modifier names, `{$row@index}`, `{$rows|@count}`,
  `{$smarty.config.$key}` and plugin calls such as `{html_options values=…}`.

### Fixed

- Only the `$` of a variable was coloured. The name is coloured again, and stays coloured inside a tag the grammar
  cannot parse.
- `Reformat Code` mangled the HTML indentation of a `.tpl` file: a comment between two runs of markup left a hole in
  the block tree, and Smarty tags ignored HTML's *Do not indent children of* setting.
- `Reformat Code` threw `IndexOutOfBoundsException: Index -1` on a file holding a Smarty tag in the header of an HTML
  tag — `<div class="{$x}">` among them.
