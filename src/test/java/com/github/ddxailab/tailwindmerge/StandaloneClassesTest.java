package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class StandaloneClassesTest {

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "inline block | block",
        "underline line-through | line-through",
        "line-through no-underline | no-underline",
        "hover:block hover:inline | hover:inline",
        "hover:inline hover:block | hover:block",
        "hover:block hover:block | hover:block",
        "inline hover:inline focus:inline hover:block hover:focus:block | inline focus:inline hover:block hover:focus:block",
    })
    void mergeStandaloneClassesFromSameGroupMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }
}
