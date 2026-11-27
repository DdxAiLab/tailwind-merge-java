package com.github.ddxailab.tailwindmerge.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ArbitraryValueTest {

    @ParameterizedTest
    @CsvSource(delimiter = '~', value = {
        "[1px]               ~ BRACKETS    ~ ''     ~ 1px",
        "[length:var(--a)]   ~ BRACKETS    ~ length ~ var(--a)",
        "[url(a:b)]          ~ BRACKETS    ~ ''     ~ url(a:b)",
        "(--a)               ~ PARENTHESES ~ ''     ~ --a",
        "(image:--a)         ~ PARENTHESES ~ image  ~ --a",
    })
    void splitsTheLabelAndTheValue(String text, ArbitraryValue.Syntax syntax, String label, String value) {
        ArbitraryValue arbitrary = ArbitraryValue.from(text).get();

        assertEquals(syntax, arbitrary.getSyntax());
        assertEquals(label, arbitrary.getLabel());
        assertEquals(value, arbitrary.getValue());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"1px", "[]", "()", "[1px", "1px]", "(a]", "w-[1px]"})
    void rejectsOtherTexts(String text) {
        assertFalse(ArbitraryValue.from(text).isPresent());
    }
}
