package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ArbitraryVariantsTest {

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "[p]:underline [p]:line-through | [p]:line-through",
        "[&>*]:underline [&>*]:line-through | [&>*]:line-through",
        "[&>*]:underline [&>*]:line-through [&_div]:line-through | [&>*]:line-through [&_div]:line-through",
        "supports-[display:grid]:flex supports-[display:grid]:grid | supports-[display:grid]:grid",
    })
    void mergeBasicArbitraryVariantsMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "dark:lg:hover:[&>*]:underline dark:lg:hover:[&>*]:line-through | dark:lg:hover:[&>*]:line-through",
        "dark:lg:hover:[&>*]:underline dark:hover:lg:[&>*]:line-through | dark:hover:lg:[&>*]:line-through",
        "hover:[&>*]:underline [&>*]:hover:line-through | hover:[&>*]:underline [&>*]:hover:line-through",
        "hover:dark:[&>*]:underline dark:hover:[&>*]:underline dark:[&>*]:hover:line-through | dark:hover:[&>*]:underline dark:[&>*]:hover:line-through",
    })
    void mergeArbitraryVariantsWithModifiersMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "[@media_screen{@media(hover:hover)}]:underline [@media_screen{@media(hover:hover)}]:line-through | [@media_screen{@media(hover:hover)}]:line-through",
        "hover:[@media_screen{@media(hover:hover)}]:underline hover:[@media_screen{@media(hover:hover)}]:line-through | hover:[@media_screen{@media(hover:hover)}]:line-through",
    })
    void mergeArbitraryVariantsWithComplexSyntaxInTheThemeMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "[&[data-open]]:underline [&[data-open]]:line-through | [&[data-open]]:line-through",
    })
    void mergeArbitraryVariantsWithAttributeSelectorsMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "[&[data-foo][data-bar]:not([data-baz])]:underline [&[data-foo][data-bar]:not([data-baz])]:line-through | [&[data-foo][data-bar]:not([data-baz])]:line-through",
    })
    void mergeArbitraryVariantsWithMultipleAttributeSelectorsMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "[&>*]:[&_div]:underline [&>*]:[&_div]:line-through | [&>*]:[&_div]:line-through",
        "[&>*]:[&_div]:underline [&_div]:[&>*]:line-through | [&>*]:[&_div]:underline [&_div]:[&>*]:line-through",
        "hover:dark:[&>*]:focus:disabled:[&_div]:underline dark:hover:[&>*]:disabled:focus:[&_div]:line-through | dark:hover:[&>*]:disabled:focus:[&_div]:line-through",
        "hover:dark:[&>*]:focus:[&_div]:disabled:underline dark:hover:[&>*]:disabled:focus:[&_div]:line-through | hover:dark:[&>*]:focus:[&_div]:disabled:underline dark:hover:[&>*]:disabled:focus:[&_div]:line-through",
    })
    void mergeMultipleArbitraryVariantsMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "[&>*]:[color:red] [&>*]:[color:blue] | [&>*]:[color:blue]",
        "[&[data-foo][data-bar]:not([data-baz])]:nod:noa:[color:red] [&[data-foo][data-bar]:not([data-baz])]:noa:nod:[color:blue] | [&[data-foo][data-bar]:not([data-baz])]:noa:nod:[color:blue]",
    })
    void mergeArbitraryVariantsWithArbitraryPropertiesMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }
}
