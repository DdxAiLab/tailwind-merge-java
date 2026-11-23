package com.github.ddxailab.tailwindmerge.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ThemeUtilityTest {

    @Test
    void fromThemeReturnsTheScaleOfTheKey() {
        Map<String, List<Object>> theme = new HashMap<>();
        List<Object> scale = Arrays.<Object>asList("hallo", "hello");
        theme.put("my-theme", scale);
        theme.put("other", Arrays.<Object>asList("x"));

        assertSame(scale, ThemeUtility.fromTheme("my-theme").apply(theme));
    }

    @Test
    void fromThemeReturnsAnEmptyListWhenTheKeyIsMissing() {
        Map<String, List<Object>> theme = new HashMap<>();

        assertTrue(ThemeUtility.fromTheme("missing").apply(theme).isEmpty());
    }

    @Test
    void fromThemeReadsTheThemeAtCallTime() {
        Map<String, List<Object>> theme = new HashMap<>();
        ThemeGetter getter = ThemeUtility.fromTheme(ThemeKeys.SPACING);

        assertTrue(getter.apply(theme).isEmpty());

        theme.put(ThemeKeys.SPACING, Arrays.<Object>asList("my-space"));

        assertEquals(Arrays.<Object>asList("my-space"), getter.apply(theme));
    }

    @Test
    void themeKeysUseTheTailwindNamespaceNames() {
        assertEquals("color", ThemeKeys.COLOR);
        assertEquals("font-weight", ThemeKeys.FONT_WEIGHT);
        assertEquals("inset-shadow", ThemeKeys.INSET_SHADOW);
        assertEquals("text-shadow", ThemeKeys.TEXT_SHADOW);
        assertEquals("drop-shadow", ThemeKeys.DROP_SHADOW);
    }
}
