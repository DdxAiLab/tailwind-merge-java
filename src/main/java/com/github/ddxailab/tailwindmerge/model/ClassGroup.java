package com.github.ddxailab.tailwindmerge.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a group of CSS classes with associated metadata for the TailwindMerge utility.
 *
 * <p>Each definition is one of:</p>
 * <ul>
 *   <li>a {@link String}: a class name part (an empty string matches the base class name itself);</li>
 *   <li>a {@link java.util.function.Predicate Predicate&lt;String&gt;}: a validator of the class name part;</li>
 *   <li>a theme getter: resolved against the theme when the class map is created;</li>
 *   <li>a nested {@link ClassGroup}.</li>
 * </ul>
 */
public final class ClassGroup {

    private final String baseClassName;
    private final List<Object> definitions;

    private ClassGroup(String baseClassName, Object[] definitions) {
        Objects.requireNonNull(definitions, "definitions");
        this.baseClassName = baseClassName;
        this.definitions = Collections.unmodifiableList(new ArrayList<>(Arrays.asList(definitions)));
    }

    /**
     * Creates a class group without a base class name (a standalone group).
     *
     * @param definitions the definitions associated with the class group
     * @return the class group
     */
    public static ClassGroup of(Object... definitions) {
        return new ClassGroup(null, definitions);
    }

    /**
     * Creates a class group whose definitions are nested under a base class name.
     *
     * @param baseClassName the base class name for the group
     * @param definitions   the definitions associated with the class group
     * @return the class group
     */
    public static ClassGroup withBase(String baseClassName, Object... definitions) {
        return new ClassGroup(baseClassName, definitions);
    }

    /**
     * Gets the base class name for the group.
     *
     * @return the base class name, or {@code null} for a standalone group
     */
    public String getBaseClassName() {
        return baseClassName;
    }

    /**
     * Gets the definitions associated with the class group.
     *
     * @return an unmodifiable list of definitions
     */
    public List<Object> getDefinitions() {
        return definitions;
    }
}
