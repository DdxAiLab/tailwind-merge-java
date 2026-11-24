# Configuration

## Installation

tailwind-merge-java is distributed through [JitPack](https://jitpack.io). Add the repository and the dependency
(replace `TAG` with a release tag):

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.DdxAiLab</groupId>
        <artifactId>tailwind-merge-java</artifactId>
        <version>TAG</version>
    </dependency>
</dependencies>
```

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.DdxAiLab:tailwind-merge-java:TAG'
}
```

The library has no runtime dependencies and works on Java 8 or newer.

## Basic usage

If you're using Tailwind CSS without any extra config, you can use [`merge`](./api-reference.md#twmerge) right
away with the shared default instance:

```java
import com.github.ddxailab.tailwindmerge.TwMerge;

TwMerge.getDefault().merge("px-2 py-1 bg-red hover:bg-dark-red", "p-3 bg-[#B91C1C]");
// → "hover:bg-dark-red p-3 bg-[#B91C1C]"
```

You can safely stop reading the documentation here.

## Usage with custom Tailwind config

If you're using a custom Tailwind config, you may need to configure tailwind-merge-java as well to merge classes
properly.

The default configuration still works if all the following points apply to your Tailwind config:

- Only using color names which don't clash with other Tailwind class names
- Only deviating by number values from number-based Tailwind classes
- Only using font-family classes which don't clash with default font-weight classes
- Sticking to default Tailwind config for everything else

Otherwise, extend or override the default configuration and pass it to the constructor:

```java
import com.github.ddxailab.tailwindmerge.TwMerge;
import com.github.ddxailab.tailwindmerge.TwMergeConfig;
import com.github.ddxailab.tailwindmerge.common.ThemeKeys;
import com.github.ddxailab.tailwindmerge.model.ExtendedConfig;
import java.util.Arrays;
import java.util.Collections;

ExtendedConfig extension = new ExtendedConfig();
extension.setTheme(Collections.singletonMap(ThemeKeys.TEXT, Arrays.<Object>asList("huge")));

TwMergeConfig config = new TwMergeConfig();
config.extend(extension);

TwMerge twMerge = new TwMerge(config);
```

`TwMerge` builds its lookup structures from the configuration once, when it's created, so changes made to the
configuration afterwards have no effect.

### Sharing the instance

Create the instance once and share it: it is thread-safe and its cache is only useful if it is reused. There is
no dependency injection container in the library; register the instance in whatever you use. E.g. with Spring:

```java
@Configuration
class TailwindMergeConfiguration {

    @Bean
    TwMerge twMerge() {
        TwMergeConfig config = new TwMergeConfig();
        // config.extend(...)
        return new TwMerge(config);
    }
}
```

With the default configuration, `TwMerge.getDefault()` is already a shared, lazily created instance.

### Extending and overriding

- `extend` adds values to the existing ones. The values of theme scales and class groups are appended, and keys
  which don't exist yet are added.
- `override` replaces the values of the given keys completely. Keys which aren't mentioned keep their default
  values.

If you use both, call `override` before `extend`. Both methods return the configuration, so they can be chained.

### Theme

In your Tailwind config you can modify the theme variable namespaces to add classes with custom values.
tailwind-merge-java follows the same naming scheme as Tailwind CSS for its theme scales, and the `ThemeKeys` class
provides a constant for each of them:

| Tailwind CSS namespace | Theme key | Constant |
| --- | --- | --- |
| `--color-*` | `color` | `ThemeKeys.COLOR` |
| `--font-*` | `font` | `ThemeKeys.FONT` |
| `--text-*` | `text` | `ThemeKeys.TEXT` |
| `--font-weight-*` | `font-weight` | `ThemeKeys.FONT_WEIGHT` |
| `--tracking-*` | `tracking` | `ThemeKeys.TRACKING` |
| `--leading-*` | `leading` | `ThemeKeys.LEADING` |
| `--breakpoint-*` | `breakpoint` | `ThemeKeys.BREAKPOINT` |
| `--container-*` | `container` | `ThemeKeys.CONTAINER` |
| `--spacing-*` | `spacing` | `ThemeKeys.SPACING` |
| `--radius-*` | `radius` | `ThemeKeys.RADIUS` |
| `--shadow-*` | `shadow` | `ThemeKeys.SHADOW` |
| `--inset-shadow-*` | `inset-shadow` | `ThemeKeys.INSET_SHADOW` |
| `--text-shadow-*` | `text-shadow` | `ThemeKeys.TEXT_SHADOW` |
| `--drop-shadow-*` | `drop-shadow` | `ThemeKeys.DROP_SHADOW` |
| `--blur-*` | `blur` | `ThemeKeys.BLUR` |
| `--perspective-*` | `perspective` | `ThemeKeys.PERSPECTIVE` |
| `--aspect-*` | `aspect` | `ThemeKeys.ASPECT` |
| `--ease-*` | `ease` | `ThemeKeys.EASE` |
| `--animate-*` | `animate` | `ThemeKeys.ANIMATE` |

If you modified one of these namespaces in your Tailwind config, add the variable names to the theme as well, so
that tailwind-merge-java knows about them. E.g. after adding `--text-huge: 100px` to your Tailwind config:

```java
ExtendedConfig extension = new ExtendedConfig();
// ↓ `text` is the key of the `--text-*` namespace
//                                                        ↓ `huge` is the variable name without the namespace prefix
extension.setTheme(Collections.singletonMap(ThemeKeys.TEXT, Arrays.<Object>asList("huge")));
TwMergeConfig config = new TwMergeConfig().extend(extension);
TwMerge twMerge = new TwMerge(config);

twMerge.merge("text-lg text-huge"); // → "text-huge"
twMerge.merge("text-huge text-sm"); // → "text-sm"
```

A theme scale is applied to all class groups which use it, e.g. extending `ThemeKeys.SPACING` adds the new values
to padding, margin, gap, inset and all other spacing classes at once.

### Class groups

Classes which modify the same CSS property form a class group, and tailwind-merge-java only keeps the last class
of each group. A `ClassGroup` consists of a base class name and its definitions, which can be:

- strings for fixed class parts, where an empty string stands for the base class name itself
- validators from `Validators` (or any `Predicate<String>`) for class parts that can't be listed, e.g. arbitrary
  values
- theme scales referenced with `ThemeUtility.fromTheme`
- nested `ClassGroup` instances for class parts that share a prefix

Use `ClassGroup.withBase(baseClassName, definitions...)` for a group with a base class name and
`ClassGroup.of(definitions...)` for one without. E.g. a class group for custom `elevation-*` utilities:

```java
import com.github.ddxailab.tailwindmerge.common.Validators;
import com.github.ddxailab.tailwindmerge.model.ClassGroup;

ExtendedConfig extension = new ExtendedConfig();
extension.setClassGroups(Collections.singletonMap(
        // ↓ Resolves to elevation, elevation-low, elevation-high and elevation-[...]
        "elevation",
        ClassGroup.withBase("elevation", "", "low", "high", Validators.IS_ARBITRARY_VALUE)));

TwMergeConfig config = new TwMergeConfig().extend(extension);

new TwMerge(config).merge("elevation-low elevation-high"); // → "elevation-high"
```

The key (`"elevation"`) is the ID of the class group. It's only used internally and has to be unique among all
class groups. To add values to a default class group, extend it under its ID (e.g. `"shadow"`); the IDs are listed
in the default configuration (`TwMergeConfig` and the `*ClassGroups` classes next to it in the source).

If several of your class groups share the same values, you can put them into a custom theme scale and reference
it with `ThemeUtility.fromTheme("elevation")` in each class group.

> Java note: a lambda cannot be passed directly where an `Object` is expected, so a custom validator has to be
> stored in a `Predicate<String>` variable first:
>
> ```java
> Predicate<String> isEven = value -> value.matches("\\d*[02468]");
> ClassGroup.withBase("size", isEven);
> ```

### Prefix

If your Tailwind config uses a prefix, set it in the configuration. Only classes that start with the prefix are
merged; everything else is left untouched:

```java
TwMergeConfig config = new TwMergeConfig();
config.setPrefix("tw");

new TwMerge(config).merge("tw:p-3 tw:p-2"); // → "tw:p-2"
```

---

[Back to overview](./README.md)
