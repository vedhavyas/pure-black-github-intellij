#!/usr/bin/env bash
#
# Build the plugin and deploy it into the live IntelliJ IDEA 2026.1 plugins
# directory on this machine. For the tight local feedback loop:
#
#   edit source → ./scripts/dev-install.sh → restart IDEA → observe.
#
# Flags:
#   --reset-firstrun   Also wipe pure-black-github-firstrun.xml so the
#                      first-run notification fires again on next start.
#   --open             After installing, launch IDEA automatically.
#
# Requires IDEA to be fully quit (⌘Q). The script refuses to run otherwise —
# replacing a jar IDEA is holding open leads to half-applied state.

set -euo pipefail

PROJECT_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
IDEA_CONFIG="$HOME/Library/Application Support/JetBrains/IntelliJIdea2026.1"
IDEA_PLUGINS="$IDEA_CONFIG/plugins"
PLUGIN_DIR="$IDEA_PLUGINS/pure-black-github"
FIRSTRUN_STATE="$IDEA_CONFIG/options/pure-black-github-firstrun.xml"

reset_firstrun=0
open_idea=0
for arg in "$@"; do
  case "$arg" in
    --reset-firstrun) reset_firstrun=1 ;;
    --open)           open_idea=1 ;;
    -h|--help)
      sed -n '/^#$/,/^set -euo/p' "$0" | sed 's/^# \{0,1\}//' | head -n -2
      exit 0
      ;;
    *) echo "unknown flag: $arg" >&2; exit 2 ;;
  esac
done

# IDEA running? Refuse.
if pgrep -f "IntelliJ IDEA.app/Contents/MacOS/idea" >/dev/null 2>&1; then
  echo "IDEA is running. Quit it (⌘Q) before running this script." >&2
  exit 1
fi

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
echo "→ built $(basename "$zip_file")"

# Remove any existing install (hand-crafted folder or earlier zip install).
if [[ -d "$PLUGIN_DIR" ]]; then
  echo "→ removing old install $PLUGIN_DIR"
  rm -rf "$PLUGIN_DIR"
fi

echo "→ installing into $IDEA_PLUGINS"
unzip -q "$zip_file" -d "$IDEA_PLUGINS"

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
