package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.ddxailab.tailwindmerge.model.ClassGroup;
import java.util.Collections;
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Port of {@code ModifiersTests.cs}. The cases that build the configuration with {@code Extend} or
 * {@code Override} need Step 8; the ones that only change the configuration collections are here.
 */
class ModifiersTest {

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "hover:block hover:inline | hover:inline",
        "hover:block hover:focus:inline | hover:block hover:focus:inline",
        "line-through no-underline | no-underline",
        "hover:block hover:focus:inline focus:hover:inline | hover:block focus:hover:inline",
        "focus-within:inline focus-within:block | focus-within:block",
    })
    void mergeClassesWithPrefixModifiersMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "text-lg/7 text-lg/8 | text-lg/8",
        "text-lg/none leading-9 | text-lg/none leading-9",
        "leading-9 text-lg/none | text-lg/none",
        "w-full w-1/2 | w-1/2",
    })
    void mergeClassesWithPostfixModifiersMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "c:d:e:text-lg d:c:e:text-lg | d:c:e:text-lg",
        "before:after:text-lg before:after:text-lg | before:after:text-lg",
        "before:after:text-lg after:before:text-lg | before:after:text-lg after:before:text-lg",
        "x:y:before:z:text-lg y:x:before:z:text-lg | y:x:before:z:text-lg",
        "c:d:e:block d:c:e:inline | d:c:e:inline",
        "*:before:block *:before:inline | *:before:inline",
        "*:before:block before:*:inline | *:before:block before:*:inline",
        "x:y:*:z:block y:x:*:z:inline | y:x:*:z:inline",
    })
    void mergeClassesWithOrderSensitiveModifiersMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    @Test
    void mergeOrderSensitiveModifierAddedToConfigKeepsModifierOrder() {
        TwMergeConfig config = new TwMergeConfig();
        config.getOrderSensitiveModifiers().add("a");

        assertEquals("a:b:block b:a:inline", new TwMerge(config).merge("a:b:block b:a:inline"));
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "foo-bar foo-bar/baz | foo-bar/baz",
        "foo-bar/baz foo-bar | foo-bar/baz foo-bar",
    })
    void mergePostfixLookupClassGroupAddedToConfigResolvesFullClassNames(String classLists, String expected) {
        Predicate<String> namedValidator = value -> value.equals("foo-bar/baz");

        // Stands in for Override (Step 8): the two groups replace whatever is under those keys
        TwMergeConfig config = new TwMergeConfig();
        config.getClassGroups().put("base", ClassGroup.withBase("foo", "bar"));
        config.getClassGroups().put("named", ClassGroup.of(namedValidator));
        config.getConflictingClassGroups().put("named", Collections.singletonList("base"));
        config.getPostfixLookupClassGroups().add("base");

        assertEquals(expected, new TwMerge(config).merge(classLists));
    }
}
