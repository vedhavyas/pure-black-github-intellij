#!/usr/bin/env bash
#
# Build the plugin and deploy it into the live IntelliJ IDEA 2026.1 plugins
# directory on this machine. For the tight local feedback loop:
#
#   edit source → ./scripts/dev-install.sh → restart IDEA → observe.
#
# Two modes:
#
#   default         Requires IDEA quit. Unpacks the new zip into plugins/
#                   directly. Fastest full-restart path.
#
#   --hot           IDEA must be running. Builds only, then prints the zip
#                   path. You go Settings → Plugins → ⚙ → Install Plugin
#                   from Disk → pick the zip. IDEA dynamically reloads the
#                   plugin without restart.
#
# Flags:
#   --hot              Don't touch the plugins dir; let IDEA do the install
#                      via Settings (dynamic reload, no restart).
#   --reset-firstrun   Wipe pure-black-github-firstrun.xml so the first-run
#                      notification fires again on next start. Only meaningful
#                      with the default mode (requires IDEA quit).
#   --open             After default-mode install, launch IDEA automatically.
#

set -euo pipefail

PROJECT_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
IDEA_CONFIG="$HOME/Library/Application Support/JetBrains/IntelliJIdea2026.1"
IDEA_PLUGINS="$IDEA_CONFIG/plugins"
PLUGIN_DIR="$IDEA_PLUGINS/pure-black-github"
FIRSTRUN_STATE="$IDEA_CONFIG/options/pure-black-github-firstrun.xml"

mode="default"
reset_firstrun=0
open_idea=0
for arg in "$@"; do
  case "$arg" in
    --hot)            mode="hot" ;;
    --reset-firstrun) reset_firstrun=1 ;;
    --open)           open_idea=1 ;;
    -h|--help)
      sed -n '/^#$/,/^set -euo/p' "$0" | sed 's/^# \{0,1\}//' | head -n -2
      exit 0
      ;;
    *) echo "unknown flag: $arg" >&2; exit 2 ;;
  esac
done

# Java 21 picks up the toolchain-spec build.
export JAVA_HOME=/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home

cd "$PROJECT_ROOT"
echo "→ building plugin"
./gradlew -q buildPlugin

zip_file=$(ls -t build/distributions/pure-black-github-*.zip 2>/dev/null | head -1 || true)
if [[ -z "$zip_file" ]]; then
  echo "build produced no zip in build/distributions/" >&2
  exit 1
fi
zip_abs="$PROJECT_ROOT/$zip_file"
echo "→ built $(basename "$zip_file")"

if [[ "$mode" == "hot" ]]; then
  if ! pgrep -f "IntelliJ IDEA.app/Contents/MacOS/idea" >/dev/null 2>&1; then
    echo "IDEA is not running. Hot mode needs IDEA running." >&2
    echo "  run without --hot to install-and-exit, or launch IDEA first." >&2
    exit 1
  fi
  echo
  echo "HOT INSTALL — do this in your running IDEA:"
  echo "  Settings (⌘,) → Plugins → ⚙ (gear) → Install Plugin from Disk"
  echo "  Pick: $zip_abs"
  echo "  Confirm 'Upgrade' when prompted."
  echo
  echo "IDEA will reload the plugin without restart. Theme/scheme re-apply automatically."
  exit 0
fi

# Default mode — requires IDEA closed.
if pgrep -f "IntelliJ IDEA.app/Contents/MacOS/idea" >/dev/null 2>&1; then
  echo "IDEA is running. Either:" >&2
  echo "  1. Quit IDEA (⌘Q) and rerun this script, or" >&2
  echo "  2. Rerun with --hot (keeps IDEA open, installs via Settings)" >&2
  exit 1
fi

# Remove any existing install (hand-crafted folder or earlier zip install).
if [[ -d "$PLUGIN_DIR" ]]; then
  echo "→ removing old install $PLUGIN_DIR"
  rm -rf "$PLUGIN_DIR"
fi

echo "→ installing into $IDEA_PLUGINS"
unzip -q "$zip_abs" -d "$IDEA_PLUGINS"

if (( reset_firstrun )); then
  if [[ -f "$FIRSTRUN_STATE" ]]; then
    rm -f "$FIRSTRUN_STATE"
    echo "→ reset first-run flag ($FIRSTRUN_STATE)"
  else
    echo "→ first-run flag already absent"
  fi
fi

echo "✓ installed $(basename "$zip_file")"

if (( open_idea )); then
  echo "→ launching IDEA"
  open -a "IntelliJ IDEA"
else
  echo "  open IDEA to see the changes."
fi
