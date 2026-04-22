#!/usr/bin/env bash
#
# Launch a sandboxed IntelliJ IDEA with the plugin pre-installed. Uses the
# IntelliJ Platform Gradle Plugin's runIde task — a clean IDEA instance
# with its own config dir, no interference with your real IDEA. Ideal for
# fast iteration on theme.json / editor scheme where you just want to see
# the visual result.
#
# Close the sandbox IDEA to end; next run fetches a fresh sandbox.

set -euo pipefail
PROJECT_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
export JAVA_HOME=/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home
cd "$PROJECT_ROOT"
exec ./gradlew runIde
