# Conversion checklist: tailwind-merge-dotnet → tailwind-merge-java

Reference: `docs/references/tailwind-merge-dotnet/` (≈3,800 lines of C# in `src/`, ≈2,900 in `tests/`; `TwMergeConfig.cs` alone is ≈2,700).

**How to work through it**

- Read the [development guide](DEVELOPMENT.md) first (build, conventions, C# → Java notes).
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
- [x] `docs/DEVELOPMENT.md` development guide, referenced from `README.md` and `AGENTS.md`
- [x] Builds and passes tests on JDK 8, 11 and 17
- [x] Consumed from a separate local Maven project (`mvn install` + dependency) — works
- [x] First commit and push to GitHub
- [x] Tag `v0.1.0` and confirm the build is green on https://jitpack.io/#DdxAiLab/tailwind-merge-java
- [x] Consume `v0.1.0` from the real target project through JitPack

## Step 1 — Models and constants ✅

Reference: `src/Common/Constants.cs`, `src/Models/*`

- [x] `Constants` → `Constants.java` (package-private, root package)
- [x] `ClassValidator` (class ID + validator pair) → `ClassValidator.java`; the validator itself is a `Predicate<String>`
- [x] `ClassGroup` → `model/ClassGroup.java` (public; `ClassGroup.of(...)` / `ClassGroup.withBase(base, ...)`)
- [x] `ClassModifiersInfo` → `ClassModifiersInfo.java` (immutable, replaces the C# record struct)
- [x] `ClassNameNode` → `ClassNameNode.java` (trie node used by the class map)
- [x] `ExtendedConfig` → `model/ExtendedConfig.java` (public POJO with getters/setters)
- [x] Unit tests: `ClassNameNodeTest`, `model/ClassGroupTest`

## Step 2 — Validators ✅

Reference: `src/Common/Validators.cs` (246 lines), `tests/ValidatorsTests.cs` (518 lines)

- [x] Translate every regex (`Regex` → `java.util.regex.Pattern`, precompiled `static final`); check .NET vs Java regex differences (named groups, `\d`, `\w`, RegexOptions)
- [x] `common/Validators.java` (public, `Predicate<String>` constants: `IS_NUMBER`, `IS_FRACTION`, `IS_ARBITRARY_*`, `IS_TSHIRT_SIZE`, `IS_PERCENT`, …)
- [x] `common/ValidatorsTest.java` — all 222 cases ported (plus Java-specific extras)

## Step 3 — Theme keys and theme getter ✅

Reference: `src/Common/ThemeKeys.cs`, `src/Common/ThemeGetter.cs`, `tests/ThemeTests.cs`

- [x] `common/ThemeKeys.java` (19 keys, e.g. `ThemeKeys.FONT_WEIGHT`)
- [x] `common/ThemeGetter.java` (functional interface) and `common/ThemeUtility.fromTheme(...)`
- [x] `common/ThemeUtilityTest.java` — tests for `fromTheme` and the key values
- [x] `ThemeKeys_MatchDefaultThemeKeys` ported in Step 4.1 as `ThemeTest.themeKeysMatchDefaultThemeKeys` (the default theme already exists there)
- [ ] Deferred: the three `Merge_With*Theme*` theories of `ThemeTests.cs` → Step 8 (need `TwMerge`, `Extend` and `Override`), to be added to `ThemeTest.java`

## Step 4 — Default configuration (`TwMergeConfig`)

Reference: `src/TwMergeConfig.cs` (2,729 lines). Split in sub-steps; check each section against the reference before moving on.

- [x] 4.1 Skeleton: fields (`cacheSize` default 500, `prefix`, `theme`, `classGroups`, `conflictingClassGroups`, `conflictingClassGroupModifiers`, `postfixLookupClassGroups`, `orderSensitiveModifiers`), constructor, theme getters and helper scales (lines 1–285) — `TwMergeConfig.java` (public; default theme with its 19 keys, the other maps/sets start empty), `DefaultConfigScales.java` (package-private: theme getters + shared scales), tests `TwMergeConfigTest`, `ThemeTest`
- [x] 4.2 Class groups: Layout (≈285–520) — 37 groups in `LayoutClassGroups.java` (package-private, `addTo(Map)` called from the `TwMergeConfig` constructor; IDs and order checked against the C# source), test `LayoutClassGroupsTest`
- [x] 4.3 Class groups: Flexbox and Grid (≈520–832) — 56 groups (flex/grid, alignment, padding, margin, space) in `FlexboxGridClassGroups.java`; IDs and order checked against the C# source; test `FlexboxGridClassGroupsTest`
- [x] 4.4 Class groups: Sizing (≈832–917) — 13 groups in `SizingClassGroups.java`; IDs and order checked against the C# source; test `SizingClassGroupsTest`
- [x] 4.5 Class groups: Typography (≈917–1173) — 37 groups in `TypographyClassGroups.java`; IDs and order checked against the C# source; test `TypographyClassGroupsTest`
- [x] 4.6 Class groups: Backgrounds (≈1173–1269) — 14 groups in `BackgroundsClassGroups.java`; IDs and order checked against the C# source; test `BackgroundsClassGroupsTest`
- [x] 4.7 Class groups: Borders (≈1269–1523) — 48 groups in `BordersClassGroups.java`; IDs and order checked against the C# source (base class name and scale also compared for the 41 `id/base/scale` groups); test `BordersClassGroupsTest`
- [x] 4.8 Class groups: Effects (≈1523–1740) — 67 groups (shadows, rings, opacity, blend modes, masks) in `EffectsClassGroups.java`; IDs and order checked against the C# source (base class name and scale also compared for the 49 single-scale groups); test `EffectsClassGroupsTest`
- [x] 4.9 Class groups: Filters (≈1740–1896) — 21 groups in `FiltersClassGroups.java`; IDs and order checked against the C# source; test `FiltersClassGroupsTest` (every group is asserted by shape: number, bare + number, blur scale, none + arbitrary, drop shadow)
- [x] 4.10 Class groups: Tables, Transitions and Animation, Transforms (≈1896–2128) — 36 groups (6 + 6 + 24) in `TablesTransitionsTransformsClassGroups.java`, one private method per section; IDs and order checked against the C# source (base class name and scale also compared for the 20 single-scale groups); test `TablesTransitionsTransformsClassGroupsTest`
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
| Theme scale type | `Map<String, List<Object>>` | C# uses `Dictionary<string, object[]>` |
| Internal types | `ClassNameNode`, `ClassModifiersInfo`, `ClassValidator`, `Constants` (and later `TwMergeContext`, `TwMergeMapFactory`) live package-private in the root package | C# `internal` spans several namespaces; Java package-private cannot span sub-packages. Only public API types go in sub-packages (`model`, `common`) |
| Class group definitions | `Object` varargs: `String`, `Predicate<String>`, theme getter or nested `ClassGroup` | Same heterogeneous model as the C# `object[]`. Lambdas/method refs cannot be passed as `Object`, so validators are exposed as `static final Predicate<String>` constants (Step 2) |
| Collections | `List`/`Map`/`Set` instead of arrays/`Dictionary`/`HashSet`; `ClassGroup.getDefinitions()` is unmodifiable | Idiomatic Java; theme scales are `Map<String, List<Object>>` |
| Cache | In-house thread-safe LRU | Keep the library dependency-free |
| Regex flags | Every `Pattern` uses `UNIX_LINES` + `UNICODE_CHARACTER_CLASS` (+ `CASE_INSENSITIVE`/`UNICODE_CASE` where C# has `IgnoreCase`); matching uses `find()` | Mirrors .NET: only `\n` is a line terminator (`$`, `.`), and `\d`/`\w`/`\b` are Unicode-aware |
| Number parsing | Own parser (regex + `Double.valueOf`) instead of `Double.parseDouble` directly | `parseDouble` accepts `1d`, `1f`, `0x1p3`; .NET `double.TryParse(NumberStyles.Float, Invariant)` does not. `NaN` is not recognized (never accepted by `IS_NUMBER`/`IS_INTEGER` anyway); `Infinity` is a number but not an integer, as in C# |
| Validator naming | `Validators.IS_*` constants (`IsAny` → `IS_ANY`) | Follows the constants-as-`Predicate` decision above |
| Default config layout | `TwMergeConfig` (public) builds the theme and delegates to package-private section helpers; the shared theme getters and scales live in `DefaultConfigScales` as `static final Object[]` / `ThemeGetter` constants, and `DefaultConfigScales.concat(...)` replaces the C# spread `[.. scale]` | They are immutable and stateless, so one shared copy is enough; each section class stays well under the 64 KB method limit. A scale passed next to other arguments would be nested, hence `concat` |
| Config collections | `postfixLookupClassGroups` and `orderSensitiveModifiers` are `Set<String>`; `conflictingClassGroups(Modifiers)` are `Map<String, List<String>>`; maps keep insertion order (`LinkedHashMap`/`LinkedHashSet`) | `HashSet<string>` / `string[]` in C#; ordering mirrors `Dictionary` enumeration |
| `ThemeGetter` type | Dedicated `@FunctionalInterface` (`List<Object> apply(Map<String, List<Object>>)`) instead of `Function` | Class group definitions are `Object`s; the map factory must tell a theme getter from a `Predicate` validator (C# uses `is ThemeGetter`) |
