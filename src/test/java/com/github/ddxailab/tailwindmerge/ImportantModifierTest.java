package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ImportantModifierTest {

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "font-medium! font-bold! | font-bold!",
        "font-medium! font-bold! font-thin | font-bold! font-thin",
        "right-2! -inset-x-px! | -inset-x-px!",
        "focus:inline! focus:block! | focus:block!",
        "[--my-var:20px]! [--my-var:30px]! | [--my-var:30px]!",
        // Tailwind CSS v3 legacy syntax
        "font-medium! !font-bold | !font-bold",
        "!font-medium !font-bold | !font-bold",
        "!font-medium !font-bold font-thin | !font-bold font-thin",
        "!right-2 !-inset-x-px | !-inset-x-px",
        "focus:!inline focus:!block | focus:!block",
        "![--my-var:20px] ![--my-var:30px] | ![--my-var:30px]",
    })
    void mergeClassesWithImportantModifierMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }
}
