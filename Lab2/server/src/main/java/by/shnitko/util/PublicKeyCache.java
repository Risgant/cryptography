package by.shnitko.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PublicKeyCache {
    private static Map<UUID, String> cache = new HashMap<>();

    public static String getKey(UUID userId) {
        return cache.get(userId);
    }

    public static void saveKey(UUID userId, String key) {
        cache.put(userId, key);
    }

    public static void deleteKey(UUID userId) {
        cache.remove(userId);
    }
}
