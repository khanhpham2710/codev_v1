package app;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CacheManager {

    // Volatile ensures visibility across threads
    private static volatile CacheManager instance;

    private final Map<String, Object> cache;

    private CacheManager() {
        cache = Collections.synchronizedMap(new HashMap<>());
    }

    /**
     * Get the singleton instance using double-checked locking.
     */
    public static CacheManager getInstance() {
        if (instance == null) { // First check
            synchronized (CacheManager.class) {
                if (instance == null) { // Second check
                    instance = new CacheManager();
                }
            }
        }
        return instance;
    }

    public void put(String key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("Cache key cannot be null");
        }
        cache.put(key, value);
    }

    public Object get(String key) {
        if (key == null) {
            return null;
        }
        return cache.get(key);
    }

    public void remove(String key) {
        if (key != null) {
            cache.remove(key);
        }
    }

    public void clear() {
        cache.clear();
    }
}
