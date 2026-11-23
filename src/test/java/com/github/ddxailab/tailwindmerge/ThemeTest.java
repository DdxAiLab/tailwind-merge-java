package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.ddxailab.tailwindmerge.common.ThemeKeys;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

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
}
