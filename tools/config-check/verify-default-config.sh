#!/usr/bin/env bash
# Compares the default TwMergeConfig of this library with the one of the C# reference.
#
# Usage: tools/config-check/verify-default-config.sh [path/to/TwMergeConfig.cs]
#
# Exit code 0 when both configurations are identical, 1 when they differ (the diff is printed).
# See tools/config-check/README.md.
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
cd "$ROOT"

CS_SOURCE="${1:-docs/references/tailwind-merge-dotnet/src/TwMergeConfig.cs}"
WORK="target/config-check"

if [[ ! -f "$CS_SOURCE" ]]; then
    echo "C# reference not found: $CS_SOURCE" >&2
    echo "Pass the path to TwMergeConfig.cs (docs/references/ is git-ignored and must be cloned locally)." >&2
    exit 2
fi

# Classpath separator: ';' for the native Java of Windows (Git Bash / MSYS / Cygwin), ':' elsewhere.
case "$(uname -s)" in
    MINGW* | MSYS* | CYGWIN*) SEP=';' ;;
    *) SEP=':' ;;
esac

JAVA_BIN=""
if [[ -n "${JAVA_HOME:-}" ]]; then
    JAVA_BIN="$JAVA_HOME/bin/"
fi

# Find a working Python 3 (on Windows "python3" is often a Microsoft Store stub that does not run).
PYTHON=""
for candidate in python3 python py; do
    if command -v "$candidate" > /dev/null 2>&1 && "$candidate" -c 'import sys; sys.exit(0 if sys.version_info[0] == 3 else 1)' > /dev/null 2>&1; then
        PYTHON="$candidate"
        break
    fi
done
if [[ -z "$PYTHON" ]]; then
    echo "Python 3 is required to parse the C# reference." >&2
    exit 2
fi

echo "== Compiling the library"
mvn -B -ntp -q compile

echo "== Dumping the Java configuration"
mkdir -p "$WORK/classes"
"${JAVA_BIN}javac" -cp target/classes -d "$WORK/classes" tools/config-check/DumpJavaConfig.java
"${JAVA_BIN}java" -cp "target/classes${SEP}${WORK}/classes" DumpJavaConfig | tr -d '\r' > "$WORK/java.txt"

echo "== Dumping the C# configuration ($CS_SOURCE)"
"$PYTHON" -I tools/config-check/dump_cs_config.py "$CS_SOURCE" | tr -d '\r' > "$WORK/cs.txt"

echo "== Comparing"
if grep -q '?UNKNOWN?' "$WORK/java.txt"; then
    echo "The Java dump contains definitions that are neither a Validators constant nor a theme getter:" >&2
    grep -n '?UNKNOWN?' "$WORK/java.txt" | cut -c1-200 >&2
    exit 1
fi

if diff "$WORK/cs.txt" "$WORK/java.txt" > "$WORK/diff.txt"; then
    echo "IDENTICAL: $(wc -l < "$WORK/cs.txt" | tr -d ' ') lines" \
        "($(grep -c '^GROUP ' "$WORK/cs.txt") class groups," \
        "$(grep -c '^THEME ' "$WORK/cs.txt") theme keys," \
        "$(grep -c '^CONFLICT ' "$WORK/cs.txt") conflicting class groups)"
else
    echo "DIFFERENT: lines starting with '<' are the C# reference, '>' are this library" >&2
    cat "$WORK/diff.txt" >&2
    echo "(dumps kept in $WORK/cs.txt and $WORK/java.txt)" >&2
    exit 1
fi
