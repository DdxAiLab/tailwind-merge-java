# Parsing a single class name (`TwMerge.parse`)

## Motivation

`merge` already splits every class internally (variants, `!` modifier, modifier after `/`, class group), but that
interpretation was not accessible. Anyone who needed to understand a class had to write their own parser, at the risk of it
disagreeing with the library.

`TwMerge.parse` exposes that interpretation as a public, generic feature: it uses the same splitting into parts and the same
class group resolution as `merge`, with the configuration of the instance (`prefix`, `theme`, `classGroups`,
`legacyBgOpacity`). There is no second parser: the class group resolution was extracted into a private method that `merge`
and `parse` now share.

The behavior and the existing API of `merge` do not change. Java 8 is still the baseline and no runtime dependencies are
added.

## API

### `TwMerge`

```java
public ParsedClass parse(String className)
```

Throws `IllegalArgumentException` if `className` is `null`, is blank or holds more than one class.

### `ParsedClass`

Package `com.github.ddxailab.tailwindmerge`. Immutable; only `TwMerge.parse` creates it.

| Signature | Content |
| --- | --- |
| `String getClassName()` | The parsed class, without surrounding white space |
| `List<String> getVariants()` | Variants in the order they are written, without `:`; arbitrary ones included. Unmodifiable list |
| `boolean isImportant()` | `!` at the end (`p-4!`) or at the start of the base class, v3 style (`!p-4`) |
| `String getBaseClassName()` | Without prefix, variants, `!` or modifier. Keeps the `-` of negative values |
| `boolean isNegative()` | Recognized class whose base class starts with `-` |
| `String getModifier()` | Text after `/` when it is a modifier, as written; `""` if there is none |
| `Optional<ArbitraryValue> getArbitraryValue()` | The `[..]` or `(..)` value of the utility |
| `Optional<ParsedClass.ArbitraryProperty> getArbitraryProperty()` | Declaration of an arbitrary property (`[padding:1rem]`) |
| `String getClassGroupId()` | Class group ID; `""` if the class is not recognized |
| `boolean isRecognized()` | `true` if it belongs to a class group |
| `boolean isExternal()` | `true` if a `prefix` is configured and the class lacks it |
| `String toString()` | Same as `getClassName()` |

### `ParsedClass.ArbitraryProperty`

```java
public String getName()    // "padding"
public String getValue()   // "1rem"
```

### `ArbitraryValue`

Package `com.github.ddxailab.tailwindmerge.common`. Immutable. Applies the same regular expressions as
`Validators.IS_ARBITRARY_VALUE` and `IS_ARBITRARY_VARIABLE`.

```java
public static Optional<ArbitraryValue> from(String text)   // "[length:var(--x)]", "(--x)"
public ArbitraryValue.Syntax getSyntax()                   // BRACKETS or PARENTHESES
public String getLabel()                                   // "length", "family-name", ... or ""
public String getValue()                                   // without delimiters and label
```

`from` returns an empty `Optional` if the text is `null` or is not enclosed in `[]` or `()` with something inside.

## Examples

```java
TwMerge twMerge = TwMerge.getDefault();

ParsedClass parsed = twMerge.parse("md:hover:bg-red-300/50!");
parsed.getVariants();       // ["md", "hover"]
parsed.isImportant();       // true
parsed.getBaseClassName();  // "bg-red-300"
parsed.getModifier();       // "50"
parsed.getClassGroupId();   // "bg-color"

ArbitraryValue value = twMerge.parse("text-(length:--size)").getArbitraryValue().get();
value.getSyntax();          // PARENTHESES
value.getLabel();           // "length"
value.getValue();           // "--size"

ParsedClass.ArbitraryProperty property =
        twMerge.parse("[padding:1rem]").getArbitraryProperty().get();
property.getName();         // "padding"
property.getValue();        // "1rem"
```

| Class | `variants` | `baseClassName` | `modifier` | `classGroupId` | Other |
| --- | --- | --- | --- | --- | --- |
| `p-4` | `[]` | `p-4` | `""` | `p` | |
| `-m-4` | `[]` | `-m-4` | `""` | `m` | negative |
| `!p-4` | `[]` | `p-4` | `""` | `p` | important |
| `[&>*]:underline` | `[[&>*]]` | `underline` | `""` | `text-decoration` | |
| `w-1/2` | `[]` | `w-1/2` | `""` | `w` | fraction |
| `aspect-4/3` | `[]` | `aspect-4/3` | `""` | `aspect` | fraction |
| `text-lg/7` | `[]` | `text-lg` | `7` | `font-size` | |
| `bg-red-300/50` | `[]` | `bg-red-300` | `50` | `bg-color` | |
| `w-[10px]` | `[]` | `w-[10px]` | `""` | `w` | value `10px`, brackets |
| `font-(family-name:--f)` | `[]` | `font-(family-name:--f)` | `""` | `font-family` | label `family-name`, parentheses |
| `[padding:1rem]` | `[]` | `[padding:1rem]` | `""` | `arbitrary..padding` | arbitrary property |
| `hover:foo/50` | `[hover]` | `foo/50` | `""` | `""` | not recognized |

## Edge cases

### Input

| Input | Result |
| --- | --- |
| `null` | `IllegalArgumentException` |
| `""` or only white space | `IllegalArgumentException` |
| Several classes (`"p-4 m-2"`) | `IllegalArgumentException` |
| White space inside brackets (`"[padding: 1rem]"`) | `IllegalArgumentException`: they are two classes, as in `merge` |
| Surrounding white space (`" p-4 "`) | Ignored; `getClassName()` returns `p-4` |

### Prefix

With `prefix = "tw"`:

- `tw:hover:p-4` is parsed as usual; `tw` is not listed among the variants and `getClassName()` keeps the whole class.
- `p-4` or `hover:tw:p-4` (not starting with the prefix) are not an error: `isExternal()` is `true`, `isRecognized()` is
  `false`, `getBaseClassName()` is the whole class, there are no variants or modifiers and `!` is not interpreted.

Without a configured `prefix`, `isExternal()` is always `false`.

### Unrecognized classes

The original class, the variants, `isImportant()` and the base class (everything left after variants, prefix and `!`,
including any `/`) are filled in. `isNegative()` is `false`, the modifier is `""` and there is no arbitrary value or
property.

"Recognized" means that the configuration assigns the class to a group, not that Tailwind generates CSS for it: color groups
accept any name, so `bg-anything` is recognized as `bg-color`.

### The `/` character

- It is not a modifier when it is part of the value: the group declares `Validators.IS_FRACTION` (or a literal definition
  with `/`) for that value. A custom configuration that accepts fractions with another validator will see the slash as a
  modifier.
- It is not a modifier when `merge` resolves the group with the full name (`aspect-4/3`, `@container/sidebar`, which
  belongs to the `container-named` group).
- Slashes inside `[]` or `()` never split.
- With several slashes only the last one counts (`w-1/2/50`: base `w-1/2`, modifier `50`).
- A trailing slash (`bg-red-300/`) stays in the base class; the modifier is `""`.
- The modifier is returned raw (`[0.5]`, `(--leading)`). `ArbitraryValue.from` can be used to split it.

### Arbitrary values

- `getArbitraryValue()` reports only the value of the utility: the `[..]` or `(..)` that follows a `-` and ends the base
  class. Those of variants, modifiers and arbitrary properties do not appear there.
- Values are returned as written: `_` is not turned into a space.
- `[foo]` (without `:`) is not an arbitrary property: the class is not recognized.

### Other

- Only one `!` is removed: `p-4!!` is not recognized.
- With `legacyBgOpacity` enabled (the default), `bg-opacity-50` has the `bg-opacity` group, the same one `merge` uses. That
  ID does not exist in `TwMergeConfig.getClassGroups()`. Disabled, it is `bg-color`.
- The result is not stored in the cache of `merge`.
- `parse` is thread-safe (as long as the custom validators of the configuration are).

### Limitation: leading `!` combined with a modifier

When a class uses the leading `!` of v3 **and also** a modifier, `merge` computes the position of the slash off by one
character. This behavior is inherited from the reference library. `parse` splits the base class and the modifier correctly,
but reports the same group `merge` uses, which may not be the expected one:

| Class | Group with leading `!` | Group with trailing `!` |
| --- | --- | --- |
| `text-lg/7`, `text-[14px]/6` | `text-color` | `font-size` |
| `shadow-lg/50` | `shadow-color` | `shadow` |
| `inset-shadow-sm/40` | `inset-shadow-color` | `inset-shadow` |
| `drop-shadow-lg/50` | `drop-shadow-color` | `drop-shadow` |
| `text-shadow-lg/50` | `text-shadow-color` | `text-shadow` |
| `bg-linear-45/oklch`, `bg-conic-180/oklch` | `bg-color` | `bg-image` |
| `ring-2/50` | `ring-color` | `ring-w` |
| `leading-6/2`, `[color:red]/50` | not recognized | recognized |

The table lists the cases that were checked; there may be others. It does not happen with the trailing `!`, or with the
leading one without a modifier. Fixing it would change the result of `merge`, so it is out of the scope of this change.

## Modified files

| File | Change |
| --- | --- |
| `src/main/java/.../tailwindmerge/TwMerge.java` | `parse` method; class group resolution extracted into `resolve`, shared with `merge` |
| `src/main/java/.../tailwindmerge/ParsedClass.java` | New: result of the parsing |
| `src/main/java/.../tailwindmerge/TwMergeContext.java` | `isSlashPartOfValue` and constant for the group prefix of arbitrary properties |
| `src/main/java/.../common/ArbitraryValue.java` | New: arbitrary value or variable with its label |
| `src/main/java/.../common/Validators.java` | Two patterns go from private to package-private so they can be reused |
| `src/test/java/.../tailwindmerge/ParseTest.java` | New |
| `src/test/java/.../common/ArbitraryValueTest.java` | New |
| `pom.xml` | Version 0.4.0 |
| `docs/api-reference.md` | Reference of the new API |
| `docs/CHECKLIST.md` | Row in the decisions table and 0.4.0 release |
| `docs/features-20261127-es.md` | Spanish version of this document |
| `docs/features-20261127-en.md` | This document |

## Test results

- `mvn -B -ntp clean verify` succeeds on JDK 8 (1.8.0_211), 11.0.24 and 17.0.12: **1041 tests**, 0 failures, 0 errors,
  0 skipped. There were 936 before the change; the 105 new ones are 91 from `ParseTest` and 14 from `ArbitraryValueTest`.
- `javadoc -Xdoclint:all` (JDK 17) reports no warnings.
- Comparison of `merge` between the jar built before the change and the new code, with and without a prefix: 600,000 calls
  with inputs generated from the tests and from random fragments, with no difference.
- `parse` on 283,667 inputs generated the same way, with no exception other than `IllegalArgumentException`.
- `tools/config-check`: the default configuration is still identical to the reference one.

Not verified: consuming the artifact from JitPack (it requires publishing a tag).

## Version

**0.4.0** (tag `v0.4.0`): it adds public API without breaking the existing one.
