# GitHub Dark — Pure Black

A no-compromise pure-black (`#000000`) variant of GitHub Dark for IntelliJ Platform IDEs. Built to feel like a TUI — flat, quiet, zero chrome tint.

## What's pure black

- Editor pane, gutter, console, terminal
- All tool windows (Project view, Terminal, Services, Build, Run, Debug, Commit)
- Tab bar, main toolbar, status bar, popups, dialogs, menus
- Text fields, combo boxes, dropdowns

## What's not

- Subtle `#30363d` 1-pixel borders between panels so you can tell them apart without chrome tint. Uses GitHub Primer's `border.default`.

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

Requires JDK 21 and the Gradle wrapper (included).

```bash
./gradlew runIde       # launches a sandboxed IDE with the plugin installed
./gradlew buildPlugin  # produces a distributable .zip
./gradlew verifyPlugin # runs JetBrains' plugin verifier
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
