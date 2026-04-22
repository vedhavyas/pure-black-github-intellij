# GitHub Dark — Pure Black

A no-compromise pure-black (`#000000`) variant of GitHub Dark for IntelliJ Platform IDEs. Built to feel like a TUI — flat, quiet, zero chrome tint.

## What's pure black

- Editor pane, gutter, console, terminal
- All tool windows (Project view, Terminal, Services, Build, Run, Debug, Commit)
- Tab bar, main toolbar, status bar, popups, dialogs, menus
- Text fields, combo boxes, dropdowns

## What's not

- Subtle `#30363d` 1-pixel borders between panels so you can tell them apart without chrome tint. Uses GitHub Primer's `border.default`.

## Also bundled (opt-in)

- **Keymap "Vedhavyas Mac"** — Mac OS X keymap plus `Cmd+Enter` (Claude Code GUI chat newline) and `Alt+N` (New Element menu). Enable: Settings → Keymap → "Vedhavyas Mac".
- **Code style "Vedhavyas Default"** — indent preferences for Python (dict alignment, smart tabs), Rust (smart tabs), Terraform (tabs), Protobuf/prototext (tabs). Enable: Settings → Editor → Code Style → "Vedhavyas Default".

Both are optional — theme works standalone if you want to keep your existing keymap and code style.

## Recommended companion plugins

Install these from Marketplace if not already there:

- [Rust](https://plugins.jetbrains.com/plugin/22407-rust) — for `*.rs`
- [Go](https://plugins.jetbrains.com/plugin/9568-go) — for `*.go`
- [Python](https://plugins.jetbrains.com/plugin/631-python) — for `*.py`
- [Atom Material Icons](https://plugins.jetbrains.com/plugin/10044-atom-material-icons) — colorful file icons
- [GitHub Copilot](https://plugins.jetbrains.com/plugin/17718-github-copilot) — if you use it
- [Ignore](https://plugins.jetbrains.com/plugin/7495--ignore) — `.gitignore` syntax highlighting
- [Just](https://plugins.jetbrains.com/plugin/18658-just) — `Justfile` support
- [Makefile Language](https://plugins.jetbrains.com/plugin/9333-makefile-language)
- [.env files support](https://plugins.jetbrains.com/plugin/9525--env-files-support)

## Font setup (macOS)

```bash
brew install --cask font-fira-code
```

Bundled editor scheme references Fira Code. If not installed, IDEA falls back to another monospace (you'll see the font but without ligatures).

## Install

### From JetBrains Marketplace (recommended once published)

`Settings → Plugins → Marketplace → search "GitHub Dark Pure Black" → Install → Restart`

Then:

- `Settings → Appearance & Behavior → Appearance → Theme → GitHub Dark Pure Black`
- `Settings → Editor → Color Scheme → GitHub Dark Pure Black`

### From source

```bash
git clone https://github.com/vedhavyas/pure-black-github-intellij
cd pure-black-github-intellij
./gradlew buildPlugin
# Install build/distributions/pure-black-github-*.zip via Settings → Plugins → ⚙ → Install Plugin from Disk
```

## Development

Requires JDK 21 (`brew install openjdk@21`) and the Gradle wrapper (included).

### Feedback-loop scripts

The `scripts/` directory has three helpers for fast local iteration:

```bash
# Build and install into your real IDEA 2026.1 (requires IDEA quit first).
# Pass --reset-firstrun to also clear the one-time notification flag,
# or --open to relaunch IDEA afterward.
./scripts/dev-install.sh
./scripts/dev-install.sh --reset-firstrun --open

# Launch a sandboxed IDEA with the plugin pre-installed. Doesn't touch
# your real IDEA config. Good for pure visual iteration on theme.json.
./scripts/dev-sandbox.sh

# Delete the first-run state file so the notification fires again
# without reinstalling.
./scripts/reset-firstrun.sh
```

### Gradle directly

```bash
./gradlew buildPlugin  # produces build/distributions/pure-black-github-*.zip
./gradlew runIde       # same as dev-sandbox.sh
./gradlew verifyPlugin # runs JetBrains' plugin verifier against the IDEA range
```

## Compatibility

Targets IntelliJ Platform 2024.3+ (build `243`). Depends on the Islands UI which shipped stable in that release.

Tested on IntelliJ IDEA Ultimate 2026.1. Should work on the full IntelliJ Platform family (WebStorm, PyCharm, GoLand, RustRover, RubyMine, PhpStorm, CLion, DataGrip) but not independently verified.

## Publishing

Releases are signed and published via GitHub Actions. Bump the version in `build.gradle.kts`, update `CHANGELOG.md`, push a `vX.Y.Z` tag — CI builds, signs, and submits to JetBrains Marketplace automatically.

Signing requires two secrets stored in GitHub repo settings:

- `CERTIFICATE_CHAIN` — your JetBrains signing certificate chain (PEM)
- `PRIVATE_KEY` — the matching private key (PEM, password-protected)
- `PRIVATE_KEY_PASSWORD` — the password
- `PUBLISH_TOKEN` — your JetBrains Marketplace Hub permanent token

See [Signing Plugins](https://plugins.jetbrains.com/docs/intellij/plugin-signing.html) on JetBrains' docs for how to generate these.

## License

[MIT](LICENSE). Do whatever you want; no warranty.

## Credits

- Heavily inspired by [Mallowigi's Material Theme UI](https://github.com/mallowigi/material-theme-jetbrains) and its GitHub Dark variant.
- Color palette based on [GitHub Primer Primitives](https://primer.style/primitives) (MIT).
- Built on top of JetBrains' Islands Dark UI theme.
