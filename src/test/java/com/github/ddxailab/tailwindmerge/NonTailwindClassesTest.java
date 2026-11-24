package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class NonTailwindClassesTest {

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "non-tailwind-class inline block | non-tailwind-class block",
        "inline block inline-1 | block inline-1",
        "inline block i-inline | block i-inline",
        "focus:inline focus:block focus:inline-1 | focus:block focus:inline-1",
    })
    void mergeNonTailwindClassesMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }
}
