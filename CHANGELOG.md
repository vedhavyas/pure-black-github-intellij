# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.2.4] — 2026-04-22

### Changed
- **De-branded for public release.** Personal-name strings that were
  user-visible in the IDE got neutralized:
  - Keymap **"Vedhavyas Mac"** → **"Pure Black Mac"** (same two
    bindings: `Cmd+Enter` for Claude Code GUI, `Alt+N` for New Element
    menu). Resource file also renamed.
  - Code style **"Vedhavyas Default"** → **"Pure Black Default"**.
    Content reworked from personal tab preferences to language-standard
    spaces: Python 4-space (PEP 8), Rust 4-space (rustfmt), HCL /
    Protobuf / prototext 2-space. Resource file also renamed.
  - First-run notification copy changed from "Apply Vedhavyas's
    recommended IDE defaults?" to "Apply all Pure Black defaults?"
  - Vendor metadata in `plugin.xml` — contact email swapped to the
    GitHub no-reply address; users route feedback via repo issues.
- **Editor color scheme — Rust token realignment with authentic GitHub
  Dark.** Previous versions drifted from how GitHub web renders Rust:
  - `org.rust.CONSTANT` and `org.rust.STATIC` → now constant blue
    (`#79c0ff`), matching `DEFAULT_CONSTANT`. Previous lavender
    (`#d2a8ff`) was a personal preference.
  - `org.rust.ENUM`, `org.rust.TYPE_ALIAS`, `org.rust.MACRO` → italic
    removed. GitHub Dark doesn't italicize type tokens.
  - `org.rust.STRUCT`, `TRAIT`, `CRATE`, `UNION` unchanged (already
    GitHub-canonical lavender for types).
- Git commit history rewritten to use the GitHub no-reply author
  address; all commits re-signed. Historical convenience — existing
  clones need to re-fetch (`git fetch && git reset --hard origin/main`).

## [1.2.3] — 2026-04-22

### Fixed
- First-run "Apply all" now actually switches the keymap to **Vedhavyas
  Mac**. Root cause: the `<bundledKeymap>` extension expects `file="X.xml"`
  (bare filename; `keymaps/` prefix is implicit) and derives the keymap's
  registered name from the **filename** (minus `.xml`), not the XML
  `<keymap name="...">` attribute. Our declaration of
  `file="/keymaps/VedhavyasMac.xml"` caused the keymap to register under
  the internal+display name `/keymaps/VedhavyasMac`. Fixed by renaming
  the resource file to `Vedhavyas Mac.xml` and declaring it as
  `file="Vedhavyas Mac.xml"` — matches IntelliJ's own Eclipse/NetBeans
  keymap plugins.

### Kept from 1.2.2
- Robust keymap lookup (presentableName → name → getKeymap) and diagnostic
  logging of all registered keymaps on miss. Not load-bearing for the
  fixed name but valuable defense for future platform quirks.

## [1.2.2] — 2026-04-22

### Investigated
- Attempted keymap fix via alternative name lookup pathways (presentable
  name, direct `getKeymap(name)`). Didn't land because the real issue
  was how the keymap registered in the first place — see 1.2.3.

## [1.2.1] — 2026-04-22

### Fixed
- First-run "Apply all" now reliably switches the **editor scheme** to
  **GitHub Dark Pure Black**. The previous build's iteration over
  `allSchemes` could miss entries depending on registration timing;
  switched to direct `EditorColorsManager.getScheme(name)` lookup with
  an iterate-all fallback. Keymap lookup switched to the same pattern
  but the underlying bug was elsewhere — see 1.2.2.

## [1.2.0] — 2026-04-22

### Added
- **First-run notification.** On the first IDE start after installing the
  plugin, a non-intrusive notification offers to apply all defaults in one
  click:
  - Switch UI theme to "GitHub Dark Pure Black"
  - Switch editor color scheme to "GitHub Dark Pure Black"
  - Switch keymap to "Vedhavyas Mac"
  - Flip block cursor on, indent guides off, intention bulb off
- **Persistent state.** Notification shows exactly once; flag survives
  restarts. Delete `config/options/pure-black-github-firstrun.xml` to
  reset.
- **Opt-in design.** Click **Apply all** or **Dismiss** — nothing happens
  silently, never asks twice either way.
- Kotlin sources under `dev.vedhavyas.pureblackgithub`:
  - `FirstRunService` — `PersistentStateComponent` holding the shown flag
  - `FirstRunListener` — `AppLifecycleListener` that fires on first app
    frame and shows the notification
  - `DefaultsApplier` — encapsulates the switchover, each step isolated
    behind its own try/catch so API drift in one area doesn't abort the
    rest

### Notes
- Kotlin 2.1.0 added to the Gradle build.
- Soft wraps and console-only soft-wrap policy are intentionally not
  touched from the apply-all flow — their API differs across IDEA
  releases. Set manually in Settings → Editor → General if desired.

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

[Unreleased]: https://github.com/vedhavyas/pure-black-github-intellij/compare/v1.2.4...HEAD
[1.2.4]: https://github.com/vedhavyas/pure-black-github-intellij/releases/tag/v1.2.4
[1.2.3]: https://github.com/vedhavyas/pure-black-github-intellij/releases/tag/v1.2.3
[1.2.2]: https://github.com/vedhavyas/pure-black-github-intellij/releases/tag/v1.2.2
[1.2.1]: https://github.com/vedhavyas/pure-black-github-intellij/releases/tag/v1.2.1
[1.2.0]: https://github.com/vedhavyas/pure-black-github-intellij/releases/tag/v1.2.0
[1.1.0]: https://github.com/vedhavyas/pure-black-github-intellij/releases/tag/v1.1.0
[1.0.0]: https://github.com/vedhavyas/pure-black-github-intellij/releases/tag/v1.0.0
