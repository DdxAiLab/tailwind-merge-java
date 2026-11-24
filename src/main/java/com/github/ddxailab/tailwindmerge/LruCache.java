package com.github.ddxailab.tailwindmerge;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A small thread-safe least-recently-used cache.
 *
 * <p>Both reads and writes refresh the entry, so every access is serialized with a lock. The work done
 * under the lock is a single hash lookup, which is far cheaper than the merge the cache avoids.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
final class LruCache<K, V> {

    private final int capacity;
    private final Map<K, V> map;

    /**
     * Creates a cache.
     *
     * @param capacity the maximum number of entries; a value lower than 1 disables the cache
     */
    LruCache(final int capacity) {
        this.capacity = capacity;
        this.map = new LinkedHashMap<K, V>(16, 0.75f, true) {
            private static final long serialVersionUID = 1L;

            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > capacity;
            }
        };
    }

    /**
     * Gets a value and marks it as the most recently used.
     *
     * @param key the key
     * @return the cached value, or {@code null} if there is none
     */
    V get(K key) {
        if (capacity < 1) {
            return null;
        }
        synchronized (map) {
            return map.get(key);
        }
    }

    /**
     * Adds or updates a value, evicting the least recently used entry if the cache is full.
     *
     * @param key   the key
     * @param value the value
     */
    void put(K key, V value) {
        if (capacity < 1) {
            return;
        }
        synchronized (map) {
            map.put(key, value);
        }
    }

    int size() {
        synchronized (map) {
            return map.size();
        }
    }
}
