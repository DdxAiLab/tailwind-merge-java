package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/** Port of {@code ColorsClassesTests.cs}; the cases and the expected values are the ones of the reference. */
class ColorsClassesTest {

    static Stream<Arguments> mergeColorsMergesCorrectlyCases() {
        return Stream.of(
                Arguments.of("bg-grey-5 bg-hotpink", "bg-hotpink"),
                Arguments.of("hover:bg-grey-5 hover:bg-hotpink", "hover:bg-hotpink"),
                Arguments.of("stroke-[hsl(350_80%_0%)] stroke-[10px]", "stroke-[hsl(350_80%_0%)] stroke-[10px]"));
    }

    @ParameterizedTest
    @MethodSource("mergeColorsMergesCorrectlyCases")
    void mergeColorsMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    static Stream<Arguments> mergeColorFunctionsWithPercentagesMergesCorrectlyCases() {
        return Stream.of(
                Arguments.of("text-sm text-[color(display-p3_1_0_0/50%)]", "text-sm text-[color(display-p3_1_0_0/50%)]"),
                Arguments.of("text-[color(display-p3_1_0_0/50%)] text-sm", "text-[color(display-p3_1_0_0/50%)] text-sm"),
                Arguments.of("text-red-500 text-[color(display-p3_1_0_0/50%)]", "text-[color(display-p3_1_0_0/50%)]"),
                Arguments.of("text-[color(display-p3_1_0_0/50%)] text-red-500", "text-red-500"),
                Arguments.of("border-2 border-[color(display-p3_1_0_0/50%)]", "border-2 border-[color(display-p3_1_0_0/50%)]"),
                Arguments.of("border-[color(display-p3_1_0_0/50%)] border-2", "border-[color(display-p3_1_0_0/50%)] border-2"),
                Arguments.of("stroke-2 stroke-[color(display-p3_1_0_0/50%)]", "stroke-2 stroke-[color(display-p3_1_0_0/50%)]"),
                Arguments.of("stroke-[color(display-p3_1_0_0/50%)] stroke-2", "stroke-[color(display-p3_1_0_0/50%)] stroke-2"));
    }

    @ParameterizedTest
    @MethodSource("mergeColorFunctionsWithPercentagesMergesCorrectlyCases")
    void mergeColorFunctionsWithPercentagesMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    static Stream<Arguments> mergeLightDarkFunctionsWithPercentagesMergesCorrectlyCases() {
        return Stream.of(
                Arguments.of("text-sm text-[light-dark(white,rgb(0_0_0/50%))]", "text-sm text-[light-dark(white,rgb(0_0_0/50%))]"),
                Arguments.of("text-[light-dark(white,rgb(0_0_0/50%))] text-sm", "text-[light-dark(white,rgb(0_0_0/50%))] text-sm"),
                Arguments.of("text-red-500 text-[light-dark(white,rgb(0_0_0/50%))]", "text-[light-dark(white,rgb(0_0_0/50%))]"),
                Arguments.of("text-[light-dark(white,rgb(0_0_0/50%))] text-red-500", "text-red-500"),
                Arguments.of("border-2 border-[light-dark(white,rgb(0_0_0/50%))]", "border-2 border-[light-dark(white,rgb(0_0_0/50%))]"),
                Arguments.of("border-[light-dark(white,rgb(0_0_0/50%))] border-2", "border-[light-dark(white,rgb(0_0_0/50%))] border-2"),
                Arguments.of("stroke-2 stroke-[light-dark(white,rgb(0_0_0/50%))]", "stroke-2 stroke-[light-dark(white,rgb(0_0_0/50%))]"),
                Arguments.of("stroke-[light-dark(white,rgb(0_0_0/50%))] stroke-2", "stroke-[light-dark(white,rgb(0_0_0/50%))] stroke-2"));
    }

    @ParameterizedTest
    @MethodSource("mergeLightDarkFunctionsWithPercentagesMergesCorrectlyCases")
    void mergeLightDarkFunctionsWithPercentagesMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }
}
