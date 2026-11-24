package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Support for the {@code bg-opacity-*} classes of Tailwind CSS v3. It is an addition to the reference
 * library, which treats {@code bg-opacity-50} as one more background color.
 */
class LegacyBgOpacityTest {

    static Stream<Arguments> cases() {
        return Stream.of(
                // The opacity goes into the color, whatever the order of the two classes
                Arguments.of("bg-red-300 bg-opacity-50", "bg-red-300/50"),
                Arguments.of("bg-opacity-50 bg-red-300", "bg-red-300/50"),
                Arguments.of("px-2 bg-opacity-50 py-1 bg-red-300", "px-2 py-1 bg-red-300/50"),
                // The last opacity wins
                Arguments.of("bg-red-300 bg-opacity-50 bg-opacity-75", "bg-red-300/75"),
                Arguments.of("bg-opacity-50 bg-red-300 bg-opacity-75", "bg-red-300/75"),
                // The opacity is applied to the color that survives
                Arguments.of("bg-red-300 bg-opacity-50 bg-blue-500", "bg-blue-500/50"),
                Arguments.of("bg-red-300/25 bg-blue-500 bg-opacity-50", "bg-blue-500/50"),
                // Without a color there is nothing to merge into
                Arguments.of("bg-opacity-50", "bg-opacity-50"),
                Arguments.of("bg-opacity-50 p-2", "bg-opacity-50 p-2"),
                // Arbitrary values and variables
                Arguments.of("bg-[#B91C1C] bg-opacity-[0.35]", "bg-[#B91C1C]/[0.35]"),
                Arguments.of("bg-red-300 bg-opacity-[.5]", "bg-red-300/[.5]"),
                Arguments.of("bg-red-300 bg-opacity-(--my-opacity)", "bg-red-300/(--my-opacity)"),
                Arguments.of("bg-(--my-color) bg-opacity-50", "bg-(--my-color)/50"),
                // Only the color of the same modifiers receives the opacity
                Arguments.of("hover:bg-red-300 hover:bg-opacity-50", "hover:bg-red-300/50"),
                Arguments.of("hover:focus:bg-red-300 focus:hover:bg-opacity-50", "hover:focus:bg-red-300/50"),
                Arguments.of("bg-red-300 hover:bg-opacity-50", "bg-red-300 hover:bg-opacity-50"),
                Arguments.of("hover:bg-red-300 bg-opacity-50", "hover:bg-red-300 bg-opacity-50"),
                Arguments.of("bg-red-300 bg-opacity-50 hover:bg-blue-500 hover:bg-opacity-25",
                        "bg-red-300/50 hover:bg-blue-500/25"),
                // The important modifier, in both syntaxes
                Arguments.of("bg-red-300! bg-opacity-50!", "bg-red-300/50!"),
                Arguments.of("!bg-red-300 !bg-opacity-50", "!bg-red-300/50"),
                Arguments.of("bg-red-300! bg-opacity-50", "bg-red-300! bg-opacity-50"),
                // An explicit opacity of the color wins, as in Tailwind CSS v3
                Arguments.of("bg-red-300/25 bg-opacity-50", "bg-red-300/25"),
                Arguments.of("bg-opacity-50 bg-red-300/25", "bg-red-300/25"),
                // Not background colors
                Arguments.of("bg-none bg-opacity-50", "bg-none bg-opacity-50"),
                Arguments.of("bg-cover bg-opacity-50 bg-red-300", "bg-cover bg-red-300/50"),
                // Values that are not an opacity are the same as before: a color called "opacity-foo"
                Arguments.of("bg-opacity-foo bg-red-300", "bg-red-300"),
                // Other opacity utilities are untouched
                Arguments.of("opacity-50 bg-red-300", "opacity-50 bg-red-300"),
                Arguments.of("text-opacity-50 text-red-300", "text-red-300"));
    }

    @ParameterizedTest
    @MethodSource("cases")
    void mergesTheOpacityIntoTheColor(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    @Test
    void worksAcrossSeveralArguments() {
        // The color keeps its position; the opacity class disappears
        assertEquals("bg-red-300/50 p-2", new TwMerge().merge("bg-red-300 p-2", "bg-opacity-50"));
    }

    @Test
    void worksWithAPrefix() {
        TwMergeConfig config = new TwMergeConfig();
        config.setPrefix("tw");
        TwMerge twMerge = new TwMerge(config);

        assertEquals("tw:bg-red-300/50", twMerge.merge("tw:bg-red-300 tw:bg-opacity-50"));
        assertEquals("tw:bg-red-300/50", twMerge.merge("tw:bg-opacity-50 tw:bg-red-300"));
        // Classes without the prefix are not Tailwind classes
        assertEquals("bg-red-300 bg-opacity-50", twMerge.merge("bg-red-300 bg-opacity-50"));
    }

    @Test
    void repeatedCallsGiveTheSameResult() {
        TwMerge twMerge = new TwMerge();

        assertEquals("bg-red-300/50", twMerge.merge("bg-red-300 bg-opacity-50"));
        assertEquals("bg-red-300/50", twMerge.merge("bg-red-300 bg-opacity-50"));
    }

    @Test
    void isEnabledByDefault() {
        assertTrue(new TwMergeConfig().isLegacyBgOpacity());
    }

    @Test
    void whenDisabledItBehavesLikeTheReference() {
        TwMergeConfig config = new TwMergeConfig();
        config.setLegacyBgOpacity(false);
        TwMerge twMerge = new TwMerge(config);

        // bg-opacity-50 is just another background color
        assertEquals("bg-opacity-50", twMerge.merge("bg-red-300 bg-opacity-50"));
        assertEquals("bg-red-300", twMerge.merge("bg-opacity-50 bg-red-300"));
        assertEquals("bg-red-300/50", twMerge.merge("bg-red-300/50"));
    }

    @Test
    void changingTheOptionAfterCreationHasNoEffect() {
        TwMergeConfig config = new TwMergeConfig();
        TwMerge twMerge = new TwMerge(config);

        config.setLegacyBgOpacity(false);

        assertEquals("bg-red-300/50", twMerge.merge("bg-red-300 bg-opacity-50"));
    }
}
