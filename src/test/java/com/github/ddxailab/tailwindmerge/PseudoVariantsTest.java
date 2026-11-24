package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PseudoVariantsTest {

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "empty:p-2 empty:p-3 | empty:p-3",
        "hover:empty:p-2 hover:empty:p-3 | hover:empty:p-3",
        "read-only:p-2 read-only:p-3 | read-only:p-3",
    })
    void mergePseudoVariantsMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "group-empty:p-2 group-empty:p-3 | group-empty:p-3",
        "peer-empty:p-2 peer-empty:p-3 | peer-empty:p-3",
        "group-empty:p-2 peer-empty:p-3 | group-empty:p-2 peer-empty:p-3",
        "hover:group-empty:p-2 hover:group-empty:p-3 | hover:group-empty:p-3",
        "group-read-only:p-2 group-read-only:p-3 | group-read-only:p-3",
    })
    void mergePseudoVariantsGroupsMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }
}
