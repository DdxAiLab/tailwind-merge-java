package com.github.ddxailab.tailwindmerge;

import java.util.List;

/**
 * The result of splitting a class name into its modifiers and its base class name.
 */
final class ClassModifiersInfo {

    private final List<String> modifiers;
    private final boolean hasImportantModifier;
    private final String baseClassName;
    private final Integer postfixModifierPosition;
    private final boolean external;

    ClassModifiersInfo(
            List<String> modifiers,
            boolean hasImportantModifier,
            String baseClassName,
            Integer postfixModifierPosition,
            boolean external) {
        this.modifiers = modifiers;
        this.hasImportantModifier = hasImportantModifier;
        this.baseClassName = baseClassName;
        this.postfixModifierPosition = postfixModifierPosition;
        this.external = external;
    }

    ClassModifiersInfo(
            List<String> modifiers,
            boolean hasImportantModifier,
            String baseClassName,
            Integer postfixModifierPosition) {
        this(modifiers, hasImportantModifier, baseClassName, postfixModifierPosition, false);
    }

    List<String> getModifiers() {
        return modifiers;
    }

    boolean hasImportantModifier() {
        return hasImportantModifier;
    }

    String getBaseClassName() {
        return baseClassName;
    }

    /**
     * Gets the position of the postfix modifier ({@code /}) inside the base class name.
     *
     * @return the position, or {@code null} if there is no postfix modifier
     */
    Integer getPostfixModifierPosition() {
        return postfixModifierPosition;
    }

    /**
     * Tells whether the class does not belong to Tailwind (it lacks the configured prefix).
     *
     * @return {@code true} for an external class
     */
    boolean isExternal() {
        return external;
    }
}
