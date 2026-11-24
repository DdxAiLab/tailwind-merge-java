package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TwMergeTest {

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "mix-blend-normal mix-blend-multiply | mix-blend-multiply",
        "h-10 h-min | h-min",
        "stroke-black stroke-1 | stroke-black stroke-1",
        "stroke-2 stroke-[3] | stroke-[3]",
        "outline-black outline-1 | outline-black outline-1",
        "grayscale-0 grayscale-[50%] | grayscale-[50%]",
        "grow grow-[2] | grow-[2]",
    })
    void mergeMergesCorrectly(String classList, String expected) {
        assertEquals(expected, new TwMerge().merge(classList));
    }

    @Test
    void mergeNothingToMergeReturnsEmptyString() {
        TwMerge twMerge = new TwMerge();

        assertEquals("", twMerge.merge(""));
        assertEquals("", twMerge.merge((String) null));
        assertEquals("", twMerge.merge());
        assertEquals("", twMerge.merge((String[]) null));
        assertEquals("", twMerge.merge(" ", null, "\t\n"));
    }

    @Test
    void mergeJoinsSeveralArguments() {
        assertEquals("px-2 py-1", new TwMerge().merge("px-2", "py-1"));
        assertEquals("px-4", new TwMerge().merge("px-2", null, "", "px-4"));
    }

    @Test
    void mergeSameValuesReturnsCachedResult() {
        TwMerge twMerge = new TwMerge();

        assertEquals("h-min", twMerge.merge("h-10 h-min"));
        assertEquals("h-min", twMerge.merge("h-10 h-min"));
    }

    @Test
    void mergePrefixSetAfterCreationIgnoresChange() {
        TwMergeConfig config = new TwMergeConfig();
        TwMerge twMerge = new TwMerge(config);

        config.setPrefix("tw");

        assertEquals("p-3", twMerge.merge("p-2 p-3"));
    }

    @Test
    void mergeConflictsAddedToConfigAfterCreationIgnoresChanges() {
        TwMergeConfig config = new TwMergeConfig();
        TwMerge twMerge = new TwMerge(config);

        config.getConflictingClassGroups().put("m", java.util.Collections.singletonList("h"));

        assertEquals("h-4 m-4", twMerge.merge("h-4 m-4"));
    }

    @Test
    void mergeWithTinyCacheStillMergesCorrectly() {
        TwMergeConfig config = new TwMergeConfig();
        config.setCacheSize(2);
        TwMerge twMerge = new TwMerge(config);

        for (int round = 0; round < 3; round++) {
            assertEquals("p-2", twMerge.merge("p-1 p-2"));
            assertEquals("m-2", twMerge.merge("m-1 m-2"));
            assertEquals("h-2", twMerge.merge("h-1 h-2"));
        }
    }

    @Test
    void mergeWithCacheDisabledStillMergesCorrectly() {
        TwMergeConfig config = new TwMergeConfig();
        config.setCacheSize(0);
        TwMerge twMerge = new TwMerge(config);

        assertEquals("p-2", twMerge.merge("p-1 p-2"));
        assertEquals("p-2", twMerge.merge("p-1 p-2"));
    }

    @Test
    void constructorRejectsNullConfig() {
        assertThrows(IllegalArgumentException.class, () -> new TwMerge(null));
    }

    @Test
    void mergeIsThreadSafe() throws Exception {
        TwMergeConfig config = new TwMergeConfig();
        config.setCacheSize(8); // small, so entries are evicted while the threads run
        TwMerge twMerge = new TwMerge(config);

        ExecutorService executor = Executors.newFixedThreadPool(8);
        try {
            List<Future<Boolean>> futures = new ArrayList<>();
            for (int t = 0; t < 8; t++) {
                futures.add(executor.submit(new Callable<Boolean>() {
                    @Override
                    public Boolean call() {
                        for (int i = 0; i < 2000; i++) {
                            int n = i % 20;
                            if (!("p-" + (n + 1)).equals(twMerge.merge("p-" + n + " p-" + (n + 1)))) {
                                return false;
                            }
                        }
                        return true;
                    }
                }));
            }
            for (Future<Boolean> future : futures) {
                assertTrue(future.get());
            }
        } finally {
            executor.shutdownNow();
        }
    }
}
