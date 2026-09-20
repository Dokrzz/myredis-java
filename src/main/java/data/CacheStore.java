package data;

import java.util.HashMap;
import java.util.Optional;

public final class CacheStore {

    private static CacheStore INSTANCE;
    private static HashMap<String, String> redisStore = new HashMap<>();

    private CacheStore() {}

    public static CacheStore getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new CacheStore();
        }

        return INSTANCE;
    }

    public static boolean put(String key, String value) {
        redisStore.put(key, value);
        return true;
    }

    public static Optional<String> get(String key) {
        String value = redisStore.get(key);

        return Optional.ofNullable(value);
    }


}
