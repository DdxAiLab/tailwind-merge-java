package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class WonkyInputsTest {

    static Stream<Arguments> wonkyInputs() {
        return Stream.of(
                Arguments.of(" block", "block"),
                Arguments.of("block ", "block"),
                Arguments.of(" block ", "block"),
                Arguments.of("  block  px-2     py-4  ", "block px-2 py-4"),
                Arguments.of("block\npx-2", "block px-2"),
                Arguments.of("\nblock\npx-2\n", "block px-2"),
                Arguments.of("  block\n        \n        px-2   \n          py-4  ", "block px-2 py-4"),
                Arguments.of("\r  block\n\r        \n        px-2   \n          py-4  ", "block px-2 py-4"));
    }

    @ParameterizedTest
    @MethodSource("wonkyInputs")
    void mergeWonkyInputsMergesCorrectly(String classList, String expected) {
        assertEquals(expected, new TwMerge().merge(classList));
    }

    @Test
    void mergeWonkyInputsInMultipleArgumentsMergesCorrectly() {
        assertEquals("block px-2 py-4", new TwMerge().merge("  block  px-2", " ", "     py-4  "));
    }
}
