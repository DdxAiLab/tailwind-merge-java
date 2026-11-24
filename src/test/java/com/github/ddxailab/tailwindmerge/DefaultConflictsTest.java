package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class DefaultConflictsTest {

    private final TwMergeConfig config = new TwMergeConfig();

    @Test
    void defaultConfigHasTheReferenceNumberOfConflictingClassGroups() {
        assertEquals(49, config.getConflictingClassGroups().size());
    }

    @Test
    void conflictingClassGroupsKeepTheReferenceOrder() {
        List<String> keys = new ArrayList<>(config.getConflictingClassGroups().keySet());

        assertEquals(Arrays.asList("container-named", "overflow", "overscroll", "inset", "inset-x", "inset-y"),
                keys.subList(0, 6));
        assertEquals(Arrays.asList("scroll-py", "touch", "touch-x", "touch-y", "touch-pz"),
                keys.subList(keys.size() - 5, keys.size()));
    }

    @Test
    void everyReferencedClassGroupExists() {
        Map<String, List<String>> conflicts = config.getConflictingClassGroups();

        for (Map.Entry<String, List<String>> entry : conflicts.entrySet()) {
            assertTrue(config.getClassGroups().containsKey(entry.getKey()), "unknown key " + entry.getKey());
            for (String value : entry.getValue()) {
                assertTrue(config.getClassGroups().containsKey(value),
                        entry.getKey() + " conflicts with unknown class group " + value);
            }
        }
        for (Map.Entry<String, List<String>> entry : config.getConflictingClassGroupModifiers().entrySet()) {
            assertTrue(config.getClassGroups().containsKey(entry.getKey()));
            assertTrue(config.getClassGroups().keySet().containsAll(entry.getValue()));
        }
        assertTrue(config.getClassGroups().keySet().containsAll(config.getPostfixLookupClassGroups()));
    }

    @Test
    void aGroupNeverConflictsWithItself() {
        for (Map.Entry<String, List<String>> entry : config.getConflictingClassGroups().entrySet()) {
            assertFalse(entry.getValue().contains(entry.getKey()), entry.getKey());
        }
    }

    @Test
    void simpleConflicts() {
        Map<String, List<String>> conflicts = config.getConflictingClassGroups();

        assertEquals(Arrays.asList("container-type"), conflicts.get("container-named"));
        assertEquals(Arrays.asList("overflow-x", "overflow-y"), conflicts.get("overflow"));
        assertEquals(Arrays.asList("overscroll-x", "overscroll-y"), conflicts.get("overscroll"));
        assertEquals(Arrays.asList("basis", "grow", "shrink"), conflicts.get("flex"));
        assertEquals(Arrays.asList("gap-x", "gap-y"), conflicts.get("gap"));
        assertEquals(Arrays.asList("w", "h"), conflicts.get("size"));
        assertEquals(Arrays.asList("leading"), conflicts.get("font-size"));
        assertEquals(Arrays.asList("display", "overflow"), conflicts.get("line-clamp"));
        assertEquals(Arrays.asList("border-spacing-x", "border-spacing-y"), conflicts.get("border-spacing"));
    }

    @Test
    void insetPaddingAndMarginConflicts() {
        Map<String, List<String>> conflicts = config.getConflictingClassGroups();

        assertEquals(Arrays.asList("inset-x", "inset-y", "inset-bs", "inset-be", "start", "end", "top", "right",
                "bottom", "left"), conflicts.get("inset"));
        assertEquals(Arrays.asList("start", "end", "right", "left"), conflicts.get("inset-x"));
        assertEquals(Arrays.asList("inset-bs", "inset-be", "top", "bottom"), conflicts.get("inset-y"));
        assertEquals(Arrays.asList("px", "py", "ps", "pe", "pbs", "pbe", "pt", "pr", "pb", "pl"), conflicts.get("p"));
        assertEquals(Arrays.asList("ps", "pe", "pr", "pl"), conflicts.get("px"));
        assertEquals(Arrays.asList("pbs", "pbe", "pt", "pb"), conflicts.get("py"));
        assertEquals(Arrays.asList("mx", "my", "ms", "me", "mbs", "mbe", "mt", "mr", "mb", "ml"), conflicts.get("m"));
        assertEquals(Arrays.asList("ms", "me", "mr", "ml"), conflicts.get("mx"));
        assertEquals(Arrays.asList("mbs", "mbe", "mt", "mb"), conflicts.get("my"));
    }

    @Test
    void fontVariantNumericConflicts() {
        Map<String, List<String>> conflicts = config.getConflictingClassGroups();

        assertEquals(Arrays.asList("fvn-ordinal", "fvn-slashed-zero", "fvn-figure", "fvn-spacing", "fvn-fraction"),
                conflicts.get("fvn-normal"));
        for (String id : Arrays.asList("fvn-ordinal", "fvn-slashed-zero", "fvn-figure", "fvn-spacing",
                "fvn-fraction")) {
            assertEquals(Arrays.asList("fvn-normal"), conflicts.get(id), id);
        }
    }

    @Test
    void roundedConflicts() {
        Map<String, List<String>> conflicts = config.getConflictingClassGroups();

        assertEquals(14, conflicts.get("rounded").size());
        assertEquals(Arrays.asList("rounded-ss", "rounded-es"), conflicts.get("rounded-s"));
        assertEquals(Arrays.asList("rounded-se", "rounded-ee"), conflicts.get("rounded-e"));
        assertEquals(Arrays.asList("rounded-tl", "rounded-tr"), conflicts.get("rounded-t"));
        assertEquals(Arrays.asList("rounded-tr", "rounded-br"), conflicts.get("rounded-r"));
        assertEquals(Arrays.asList("rounded-br", "rounded-bl"), conflicts.get("rounded-b"));
        assertEquals(Arrays.asList("rounded-tl", "rounded-bl"), conflicts.get("rounded-l"));
    }

    @Test
    void borderConflicts() {
        Map<String, List<String>> conflicts = config.getConflictingClassGroups();

        assertEquals(Arrays.asList("border-w-x", "border-w-y", "border-w-s", "border-w-e", "border-w-bs",
                "border-w-be", "border-w-t", "border-w-r", "border-w-b", "border-w-l"), conflicts.get("border-w"));
        assertEquals(Arrays.asList("border-w-s", "border-w-e", "border-w-r", "border-w-l"),
                conflicts.get("border-w-x"));
        assertEquals(Arrays.asList("border-w-bs", "border-w-be", "border-w-t", "border-w-b"),
                conflicts.get("border-w-y"));
        assertEquals(10, conflicts.get("border-color").size());
        assertEquals(Arrays.asList("border-color-s", "border-color-e", "border-color-r", "border-color-l"),
                conflicts.get("border-color-x"));
        assertEquals(Arrays.asList("border-color-bs", "border-color-be", "border-color-t", "border-color-b"),
                conflicts.get("border-color-y"));
    }

    @Test
    void translateScrollAndTouchConflicts() {
        Map<String, List<String>> conflicts = config.getConflictingClassGroups();

        assertEquals(Arrays.asList("translate-x", "translate-y", "translate-none"), conflicts.get("translate"));
        assertEquals(Arrays.asList("translate", "translate-x", "translate-y", "translate-z"),
                conflicts.get("translate-none"));
        assertEquals(10, conflicts.get("scroll-m").size());
        assertEquals(Arrays.asList("scroll-ms", "scroll-me", "scroll-mr", "scroll-ml"), conflicts.get("scroll-mx"));
        assertEquals(Arrays.asList("scroll-mbs", "scroll-mbe", "scroll-mt", "scroll-mb"), conflicts.get("scroll-my"));
        assertEquals(10, conflicts.get("scroll-p").size());
        assertEquals(Arrays.asList("scroll-ps", "scroll-pe", "scroll-pr", "scroll-pl"), conflicts.get("scroll-px"));
        assertEquals(Arrays.asList("scroll-pbs", "scroll-pbe", "scroll-pt", "scroll-pb"), conflicts.get("scroll-py"));
        assertEquals(Arrays.asList("touch-x", "touch-y", "touch-pz"), conflicts.get("touch"));
        for (String id : Arrays.asList("touch-x", "touch-y", "touch-pz")) {
            assertEquals(Arrays.asList("touch"), conflicts.get(id), id);
        }
    }

    @Test
    void conflictingClassGroupModifiers() {
        Map<String, List<String>> modifiers = config.getConflictingClassGroupModifiers();

        assertEquals(1, modifiers.size());
        assertEquals(Arrays.asList("leading"), modifiers.get("font-size"));
    }

    @Test
    void postfixLookupClassGroups() {
        assertEquals(Collections.singleton("container-type"), config.getPostfixLookupClassGroups());
    }

    @Test
    void orderSensitiveModifiers() {
        assertEquals(Arrays.asList("*", "**", "after", "backdrop", "before", "details-content", "file",
                "first-letter", "first-line", "marker", "placeholder", "selection"),
                new ArrayList<>(config.getOrderSensitiveModifiers()));
    }

    @Test
    void everyConfigGetsItsOwnCopyOfTheConflicts() {
        TwMergeConfig other = new TwMergeConfig();

        config.getConflictingClassGroups().get("overflow").add("changed");
        config.getPostfixLookupClassGroups().add("changed");

        assertEquals(Arrays.asList("overflow-x", "overflow-y"), other.getConflictingClassGroups().get("overflow"));
        assertFalse(other.getPostfixLookupClassGroups().contains("changed"));
    }
}
