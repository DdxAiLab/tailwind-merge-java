package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/** Port of {@code PerSideBorderColorsTests.cs}; the cases and the expected values are the ones of the reference. */
class PerSideBorderColorsTest {

    static Stream<Arguments> mergePerSideBorderColorsMergesCorrectlyCases() {
        return Stream.of(
                Arguments.of("border-t-some-blue border-t-other-blue", "border-t-other-blue"),
                Arguments.of("border-t-some-blue border-some-blue", "border-some-blue"),
                Arguments.of("border-some-blue border-s-some-blue", "border-some-blue border-s-some-blue"),
                Arguments.of("border-e-some-blue border-some-blue", "border-some-blue"));
    }

    @ParameterizedTest
    @MethodSource("mergePerSideBorderColorsMergesCorrectlyCases")
    void mergePerSideBorderColorsMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }
}
