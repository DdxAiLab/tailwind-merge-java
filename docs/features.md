# Features

## Merging behavior

tailwind-merge-java is built to be intuitive. It follows a set of rules to determine which class wins when there
are conflicts. Here is a brief overview of its conflict resolution.

### Last conflicting class wins

```java
twMerge.merge("p-5 p-2 p-4"); // → "p-4"
```

### Allows refinements

```java
twMerge.merge("p-3 px-5"); // → "p-3 px-5"
twMerge.merge("inset-x-4 right-4"); // → "inset-x-4 right-4"
```

### Resolves non-trivial conflicts

```java
twMerge.merge("inset-x-px -inset-1"); // → "-inset-1"
twMerge.merge("bottom-auto inset-y-6"); // → "inset-y-6"
twMerge.merge("inline block"); // → "block"
```

### Supports modifiers and stacked modifiers

```java
twMerge.merge("p-2 hover:p-4"); // → "p-2 hover:p-4"
twMerge.merge("hover:p-2 hover:p-4"); // → "hover:p-4"
twMerge.merge("hover:focus:p-2 focus:hover:p-4"); // → "focus:hover:p-4"
```

tailwind-merge-java knows when the order of standard modifiers matters and when not and resolves conflicts
accordingly.

### Supports arbitrary values

```java
twMerge.merge("bg-black bg-(--my-color) bg-[color:var(--mystery-var)]"); // → "bg-[color:var(--mystery-var)]"
twMerge.merge("grid-cols-[1fr,auto] grid-cols-2"); // → "grid-cols-2"
```

> **Note — Labels necessary in ambiguous cases**
>
> When using arbitrary values in ambiguous classes like `text-[calc(var(--rebecca)-1rem)]` tailwind-merge-java looks at the arbitrary value for clues to determine what type of class it is. In this case, like in most ambiguous classes, it would try to figure out whether `calc(var(--rebecca)-1rem)` is a length (making it a font-size class) or a color (making it a text-color class). For lengths it takes clues into account like the presence of the `calc()` function or a digit followed by a length unit like `1rem`.
>
> But it isn't always possible to figure out the type by looking at the arbitrary value. E.g. in the class `text-[theme(myCustomScale.rebecca)]` tailwind-merge-java can't know the type of the arbitrary value and will default to a text-color class. To make tailwind-merge-java understand the correct type of the arbitrary value in those cases, you can use CSS data type labels [which are used by Tailwind CSS to disambiguate classes](https://tailwindcss.com/docs/adding-custom-styles#resolving-ambiguities): `text-[length:theme(myCustomScale.rebecca)]`.

### Supports arbitrary properties

```java
twMerge.merge("[mask-type:luminance] [mask-type:alpha]"); // → "[mask-type:alpha]"
twMerge.merge("[--scroll-offset:56px] lg:[--scroll-offset:44px]");
// → "[--scroll-offset:56px] lg:[--scroll-offset:44px]"

// Don't do this!
twMerge.merge("[padding:1rem] p-8"); // → "[padding:1rem] p-8"
```

> **Note**
>
> tailwind-merge-java does not resolve conflicts between arbitrary properties and their matching Tailwind classes.

### Supports arbitrary variants

```java
twMerge.merge("[&:nth-child(3)]:py-0 [&:nth-child(3)]:py-4"); // → "[&:nth-child(3)]:py-4"
twMerge.merge("dark:hover:[&:nth-child(3)]:py-0 hover:dark:[&:nth-child(3)]:py-4");
// → "hover:dark:[&:nth-child(3)]:py-4"

// Don't do this!
twMerge.merge("[&:focus]:ring focus:ring-4"); // → "[&:focus]:ring focus:ring-4"
```

> **Note**
>
> Similarly to arbitrary properties, tailwind-merge-java does not resolve conflicts between arbitrary variants and their matching predefined modifiers for bundle size reasons.

The order of standard modifiers before and after an arbitrary variant in isolation (all modifiers before are one
group, all modifiers after are another group) does not matter for tailwind-merge-java. However, it does matter
whether a standard modifier is before or after an arbitrary variant both for Tailwind CSS and tailwind-merge-java
because the resulting CSS selectors are different.

### Supports important modifier

```java
twMerge.merge("p-3! p-4! p-5"); // → "p-4! p-5"
twMerge.merge("right-2! -inset-x-1!"); // → "-inset-x-1!"
```

### Supports the legacy `bg-opacity-*` classes

Tailwind CSS v3 set the opacity of a background color with a separate class. Tailwind CSS v4 removed it in favor
of the `/<value>` syntax. tailwind-merge-java understands the old classes: they do not remove the background
color, and once the conflicts are resolved the value is moved into the color that survives.

```java
twMerge.merge("bg-red-300 bg-opacity-50"); // → "bg-red-300/50"
twMerge.merge("bg-opacity-50 bg-red-300"); // → "bg-red-300/50" (the order does not matter)
twMerge.merge("bg-red-300 bg-opacity-50 bg-blue-500"); // → "bg-blue-500/50"
twMerge.merge("bg-red-300 bg-opacity-[0.35]"); // → "bg-red-300/[0.35]"
twMerge.merge("hover:bg-red-300 hover:bg-opacity-50"); // → "hover:bg-red-300/50"
```

- The opacity is only applied to the color that has the **same modifiers**: `bg-red-300 hover:bg-opacity-50`
  is left as it is, because the opacity only applies on hover.
- A color that already has its own opacity keeps it, as in Tailwind CSS v3: `bg-red-300/25 bg-opacity-50` →
  `bg-red-300/25`.
- Without a background color, the `bg-opacity-*` class is kept: `bg-opacity-50` → `bg-opacity-50`.
- Only `bg-opacity-*` is supported, not `text-opacity-*`, `border-opacity-*` and the like.
- It is on by default. To get the behavior of the reference library, where `bg-opacity-50` is just another
  background color, call `config.setLegacyBgOpacity(false)`.

### Supports postfix modifiers

```java
twMerge.merge("text-sm leading-6 text-lg/7"); // → "text-lg/7"
```

### Preserves non-Tailwind classes

```java
twMerge.merge("p-5 p-2 my-non-tailwind-class p-4"); // → "my-non-tailwind-class p-4"
```

### Supports custom colors out of the box

```java
twMerge.merge("text-red text-secret-sauce"); // → "text-secret-sauce"
```

## Composition

tailwind-merge-java has some features that simplify composing class strings together.

### Supports multiple arguments

```java
twMerge.merge("some-class", "another-class yet-another-class", "so-many-classes");
// → "some-class another-class yet-another-class so-many-classes"
```

`null` and blank arguments are ignored, so optional classes can be passed directly:

```java
twMerge.merge("px-2", isActive ? "bg-blue-500" : null, extraClasses);
```

When there is nothing to merge, `merge` returns an empty string (never `null`).

## Performance

### Results are cached

Results get cached by default, so you don't need to worry about wasteful re-renders. The library uses a
computationally lightweight [LRU cache](<https://en.wikipedia.org/wiki/Cache_replacement_policies#Least_recently_used_(LRU)>)
which stores up to 500 different results by default (see `TwMergeConfig.setCacheSize`; a size lower than 1 turns
the cache off). The cache is applied after all arguments are joined together to a single string. This means that
if you call `merge` repeatedly with different arguments that result in the same string when joined, the cache
will be hit.

### Thread safety

A `TwMerge` instance is immutable apart from its cache, which is thread-safe, so a single instance can be shared
by all the threads of an application.

### Lazy initialization

`TwMerge.getDefault()` creates the shared default instance the first time it is called, rather than when the
class is loaded.

---

[Back to overview](./README.md)
