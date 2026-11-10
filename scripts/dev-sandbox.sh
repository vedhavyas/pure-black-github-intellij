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
# A hardcoded JDK path breaks when the JDK moves or on a new machine. JAVA_HOME wins
# when it is set; each probe clears it because the macOS java stub hangs when it
# points at a non-JDK. A non-empty java.home is not enough - a JRE or an older JDK
# reports one too - so the probe also checks the spec version and for javac.
required_major=21
orig_home="${JAVA_HOME:-}"
probe=""
JAVA_HOME=""
for candidate in "${orig_home:+$orig_home/bin/java}" "$(command -v java || true)"; do
  [[ -n "$candidate" && -x "$candidate" ]] || continue
  probe="$(JAVA_HOME= "$candidate" -XshowSettings:properties -version 2>&1)" || true
  java_home="$(sed -n 's/^ *java\.home = //p' <<<"$probe")"
  spec="$(sed -n 's/^ *java\.specification\.version = //p' <<<"$probe")"
  major="${spec#1.}"
  major="${major%%.*}"
  if [[ -n "$java_home" && -x "$java_home/bin/javac" && "$major" == "$required_major" ]]; then
    JAVA_HOME="$java_home"
    break
  fi
done
if [[ -z "$JAVA_HOME" ]]; then
  echo "no usable JDK ${required_major} found - this build needs a JDK ${required_major}" >&2
  echo "  see the JDK note under Development in README.md." >&2
  echo "$probe" >&2
  exit 1
fi
export JAVA_HOME
cd "$PROJECT_ROOT"
exec ./gradlew runIde
