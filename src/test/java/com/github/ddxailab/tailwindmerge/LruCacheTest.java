package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class LruCacheTest {

    @Test
    void storesAndReturnsValues() {
        LruCache<String, String> cache = new LruCache<>(2);

        cache.put("a", "1");

        assertEquals("1", cache.get("a"));
        assertNull(cache.get("missing"));
    }

    @Test
    void evictsTheLeastRecentlyUsedEntry() {
        LruCache<String, String> cache = new LruCache<>(2);
        cache.put("a", "1");
        cache.put("b", "2");

        cache.get("a"); // "b" is now the least recently used
        cache.put("c", "3");

        assertEquals("1", cache.get("a"));
        assertNull(cache.get("b"));
        assertEquals("3", cache.get("c"));
        assertEquals(2, cache.size());
    }

    @Test
    void updatingAnEntryReplacesItsValue() {
        LruCache<String, String> cache = new LruCache<>(2);
        cache.put("a", "1");
        cache.put("a", "2");

        assertEquals("2", cache.get("a"));
        assertEquals(1, cache.size());
    }

    @Test
    void capacityBelowOneDisablesTheCache() {
        LruCache<String, String> cache = new LruCache<>(0);

        cache.put("a", "1");

        assertNull(cache.get("a"));
        assertEquals(0, cache.size());
    }
}
