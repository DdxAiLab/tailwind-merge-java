# config-check

Checks that the default configuration of this library (`TwMergeConfig`) is **identical, definition by
definition, to the default configuration of the C# reference** (`TwMergeConfig.cs` in
`docs/references/tailwind-merge-dotnet/`).

Counting the class groups is not enough: a wrong validator, a missing `""` entry, a swapped order or a
missing base class name is a silent bug that no compile error reveals. This tool compares everything.

It is a development tool only. It is not part of the published library and Maven does not build it.

## Usage

```bash
bash tools/config-check/verify-default-config.sh [path/to/TwMergeConfig.cs]
```

Without an argument it reads `docs/references/tailwind-merge-dotnet/src/TwMergeConfig.cs`.

| Exit code | Meaning |
|---|---|
| `0` | Both configurations are identical (`IDENTICAL: 451 lines (378 class groups, ...)`) |
| `1` | They differ; the diff is printed (`<` = C# reference, `>` = this library) |
| `2` | Missing C# source or no Python 3 available |

Run it whenever the default configuration changes (class groups, scales, theme, conflicts), and when the
reference is updated to a newer Tailwind CSS version: the diff then lists exactly what has to be ported.

### Prerequisites

- JDK 8+ (`JAVA_HOME` is honoured) and Maven, as for the rest of the project.
- Python 3 (standard library only). On Windows `python3` is often a Microsoft Store stub that does not
  run; the script tries `python3`, `python` and `py` and uses the first one that works.
- Bash (Git Bash on Windows). Run it with `bash ...`; the `.gitattributes` of the repository keeps `*.sh`
  files with LF endings so they work even with `core.autocrlf=true`.
- The C# reference cloned in `docs/references/tailwind-merge-dotnet/` (it is git-ignored).

## How it works

```
TwMergeConfig.cs ──► dump_cs_config.py ──► target/config-check/cs.txt   ┐
                                                                         ├─► diff
TwMergeConfig (Java) ──► DumpJavaConfig ──► target/config-check/java.txt ┘
```

1. `dump_cs_config.py` tokenizes the constructor of the C# `TwMergeConfig`, drops the comments, evaluates the
   scale variables (`unambiguousSpacingScale`, `insetScale`, ...) and the collection-expression spreads
   (`[.. scale]`), and prints the result in the normalized format below.
2. `DumpJavaConfig.java` builds `new TwMergeConfig()` and prints it in the same format. Validators and theme
   getters are lambdas, so it identifies them **by reference**, through the `Validators.IS_*` constants and
   the `DefaultConfigScales.*_THEME` getters (reflection). Anything it cannot identify is printed as
   `?UNKNOWN?` and makes the script fail.
3. `verify-default-config.sh` compiles the library, runs both dumpers and diffs the two text files. The
   dumps are left in `target/config-check/` (git-ignored) for inspection.

### Normalized format

One line per entry; the order is the declaration order, so a reordering is also detected:

```
cacheSize=500
prefix=None
THEME ANIMATE = ["spin", "ping", "pulse", "bounce"]
GROUP aspect = G<"aspect">["auto", "square", T:ASPECT_THEME, V:IS_FRACTION, V:IS_ARBITRARY_VALUE, V:IS_ARBITRARY_VARIABLE]
GROUP bg-image = G<"bg">["none", G<"linear">[G<"to">["t", "tr", ...], V:IS_INTEGER, ...], ...]
CONFLICT inset = [inset-x, inset-y, inset-bs, ...]
CONFLICTMOD font-size = [leading]
POSTFIX = [container-type]
ORDER = [*, **, after, ...]
```

| Token | Meaning | C# | Java |
|---|---|---|---|
| `"text"` | class name part (`""` is the base class name itself) | `"text"` | `"text"` |
| `V:IS_X` | validator | `V.IsX` | `Validators.IS_X` |
| `T:X_THEME` | theme getter | `xTheme` (variable) | `DefaultConfigScales.X_THEME` |
| `G<"base">[...]` | nested `ClassGroup` with a base class name | `new ClassGroup( "base", [...] )` | `ClassGroup.withBase("base", ...)` |
| `G<->[...]` | `ClassGroup` without base class name | `new ClassGroup( [...] )` | `ClassGroup.of(...)` |

The name mapping is mechanical (`IsArbitraryValue` → `IS_ARBITRARY_VALUE`, `fontWeightTheme` →
`FONT_WEIGHT_THEME`). It relies on the Java constants keeping the names the port gave them (see the
*Validator naming* decision in `docs/CHECKLIST.md`). The one deliberate rename, the reference's
`traslateScale` typo (`TRANSLATE_SCALE`), does not appear in the dumps because scales are expanded.

## Reading a diff

```
58c58
< GROUP z = G<"z">["auto", V:IS_NUMBER, V:IS_ARBITRARY_VALUE, V:IS_ARBITRARY_VARIABLE]
---
> GROUP z = G<"z">["auto", V:IS_INTEGER, V:IS_ARBITRARY_VALUE, V:IS_ARBITRARY_VARIABLE]
```

`<` is what the C# reference declares, `>` what this library declares. The fix goes in the Java side (the
section class of the group, e.g. `LayoutClassGroups`); never edit the reference.

## Limits

- It proves the Java config **equals the C# config**, not that the C# config is right about Tailwind CSS.
  Behavior is covered by the ported tests (steps 7–9 of `docs/CHECKLIST.md`).
- The Python parser understands the constructs `TwMergeConfig.cs` uses today (string/validator/theme-getter
  definitions, `new ClassGroup(...)`, scale variables, spreads, dictionary initializers). If the reference
  starts using a new construct, the script stops with an explicit error instead of guessing.
- `Extend` / `Override` are not covered (they are runtime operations, Step 8): only the default state of a
  freshly created `TwMergeConfig` is compared.

## Files

| File | Purpose |
|---|---|
| `verify-default-config.sh` | Entry point: compiles, dumps both sides and diffs |
| `dump_cs_config.py` | Parses the C# constructor and prints the normalized dump |
| `DumpJavaConfig.java` | Prints `new TwMergeConfig()` in the same format |
