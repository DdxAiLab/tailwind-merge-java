package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PrefixTest {

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "tw:block tw:hidden | tw:hidden",
        "block hidden | block hidden",
        "tw:p-3 tw:p-2 | tw:p-2",
        "p-3 p-2 | p-3 p-2",
        "tw:right-0! tw:inset-0! | tw:inset-0!",
        "tw:hover:focus:right-0! tw:focus:hover:inset-0! | tw:focus:hover:inset-0!",
    })
    void shouldMergeCorrectlyWithPrefix(String classLists, String expected) {
        TwMergeConfig config = new TwMergeConfig();
        config.setPrefix("tw");

        assertEquals(expected, new TwMerge(config).merge(classLists));
    }
}
