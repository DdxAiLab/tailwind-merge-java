# Conversion checklist: tailwind-merge-dotnet → tailwind-merge-java

Reference: `docs/references/tailwind-merge-dotnet/` (≈3,800 lines of C# in `src/`, ≈2,900 in `tests/`; `TwMergeConfig.cs` alone is ≈2,700).

**How to work through it**

- One step = one branch/commit series. Do not start a step before the previous one builds and its tests pass (`mvn clean verify`).
- Port each C# class together with its tests (same cases, same expected strings).
- When a step is done, tick its boxes here and record any deviation under [Decisions](#decisions).

Legend: `[x]` done · `[ ]` pending

---

## Step 0 — Project base ✅

Goal: a minimal, publishable library to prove it can be consumed from another project via JitPack.

- [x] Maven project, Java 8 baseline, JUnit 5 (`pom.xml`)
- [x] Coordinates `com.github.DdxAiLab:tailwind-merge-java`
- [x] Source jar attached to the build
- [x] `jitpack.yml`
- [x] Minimal utility: `TwMerge.merge(String...)` (joins class names, normalizes whitespace) + tests
- [x] `README.md` (based on the reference), `AGENTS.md`, `.gitignore`, `LICENSE` (MIT, same as the reference)
- [x] Builds and passes tests on JDK 8, 11 and 17
- [x] Consumed from a separate local Maven project (`mvn install` + dependency) — works
- [ ] First commit and push to GitHub
- [ ] Tag `v0.1.0` and confirm the build is green on https://jitpack.io/#DdxAiLab/tailwind-merge-java
- [ ] Consume `v0.1.0` from the real target project through JitPack

## Step 1 — Models and constants

Reference: `src/Common/Constants.cs`, `src/Models/*`

- [ ] `Constants` → `common/Constants.java`
- [ ] `ClassValidator` (delegate) → functional interface `ClassValidator` (`Predicate<String>`-like)
- [ ] `ClassGroup` → `model/ClassGroup.java` (a group definition is a mix of strings, validators and nested maps)
- [ ] `ClassModifiersInfo` → `model/ClassModifiersInfo.java` (immutable value class, replaces the C# tuple)
- [ ] `ClassNameNode` → `model/ClassNameNode.java` (trie node used by the class map)
- [ ] `ExtendedConfig` → `model/ExtendedConfig.java` (can wait for Step 7)
- [ ] Unit tests for the data structures

## Step 2 — Validators

Reference: `src/Common/Validators.cs` (246 lines), `tests/ValidatorsTests.cs` (518 lines)

- [ ] Translate every regex (`Regex` → `java.util.regex.Pattern`, precompiled `static final`); check .NET vs Java regex differences (named groups, `\d`, `\w`, RegexOptions)
- [ ] `Validators.java` (is-number, is-fraction, is-arbitrary-value/length/size/…, is-tshirt-size, is-percent, …)
- [ ] `ValidatorsTest.java` — all cases ported

## Step 3 — Theme keys and theme getter

Reference: `src/Common/ThemeKeys.cs`, `src/Common/ThemeGetter.cs`, `tests/ThemeTests.cs`

- [ ] `ThemeKeys.java`
- [ ] `ThemeGetter` / `ThemeUtility.fromTheme(...)`
- [ ] Decide the Java type for theme scales (`Map<String, Object[]>` → `Map<String, List<Object>>`), see [Decisions](#decisions)
- [ ] `ThemeTest.java`

## Step 4 — Default configuration (`TwMergeConfig`)

Reference: `src/TwMergeConfig.cs` (2,729 lines). Split in sub-steps; check each section against the reference before moving on.

- [ ] 4.1 Skeleton: fields (`cacheSize` default 500, `prefix`, `theme`, `classGroups`, `conflictingClassGroups`, `conflictingClassGroupModifiers`, `postfixLookupClassGroups`, `orderSensitiveModifiers`), constructor, theme getters and helper scales (lines 1–285)
- [ ] 4.2 Class groups: Layout (≈285–520)
- [ ] 4.3 Class groups: Flexbox and Grid (≈520–832)
- [ ] 4.4 Class groups: Sizing (≈832–917)
- [ ] 4.5 Class groups: Typography (≈917–1173)
- [ ] 4.6 Class groups: Backgrounds (≈1173–1269)
- [ ] 4.7 Class groups: Borders (≈1269–1523)
- [ ] 4.8 Class groups: Effects (≈1523–1740)
- [ ] 4.9 Class groups: Filters (≈1740–1896)
- [ ] 4.10 Class groups: Tables, Transitions and Animation, Transforms (≈1896–2128)
- [ ] 4.11 Class groups: Interactivity (≈2128–2411)
- [ ] 4.12 Class groups: SVG, Accessibility (≈2411–2600)
- [ ] 4.13 Conflicting class groups, conflicting class group modifiers, postfix lookup groups, order-sensitive modifiers
- [ ] 4.14 Verify the number of groups/keys against the C# config (count them in both) — a missing group is a silent bug
- [ ] Tip: split the Java config into several package-private helper classes (one per section) to avoid a 2,700-line file and the JVM 64 KB method limit in the constructor

## Step 5 — Class map factory

Reference: `src/TwMergeMapFactory.cs`, `tests/ClassMapFactoryTests.cs` (399 lines)

- [ ] `TwMergeMapFactory.create(config)` → builds the `ClassNameNode` trie
- [ ] `ClassMapFactoryTest.java`

## Step 6 — Context (modifier splitting and group lookup)

Reference: `src/TwMergeContext.cs` (263 lines)

- [ ] `getClassGroupId(className)`
- [ ] `getConflictingClassGroupIds(classGroupId, hasPostfixModifier)`
- [ ] `isPostfixLookupClassGroup(classGroupId)`
- [ ] `splitModifiers(className)` (variants, `!` important modifier, postfix `/`, arbitrary values with brackets, prefix handling)
- [ ] `sortModifiers(modifiers)` (respects order-sensitive modifiers)
- [ ] Tests: `ModifiersTest`, `ImportantModifierTest`, `ArbitraryVariantsTest`, `PseudoVariantsTest`

## Step 7 — Merge engine and cache

Reference: `src/TwMerge.cs` (166 lines)

- [ ] Thread-safe LRU cache without dependencies (e.g. `LinkedHashMap` access-ordered + synchronization, or a small dedicated class)
- [ ] `TwMerge(TwMergeConfig)` constructor + default constructor
- [ ] Real `merge(String...)`: whitespace split, reverse iteration, conflict detection, modifier sorting, result assembly
- [ ] Replace the scaffold implementation and update `TwMergeTest`
- [ ] Core tests: `TwMergeTest`, `NonConflictingClassesTest`, `NonTailwindClassesTest`, `WonkyInputsTest`, `StandaloneClassesTest`

## Step 8 — Configuration extension

Reference: `TwMergeConfig.cs` lines ≈2600–2729 (extend/override), `src/Models/ExtendedConfig.cs`

- [ ] Extend: theme, class groups, conflicting class groups, conflicting modifiers, postfix lookup groups, order-sensitive modifiers
- [ ] Override of the same sections
- [ ] Public API to configure `TwMerge` (builder or `Consumer<TwMergeConfig>`; replaces `IOptions<TwMergeConfig>`)
- [ ] Tests: `ExtendTest`, `PrefixTest`, `ThemeTest` (extension cases)

## Step 9 — Behavior tests (remaining)

Reference: `tests/*.cs`. Tick each file once its Java version passes.

- [ ] `ArbitraryPropertiesTests` (74)
- [ ] `ArbitraryValuesTests` (157)
- [ ] `ClassGroupsTests` (158)
- [ ] `ColorsClassesTests` (91)
- [ ] `ContentUtilitiesTests` (17)
- [ ] `NegativeValueTests` (39)
- [ ] `PerSideBorderColorsTests` (17)
- [ ] `TailwindCssVersionsTests` (327)
- [ ] `TwMergeTests` (134)
- [ ] Fix every discrepancy found against the reference (regex semantics, ordering, etc.)

## Step 10 — Integration and documentation

- [ ] Dependency-injection integration: the C# `ServiceCollectionExtensions` has no direct Java equivalent — decide whether to provide a shared default instance (`TwMerge.getDefault()`) and/or a Spring/CDI adapter in a separate artifact
- [ ] Port docs from `docs/references/tailwind-merge-dotnet/docs/`: what-is-it-for, features, limitations, configuration, api-reference (Java examples)
- [ ] `CONTRIBUTING.md` and `CODE_OF_CONDUCT.md` adapted from the reference
- [ ] Javadoc for the whole public API; attach a javadoc jar to the build
- [ ] Update `README.md`: remove the "work in progress" note, replace "coming soon" links, add the Thread-safe LRU cache claim only once implemented
- [ ] Optional: concurrency stress test and a small benchmark vs. the C# numbers

## Step 11 — Release

- [ ] `mvn clean verify` green on JDK 8, 11 and 17
- [ ] Tag `v1.0.0` (or a `0.x` if behavior is not yet complete) and push
- [ ] JitPack build green; log and artifacts checked
- [ ] Verified from a clean consumer project (Maven and Gradle)

---

## Decisions

| Topic | Decision | Reason |
|---|---|---|
| Build tool | Maven | Simplest for JitPack; `mvn` available locally |
| Java baseline | Java 8 (`--release 8` enforced on JDK 9+) | Maximum compatibility with legacy consumer projects; JDK 8 is the default `JAVA_HOME` here |
| Package | `com.github.ddxailab.tailwindmerge` | Mirrors the JitPack groupId |
| Dependencies | None at runtime; JUnit 5 for tests | C# uses `Planetarium.LruCacheNet` and `Microsoft.Extensions.Options`; neither is needed in Java |
| `null` results | `merge` returns `""` instead of `null` | Idiomatic Java; avoids NPEs in consumers (C# returns `null`) |
| Internals visibility | Package-private classes + tests in the same package | Replaces C# `InternalsVisibleTo` |
| Theme scale type | *to decide in Step 3* | C# uses `Dictionary<string, object[]>`; probably `Map<String, List<Object>>` |
| Cache | In-house thread-safe LRU | Keep the library dependency-free |
