# API reference

Reference to all public members of tailwind-merge-java. The full Javadoc is attached to every build as
`tailwind-merge-java-<version>-javadoc.jar`.

Packages:

- `com.github.ddxailab.tailwindmerge`: `TwMerge`, `ParsedClass`, `TwMergeConfig`
- `com.github.ddxailab.tailwindmerge.model`: `ClassGroup`, `ExtendedConfig`
- `com.github.ddxailab.tailwindmerge.common`: `ThemeGetter`, `ThemeUtility`, `ThemeKeys`, `Validators`,
  `ArbitraryValue`

## `TwMerge`

```java
/**
 * A utility for merging conflicting Tailwind CSS classes and for parsing single class names.
 */
public final class TwMerge
```

Instances are safe to share between threads.

### Constructors

```java
/** Initializes a new instance with the default configuration. */
public TwMerge()

/**
 * Initializes a new instance with the given configuration.
 *
 * @param config the configuration; it is copied, so later changes to it are ignored
 */
public TwMerge(TwMergeConfig config)
```

`TwMerge(TwMergeConfig)` throws `IllegalArgumentException` if `config` is `null`.

### Methods

```java
/**
 * Merges the given class names, resolving any conflicts if present.
 *
 * @param classNames the CSS classes to be merged; null and blank entries are ignored
 * @return the merged CSS classes, or an empty string if there is nothing to merge
 */
public String merge(String... classNames)

/**
 * Gets a shared instance with the default configuration, created lazily on first use.
 *
 * @return the shared instance; it is thread-safe
 */
public static TwMerge getDefault()

/**
 * Splits a single class name into its parts, interpreting it as merge does with the configuration
 * of this instance (prefix, theme and class groups).
 *
 * @param className the class to parse; white space around it is ignored
 * @return the parts of the class
 * @throws IllegalArgumentException if className is null, is blank or holds more than one class
 */
public ParsedClass parse(String className)
```

## `ParsedClass`

```java
/**
 * The parts of a single class name, as merge understands them.
 */
public final class ParsedClass
```

An immutable value created by `TwMerge.parse`. No method returns `null`.

| Method | Description |
| --- | --- |
| `String getClassName()` | The parsed class name, without surrounding white space |
| `List<String> getVariants()` | The variants in the order they are written, arbitrary ones included, without `:` (unmodifiable) |
| `boolean isImportant()` | Whether it has the important modifier: `p-4!`, or `!p-4` as in Tailwind CSS v3 |
| `String getBaseClassName()` | Without prefix, variants, important and postfix modifiers; keeps the `-` of a negative value |
| `boolean isNegative()` | A recognized class whose base class name starts with `-` |
| `String getModifier()` | The text after the `/` (`bg-red-300/50`, `text-lg/7`), or `""`. The slash of a fraction (`w-1/2`) is not a modifier |
| `Optional<ArbitraryValue> getArbitraryValue()` | The `[...]` or `(...)` value of the utility (`w-[10px]`, `text-(length:--x)`) |
| `Optional<ParsedClass.ArbitraryProperty> getArbitraryProperty()` | The declaration of an arbitrary property (`[padding:1rem]`), with `getName()` and `getValue()` |
| `String getClassGroupId()` | The class group ID (`arbitrary..<property>` for arbitrary properties, `bg-opacity` for the legacy `bg-opacity-*` classes), or `""` |
| `boolean isRecognized()` | Whether the class belongs to a class group |
| `boolean isExternal()` | Whether a prefix is configured and the class lacks it |

For a class that is not recognized only the class name, the variants, the important modifier and the base class
name are filled in. An external class is not interpreted at all: its base class name is the whole class name.

When the leading `!` of Tailwind CSS v3 is combined with a postfix modifier (`!text-lg/7`), `merge` resolves the
class group with a position that is off by one, as the reference does, and `parse` reports that same group
(`text-color` instead of `font-size`), or none. The trailing form (`text-lg/7!`) is not affected.

## `ArbitraryValue`

```java
/**
 * An arbitrary value ([10px]) or arbitrary variable ((--my-width)) split into its label and value.
 */
public final class ArbitraryValue
```

| Method | Description |
| --- | --- |
| `static Optional<ArbitraryValue> from(String text)` | Reads a value with its delimiters; empty if the text is not one |
| `ArbitraryValue.Syntax getSyntax()` | `BRACKETS` or `PARENTHESES` |
| `String getLabel()` | The label (`length`, `family-name`, ...) without the colon, or `""` |
| `String getValue()` | The value as written, without delimiters and label |

## `TwMergeConfig`

```java
/**
 * Represents the configuration settings for the TwMerge.
 * A new instance holds the default Tailwind CSS configuration.
 */
public class TwMergeConfig
```

### Constructors

```java
public TwMergeConfig()
```

### Properties

Each property has a getter and a setter (`getCacheSize` / `setCacheSize`, ...).

| Property | Type | Description |
| --- | --- | --- |
| `cacheSize` | `int` | Maximum size of the LRU cache used for memoizing results. The default is 500; a value lower than 1 turns the cache off |
| `legacyBgOpacity` | `boolean` | Whether the legacy `bg-opacity-*` classes (Tailwind CSS v3) are supported: they do not remove the background color and are merged into it as `bg-<color>/<value>`. The default is `true`; see [Features](./features.md#supports-the-legacy-bg-opacity--classes) |
| `prefix` | `String` | Custom prefix of all the Tailwind CSS generated utility classes |
| `theme` | `Map<String, List<Object>>` | Theme scales |
| `classGroups` | `Map<String, ClassGroup>` | Class groups |
| `conflictingClassGroups` | `Map<String, List<String>>` | Class groups that a class group overrides |
| `conflictingClassGroupModifiers` | `Map<String, List<String>>` | Class groups that a class group with a postfix modifier overrides |
| `postfixLookupClassGroups` | `Set<String>` | Class group IDs that are resolved again with their postfix modifier attached. Needed when a slash can make the full class name belong to a different class group than the part before the slash |
| `orderSensitiveModifiers` | `Set<String>` | Modifiers whose order among multiple modifiers is preserved because their order changes which element gets targeted |

### Methods

```java
/**
 * Creates a new instance with the default settings.
 */
public static TwMergeConfig createDefault()

/**
 * Extends the current configuration with the values from the provided ExtendedConfig.
 *
 * @return this configuration, to allow chaining
 */
public TwMergeConfig extend(ExtendedConfig extendedConfig)

/**
 * Overrides the current configuration with the values from the provided ExtendedConfig.
 *
 * @return this configuration, to allow chaining
 */
public TwMergeConfig override(ExtendedConfig extendedConfig)
```

## `ExtendedConfig`

```java
/**
 * Represents the extended configuration for the TailwindMerge utility.
 */
public class ExtendedConfig
```

Every property is optional (`null` means "not given") and has a getter and a setter.

| Property | Type | Description |
| --- | --- | --- |
| `theme` | `Map<String, List<Object>>` | The theme for the configuration extension |
| `classGroups` | `Map<String, ClassGroup>` | The class groups for the configuration extension |
| `conflictingClassGroups` | `Map<String, List<String>>` | The conflicting class groups |
| `conflictingClassGroupModifiers` | `Map<String, List<String>>` | The conflicting class group modifiers |
| `postfixLookupClassGroups` | `List<String>` | The class group IDs which should be resolved again with their postfix modifier attached |
| `orderSensitiveModifiers` | `List<String>` | The modifiers whose order among multiple modifiers is preserved |

With `extend`, maps are appended per key and the lists are added to the existing sets. With `override`, maps
replace per key and a given list replaces the whole set (an empty list clears it).

## `ThemeGetter`

```java
/**
 * Represents a method that retrieves a list of objects from a theme based on a specified key.
 */
@FunctionalInterface
public interface ThemeGetter {
    List<Object> apply(Map<String, List<Object>> theme);
}
```

## `ThemeUtility`

```java
/**
 * Provides utility method for working with theme configurations.
 */
public final class ThemeUtility
```

### Methods

```java
/**
 * Creates a ThemeGetter that retrieves values from a theme based on the specified key.
 *
 * @param key the key to use for retrieving values from the theme
 */
public static ThemeGetter fromTheme(String key)
```

## `ThemeKeys`

```java
/**
 * Provides the keys of the theme scales in the default configuration.
 */
public final class ThemeKeys
```

### Constants

| Constant | Value | Tailwind CSS namespace |
| --- | --- | --- |
| `ThemeKeys.COLOR` | `color` | `--color-*` |
| `ThemeKeys.FONT` | `font` | `--font-*` |
| `ThemeKeys.TEXT` | `text` | `--text-*` |
| `ThemeKeys.FONT_WEIGHT` | `font-weight` | `--font-weight-*` |
| `ThemeKeys.TRACKING` | `tracking` | `--tracking-*` |
| `ThemeKeys.LEADING` | `leading` | `--leading-*` |
| `ThemeKeys.BREAKPOINT` | `breakpoint` | `--breakpoint-*` |
| `ThemeKeys.CONTAINER` | `container` | `--container-*` |
| `ThemeKeys.SPACING` | `spacing` | `--spacing-*` |
| `ThemeKeys.RADIUS` | `radius` | `--radius-*` |
| `ThemeKeys.SHADOW` | `shadow` | `--shadow-*` |
| `ThemeKeys.INSET_SHADOW` | `inset-shadow` | `--inset-shadow-*` |
| `ThemeKeys.TEXT_SHADOW` | `text-shadow` | `--text-shadow-*` |
| `ThemeKeys.DROP_SHADOW` | `drop-shadow` | `--drop-shadow-*` |
| `ThemeKeys.BLUR` | `blur` | `--blur-*` |
| `ThemeKeys.PERSPECTIVE` | `perspective` | `--perspective-*` |
| `ThemeKeys.ASPECT` | `aspect` | `--aspect-*` |
| `ThemeKeys.EASE` | `ease` | `--ease-*` |
| `ThemeKeys.ANIMATE` | `animate` | `--animate-*` |

## `Validators`

```java
/**
 * Provides a set of validation functions for different class name parts.
 */
public final class Validators
```

### Constants

Every validator is a `public static final Predicate<String>` of the class part to check.

- `IS_ANY` always returns true. Be careful with this validator as it might match unwanted classes. It is meant
  for matching colors or when you are certain there are no other class groups in a namespace.
- `IS_ANY_NON_ARBITRARY` checks if the class part is not an arbitrary value or arbitrary variable.
- `IS_ARBITRARY_FAMILY_NAME` checks whether class part is an arbitrary value which starts with `family-name:` (`[family-name:Open_Sans]`) which is necessary for font-family classNames.
- `IS_ARBITRARY_IMAGE` checks whether class part is an arbitrary value which is an image, e.g. by starting with `image:`, `url:`, `linear-gradient(` or `url(` (`[url('/path-to-image.png')]`, `[image:var(--maybe-an-image-at-runtime)]`) which is necessary for background-image classNames.
- `IS_ARBITRARY_LENGTH` checks for arbitrary length values (`[3%]`, `[4px]`, `[length:var(--my-var)]`).
- `IS_ARBITRARY_NUMBER` checks whether class part is an arbitrary value which starts with `number:` or is a number (`[number:var(--value)]`, `[450]`) which is necessary for line-clamp and stroke-width classNames.
- `IS_ARBITRARY_POSITION` checks whether class part is an arbitrary value which starts with `position:` (`[position:200px_100px]`) which is necessary for background-position classNames.
- `IS_ARBITRARY_SHADOW` checks whether class part is an arbitrary value which starts with the same pattern as a shadow value (`[0_35px_60px_-15px_rgba(0,0,0,0.3)]`), namely with two lengths separated by an underscore, optionally prepended by `inset`.
- `IS_ARBITRARY_SIZE` checks whether class part is an arbitrary value which starts with `size:` (`[size:200px_100px]`) which is necessary for background-size classNames.
- `IS_ARBITRARY_VALUE` checks whether the class part is enclosed in brackets (`[something]`).
- `IS_ARBITRARY_VARIABLE` checks whether the class part is an arbitrary variable (`(--my-var)`).
- `IS_ARBITRARY_VARIABLE_FAMILY_NAME` checks whether class part is an arbitrary variable with the `family-name` label (`(family-name:--my-font)`).
- `IS_ARBITRARY_VARIABLE_IMAGE` checks whether class part is an arbitrary variable with the `image` or `url` label (`(image:--my-image)`).
- `IS_ARBITRARY_VARIABLE_LENGTH` checks whether class part is an arbitrary variable with the `length` label (`(length:--my-length)`).
- `IS_ARBITRARY_VARIABLE_POSITION` checks whether class part is an arbitrary variable with the `position` label (`(position:--my-position)`).
- `IS_ARBITRARY_VARIABLE_SHADOW` checks whether class part is an arbitrary variable with the `shadow` label or no label at all (`(shadow:--my-shadow)`, `(--my-shadow)`).
- `IS_ARBITRARY_VARIABLE_SIZE` checks whether class part is an arbitrary variable with the `size`, `length` or `percentage` label (`(size:--my-size)`).
- `IS_ARBITRARY_VARIABLE_WEIGHT` checks whether class part is an arbitrary variable with the `weight` or `number` label or no label at all (`(weight:--my-weight)`, `(--my-weight)`).
- `IS_ARBITRARY_WEIGHT` checks whether class part is an arbitrary value which starts with `weight:` or `number:` or has no label at all (`[weight:var(--value)]`, `[400]`, `[bold]`) which is necessary for font-weight classNames.
- `IS_FRACTION` checks whether class part is a fraction of two numbers (`1/2`, `127/256`).
- `IS_INTEGER` checks for integer values (`3`).
- `IS_NAMED_CONTAINER_QUERY` checks whether class part is a named container query class (`@container/sidebar`, `@container-size/main`, `@container-normal/content`).
- `IS_NUMBER` checks for numbers (`3`, `1.5`).
- `IS_PERCENT` checks for percent values (`12.5%`) which is used for color stop positions.
- `IS_TSHIRT_SIZE` checks whether class part is a T-shirt size (`sm`, `xl`), optionally with a preceding number (`2xl`).

## `ClassGroup`

```java
/**
 * Represents a group of CSS classes with associated metadata for the TailwindMerge utility.
 */
public final class ClassGroup
```

An immutable value; the list of definitions cannot be modified.

### Factory methods

```java
/**
 * Creates a class group without a base class name.
 *
 * @param definitions Strings, Predicate<String> validators, ThemeGetters or nested ClassGroups
 */
public static ClassGroup of(Object... definitions)

/**
 * Creates a class group with the specified base class name and definitions.
 *
 * @param baseClassName the base class name for the group
 * @param definitions   Strings, Predicate<String> validators, ThemeGetters or nested ClassGroups
 */
public static ClassGroup withBase(String baseClassName, Object... definitions)
```

### Properties

```java
/** Gets the base class name for the group, or null if it has none. */
public String getBaseClassName()

/** Gets the definitions associated with the class group (unmodifiable). */
public List<Object> getDefinitions()
```

---

[Back to overview](./README.md)
