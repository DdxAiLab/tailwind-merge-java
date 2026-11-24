package com.github.ddxailab.tailwindmerge;

import static com.github.ddxailab.tailwindmerge.TestData.list;
import static com.github.ddxailab.tailwindmerge.TestData.map;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.ddxailab.tailwindmerge.common.ThemeKeys;
import com.github.ddxailab.tailwindmerge.common.ThemeUtility;
import com.github.ddxailab.tailwindmerge.model.ClassGroup;
import com.github.ddxailab.tailwindmerge.model.ExtendedConfig;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ThemeTest {

    @Test
    void themeKeysMatchDefaultThemeKeys() throws IllegalAccessException {
        // Arrange
        List<String> themeKeys = new ArrayList<>();
        for (Field field : ThemeKeys.class.getFields()) {
            if (Modifier.isStatic(field.getModifiers()) && field.getType() == String.class) {
                themeKeys.add((String) field.get(null));
            }
        }

        // Act
        List<String> defaultThemeKeys = new ArrayList<>(new TwMergeConfig().getTheme().keySet());

        // Assert
        Collections.sort(themeKeys);
        Collections.sort(defaultThemeKeys);
        assertEquals(defaultThemeKeys, themeKeys);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "p-3 p-my-space p-my-margin | p-my-space p-my-margin",
        "leading-3 leading-my-space leading-my-leading | leading-my-leading",
    })
    void mergeWithExtendedThemeScaleShouldMergeCorrectly(String classLists, String expected) {
        TwMergeConfig config = new TwMergeConfig();

        ExtendedConfig extension = new ExtendedConfig();
        extension.setTheme(map(
                "spacing", list((Object) "my-space"),
                "leading", list((Object) "my-leading")));
        config.extend(extension);

        assertEquals(expected, new TwMerge(config).merge(classLists));
    }

    // `leading-none` is a static Tailwind v4 utility (`line-height: 1`) that does not come from the
    // `--leading-*` theme namespace, so overriding the `leading` theme must not stop it from
    // conflicting with other line-height utilities.
    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "leading-tight leading-none | leading-none",
        "leading-none leading-tight | leading-tight",
        "leading-4 leading-none | leading-none",
    })
    void mergeWithOverriddenLeadingThemeScaleShouldKeepLeadingNone(String classLists, String expected) {
        TwMergeConfig config = new TwMergeConfig();

        ExtendedConfig overrides = new ExtendedConfig();
        overrides.setTheme(map("leading", list((Object) "tight")));
        config.override(overrides);

        assertEquals(expected, new TwMerge(config).merge(classLists));
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "p-3 p-hello p-hallo | p-3 p-hello p-hallo",
        "px-3 px-hello px-hallo | px-hallo",
    })
    void mergeWithExtendedThemeObjectShouldMergeCorrectly(String classLists, String expected) {
        TwMergeConfig config = new TwMergeConfig();

        ExtendedConfig extension = new ExtendedConfig();
        extension.setTheme(map("my-theme", list((Object) "hallo", "hello")));
        extension.setClassGroups(map("px", ClassGroup.withBase("px", ThemeUtility.fromTheme("my-theme"))));
        config.extend(extension);

        assertEquals(expected, new TwMerge(config).merge(classLists));
    }
}
