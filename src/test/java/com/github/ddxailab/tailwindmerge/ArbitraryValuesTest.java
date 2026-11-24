package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/** Port of {@code ArbitraryValuesTests.cs}; the cases and the expected values are the ones of the reference. */
class ArbitraryValuesTest {

    static Stream<Arguments> mergeArbitraryValuesMergesCorrectlyCases() {
        return Stream.of(
                Arguments.of("m-[2px] m-[10px]", "m-[10px]"),
                Arguments.of("m-[2px] m-[11svmin] m-[12in] m-[13lvi] m-[14vb] m-[15vmax] m-[16mm] m-[17%] m-[18em] m-[19px] m-[10dvh]", "m-[10dvh]"),
                Arguments.of("h-[10px] h-[11cqw] h-[12cqh] h-[13cqi] h-[14cqb] h-[15cqmin] h-[16cqmax]", "h-[16cqmax]"),
                Arguments.of("z-20 z-[99]", "z-[99]"),
                Arguments.of("my-[2px] m-[10rem]", "m-[10rem]"),
                Arguments.of("cursor-pointer cursor-[grab]", "cursor-[grab]"),
                Arguments.of("m-[2px] m-[calc(100%-var(--arbitrary))]", "m-[calc(100%-var(--arbitrary))]"),
                Arguments.of("m-[2px] m-[length:var(--mystery-var)]", "m-[length:var(--mystery-var)]"),
                Arguments.of("opacity-10 opacity-[0.025]", "opacity-[0.025]"),
                Arguments.of("scale-75 scale-[1.7]", "scale-[1.7]"),
                Arguments.of("brightness-90 brightness-[1.75]", "brightness-[1.75]"),
                Arguments.of("min-h-[0.5px] min-h-[0]", "min-h-[0]"),
                Arguments.of("text-[0.5px] text-[color:0]", "text-[0.5px] text-[color:0]"),
                Arguments.of("text-[0.5px] text-(--my-0)", "text-[0.5px] text-(--my-0)"));
    }

    @ParameterizedTest
    @MethodSource("mergeArbitraryValuesMergesCorrectlyCases")
    void mergeArbitraryValuesMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    static Stream<Arguments> mergeArbitraryLengthsWithLabelsAndModifiersMergesCorrectlyCases() {
        return Stream.of(
                Arguments.of("hover:m-[2px] hover:m-[length:var(--c)]", "hover:m-[length:var(--c)]"),
                Arguments.of("hover:focus:m-[2px] focus:hover:m-[length:var(--c)]", "focus:hover:m-[length:var(--c)]"),
                Arguments.of("border-b border-[color:rgb(var(--color-gray-500-rgb)/50%))]", "border-b border-[color:rgb(var(--color-gray-500-rgb)/50%))]"),
                Arguments.of("border-[color:rgb(var(--color-gray-500-rgb)/50%))] border-b", "border-[color:rgb(var(--color-gray-500-rgb)/50%))] border-b"),
                Arguments.of("border-b border-[color:rgb(var(--color-gray-500-rgb)/50%))] border-some-coloooor", "border-b border-some-coloooor"));
    }

    @ParameterizedTest
    @MethodSource("mergeArbitraryLengthsWithLabelsAndModifiersMergesCorrectlyCases")
    void mergeArbitraryLengthsWithLabelsAndModifiersMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    static Stream<Arguments> mergeComplexArbitraryValuesMergesCorrectlyCases() {
        return Stream.of(
                Arguments.of("grid-rows-[1fr,auto] grid-rows-2", "grid-rows-2"),
                Arguments.of("grid-rows-[repeat(20,minmax(0,1fr))] grid-rows-3", "grid-rows-3"));
    }

    @ParameterizedTest
    @MethodSource("mergeComplexArbitraryValuesMergesCorrectlyCases")
    void mergeComplexArbitraryValuesMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    static Stream<Arguments> mergeAmbiguousArbitraryValuesMergesCorrectlyCases() {
        return Stream.of(
                Arguments.of("mt-2 mt-[calc(theme(fontSize.4xl)/1.125)]", "mt-[calc(theme(fontSize.4xl)/1.125)]"),
                Arguments.of("p-2 p-[calc(theme(fontSize.4xl)/1.125)_10px]", "p-[calc(theme(fontSize.4xl)/1.125)_10px]"),
                Arguments.of("mt-2 mt-[length:theme(someScale.someValue)]", "mt-[length:theme(someScale.someValue)]"),
                Arguments.of("mt-2 mt-[theme(someScale.someValue)]", "mt-[theme(someScale.someValue)]"),
                Arguments.of("text-2xl text-[length:theme(someScale.someValue)]", "text-[length:theme(someScale.someValue)]"),
                Arguments.of("text-2xl text-[calc(theme(fontSize.4xl)/1.125)]", "text-[calc(theme(fontSize.4xl)/1.125)]"),
                Arguments.of("bg-cover bg-[percentage:30%] bg-[size:200px_100px] bg-[length:200px_100px]", "bg-[percentage:30%] bg-[length:200px_100px]"),
                Arguments.of("bg-none bg-[url(.)] bg-[image:.] bg-[url:.] bg-[linear-gradient(.)] bg-linear-to-r", "bg-linear-to-r"),
                Arguments.of("border-[color-mix(in_oklab,var(--background),var(--calendar-color)_30%)] border", "border-[color-mix(in_oklab,var(--background),var(--calendar-color)_30%)] border"),
                Arguments.of("font-[400] font-[600]", "font-[600]"),
                Arguments.of("font-[var(--a)] font-[var(--b)]", "font-[var(--b)]"),
                Arguments.of("font-[weight:var(--a)] font-[var(--b)]", "font-[var(--b)]"),
                Arguments.of("font-[400] font-[weight:var(--b)]", "font-[weight:var(--b)]"),
                Arguments.of("font-[weight:var(--a)] font-[weight:var(--b)]", "font-[weight:var(--b)]"),
                Arguments.of("font-[family-name:var(--a)] font-[var(--b)]", "font-[family-name:var(--a)] font-[var(--b)]"));
    }

    @ParameterizedTest
    @MethodSource("mergeAmbiguousArbitraryValuesMergesCorrectlyCases")
    void mergeAmbiguousArbitraryValuesMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    static Stream<Arguments> mergeArbitraryCustomPropertiesMergesCorrectlyCases() {
        return Stream.of(
                Arguments.of("bg-red bg-(--other-red) bg-bottom bg-(position:-my-pos)", "bg-(--other-red) bg-(position:-my-pos)"),
                Arguments.of("shadow-xs shadow-(shadow:--something) shadow-red shadow-(--some-other-shadow) shadow-(color:--some-color)", "shadow-(--some-other-shadow) shadow-(color:--some-color)"),
                Arguments.of("bg-red-500 bg-(image:--my-image)", "bg-red-500 bg-(image:--my-image)"),
                Arguments.of("bg-(image:--my-image) bg-red-500", "bg-(image:--my-image) bg-red-500"),
                Arguments.of("bg-none bg-(image:--my-image)", "bg-(image:--my-image)"),
                Arguments.of("bg-(url:--my-image) bg-linear-to-r", "bg-linear-to-r"),
                Arguments.of("bg-linear-45 bg-linear-(--my-angle)", "bg-linear-(--my-angle)"),
                Arguments.of("bg-radial bg-radial-(--my-position)", "bg-radial-(--my-position)"),
                Arguments.of("bg-conic-10 bg-conic-(--my-angle)", "bg-conic-(--my-angle)"),
                Arguments.of("bg-linear-(--my-angle) bg-conic", "bg-conic"),
                Arguments.of("font-(--a) font-(--b)", "font-(--b)"),
                Arguments.of("font-(weight:--a) font-(--b)", "font-(--b)"),
                Arguments.of("font-(family-name:--a) font-(--b)", "font-(family-name:--a) font-(--b)"),
                Arguments.of("z-10 z-(--my-z)", "z-(--my-z)"),
                Arguments.of("z-(--my-z) z-auto", "z-auto"),
                Arguments.of("outline-offset-2 outline-offset-(--my-offset)", "outline-offset-(--my-offset)"),
                Arguments.of("outline-offset-2 outline-offset-[var(--my-offset)]", "outline-offset-[var(--my-offset)]"));
    }

    @ParameterizedTest
    @MethodSource("mergeArbitraryCustomPropertiesMergesCorrectlyCases")
    void mergeArbitraryCustomPropertiesMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }
}
