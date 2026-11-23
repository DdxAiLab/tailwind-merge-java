package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TwMergeTest {

    private final TwMerge twMerge = new TwMerge();

    @Test
    void joinsClassNames() {
        assertEquals("px-2 py-1", twMerge.merge("px-2", "py-1"));
    }

    @Test
    void normalizesWhitespace() {
        assertEquals("px-2 py-1 bg-red-500", twMerge.merge("  px-2   py-1 ", "\tbg-red-500\n"));
    }

    @Test
    void ignoresNullAndBlankEntries() {
        assertEquals("px-2", twMerge.merge(null, "", "   ", "px-2"));
    }

    @Test
    void returnsEmptyStringWhenNothingToMerge() {
        assertEquals("", twMerge.merge());
        assertEquals("", twMerge.merge((String[]) null));
        assertEquals("", twMerge.merge(" ", null));
    }
}
