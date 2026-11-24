# What is it for

## Video introduction

[Watch this introduction video from Simon Vrachliotis (@simonswiss) ↓
![The "why" behind tailwind-merge](https://img.youtube.com/vi/tfgLd5ZSNPc/maxresdefault.jpg)](https://www.youtube.com/watch?v=tfgLd5ZSNPc (Watch YouTube video "Tailwind-Merge Is Incredibly Useful — And Here's Why!"))

## Thanks, but I prefer to read

If you use Tailwind CSS with a component-based UI renderer on the JVM (Thymeleaf fragments, JSF composite
components, Vaadin, ZK, or a plain Java helper that builds HTML), you're probably familiar with the situation
that you want to change some styles of a component, but only in a one-off case.

```java
// A reusable input component
static String input(String cssClass) {
    String classes = "border rounded px-2 py-1 " + cssClass;
    return "<input class=\"" + classes + "\">";
}

// A one-off usage: only the padding should change
input("p-3");
```

When the one-off input is rendered, an input with the class `border rounded px-2 py-1 p-3` gets created.
But because of the way the [CSS cascade](https://developer.mozilla.org/en-US/docs/Web/CSS/Cascade) works, the
styles of the `p-3` class are ignored. The order of the classes in the string doesn't matter at all and the only
way to apply the `p-3` styles is to remove both `px-2` and `py-1`.

This is where `TwMerge` comes in.

```java
static String input(String cssClass) {
    // ↓ Now `cssClass` can override conflicting classes
    String classes = TwMerge.getDefault().merge("border rounded px-2 py-1", cssClass);
    return "<input class=\"" + classes + "\">";
}
```

`TwMerge` overrides conflicting classes and keeps everything else untouched. In the case of the one-off input,
the input is now rendered with the classes `border rounded p-3`.

---

[Back to overview](./README.md)
