# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.1.0] — 2026-04-22

### Added
- Bundled keymap **Vedhavyas Mac** — Mac OS X parent with two overrides:
  `Cmd+Enter` for Claude Code GUI chat newline, `Alt+N` for New Element menu.
  Opt-in: Settings → Keymap → "Vedhavyas Mac".
- Bundled code style **Vedhavyas Default** — indent preferences for Python
  (dict alignment, smart tabs), Rust (smart tabs), Terraform (tabs),
  Protobuf (tabs). Opt-in: Settings → Editor → Code Style → "Vedhavyas Default".

### Notes
- Keymap and code style ship as selectable options, not force-applied. Future
  v1.2 may add an opt-in first-run flow that selects theme + scheme + keymap +
  code style + editor behavior (block cursor, indent guides off) in one click.

## [1.0.0] — 2026-04-22

### Added
- Initial release.
- Pure black (`#000000`) UI theme extending IntelliJ's built-in Islands Dark.
- Bundled editor color scheme with pure-black editor, gutter, and console backgrounds.
- Subtle `#30363d` (GitHub Primer `border.default`) 1-pixel borders between panels
  so editor / sidebar / tool windows remain distinguishable.
- GitHub Primer 2024+ syntax palette: coral `#ff7b72` keywords, lavender `#d2a8ff`
  types/functions, light blue `#a5d6ff` strings, grey `#8b949e` comments, orange
  `#ffa657` parameters/fields.
- Fira Code 14pt, ligatures enabled by default.
- Rust-specific token overrides (struct, enum, trait, crate, macro).

[Unreleased]: https://github.com/vedhavyas/pure-black-github-intellij/compare/v1.1.0...HEAD
[1.1.0]: https://github.com/vedhavyas/pure-black-github-intellij/releases/tag/v1.1.0
[1.0.0]: https://github.com/vedhavyas/pure-black-github-intellij/releases/tag/v1.0.0
