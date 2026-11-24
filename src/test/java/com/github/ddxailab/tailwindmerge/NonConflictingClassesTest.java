package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class NonConflictingClassesTest {

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "border-t border-white/10 | border-t border-white/10",
        "border-t border-white | border-t border-white",
        "text-3.5xl text-black | text-3.5xl text-black",
    })
    void mergeNonConflictingClassesMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }
}
