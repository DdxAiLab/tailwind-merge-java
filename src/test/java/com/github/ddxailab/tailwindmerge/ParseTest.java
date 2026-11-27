package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.ddxailab.tailwindmerge.common.ArbitraryValue;
import com.github.ddxailab.tailwindmerge.common.ArbitraryValue.Syntax;
import com.github.ddxailab.tailwindmerge.common.ThemeKeys;
import com.github.ddxailab.tailwindmerge.model.ClassGroup;
import com.github.ddxailab.tailwindmerge.model.ExtendedConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/** Parsing of single class names. It is an addition to the reference library. */
class ParseTest {

    private final TwMerge twMerge = new TwMerge();

    @Test
    void parsesAPlainClass() {
        ParsedClass parsed = twMerge.parse("p-4");

        assertAll(
                () -> assertEquals("p-4", parsed.getClassName()),
                () -> assertEquals("p-4", parsed.toString()),
                () -> assertEquals(Collections.emptyList(), parsed.getVariants()),
                () -> assertFalse(parsed.isImportant()),
                () -> assertEquals("p-4", parsed.getBaseClassName()),
                () -> assertFalse(parsed.isNegative()),
                () -> assertEquals("", parsed.getModifier()),
                () -> assertFalse(parsed.getArbitraryValue().isPresent()),
                () -> assertFalse(parsed.getArbitraryProperty().isPresent()),
                () -> assertEquals("p", parsed.getClassGroupId()),
                () -> assertTrue(parsed.isRecognized()),
                () -> assertFalse(parsed.isExternal()));
    }

    @Test
    void keepsTheOriginalOrderOfTheVariants() {
        ParsedClass parsed = twMerge.parse("md:hover:[&>*]:data-[state=open]:supports-(--x):focus:underline");

        assertEquals(
                Arrays.asList("md", "hover", "[&>*]", "data-[state=open]", "supports-(--x)", "focus"),
                parsed.getVariants());
        assertEquals("underline", parsed.getBaseClassName());
        assertEquals("text-decoration", parsed.getClassGroupId());
    }

    @Test
    void doesNotSplitSeparatorsInsideBracketsOrParentheses() {
        ParsedClass parsed = twMerge.parse("[&:nth-child(1/2)]:bg-[url(https://a.test/b.png)]");

        assertEquals(Collections.singletonList("[&:nth-child(1/2)]"), parsed.getVariants());
        assertEquals("bg-[url(https://a.test/b.png)]", parsed.getBaseClassName());
        assertEquals("", parsed.getModifier());
        assertEquals("bg-image", parsed.getClassGroupId());
        assertArbitrary(parsed, Syntax.BRACKETS, "", "url(https://a.test/b.png)");
    }

    @Test
    void variantsCannotBeModified() {
        List<String> variants = twMerge.parse("hover:p-4").getVariants();

        assertThrows(UnsupportedOperationException.class, () -> variants.add("focus"));
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "p-4!                 | p-4",
        "!p-4                 | p-4",
        "hover:p-4!           | p-4",
        "hover:!p-4           | p-4",
        "!-m-4                | -m-4",
        "![color:red]         | [color:red]",
        "[color:red]!         | [color:red]",
    })
    void detectsBothImportantSyntaxes(String className, String baseClassName) {
        ParsedClass parsed = twMerge.parse(className);

        assertTrue(parsed.isImportant());
        assertTrue(parsed.isRecognized());
        assertEquals(baseClassName, parsed.getBaseClassName());
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "-m-4             | m           | -m-4",
        "-inset-x-1       | inset-x     | -inset-x-1",
        "hover:-m-[4px]   | m           | -m-[4px]",
        "-translate-x-1/2 | translate-x | -translate-x-1/2",
    })
    void detectsNegativeValues(String className, String classGroupId, String baseClassName) {
        ParsedClass parsed = twMerge.parse(className);

        assertTrue(parsed.isNegative());
        assertEquals(classGroupId, parsed.getClassGroupId());
        assertEquals(baseClassName, parsed.getBaseClassName());
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "text-lg/7                     | text-lg            | 7         | font-size",
        "text-sm/[1.5]                 | text-sm            | [1.5]     | font-size",
        "text-[14px]/6                 | text-[14px]        | 6         | font-size",
        "text-lg/(--leading)           | text-lg            | (--leading) | font-size",
        "bg-red-300/50                 | bg-red-300         | 50        | bg-color",
        "hover:bg-red-300/50!          | bg-red-300         | 50        | bg-color",
        "!bg-red-300/50                | bg-red-300         | 50        | bg-color",
        "hover:!bg-red-300/[0.5]       | bg-red-300         | [0.5]     | bg-color",
        "bg-(--my-color)/50            | bg-(--my-color)    | 50        | bg-color",
        "bg-[#B91C1C]/[var(--a,1/2)]   | bg-[#B91C1C]       | [var(--a,1/2)] | bg-color",
        "w-1/2/50                      | w-1/2              | 50        | w",
        "[color:red]/50                | [color:red]        | 50        | arbitrary..color",
    })
    void separatesThePostfixModifier(String className, String baseClassName, String modifier, String classGroupId) {
        ParsedClass parsed = twMerge.parse(className);

        assertEquals(baseClassName, parsed.getBaseClassName());
        assertEquals(modifier, parsed.getModifier());
        assertEquals(classGroupId, parsed.getClassGroupId());
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "w-1/2              | w-1/2              | w",
        "!w-1/2             | w-1/2              | w",
        "md:w-2.5/6!        | w-2.5/6            | w",
        "h-1/3              | h-1/3              | h",
        "size-1/2           | size-1/2           | size",
        "min-w-1/2          | min-w-1/2          | min-w",
        "basis-1/3          | basis-1/3          | basis",
        "flex-1/2           | flex-1/2           | flex",
        "inset-1/2          | inset-1/2          | inset",
        "top-1/2            | top-1/2            | top",
        "-translate-y-1/2   | -translate-y-1/2   | translate-y",
        "aspect-4/3         | aspect-4/3         | aspect",
        "@container/sidebar | @container/sidebar | container-named",
    })
    void keepsSlashesThatBelongToTheValue(String className, String baseClassName, String classGroupId) {
        ParsedClass parsed = twMerge.parse(className);

        assertEquals(baseClassName, parsed.getBaseClassName());
        assertEquals("", parsed.getModifier());
        assertEquals(classGroupId, parsed.getClassGroupId());
    }

    @Test
    void aTrailingSlashIsNotAModifier() {
        ParsedClass parsed = twMerge.parse("bg-red-300/");

        assertEquals("bg-red-300/", parsed.getBaseClassName());
        assertEquals("", parsed.getModifier());
    }

    /**
     * {@code merge} resolves the class group of a class with a v3 important modifier and a postfix
     * modifier using a position that is off by one, as the reference does. The parser reports the same
     * group, with the modifier correctly separated.
     */
    @Test
    void reportsTheSameClassGroupAsMergeForALeadingImportantModifier() {
        ParsedClass parsed = twMerge.parse("!text-lg/7");

        assertEquals("text-lg", parsed.getBaseClassName());
        assertEquals("7", parsed.getModifier());
        assertEquals("text-color", parsed.getClassGroupId());
        // merge agrees: it does not see a conflict between these two font sizes
        assertEquals("!text-lg/7 !text-sm", twMerge.merge("!text-lg/7 !text-sm"));
        assertEquals("font-size", twMerge.parse("text-lg/7!").getClassGroupId());
    }

    static Stream<Arguments> arbitraryValues() {
        return Stream.of(
                Arguments.of("w-[10px]", "w", Syntax.BRACKETS, "", "10px"),
                Arguments.of("-m-[4px]", "m", Syntax.BRACKETS, "", "4px"),
                Arguments.of("w-(--my-width)", "w", Syntax.PARENTHESES, "", "--my-width"),
                Arguments.of("text-[length:var(--x)]", "font-size", Syntax.BRACKETS, "length", "var(--x)"),
                Arguments.of("text-(length:--x)", "font-size", Syntax.PARENTHESES, "length", "--x"),
                Arguments.of("font-(family-name:--my-font)", "font-family",
                        Syntax.PARENTHESES, "family-name", "--my-font"),
                Arguments.of("font-[family-name:Open_Sans]", "font-family",
                        Syntax.BRACKETS, "family-name", "Open_Sans"),
                Arguments.of("grid-cols-[repeat(2,minmax(0,1fr))]", "grid-cols",
                        Syntax.BRACKETS, "", "repeat(2,minmax(0,1fr))"),
                Arguments.of("bg-[#B91C1C]", "bg-color", Syntax.BRACKETS, "", "#B91C1C"),
                Arguments.of("hover:border-t-[3px]!", "border-w-t", Syntax.BRACKETS, "", "3px"),
                Arguments.of("text-[14px]/6", "font-size", Syntax.BRACKETS, "", "14px"));
    }

    @ParameterizedTest
    @MethodSource("arbitraryValues")
    void separatesArbitraryValues(
            String className, String classGroupId, Syntax syntax, String label, String value) {
        ParsedClass parsed = twMerge.parse(className);

        assertEquals(classGroupId, parsed.getClassGroupId());
        assertFalse(parsed.getArbitraryProperty().isPresent());
        assertArbitrary(parsed, syntax, label, value);
    }

    @ParameterizedTest
    @ValueSource(strings = {"p-4", "bg-red-300/[0.5]", "text-lg/(--leading)", "data-[open]:underline"})
    void arbitraryValuesOfVariantsAndModifiersAreNotTheValueOfTheClass(String className) {
        assertFalse(twMerge.parse(className).getArbitraryValue().isPresent());
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "[padding:1rem]                  | padding     | 1rem",
        "hover:[mask-type:luminance]     | mask-type   | luminance",
        "[--my-var:20px]                 | --my-var    | 20px",
        "[background:url(a:b)]           | background  | url(a:b)",
        "[grid-template-areas:a_b-[c]]   | grid-template-areas | a_b-[c]",
    })
    void separatesArbitraryProperties(String className, String name, String value) {
        ParsedClass parsed = twMerge.parse(className);

        assertEquals("arbitrary.." + name, parsed.getClassGroupId());
        assertFalse(parsed.getArbitraryValue().isPresent());
        assertTrue(parsed.getArbitraryProperty().isPresent());
        assertEquals(name, parsed.getArbitraryProperty().get().getName());
        assertEquals(value, parsed.getArbitraryProperty().get().getValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {"foo", "foo-bar", "-foo", "foo/50", "foo-[1px]", "[foo]", "p-4!!"})
    void reportsUnknownClasses(String className) {
        ParsedClass parsed = twMerge.parse("hover:" + className);

        assertAll(
                () -> assertFalse(parsed.isRecognized()),
                () -> assertFalse(parsed.isExternal()),
                () -> assertEquals("", parsed.getClassGroupId()),
                () -> assertEquals(Collections.singletonList("hover"), parsed.getVariants()),
                () -> assertFalse(parsed.isNegative()),
                () -> assertEquals("", parsed.getModifier()),
                () -> assertFalse(parsed.getArbitraryValue().isPresent()),
                () -> assertFalse(parsed.getArbitraryProperty().isPresent()));
    }

    @Test
    void anUnknownClassKeepsItsSlashAndLosesItsImportantModifier() {
        ParsedClass parsed = twMerge.parse("foo/50!");

        assertTrue(parsed.isImportant());
        assertEquals("foo/50", parsed.getBaseClassName());
        assertEquals("foo/50!", parsed.getClassName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"p-4", " p-4 ", "\tp-4\n", "　p-4  "})
    void ignoresSurroundingWhiteSpace(String className) {
        assertEquals("p-4", twMerge.parse(className).getClassName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\t\n", " ", "p-4 m-2", " p-4  p-4 ", "[padding: 1rem]"})
    void rejectsBlankStringsAndSeveralClasses(String className) {
        assertThrows(IllegalArgumentException.class, () -> twMerge.parse(className));
    }

    @Test
    void rejectsNull() {
        assertThrows(IllegalArgumentException.class, () -> twMerge.parse(null));
    }

    @Test
    void usesTheConfiguredPrefix() {
        TwMergeConfig config = new TwMergeConfig();
        config.setPrefix("tw");
        TwMerge prefixed = new TwMerge(config);

        ParsedClass parsed = prefixed.parse("tw:hover:-m-4!");

        assertFalse(parsed.isExternal());
        assertEquals("tw:hover:-m-4!", parsed.getClassName());
        assertEquals(Collections.singletonList("hover"), parsed.getVariants());
        assertTrue(parsed.isImportant());
        assertTrue(parsed.isNegative());
        assertEquals("-m-4", parsed.getBaseClassName());
        assertEquals("m", parsed.getClassGroupId());

        ParsedClass important = prefixed.parse("tw:!bg-red-300/50");
        assertEquals("bg-red-300", important.getBaseClassName());
        assertEquals("50", important.getModifier());
    }

    @ParameterizedTest
    @ValueSource(strings = {"p-4", "hover:p-4!", "hover:tw:p-4", "-m-4"})
    void doesNotInterpretClassesWithoutTheConfiguredPrefix(String className) {
        TwMergeConfig config = new TwMergeConfig();
        config.setPrefix("tw");

        ParsedClass parsed = new TwMerge(config).parse(className);

        assertAll(
                () -> assertTrue(parsed.isExternal()),
                () -> assertFalse(parsed.isRecognized()),
                () -> assertEquals(className, parsed.getClassName()),
                () -> assertEquals(className, parsed.getBaseClassName()),
                () -> assertEquals(Collections.emptyList(), parsed.getVariants()),
                () -> assertFalse(parsed.isImportant()),
                () -> assertFalse(parsed.isNegative()),
                () -> assertEquals("", parsed.getModifier()),
                () -> assertEquals("", parsed.getClassGroupId()));
    }

    @Test
    void usesTheConfiguredThemeAndClassGroups() {
        TwMergeConfig config = new TwMergeConfig().extend(new ExtendedConfig());
        config.getTheme().put(ThemeKeys.TEXT, Arrays.<Object>asList("huge-text"));
        config.getClassGroups().put("custom", ClassGroup.withBase("custom", "a", "1/2"));
        TwMerge custom = new TwMerge(config);

        assertEquals("font-size", custom.parse("text-huge-text").getClassGroupId());
        assertEquals("text-color", twMerge.parse("text-huge-text").getClassGroupId());
        assertEquals("custom", custom.parse("custom-a").getClassGroupId());
        assertFalse(twMerge.parse("custom-a").isRecognized());

        // A literal definition with a slash is a value, not a modifier
        ParsedClass literal = custom.parse("custom-1/2");
        assertEquals("custom-1/2", literal.getBaseClassName());
        assertEquals("", literal.getModifier());
    }

    @Test
    void reportsTheLegacyBackgroundOpacityGroup() {
        ParsedClass parsed = twMerge.parse("hover:bg-opacity-[0.35]");

        assertEquals("bg-opacity", parsed.getClassGroupId());
        assertArbitrary(parsed, Syntax.BRACKETS, "", "0.35");

        TwMergeConfig config = new TwMergeConfig();
        config.setLegacyBgOpacity(false);
        assertEquals("bg-color", new TwMerge(config).parse("bg-opacity-50").getClassGroupId());
    }

    @Test
    void parsingDoesNotChangeTheResultOfMerge() {
        TwMerge instance = new TwMerge();

        instance.parse("p-4");

        assertEquals("p-4", instance.merge("p-2 p-4"));
        assertEquals("p-4", instance.merge("p-4"));
    }

    @Test
    void parseIsThreadSafe() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(8);
        try {
            List<Callable<Void>> tasks = new ArrayList<>();
            for (int i = 0; i < 200; i++) {
                final int number = i;
                tasks.add(() -> {
                    ParsedClass parsed = twMerge.parse("md:hover:w-[" + number + "px]!");
                    assertEquals(Arrays.asList("md", "hover"), parsed.getVariants());
                    assertEquals("w", parsed.getClassGroupId());
                    assertArbitrary(parsed, Syntax.BRACKETS, "", number + "px");
                    return null;
                });
            }
            for (Future<Void> result : executor.invokeAll(tasks)) {
                result.get();
            }
        } finally {
            executor.shutdown();
        }
    }

    private static void assertArbitrary(ParsedClass parsed, Syntax syntax, String label, String value) {
        assertTrue(parsed.getArbitraryValue().isPresent(), parsed.getClassName());
        ArbitraryValue arbitrary = parsed.getArbitraryValue().get();
        assertEquals(syntax, arbitrary.getSyntax());
        assertEquals(label, arbitrary.getLabel());
        assertEquals(value, arbitrary.getValue());
    }
}
