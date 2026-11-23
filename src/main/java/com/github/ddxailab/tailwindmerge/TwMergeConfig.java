package com.github.ddxailab.tailwindmerge;

import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ANY;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ANY_NON_ARBITRARY;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_NUMBER;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_TSHIRT_SIZE;

import com.github.ddxailab.tailwindmerge.common.ThemeKeys;
import com.github.ddxailab.tailwindmerge.model.ClassGroup;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents the configuration settings for the {@link TwMerge}.
 *
 * <p>A new instance holds the default Tailwind CSS configuration.
 */
public class TwMergeConfig {

    private int cacheSize;
    private String prefix;
    private Map<String, List<Object>> theme;
    private Map<String, ClassGroup> classGroups;
    private Map<String, List<String>> conflictingClassGroups;
    private Map<String, List<String>> conflictingClassGroupModifiers;
    private Set<String> postfixLookupClassGroups;
    private Set<String> orderSensitiveModifiers;

    /**
     * Initializes a new instance of the {@link TwMergeConfig} class with the default settings.
     */
    public TwMergeConfig() {
        cacheSize = 500;
        theme = createDefaultTheme();
        classGroups = new LinkedHashMap<>(378);
        LayoutClassGroups.addTo(classGroups);
        FlexboxGridClassGroups.addTo(classGroups);
        conflictingClassGroups = new LinkedHashMap<>();
        conflictingClassGroupModifiers = new LinkedHashMap<>();
        postfixLookupClassGroups = new LinkedHashSet<>();
        orderSensitiveModifiers = new LinkedHashSet<>();
    }

    private static Map<String, List<Object>> createDefaultTheme() {
        Map<String, List<Object>> theme = new LinkedHashMap<>(32);
        theme.put(ThemeKeys.ANIMATE, scale("spin", "ping", "pulse", "bounce"));
        theme.put(ThemeKeys.ASPECT, scale("video"));
        theme.put(ThemeKeys.BLUR, scale(IS_TSHIRT_SIZE));
        theme.put(ThemeKeys.BREAKPOINT, scale(IS_TSHIRT_SIZE));
        theme.put(ThemeKeys.COLOR, scale(IS_ANY));
        theme.put(ThemeKeys.CONTAINER, scale(IS_TSHIRT_SIZE));
        theme.put(ThemeKeys.DROP_SHADOW, scale(IS_TSHIRT_SIZE));
        theme.put(ThemeKeys.EASE, scale("in", "out", "in-out"));
        theme.put(ThemeKeys.FONT, scale(IS_ANY_NON_ARBITRARY));
        theme.put(ThemeKeys.FONT_WEIGHT, scale(
                "thin",
                "extralight",
                "light",
                "normal",
                "medium",
                "semibold",
                "bold",
                "extrabold",
                "black"));
        theme.put(ThemeKeys.INSET_SHADOW, scale(IS_TSHIRT_SIZE));
        theme.put(ThemeKeys.LEADING, scale("none", "tight", "snug", "normal", "relaxed", "loose"));
        theme.put(ThemeKeys.PERSPECTIVE, scale("dramatic", "near", "normal", "midrange", "distant", "none"));
        theme.put(ThemeKeys.RADIUS, scale(IS_TSHIRT_SIZE));
        theme.put(ThemeKeys.SHADOW, scale(IS_TSHIRT_SIZE));
        theme.put(ThemeKeys.SPACING, scale("px", IS_NUMBER));
        theme.put(ThemeKeys.TEXT, scale(IS_TSHIRT_SIZE));
        theme.put(ThemeKeys.TEXT_SHADOW, scale(IS_TSHIRT_SIZE));
        theme.put(ThemeKeys.TRACKING, scale("tighter", "tight", "normal", "wide", "wider", "widest"));
        return theme;
    }

    private static List<Object> scale(Object... values) {
        return new ArrayList<>(Arrays.asList(values));
    }

    /**
     * Gets the maximum size value of the LRU cache used for memoizing results.
     *
     * <p>The default is 500.
     *
     * @return the cache size
     */
    public int getCacheSize() {
        return cacheSize;
    }

    /**
     * Sets the maximum size value of the LRU cache used for memoizing results.
     *
     * @param cacheSize the cache size
     */
    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    /**
     * Gets the value that allows to add a custom prefix to all of Tailwind CSS generated utility classes.
     *
     * @return the prefix, or {@code null} when there is none
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Sets the value that allows to add a custom prefix to all of Tailwind CSS generated utility classes.
     *
     * @param prefix the prefix, or {@code null} for none
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Gets the theme scales of the configuration.
     *
     * @return the theme scales by key
     */
    public Map<String, List<Object>> getTheme() {
        return theme;
    }

    /**
     * Sets the theme scales of the configuration.
     *
     * @param theme the theme scales by key
     */
    public void setTheme(Map<String, List<Object>> theme) {
        this.theme = theme;
    }

    /**
     * Gets the class groups of the configuration.
     *
     * @return the class groups by ID
     */
    public Map<String, ClassGroup> getClassGroups() {
        return classGroups;
    }

    /**
     * Sets the class groups of the configuration.
     *
     * @param classGroups the class groups by ID
     */
    public void setClassGroups(Map<String, ClassGroup> classGroups) {
        this.classGroups = classGroups;
    }

    /**
     * Gets the conflicting class groups of the configuration.
     *
     * @return the IDs of the conflicting class groups by class group ID
     */
    public Map<String, List<String>> getConflictingClassGroups() {
        return conflictingClassGroups;
    }

    /**
     * Sets the conflicting class groups of the configuration.
     *
     * @param conflictingClassGroups the IDs of the conflicting class groups by class group ID
     */
    public void setConflictingClassGroups(Map<String, List<String>> conflictingClassGroups) {
        this.conflictingClassGroups = conflictingClassGroups;
    }

    /**
     * Gets the conflicting class group modifiers of the configuration.
     *
     * @return the IDs of the conflicting class groups by class group ID
     */
    public Map<String, List<String>> getConflictingClassGroupModifiers() {
        return conflictingClassGroupModifiers;
    }

    /**
     * Sets the conflicting class group modifiers of the configuration.
     *
     * @param conflictingClassGroupModifiers the IDs of the conflicting class groups by class group ID
     */
    public void setConflictingClassGroupModifiers(Map<String, List<String>> conflictingClassGroupModifiers) {
        this.conflictingClassGroupModifiers = conflictingClassGroupModifiers;
    }

    /**
     * Gets the class group IDs which should be resolved again with their postfix modifier attached.
     *
     * <p>This is needed when a slash can make the full class name belong to a different
     * class group than the part before the slash.
     *
     * @return the class group IDs
     */
    public Set<String> getPostfixLookupClassGroups() {
        return postfixLookupClassGroups;
    }

    /**
     * Sets the class group IDs which should be resolved again with their postfix modifier attached.
     *
     * @param postfixLookupClassGroups the class group IDs
     */
    public void setPostfixLookupClassGroups(Set<String> postfixLookupClassGroups) {
        this.postfixLookupClassGroups = postfixLookupClassGroups;
    }

    /**
     * Gets the modifiers whose order among multiple modifiers is preserved because their order
     * changes which element gets targeted.
     *
     * @return the modifiers
     */
    public Set<String> getOrderSensitiveModifiers() {
        return orderSensitiveModifiers;
    }

    /**
     * Sets the modifiers whose order among multiple modifiers is preserved.
     *
     * @param orderSensitiveModifiers the modifiers
     */
    public void setOrderSensitiveModifiers(Set<String> orderSensitiveModifiers) {
        this.orderSensitiveModifiers = orderSensitiveModifiers;
    }
}
