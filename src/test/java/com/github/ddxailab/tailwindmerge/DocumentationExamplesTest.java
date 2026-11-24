package com.github.ddxailab.tailwindmerge;

import static com.github.ddxailab.tailwindmerge.TestData.list;
import static com.github.ddxailab.tailwindmerge.TestData.map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.github.ddxailab.tailwindmerge.common.ThemeKeys;
import com.github.ddxailab.tailwindmerge.common.Validators;
import com.github.ddxailab.tailwindmerge.model.ClassGroup;
import com.github.ddxailab.tailwindmerge.model.ExtendedConfig;
import org.junit.jupiter.api.Test;

/**
 * The code examples of {@code docs/features.md}, {@code docs/limitations.md}, {@code docs/configuration.md}
 * and the README, so the documentation cannot drift from the behavior.
 */
class DocumentationExamplesTest {

    private final TwMerge twMerge = new TwMerge();

    @Test
    void readmeExample() {
        assertEquals("px-2 py-1 bg-red-500", TwMerge.getDefault().merge("px-2 py-1", "bg-red-500"));
    }

    @Test
    void whatIsItFor() {
        assertEquals("border rounded p-3", twMerge.merge("border rounded px-2 py-1", "p-3"));
    }

    @Test
    void featuresMergingBehavior() {
        assertEquals("p-4", twMerge.merge("p-5 p-2 p-4"));
        assertEquals("p-3 px-5", twMerge.merge("p-3 px-5"));
        assertEquals("inset-x-4 right-4", twMerge.merge("inset-x-4 right-4"));
        assertEquals("-inset-1", twMerge.merge("inset-x-px -inset-1"));
        assertEquals("inset-y-6", twMerge.merge("bottom-auto inset-y-6"));
        assertEquals("block", twMerge.merge("inline block"));
        assertEquals("p-2 hover:p-4", twMerge.merge("p-2 hover:p-4"));
        assertEquals("hover:p-4", twMerge.merge("hover:p-2 hover:p-4"));
        assertEquals("focus:hover:p-4", twMerge.merge("hover:focus:p-2 focus:hover:p-4"));
    }

    @Test
    void featuresArbitraryValues() {
        assertEquals("bg-[color:var(--mystery-var)]",
                twMerge.merge("bg-black bg-(--my-color) bg-[color:var(--mystery-var)]"));
        assertEquals("grid-cols-2", twMerge.merge("grid-cols-[1fr,auto] grid-cols-2"));
    }

    @Test
    void featuresArbitraryProperties() {
        assertEquals("[mask-type:alpha]", twMerge.merge("[mask-type:luminance] [mask-type:alpha]"));
        assertEquals("[--scroll-offset:56px] lg:[--scroll-offset:44px]",
                twMerge.merge("[--scroll-offset:56px] lg:[--scroll-offset:44px]"));
        // Conflicts with the matching Tailwind class are not resolved
        assertEquals("[padding:1rem] p-8", twMerge.merge("[padding:1rem] p-8"));
    }

    @Test
    void featuresArbitraryVariants() {
        assertEquals("[&:nth-child(3)]:py-4", twMerge.merge("[&:nth-child(3)]:py-0 [&:nth-child(3)]:py-4"));
        assertEquals("hover:dark:[&:nth-child(3)]:py-4",
                twMerge.merge("dark:hover:[&:nth-child(3)]:py-0 hover:dark:[&:nth-child(3)]:py-4"));
        // Conflicts with the matching predefined modifier are not resolved
        assertEquals("[&:focus]:ring focus:ring-4", twMerge.merge("[&:focus]:ring focus:ring-4"));
    }

    @Test
    void featuresImportantPostfixAndNonTailwind() {
        assertEquals("p-4! p-5", twMerge.merge("p-3! p-4! p-5"));
        assertEquals("-inset-x-1!", twMerge.merge("right-2! -inset-x-1!"));
        assertEquals("text-lg/7", twMerge.merge("text-sm leading-6 text-lg/7"));
        assertEquals("my-non-tailwind-class p-4", twMerge.merge("p-5 p-2 my-non-tailwind-class p-4"));
        assertEquals("text-secret-sauce", twMerge.merge("text-red text-secret-sauce"));
    }

    @Test
    void featuresComposition() {
        assertEquals("some-class another-class yet-another-class so-many-classes",
                twMerge.merge("some-class", "another-class yet-another-class", "so-many-classes"));
    }

    @Test
    void limitationsFontFamilyLabels() {
        assertEquals("font-(--my-weight)", twMerge.merge("font-(--my-family) font-(--my-weight)"));
        assertEquals("font-(family-name:--my-family) font-(--my-weight)",
                twMerge.merge("font-(family-name:--my-family) font-(--my-weight)"));
    }

    @Test
    void configurationBasicUsage() {
        assertEquals("hover:bg-dark-red p-3 bg-[#B91C1C]",
                twMerge.merge("px-2 py-1 bg-red hover:bg-dark-red", "p-3 bg-[#B91C1C]"));
    }

    @Test
    void configurationExtendTheme() {
        ExtendedConfig extension = new ExtendedConfig();
        extension.setTheme(map(ThemeKeys.TEXT, list((Object) "huge")));
        TwMergeConfig config = new TwMergeConfig();
        config.extend(extension);
        TwMerge custom = new TwMerge(config);

        assertEquals("text-huge", custom.merge("text-lg text-huge"));
        assertEquals("text-sm", custom.merge("text-huge text-sm"));
    }

    @Test
    void configurationCustomClassGroup() {
        ExtendedConfig extension = new ExtendedConfig();
        extension.setClassGroups(map(
                "elevation",
                ClassGroup.withBase("elevation", "", "low", "high", Validators.IS_ARBITRARY_VALUE)));
        TwMergeConfig config = new TwMergeConfig().extend(extension);

        assertEquals("elevation-high", new TwMerge(config).merge("elevation-low elevation-high"));
    }

    @Test
    void getDefaultReturnsTheSameInstance() {
        assertSame(TwMerge.getDefault(), TwMerge.getDefault());
    }

    @Test
    void createDefaultMatchesTheConstructor() {
        TwMergeConfig config = TwMergeConfig.createDefault();

        assertEquals(new TwMergeConfig().getClassGroups().size(), config.getClassGroups().size());
        assertEquals(500, config.getCacheSize());
    }
}
