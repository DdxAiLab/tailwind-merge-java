package com.github.ddxailab.tailwindmerge;

import static com.github.ddxailab.tailwindmerge.TestData.list;
import static com.github.ddxailab.tailwindmerge.TestData.map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.ddxailab.tailwindmerge.model.ClassGroup;
import com.github.ddxailab.tailwindmerge.model.ExtendedConfig;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Test;

/** Unit tests of {@link TwMergeConfig#extend} and {@link TwMergeConfig#override} on the config itself. */
class TwMergeConfigExtensionTest {

    @Test
    void emptyExtendedConfigChangesNothing() {
        TwMergeConfig config = new TwMergeConfig();
        ExtendedConfig empty = new ExtendedConfig();

        config.extend(empty).override(empty);

        assertEquals(19, config.getTheme().size());
        assertEquals(378, config.getClassGroups().size());
        assertEquals(49, config.getConflictingClassGroups().size());
        assertEquals(1, config.getConflictingClassGroupModifiers().size());
        assertEquals(1, config.getPostfixLookupClassGroups().size());
        assertEquals(12, config.getOrderSensitiveModifiers().size());
    }

    @Test
    void extendAppendsToExistingThemeScaleAndAddsNewKeys() {
        TwMergeConfig config = new TwMergeConfig();
        int spacingSize = config.getTheme().get("spacing").size();

        ExtendedConfig extension = new ExtendedConfig();
        extension.setTheme(map("spacing", list((Object) "huge"), "brand", list((Object) "a", "b")));
        config.extend(extension);

        assertEquals(spacingSize + 1, config.getTheme().get("spacing").size());
        assertEquals("huge", config.getTheme().get("spacing").get(spacingSize));
        assertEquals(Arrays.<Object>asList("a", "b"), config.getTheme().get("brand"));
    }

    @Test
    void overrideReplacesThemeScale() {
        TwMergeConfig config = new TwMergeConfig();

        ExtendedConfig overrides = new ExtendedConfig();
        overrides.setTheme(map("leading", list((Object) "tight")));
        config.override(overrides);

        assertEquals(Collections.<Object>singletonList("tight"), config.getTheme().get("leading"));
    }

    @Test
    void extendWithSameBaseClassNameMergesDefinitions() {
        TwMergeConfig config = new TwMergeConfig();
        config.getClassGroups().put("custom", ClassGroup.withBase("custom", "100"));

        ExtendedConfig extension = new ExtendedConfig();
        extension.setClassGroups(map("custom", ClassGroup.withBase("custom", "200")));
        config.extend(extension);

        ClassGroup group = config.getClassGroups().get("custom");
        assertEquals("custom", group.getBaseClassName());
        assertEquals(Arrays.<Object>asList("100", "200"), group.getDefinitions());
    }

    @Test
    void extendWithDifferentBaseClassNameNestsBothGroups() {
        TwMergeConfig config = new TwMergeConfig();
        ClassGroup existing = ClassGroup.withBase("custom", "100");
        config.getClassGroups().put("custom", existing);
        ClassGroup added = ClassGroup.withBase("other", "200");

        ExtendedConfig extension = new ExtendedConfig();
        extension.setClassGroups(map("custom", added));
        config.extend(extension);

        ClassGroup group = config.getClassGroups().get("custom");
        assertNull(group.getBaseClassName());
        assertEquals(2, group.getDefinitions().size());
        assertSame(existing, group.getDefinitions().get(0));
        assertSame(added, group.getDefinitions().get(1));
    }

    @Test
    void extendAppendsConflictsAndModifierConflicts() {
        TwMergeConfig config = new TwMergeConfig();
        int before = config.getConflictingClassGroups().get("p").size();

        ExtendedConfig extension = new ExtendedConfig();
        extension.setConflictingClassGroups(map("p", list("foo")));
        extension.setConflictingClassGroupModifiers(map("bar", list("baz")));
        config.extend(extension);

        assertEquals(before + 1, config.getConflictingClassGroups().get("p").size());
        assertTrue(config.getConflictingClassGroups().get("p").contains("foo"));
        assertEquals(Collections.singletonList("baz"), config.getConflictingClassGroupModifiers().get("bar"));
    }

    @Test
    void extendAddsPostfixLookupGroupsAndOrderSensitiveModifiersKeepingTheDefaults() {
        TwMergeConfig config = new TwMergeConfig();

        ExtendedConfig extension = new ExtendedConfig();
        extension.setPostfixLookupClassGroups(list("base"));
        extension.setOrderSensitiveModifiers(list("a"));
        config.extend(extension);

        assertEquals(2, config.getPostfixLookupClassGroups().size());
        assertTrue(config.getPostfixLookupClassGroups().contains("base"));
        assertEquals(13, config.getOrderSensitiveModifiers().size());
        assertTrue(config.getOrderSensitiveModifiers().contains("a"));
        assertTrue(config.getOrderSensitiveModifiers().contains("before"));
    }

    @Test
    void overrideReplacesPostfixLookupGroupsAndOrderSensitiveModifiers() {
        TwMergeConfig config = new TwMergeConfig();

        ExtendedConfig overrides = new ExtendedConfig();
        overrides.setPostfixLookupClassGroups(list("base"));
        overrides.setOrderSensitiveModifiers(list("a"));
        config.override(overrides);

        assertEquals(Collections.singleton("base"), config.getPostfixLookupClassGroups());
        assertEquals(Collections.singleton("a"), config.getOrderSensitiveModifiers());
    }

    @Test
    void overrideWithEmptyListClearsTheSets() {
        TwMergeConfig config = new TwMergeConfig();

        ExtendedConfig overrides = new ExtendedConfig();
        overrides.setPostfixLookupClassGroups(Collections.<String>emptyList());
        overrides.setOrderSensitiveModifiers(Collections.<String>emptyList());
        config.override(overrides);

        assertTrue(config.getPostfixLookupClassGroups().isEmpty());
        assertTrue(config.getOrderSensitiveModifiers().isEmpty());
    }

    @Test
    void extendDoesNotModifyTheExtendedConfig() {
        TwMergeConfig config = new TwMergeConfig();
        ExtendedConfig extension = new ExtendedConfig();
        extension.setConflictingClassGroups(map("p", list("foo")));

        config.extend(extension);
        config.extend(extension);

        assertEquals(Collections.singletonList("foo"), extension.getConflictingClassGroups().get("p"));
    }
}
