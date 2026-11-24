package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/** Port of {@code ArbitraryPropertiesTests.cs}; the cases and the expected values are the ones of the reference. */
class ArbitraryPropertiesTest {

    static Stream<Arguments> mergeArbitraryPropertiesMergesCorrectlyCases() {
        return Stream.of(
                Arguments.of("[paint-order:markers] [paint-order:normal]", "[paint-order:normal]"),
                Arguments.of("[paint-order:markers] [--my-var:2rem] [paint-order:normal] [--my-var:4px]", "[paint-order:normal] [--my-var:4px]"));
    }

    @ParameterizedTest
    @MethodSource("mergeArbitraryPropertiesMergesCorrectlyCases")
    void mergeArbitraryPropertiesMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    static Stream<Arguments> mergeArbitraryPropertiesWithModifiersMergesCorrectlyCases() {
        return Stream.of(
                Arguments.of("[paint-order:markers] hover:[paint-order:normal]", "[paint-order:markers] hover:[paint-order:normal]"),
                Arguments.of("hover:[paint-order:markers] hover:[paint-order:normal]", "hover:[paint-order:normal]"),
                Arguments.of("hover:focus:[paint-order:markers] focus:hover:[paint-order:normal]", "focus:hover:[paint-order:normal]"),
                Arguments.of("[paint-order:markers] [paint-order:normal] [--my-var:2rem] lg:[--my-var:4px]", "[paint-order:normal] [--my-var:2rem] lg:[--my-var:4px]"),
                Arguments.of("bg-[#B91C1C] bg-radial-[at_50%_75%] bg-radial-[at_25%_25%]", "bg-[#B91C1C] bg-radial-[at_25%_25%]"));
    }

    @ParameterizedTest
    @MethodSource("mergeArbitraryPropertiesWithModifiersMergesCorrectlyCases")
    void mergeArbitraryPropertiesWithModifiersMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    static Stream<Arguments> mergeComplexArbitraryPropertiesMergesCorrectlyCases() {
        return Stream.of(
                Arguments.of("[-unknown-prop:::123:::] [-unknown-prop:url(https://hi.com)]", "[-unknown-prop:url(https://hi.com)]"));
    }

    @ParameterizedTest
    @MethodSource("mergeComplexArbitraryPropertiesMergesCorrectlyCases")
    void mergeComplexArbitraryPropertiesMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    static Stream<Arguments> mergeArbitraryPropertiesWithImportantModifierMergesCorrectlyCases() {
        return Stream.of(
                Arguments.of("![some:prop] [some:other]", "![some:prop] [some:other]"),
                Arguments.of("![some:prop] [some:other] [some:one] ![some:another]", "[some:one] ![some:another]"));
    }

    @ParameterizedTest
    @MethodSource("mergeArbitraryPropertiesWithImportantModifierMergesCorrectlyCases")
    void mergeArbitraryPropertiesWithImportantModifierMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }
}
