package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/** Port of {@code ClassGroupsTests.cs}; the cases and the expected values are the ones of the reference. */
class ClassGroupsTest {

    static Stream<Arguments> mergeClassGroupsMergesCorrectlyCases() {
        return Stream.of(
                Arguments.of("inset-1 inset-x-1", "inset-1 inset-x-1"),
                Arguments.of("inset-x-1 inset-1", "inset-1"),
                Arguments.of("inset-x-1 left-1 inset-1", "inset-1"),
                Arguments.of("inset-x-1 inset-1 left-1", "inset-1 left-1"),
                Arguments.of("inset-x-1 right-1 inset-1", "inset-1"),
                Arguments.of("inset-x-1 right-1 inset-x-1", "inset-x-1"),
                Arguments.of("inset-x-1 right-1 inset-y-1", "inset-x-1 right-1 inset-y-1"),
                Arguments.of("right-1 inset-x-1 inset-y-1", "inset-x-1 inset-y-1"),
                Arguments.of("inset-x-1 hover:left-1 inset-1", "hover:left-1 inset-1"));
    }

    @ParameterizedTest
    @MethodSource("mergeClassGroupsMergesCorrectlyCases")
    void mergeClassGroupsMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    static Stream<Arguments> mergeAxisShorthandsOverrideLogicalSidesMergesCorrectlyCases() {
        return Stream.of(
                // Since Tailwind CSS v4 the axis utilities compile to logical shorthand properties (px → padding-inline),
                // which fully override their logical-side longhands (ps → padding-inline-start) in every writing mode.
                Arguments.of("ps-2 px-4", "px-4"),
                Arguments.of("pe-2 px-4", "px-4"),
                Arguments.of("px-4 ps-2", "px-4 ps-2"),
                Arguments.of("pbs-2 py-4", "py-4"),
                Arguments.of("ms-2 mx-4", "mx-4"),
                Arguments.of("mbe-2 my-4", "my-4"),
                Arguments.of("start-2 inset-x-4", "inset-x-4"),
                Arguments.of("end-2 inset-x-4", "inset-x-4"),
                Arguments.of("inset-bs-2 inset-y-4", "inset-y-4"),
                Arguments.of("border-s-2 border-x-4", "border-x-4"),
                Arguments.of("border-be-2 border-y-4", "border-y-4"),
                Arguments.of("border-s-red-500 border-x-blue-500", "border-x-blue-500"),
                Arguments.of("border-bs-red-500 border-y-blue-500", "border-y-blue-500"),
                Arguments.of("scroll-ms-2 scroll-mx-4", "scroll-mx-4"),
                Arguments.of("scroll-mbs-2 scroll-my-4", "scroll-my-4"),
                Arguments.of("scroll-ps-2 scroll-px-4", "scroll-px-4"),
                Arguments.of("scroll-pbe-2 scroll-py-4", "scroll-py-4"));
    }

    @ParameterizedTest
    @MethodSource("mergeAxisShorthandsOverrideLogicalSidesMergesCorrectlyCases")
    void mergeAxisShorthandsOverrideLogicalSidesMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    static Stream<Arguments> mergeSameClassGroupsMergesCorrectlyCases() {
        return Stream.of(
                Arguments.of("overflow-x-auto overflow-x-hidden", "overflow-x-hidden"),
                Arguments.of("basis-full basis-auto", "basis-auto"),
                Arguments.of("w-full w-fit", "w-fit"),
                Arguments.of("overflow-x-auto overflow-x-hidden overflow-x-scroll", "overflow-x-scroll"),
                Arguments.of("overflow-x-auto hover:overflow-x-hidden overflow-x-scroll", "hover:overflow-x-hidden overflow-x-scroll"),
                Arguments.of("overflow-x-auto hover:overflow-x-hidden hover:overflow-x-auto overflow-x-scroll", "hover:overflow-x-auto overflow-x-scroll"),
                Arguments.of("col-span-1 col-span-full", "col-span-full"),
                Arguments.of("columns-12 columns-auto", "columns-auto"),
                Arguments.of("columns-auto columns-2xl", "columns-2xl"),
                Arguments.of("gap-2 gap-px basis-px basis-3", "gap-px basis-3"));
    }

    @ParameterizedTest
    @MethodSource("mergeSameClassGroupsMergesCorrectlyCases")
    void mergeSameClassGroupsMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    static Stream<Arguments> mergeNoneValuesInSizingGroupsMergesCorrectlyCases() {
        return Stream.of(
                Arguments.of("max-w-lg max-w-none", "max-w-none"),
                Arguments.of("max-w-none max-w-lg", "max-w-lg"),
                Arguments.of("max-h-96 max-h-none", "max-h-none"),
                Arguments.of("max-h-none max-h-96", "max-h-96"),
                Arguments.of("max-h-[300px] max-h-none", "max-h-none"),
                Arguments.of("max-h-none max-h-screen", "max-h-screen"));
    }

    @ParameterizedTest
    @MethodSource("mergeNoneValuesInSizingGroupsMergesCorrectlyCases")
    void mergeNoneValuesInSizingGroupsMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    static Stream<Arguments> mergeRingAndShadowMergesCorrectlyCases() {
        return Stream.of(
                Arguments.of("ring shadow", "ring shadow"),
                Arguments.of("ring-2 shadow-md", "ring-2 shadow-md"),
                Arguments.of("shadow ring", "shadow ring"),
                Arguments.of("shadow-md ring-2", "shadow-md ring-2"));
    }

    @ParameterizedTest
    @MethodSource("mergeRingAndShadowMergesCorrectlyCases")
    void mergeRingAndShadowMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    static Stream<Arguments> mergeTouchClassesMergesCorrectlyCases() {
        return Stream.of(
                Arguments.of("touch-pan-x touch-pan-right", "touch-pan-right"),
                Arguments.of("touch-none touch-pan-x", "touch-pan-x"),
                Arguments.of("touch-pan-x touch-none", "touch-none"),
                Arguments.of("touch-pan-x touch-pan-y touch-pinch-zoom", "touch-pan-x touch-pan-y touch-pinch-zoom"),
                Arguments.of("touch-manipulation touch-pan-x touch-pan-y touch-pinch-zoom", "touch-pan-x touch-pan-y touch-pinch-zoom"),
                Arguments.of("touch-pan-x touch-pan-y touch-pinch-zoom touch-auto", "touch-auto"));
    }

    @ParameterizedTest
    @MethodSource("mergeTouchClassesMergesCorrectlyCases")
    void mergeTouchClassesMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    static Stream<Arguments> mergeLineClampClassesMergesCorrectlyCases() {
        return Stream.of(
                Arguments.of("overflow-auto inline line-clamp-1", "line-clamp-1"),
                Arguments.of("line-clamp-1 overflow-auto inline", "line-clamp-1 overflow-auto inline"));
    }

    @ParameterizedTest
    @MethodSource("mergeLineClampClassesMergesCorrectlyCases")
    void mergeLineClampClassesMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    static Stream<Arguments> mergeFontVariantNumericClassesMergesCorrectlyCases() {
        return Stream.of(
                Arguments.of("lining-nums tabular-nums diagonal-fractions", "lining-nums tabular-nums diagonal-fractions"),
                Arguments.of("normal-nums tabular-nums diagonal-fractions", "tabular-nums diagonal-fractions"),
                Arguments.of("tabular-nums diagonal-fractions normal-nums", "normal-nums"),
                Arguments.of("tabular-nums proportional-nums", "proportional-nums"));
    }

    @ParameterizedTest
    @MethodSource("mergeFontVariantNumericClassesMergesCorrectlyCases")
    void mergeFontVariantNumericClassesMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }
}
