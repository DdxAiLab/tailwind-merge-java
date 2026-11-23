package com.github.ddxailab.tailwindmerge.common;

import java.util.Collections;
import java.util.List;

/**
 * Provides utility methods for working with theme configurations.
 */
public final class ThemeUtility {

    private ThemeUtility() {
    }

    /**
     * Creates a {@link ThemeGetter} that retrieves the values of a theme scale by key.
     *
     * <p>The getter returns an empty list when the theme has no scale for the key.
     *
     * @param key the key to use for retrieving values from the theme
     * @return a theme getter for the key
     */
    public static ThemeGetter fromTheme(String key) {
        return theme -> {
            List<Object> value = theme.get(key);
            return value != null ? value : Collections.<Object>emptyList();
        };
    }
}
