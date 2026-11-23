# Development guide

How to build, test, extend and release `tailwind-merge-java`. For the conversion plan and its progress see [CHECKLIST.md](CHECKLIST.md).

## What this project is

A Java port of [tailwind-merge-dotnet](https://github.com/desmondinho/tailwind-merge-dotnet) (itself a C# port of [tailwind-merge](https://github.com/dcastil/tailwind-merge)). It is published through [JitPack](https://jitpack.io) and consumed as `com.github.DdxAiLab:tailwind-merge-java:<tag>`.

The C# source and tests used as the porting reference live in `docs/references/tailwind-merge-dotnet/`. That folder is read-only and is not part of the published repository (it is git-ignored).

## Prerequisites

- Maven 3.6+
- JDK 8 or newer. The build is verified on JDK 8, 11 and 17.
- `JAVA_HOME` may point to JDK 8; there is nothing to configure per JDK.

## Commands

```bash
mvn -B -ntp clean verify                 # compile + run all tests + build the jars
mvn -B -ntp test -Dtest=ClassNameNodeTest   # a single test class
mvn -B -ntp clean install                # install into ~/.m2 to try it from another local project
```

To try the library from another local project, run `mvn clean install` here and depend on `com.github.DdxAiLab:tailwind-merge-java:<version in pom.xml>`.

Before declaring a step done, run `mvn clean verify` at least on the lowest (8) and a recent (17) JDK:

```bash
JAVA_HOME="/path/to/jdk-17" mvn -B -ntp clean verify
```

## Project layout

```
pom.xml                 Maven build (Java 8 baseline)
jitpack.yml             JitPack build config
src/main/java/com/github/ddxailab/tailwindmerge/          Library code
src/main/java/com/github/ddxailab/tailwindmerge/model/    Public model types
src/main/java/com/github/ddxailab/tailwindmerge/common/   Public helpers (validators, theme)
src/test/java/...       JUnit 5 tests, same package structure as the code
docs/CHECKLIST.md       Conversion checklist and decisions
docs/DEVELOPMENT.md     This guide
docs/references/        Reference projects (C# source and tests)
```

Package mapping from the reference:

| C# | Java |
|---|---|
| Public types in `TailwindMerge` | `com.github.ddxailab.tailwindmerge` |
| Public types in `TailwindMerge.Models` | `...tailwindmerge.model` |
| Public types in `TailwindMerge.Common` | `...tailwindmerge.common` |
| `internal` types (any namespace) | package-private in the root package `...tailwindmerge` |

Java package-private visibility cannot span sub-packages, which is why the C# `internal` types (`ClassNameNode`, `TwMergeContext`, ...) all live in the root package. Tests that need them sit in the same package.

## Porting workflow

Work one checklist step at a time.

1. Read the C# files of the step and their tests in `docs/references/tailwind-merge-dotnet/`.
2. Port the code, then port its tests **with the same cases and the same expected strings**. One C# `FooTests.cs` becomes one `FooTest.java`.
3. Run `mvn clean verify`. A failing ported test is a porting bug until proven otherwise; do not change expected values to make a test pass.
4. Record any deviation from the reference in the *Decisions* table of `CHECKLIST.md`.
5. Tick the finished items in `CHECKLIST.md` and commit.

### C# → Java translation notes

| C# | Java |
|---|---|
| `string?` returned from the API | `String`, returning `""` instead of `null` for "nothing" |
| `Func<string, bool>` | `java.util.function.Predicate<String>` |
| `Dictionary<K, V>` / `HashSet<T>` / `T[]` | `Map` / `Set` / `List` |
| `record` / `record struct` | immutable final class (Java 8 has no records) |
| `str.Split('-', RemoveEmptyEntries)` | manual split that skips empty parts (`String.split` keeps leading empties) |
| `str[a..b]`, `str[a..]` | `substring(a, b)`, `substring(a)` |
| `Regex` (`[GeneratedRegex]`) | `static final java.util.regex.Pattern` (precompiled); check .NET vs Java differences (named groups syntax, `\d`/`\w` Unicode behavior, `RegexOptions`) |
| `InternalsVisibleTo` | tests in the same package as the package-private classes |
| `IOptions<TwMergeConfig>` | plain constructor/builder argument (no DI dependency) |

## Conventions

- **Java 8 baseline.** No `var`, `List.of`, `Map.of`, records, `String.isBlank()`, text blocks. On JDK 9+ the `release-8-api` profile in `pom.xml` passes `--release 8`, so a compile error about a missing API means a Java 9+ API slipped in.
- **No runtime dependencies** unless there is a strong reason (the LRU cache is implemented in-house). Test dependencies: JUnit 5 only.
- **Port faithfully.** Keep the behavior identical to the reference and keep class/method names, adapted to Java casing (`PascalCase` types, `camelCase` members, `UPPER_SNAKE_CASE` constants).
- **Thread safety.** `TwMerge` must be safe to share between threads: immutable config and context, thread-safe cache.
- **No `null` from public methods.** Return empty strings/collections instead.
- **Javadoc.** Every public type and method has Javadoc (translate the XML doc comments of the reference).
- **Language.** Source, comments, Javadoc and docs are in English.
- **Line endings.** Git may warn about LF/CRLF on Windows; it is harmless.

## Releasing (JitPack)

1. `mvn clean verify` passes on JDK 8 and 17 and the checklist is up to date. (The JitPack build runs with `-DskipTests`, so tests must be green locally first.)
2. Tag the commit and push it: `git tag vX.Y.Z && git push origin vX.Y.Z`.
3. Trigger the build at `https://jitpack.io/#DdxAiLab/tailwind-merge-java` (or request any artifact URL of the tag) and check that the log is green.
4. Consumers use the tag as the version; JitPack overrides the version declared in `pom.xml`.
5. Verify from a clean consumer project, adding `https://jitpack.io` as a repository.

A tag that was already built by JitPack must not be moved; publish a new tag instead.

## Do not

- Do not commit `target/`, IDE files or jars (see `.gitignore`).
- Do not edit anything under `docs/references/`.
- Do not modify `LICENSE` except to keep both copyright notices (original author and Java adaptation).
- Do not add runtime dependencies or raise the Java baseline without recording the decision in `CHECKLIST.md`.
