package data;

import java.util.HashMap;
import java.util.Optional;

public final class CacheStore {

    private static CacheStore INSTANCE;
    private static HashMap<String, RedisValue> redisStore = new HashMap<>();

    private CacheStore() {}

    public static CacheStore getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new CacheStore();
        }

        return INSTANCE;
    }

    public static void put(String key, String value, long expiryInMilliSeconds) {
        redisStore.put(key, new RedisValue(value, expiryInMilliSeconds));
    }

    public static Optional<String> get(String key) {
        RedisValue valueObject = redisStore.get(key);

        if(valueObject == null) {
            return Optional.empty();
        }
        else if(valueObject.isExpired()) {
            redisStore.remove(key);
            return Optional.empty();
        }
        else {
            return Optional.of(valueObject.get());
        }

    }


}
