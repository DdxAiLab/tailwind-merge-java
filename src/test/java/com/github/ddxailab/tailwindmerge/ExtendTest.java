package com.github.ddxailab.tailwindmerge;

import static com.github.ddxailab.tailwindmerge.TestData.list;
import static com.github.ddxailab.tailwindmerge.TestData.map;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.ddxailab.tailwindmerge.model.ClassGroup;
import com.github.ddxailab.tailwindmerge.model.ExtendedConfig;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ExtendTest {

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "shadow-lg shadow-100 shadow-200 | shadow-lg shadow-200",
        "custom-100 custom-200 | custom-200",
        "text-lg text-foo | text-foo",
        "px-3 py-3 p-3 | py-3 p-3",
        "p-3 px-3 py-3 | p-3 px-3 py-3",
        "mx-2 my-2 h-2 m-2 | m-2",
        "m-2 mx-2 my-2 h-2 | m-2 mx-2 my-2 h-2",
    })
    void shouldOverrideAndExtendConfigCorrectly(String classLists, String expected) {
        TwMergeConfig config = new TwMergeConfig();

        ExtendedConfig overrides = new ExtendedConfig();
        overrides.setClassGroups(map(
                "shadow", ClassGroup.withBase("shadow", "100", "200"),
                "customKey", ClassGroup.withBase("custom", "100")));
        overrides.setConflictingClassGroups(map("p", list("px")));
        config.override(overrides);

        ExtendedConfig extension = new ExtendedConfig();
        extension.setClassGroups(map(
                "shadow", ClassGroup.withBase("shadow", "300"),
                "customKey", ClassGroup.withBase("custom", "200"),
                "font-size", ClassGroup.withBase("text", "foo")));
        extension.setConflictingClassGroups(map("m", list("h")));
        config.extend(extension);

        assertEquals(expected, new TwMerge(config).merge(classLists));
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "elevated shadow-lg | shadow-lg",
        "shadow-lg elevated | elevated",
        "shadow-sm shadow-lg | shadow-lg",
        "sm lg | sm lg",
    })
    void extendClassGroupWithDifferentBaseClassNameKeepsExistingDefinitions(String classLists, String expected) {
        TwMergeConfig config = new TwMergeConfig();

        ExtendedConfig extension = new ExtendedConfig();
        extension.setClassGroups(map("shadow", ClassGroup.of("elevated")));
        config.extend(extension);

        assertEquals(expected, new TwMerge(config).merge(classLists));
    }
}
