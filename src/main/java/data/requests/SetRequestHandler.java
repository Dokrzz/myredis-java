package data.requests;

import data.CacheStore;

import java.util.List;

import static enums.DataType.SIMPLE_STRING;
import static enums.DataType.TERMINATE;

public class SetRequestHandler implements IRequestHandler {

    int expiryInMilliSeconds = -1;


    @Override
    public String handle(RedisRequest redisRequest) {
        List<String> elements = redisRequest.getElements();

        if(elements.size() < 2) {
            throw new IllegalStateException();
        }

        String key = elements.get(0);
        String value = elements.get(1);


        boolean success = CacheStore.put(key, value);

        String sb = SIMPLE_STRING.getValue() + "OK" + TERMINATE.getValue();

        return sb;
    }

    private void setExpiryInMilliSeconds(int expiryInMilliSeconds) {

    }
}
