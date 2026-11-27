package com.github.ddxailab.tailwindmerge.common;

import java.util.Optional;
import java.util.regex.Matcher;

/**
 * An arbitrary value ({@code [10px]}) or arbitrary variable ({@code (--my-width)}) of a class name,
 * split into its label and its value.
 *
 * <p>It is recognized with the same rules as {@link Validators#IS_ARBITRARY_VALUE} and
 * {@link Validators#IS_ARBITRARY_VARIABLE}. Instances are immutable.
 */
public final class ArbitraryValue {

    /** How an arbitrary value is written. */
    public enum Syntax {
        /** Between square brackets: {@code [10px]}. */
        BRACKETS,
        /** Between parentheses, the shorthand for CSS variables: {@code (--my-width)}. */
        PARENTHESES
    }

    private final Syntax syntax;
    private final String label;
    private final String value;

    private ArbitraryValue(Syntax syntax, String label, String value) {
        this.syntax = syntax;
        this.label = label;
        this.value = value;
    }

    /**
     * Reads an arbitrary value or variable, delimiters included.
     *
     * @param text the part of a class name to read, e.g. {@code [length:var(--x)]} or {@code (--x)}
     * @return the arbitrary value, or an empty optional if the text is {@code null} or is not enclosed
     *         in brackets or parentheses with something inside
     */
    public static Optional<ArbitraryValue> from(String text) {
        if (text == null) {
            return Optional.empty();
        }

        Syntax syntax = Syntax.BRACKETS;
        Matcher match = Validators.ARBITRARY_VALUE.matcher(text);
        if (!match.find()) {
            syntax = Syntax.PARENTHESES;
            match = Validators.ARBITRARY_VARIABLE.matcher(text);
            if (!match.find()) {
                return Optional.empty();
            }
        }

        String label = match.group(1);
        return Optional.of(new ArbitraryValue(syntax, label == null ? "" : label, match.group(2)));
    }

    /**
     * Gets how the value is written.
     *
     * @return the syntax
     */
    public Syntax getSyntax() {
        return syntax;
    }

    /**
     * Gets the label (data type hint) of the value, e.g. {@code length} in {@code [length:var(--x)]}.
     *
     * @return the label without the colon, or an empty string if there is none
     */
    public String getLabel() {
        return label;
    }

    /**
     * Gets the value without its delimiters and label, exactly as written (underscores are not turned
     * into spaces).
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }
}
