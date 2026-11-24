package com.github.ddxailab.tailwindmerge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The conflicting class groups, conflicting class group modifiers, postfix lookup class groups and
 * order-sensitive modifiers of the default configuration.
 */
final class DefaultConflicts {

    private DefaultConflicts() {
    }

    /**
     * Adds the conflicting class groups: the groups a class group overrides when it appears later in a class list.
     *
     * @param groups the map to fill, by class group ID
     */
    static void addConflictingClassGroups(Map<String, List<String>> groups) {
        put(groups, "container-named", "container-type");
        put(groups, "overflow", "overflow-x", "overflow-y");
        put(groups, "overscroll", "overscroll-x", "overscroll-y");
        put(groups, "inset", "inset-x", "inset-y", "inset-bs", "inset-be", "start", "end", "top", "right",
                "bottom", "left");
        put(groups, "inset-x", "start", "end", "right", "left");
        put(groups, "inset-y", "inset-bs", "inset-be", "top", "bottom");
        put(groups, "flex", "basis", "grow", "shrink");
        put(groups, "gap", "gap-x", "gap-y");
        put(groups, "p", "px", "py", "ps", "pe", "pbs", "pbe", "pt", "pr", "pb", "pl");
        put(groups, "px", "ps", "pe", "pr", "pl");
        put(groups, "py", "pbs", "pbe", "pt", "pb");
        put(groups, "m", "mx", "my", "ms", "me", "mbs", "mbe", "mt", "mr", "mb", "ml");
        put(groups, "mx", "ms", "me", "mr", "ml");
        put(groups, "my", "mbs", "mbe", "mt", "mb");
        put(groups, "size", "w", "h");
        put(groups, "font-size", "leading");
        put(groups, "fvn-normal", "fvn-ordinal", "fvn-slashed-zero", "fvn-figure", "fvn-spacing", "fvn-fraction");
        put(groups, "fvn-ordinal", "fvn-normal");
        put(groups, "fvn-slashed-zero", "fvn-normal");
        put(groups, "fvn-figure", "fvn-normal");
        put(groups, "fvn-spacing", "fvn-normal");
        put(groups, "fvn-fraction", "fvn-normal");
        put(groups, "line-clamp", "display", "overflow");
        put(groups, "rounded", "rounded-s", "rounded-e", "rounded-t", "rounded-r", "rounded-b", "rounded-l",
                "rounded-ss", "rounded-se", "rounded-ee", "rounded-es", "rounded-tl", "rounded-tr",
                "rounded-br", "rounded-bl");
        put(groups, "rounded-s", "rounded-ss", "rounded-es");
        put(groups, "rounded-e", "rounded-se", "rounded-ee");
        put(groups, "rounded-t", "rounded-tl", "rounded-tr");
        put(groups, "rounded-r", "rounded-tr", "rounded-br");
        put(groups, "rounded-b", "rounded-br", "rounded-bl");
        put(groups, "rounded-l", "rounded-tl", "rounded-bl");
        put(groups, "border-spacing", "border-spacing-x", "border-spacing-y");
        put(groups, "border-w", "border-w-x", "border-w-y", "border-w-s", "border-w-e", "border-w-bs",
                "border-w-be", "border-w-t", "border-w-r", "border-w-b", "border-w-l");
        put(groups, "border-w-x", "border-w-s", "border-w-e", "border-w-r", "border-w-l");
        put(groups, "border-w-y", "border-w-bs", "border-w-be", "border-w-t", "border-w-b");
        put(groups, "border-color", "border-color-x", "border-color-y", "border-color-s", "border-color-e",
                "border-color-bs", "border-color-be", "border-color-t", "border-color-r", "border-color-b",
                "border-color-l");
        put(groups, "border-color-x", "border-color-s", "border-color-e", "border-color-r", "border-color-l");
        put(groups, "border-color-y", "border-color-bs", "border-color-be", "border-color-t", "border-color-b");
        put(groups, "translate", "translate-x", "translate-y", "translate-none");
        put(groups, "translate-none", "translate", "translate-x", "translate-y", "translate-z");
        put(groups, "scroll-m", "scroll-mx", "scroll-my", "scroll-ms", "scroll-me", "scroll-mbs", "scroll-mbe",
                "scroll-mt", "scroll-mr", "scroll-mb", "scroll-ml");
        put(groups, "scroll-mx", "scroll-ms", "scroll-me", "scroll-mr", "scroll-ml");
        put(groups, "scroll-my", "scroll-mbs", "scroll-mbe", "scroll-mt", "scroll-mb");
        put(groups, "scroll-p", "scroll-px", "scroll-py", "scroll-ps", "scroll-pe", "scroll-pbs", "scroll-pbe",
                "scroll-pt", "scroll-pr", "scroll-pb", "scroll-pl");
        put(groups, "scroll-px", "scroll-ps", "scroll-pe", "scroll-pr", "scroll-pl");
        put(groups, "scroll-py", "scroll-pbs", "scroll-pbe", "scroll-pt", "scroll-pb");
        put(groups, "touch", "touch-x", "touch-y", "touch-pz");
        put(groups, "touch-x", "touch");
        put(groups, "touch-y", "touch");
        put(groups, "touch-pz", "touch");
    }

    /**
     * Adds the conflicting class group modifiers: the groups whose classes are overridden by a postfix modifier
     * (for example {@code text-lg/7} overrides {@code leading-*}).
     *
     * @param modifiers the map to fill, by class group ID
     */
    static void addConflictingClassGroupModifiers(Map<String, List<String>> modifiers) {
        put(modifiers, "font-size", "leading");
    }

    /**
     * Adds the class group IDs which are resolved again with their postfix modifier attached.
     *
     * @param groups the set to fill
     */
    static void addPostfixLookupClassGroups(Set<String> groups) {
        groups.add("container-type");
    }

    /**
     * Adds the modifiers whose order among multiple modifiers is preserved.
     *
     * @param modifiers the set to fill
     */
    static void addOrderSensitiveModifiers(Set<String> modifiers) {
        modifiers.addAll(Arrays.asList(
                "*",
                "**",
                "after",
                "backdrop",
                "before",
                "details-content",
                "file",
                "first-letter",
                "first-line",
                "marker",
                "placeholder",
                "selection"));
    }

    private static void put(Map<String, List<String>> map, String key, String... values) {
        map.put(key, new ArrayList<>(Arrays.asList(values)));
    }
}
