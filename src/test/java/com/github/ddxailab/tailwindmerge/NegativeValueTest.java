package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/** Port of {@code NegativeValueTests.cs}; the cases and the expected values are the ones of the reference. */
class NegativeValueTest {

    static Stream<Arguments> mergeNegativeValuesMergesCorrectlyCases() {
        return Stream.of(
                Arguments.of("-m-2 -m-5", "-m-5"),
                Arguments.of("-top-12 -top-2000", "-top-2000"));
    }

    @ParameterizedTest
    @MethodSource("mergeNegativeValuesMergesCorrectlyCases")
    void mergeNegativeValuesMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    static Stream<Arguments> mergeNegativeAndPositiveValuesMergesCorrectlyCases() {
        return Stream.of(
                Arguments.of("-m-2 m-auto", "m-auto"),
                Arguments.of("top-12 -top-69", "-top-69"));
    }

    @ParameterizedTest
    @MethodSource("mergeNegativeAndPositiveValuesMergesCorrectlyCases")
    void mergeNegativeAndPositiveValuesMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    static Stream<Arguments> mergeNegativeValuesAcrossGroupsMergesCorrectlyCases() {
        return Stream.of(
                Arguments.of("-right-1 inset-x-1", "inset-x-1"),
                Arguments.of("hover:focus:-right-1 focus:hover:inset-x-1", "focus:hover:inset-x-1"));
    }

    @ParameterizedTest
    @MethodSource("mergeNegativeValuesAcrossGroupsMergesCorrectlyCases")
    void mergeNegativeValuesAcrossGroupsMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }
}
