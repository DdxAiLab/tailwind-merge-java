package com.github.ddxailab.tailwindmerge.common;

import java.util.List;
import java.util.Map;

/**
 * Retrieves a list of values from a theme.
 *
 * <p>It is a dedicated interface (and not a {@code java.util.function.Function}) so that a theme
 * getter can be told apart from a validator when it is used as a class group definition.
 */
@FunctionalInterface
public interface ThemeGetter {

    /**
     * Retrieves the values of a theme scale.
     *
     * @param theme the theme to retrieve values from
     * @return the values associated with the key of this getter in the theme
     */
    List<Object> apply(Map<String, List<Object>> theme);
}
