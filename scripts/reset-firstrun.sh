#!/usr/bin/env bash
#
# Delete pure-black-github-firstrun.xml so the one-time notification
# fires again on the next IDEA start. Useful while iterating on the
# first-run flow without reinstalling.

set -euo pipefail
FIRSTRUN_STATE="$HOME/Library/Application Support/JetBrains/IntelliJIdea2026.1/options/pure-black-github-firstrun.xml"
if [[ -f "$FIRSTRUN_STATE" ]]; then
  rm -f "$FIRSTRUN_STATE"
  echo "✓ reset: $FIRSTRUN_STATE"
else
  echo "- already clean: $FIRSTRUN_STATE"
fi
