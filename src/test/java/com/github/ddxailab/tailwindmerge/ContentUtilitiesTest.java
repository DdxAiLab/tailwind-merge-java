package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/** Port of {@code ContentUtilitiesTests.cs}; the cases and the expected values are the ones of the reference. */
class ContentUtilitiesTest {

    static Stream<Arguments> mergeContentUtilitiesMergesCorrectlyCases() {
        return Stream.of(
                Arguments.of("content-['hello'] content-[attr(data-content)]", "content-[attr(data-content)]"));
    }

    @ParameterizedTest
    @MethodSource("mergeContentUtilitiesMergesCorrectlyCases")
    void mergeContentUtilitiesMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }
}
