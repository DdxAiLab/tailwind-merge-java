package com.github.ddxailab.tailwindmerge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Small helpers to write the collection initializers of the C# tests in Java 8. */
final class TestData {

    private TestData() {
    }

    /** Builds an ordered map from alternating keys and values. */
    @SuppressWarnings("unchecked")
    static <V> Map<String, V> map(Object... keysAndValues) {
        Map<String, V> map = new LinkedHashMap<>();
        for (int i = 0; i < keysAndValues.length; i += 2) {
            map.put((String) keysAndValues[i], (V) keysAndValues[i + 1]);
        }
        return map;
    }

    @SafeVarargs
    static <T> List<T> list(T... values) {
        return new ArrayList<>(Arrays.asList(values));
    }
}
