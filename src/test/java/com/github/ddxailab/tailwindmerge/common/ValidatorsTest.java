package com.github.ddxailab.tailwindmerge.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class ValidatorsTest {

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource(delimiter = '|', value = {
            "test | true",
            "something | true"
    })
    void isAny(String value, boolean expected) {
        assertEquals(expected, Validators.IS_ANY.test(value));
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource(delimiter = '|', value = {
            "test | true",
            "1234-hello-world | true",
            "[hello | true",
            "hello] | true",
            "[) | true",
            "(hello] | true",
            "[test] | false",
            "[label:test] | false",
            "(test) | false",
            "(label:test) | false"
    })
    void isAnyNonArbitrary(String value, boolean expected) {
        assertEquals(expected, Validators.IS_ANY_NON_ARBITRARY.test(value));
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource(delimiter = '|', value = {
            "[family-name:Open_Sans] | true",
            "[family-name:var(--my-font)] | true",
            "[Open_Sans] | false",
            "[number:400] | false",
            "[weight:400] | false",
            "family-name:test | false",
            "(family-name:test) | false"
    })
    void isArbitraryFamilyName(String value, boolean expected) {
        assertEquals(expected, Validators.IS_ARBITRARY_FAMILY_NAME.test(value));
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource(delimiter = '|', value = {
            "[url:var(--my-url)] | true",
            "[url(something)] | true",
            "[url:bla] | true",
            "[image:bla] | true",
            "[linear-gradient(something)] | true",
            "[repeating-conic-gradient(something)] | true",
            "[var(--my-url)] | false",
            "[bla] | false",
            "url:2px | false",
            "url(2px) | false"
    })
    void isArbitraryImage(String value, boolean expected) {
        assertEquals(expected, Validators.IS_ARBITRARY_IMAGE.test(value));
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource(delimiter = '|', value = {
            "[3.7%] | true",
            "[481px] | true",
            "[19.1rem] | true",
            "[50vw] | true",
            "[56vh] | true",
            "[length:var(--arbitrary)] | true",
            "1 | false",
            "3px | false",
            "1d5 | false",
            "[1] | false",
            "[12px | false",
            "12px] | false",
            "one | false",
            "[color(display-p3_1_0_0/50%)] | false",
            "[light-dark(white,rgb(0_0_0/50%))] | false",
            "[color-mix(in_srgb,red_50%,blue)] | false"
    })
    void isArbitraryLength(String value, boolean expected) {
        assertEquals(expected, Validators.IS_ARBITRARY_LENGTH.test(value));
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource(delimiter = '|', value = {
            "[number:black] | true",
            "[number:bla] | true",
            "[number:230] | true",
            "[450] | true",
            "[2px] | false",
            "[bla] | false",
            "[black] | false",
            "black | false",
            "450 | false"
    })
    void isArbitraryNumber(String value, boolean expected) {
        assertEquals(expected, Validators.IS_ARBITRARY_NUMBER.test(value));
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource(delimiter = '|', value = {
            "[position:2px] | true",
            "[position:bla] | true",
            "[percentage:bla] | true",
            "[2px] | false",
            "[bla] | false",
            "position:2px | false"
    })
    void isArbitraryPosition(String value, boolean expected) {
        assertEquals(expected, Validators.IS_ARBITRARY_POSITION.test(value));
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource(delimiter = '|', value = {
            "[0_35px_60px_-15px_rgba(0,0,0,0.3)] | true",
            "[inset_0_1px_0,inset_0_-1px_0] | true",
            "[0_0_#00f] | true",
            "[.5rem_0_rgba(5,5,5,5)] | true",
            "[-.5rem_0_#123456] | true",
            "[0.5rem_-0_#123456] | true",
            "[0.5rem_-0.005vh_#123456] | true",
            "[0.5rem_-0.005vh] | true",
            "[rgba(5,5,5,5)] | false",
            "[#00f] | false",
            "[something-else] | false"
    })
    void isArbitraryShadow(String value, boolean expected) {
        assertEquals(expected, Validators.IS_ARBITRARY_SHADOW.test(value));
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource(delimiter = '|', value = {
            "[size:2px] | true",
            "[size:bla] | true",
            "[length:bla] | true",
            "[2px] | false",
            "[bla] | false",
            "size:2px | false",
            "[percentage:bla] | false"
    })
    void isArbitrarySize(String value, boolean expected) {
        assertEquals(expected, Validators.IS_ARBITRARY_SIZE.test(value));
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource(delimiter = '|', value = {
            "[1] | true",
            "[bla] | true",
            "[not-an-arbitrary-value?] | true",
            "[auto,auto,minmax(0,1fr),calc(100vw-50%)] | true",
            "[] | false",
            "[1 | false",
            "1] | false",
            "1 | false",
            "one | false",
            "o[n]e | false"
    })
    void isArbitraryValue(String value, boolean expected) {
        assertEquals(expected, Validators.IS_ARBITRARY_VALUE.test(value));
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource(delimiter = '|', value = {
            "(1) | true",
            "(bla) | true",
            "(not-an-arbitrary-value?) | true",
            "(--my-arbitrary-variable) | true",
            "(label:--my-arbitrary-variable) | true",
            "() | false",
            "(1 | false",
            "1) | false",
            "1 | false",
            "one | false",
            "o(n)e | false"
    })
    void isArbitraryVariable(String value, boolean expected) {
        assertEquals(expected, Validators.IS_ARBITRARY_VARIABLE.test(value));
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource(delimiter = '|', value = {
            "(family-name:test) | true",
            "(other:test) | false",
            "(test) | false",
            "family-name:test | false"
    })
    void isArbitraryVariableFamilyName(String value, boolean expected) {
        assertEquals(expected, Validators.IS_ARBITRARY_VARIABLE_FAMILY_NAME.test(value));
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource(delimiter = '|', value = {
            "(image:test) | true",
            "(url:test) | true",
            "(other:test) | false",
            "(test) | false",
            "image:test | false"
    })
    void isArbitraryVariableImage(String value, boolean expected) {
        assertEquals(expected, Validators.IS_ARBITRARY_VARIABLE_IMAGE.test(value));
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource(delimiter = '|', value = {
            "(length:test) | true",
            "(other:test) | false",
            "(test) | false",
            "length:test | false"
    })
    void isArbitraryVariableLength(String value, boolean expected) {
        assertEquals(expected, Validators.IS_ARBITRARY_VARIABLE_LENGTH.test(value));
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource(delimiter = '|', value = {
            "(position:test) | true",
            "(other:test) | false",
            "(test) | false",
            "position:test | false",
            "percentage:test | false"
    })
    void isArbitraryVariablePosition(String value, boolean expected) {
        assertEquals(expected, Validators.IS_ARBITRARY_VARIABLE_POSITION.test(value));
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource(delimiter = '|', value = {
            "(shadow:test) | true",
            "(test) | true",
            "(other:test) | false",
            "shadow:test | false"
    })
    void isArbitraryVariableShadow(String value, boolean expected) {
        assertEquals(expected, Validators.IS_ARBITRARY_VARIABLE_SHADOW.test(value));
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource(delimiter = '|', value = {
            "(size:test) | true",
            "(length:test) | true",
            "(other:test) | false",
            "(test) | false",
            "shadow:test | false",
            "size:test | false",
            "(percentage:test) | false"
    })
    void isArbitraryVariableSize(String value, boolean expected) {
        assertEquals(expected, Validators.IS_ARBITRARY_VARIABLE_SIZE.test(value));
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource(delimiter = '|', value = {
            "(weight:test) | true",
            "(number:test) | true",
            "(--my-weight) | true",
            "(other:test) | false",
            "weight:test | false",
            "[weight:test] | false"
    })
    void isArbitraryVariableWeight(String value, boolean expected) {
        assertEquals(expected, Validators.IS_ARBITRARY_VARIABLE_WEIGHT.test(value));
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource(delimiter = '|', value = {
            "[weight:400] | true",
            "[weight:bold] | true",
            "[number:400] | true",
            "[number:var(--my-weight)] | true",
            "[400] | true",
            "[bold] | true",
            "[family-name:test] | false",
            "weight:400 | false",
            "(weight:400) | false"
    })
    void isArbitraryWeight(String value, boolean expected) {
        assertEquals(expected, Validators.IS_ARBITRARY_WEIGHT.test(value));
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource(delimiter = '|', value = {
            "1/2 | true",
            "123/209 | true",
            "1 | false",
            "1/2/3 | false",
            "[1/2] | false"
    })
    void isFraction(String value, boolean expected) {
        assertEquals(expected, Validators.IS_FRACTION.test(value));
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource(delimiter = '|', value = {
            "1 | true",
            "123 | true",
            "8312 | true",
            "1.0 | true",
            "1e3 | true",
            "99999999999 | true",
            "[8312] | false",
            "[2] | false",
            "[8312px] | false",
            "[8312%] | false",
            "[8312rem] | false",
            "8312.2 | false",
            "1.2 | false",
            "one | false",
            "1/2 | false",
            "1% | false",
            "1px | false",
            "1,000 | false",
            "NaN | false"
    })
    void isInteger(String value, boolean expected) {
        assertEquals(expected, Validators.IS_INTEGER.test(value));
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource(delimiter = '|', value = {
            "1 | true",
            "123 | true",
            "8312 | true",
            "8312.2 | true",
            "1.2 | true",
            "[8312] | false",
            "[2] | false",
            "[8312px] | false",
            "[8312%] | false",
            "[8312rem] | false",
            "one | false",
            "1/2 | false",
            "1% | false",
            "1px | false",
            "1,000 | false",
            "1,5 | false",
            "NaN | false"
    })
    void isNumber(String value, boolean expected) {
        assertEquals(expected, Validators.IS_NUMBER.test(value));
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource(delimiter = '|', value = {
            "1% | true",
            "100.001% | true",
            ".01% | true",
            "0% | true",
            "0 | false",
            "one% | false"
    })
    void isPercent(String value, boolean expected) {
        assertEquals(expected, Validators.IS_PERCENT.test(value));
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource(delimiter = '|', value = {
            "@container/sidebar | true",
            "@container-normal/sidebar | true",
            "@container-size/sidebar | true",
            "@container/[sidebar] | true",
            "@container-size/(--sidebar) | true",
            "@container | false",
            "@container-normal | false",
            "@container-size | false",
            "@container/ | false",
            "@container-normal/ | false",
            "@container-size/ | false",
            "@container-[size]/sidebar | false",
            "@container-foo/sidebar | false",
            "container/sidebar | false",
            "hover:@container/sidebar | false"
    })
    void isNamedContainerQuery(String value, boolean expected) {
        assertEquals(expected, Validators.IS_NAMED_CONTAINER_QUERY.test(value));
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource(delimiter = '|', value = {
            "xs | true",
            "sm | true",
            "md | true",
            "lg | true",
            "xl | true",
            "2xl | true",
            "2.5xl | true",
            "10xl | true",
            "2xs | true",
            "2lg | true",
            "'' | false",
            "hello | false",
            "1 | false",
            "xl3 | false",
            "2xl3 | false",
            "-xl | false",
            "[sm] | false"
    })
    void isTshirtSize(String value, boolean expected) {
        assertEquals(expected, Validators.IS_TSHIRT_SIZE.test(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"fa-IR", "ar-SA", "de-DE"})
    void isNumberAndIsIntegerDontDependOnDefaultLocale(String languageTag) {
        Locale original = Locale.getDefault();
        Locale.setDefault(Locale.forLanguageTag(languageTag));
        try {
            assertTrue(Validators.IS_INTEGER.test("-1"));
            assertTrue(Validators.IS_NUMBER.test("-1.5"));
            assertFalse(Validators.IS_NUMBER.test("1,5"));
        } finally {
            Locale.setDefault(original);
        }
    }

    // Extra cases: Java's Double.parseDouble accepts things that .NET's double.TryParse
    // (NumberStyles.Float, invariant culture) does not, so the port must not leak them.

    @ParameterizedTest(name = "[{0}] is not a number")
    @ValueSource(strings = {"1d", "1f", "1D", "1F", "0x10", "0x1p3", "", " ", "-", ".", "e3", "1e", "1_000",
            "\u0661\u0662\u0663"})
    void isNumberRejectsJavaOnlyLiterals(String value) {
        assertFalse(Validators.IS_NUMBER.test(value));
        assertFalse(Validators.IS_INTEGER.test(value));
    }

    @ParameterizedTest(name = "[{0}] is a number")
    @ValueSource(strings = {"+1", "-1", "1.", ".5", "-.5", "1E3", "1e+3", "1e-3", " 1 ", "\t1\n", "1e999",
            "Infinity", "-Infinity", "+infinity"})
    void isNumberAcceptsDotNetNumberStylesFloat(String value) {
        assertTrue(Validators.IS_NUMBER.test(value));
    }

    @Test
    void isIntegerHandlesExponentsAndInfinity() {
        assertTrue(Validators.IS_INTEGER.test("1e3"));
        assertTrue(Validators.IS_INTEGER.test("-0"));
        assertTrue(Validators.IS_INTEGER.test("5."));
        assertTrue(Validators.IS_INTEGER.test(" 7 "));
        assertFalse(Validators.IS_INTEGER.test("1e-3"));
        assertFalse(Validators.IS_INTEGER.test("Infinity"));
        assertFalse(Validators.IS_INTEGER.test("1e999"));
    }

    @Test
    void isPercentDelegatesToIsNumber() {
        assertTrue(Validators.IS_PERCENT.test("-1.5%"));
        assertFalse(Validators.IS_PERCENT.test("%"));
        assertFalse(Validators.IS_PERCENT.test("1d%"));
    }

    @Test
    void endAnchorIgnoresOnlyATrailingNewLineLikeDotNet() {
        // .NET: '$' matches before a final '\n' but '.' and '$' do not treat '\r' as a line terminator.
        assertTrue(Validators.IS_ARBITRARY_VALUE.test("[a]\n"));
        assertFalse(Validators.IS_ARBITRARY_VALUE.test("[a]\r"));
        assertTrue(Validators.IS_ARBITRARY_VALUE.test("[a\rb]"));
        assertFalse(Validators.IS_ARBITRARY_VALUE.test("[a\nb]"));
    }

    @Test
    void validatorsAreNamedFunctionsUsableAsPredicates() {
        assertEquals(true, Validators.IS_ANY.and(Validators.IS_NUMBER).test("1"));
        assertEquals(false, Validators.IS_ANY.and(Validators.IS_NUMBER).test("x"));
    }
}
