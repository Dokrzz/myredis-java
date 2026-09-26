package data.requests;

import data.CacheStore;
import enums.Argument;

import java.util.List;

import static enums.DataType.SIMPLE_STRING;
import static enums.DataType.TERMINATE;

public class SetRequestHandler implements IRequestHandler {

    private Argument expiryType;
    private long expirtyAmount;
    int expiryInMilliSeconds = -1;


    @Override
    public String handle(RedisRequest redisRequest) {
        List<String> elements = redisRequest.getElements();

        if(elements.size() < 2) {
            throw new IllegalStateException();
        }

        String key = elements.get(0);
        String value = elements.get(1);




        CacheStore.put(key, value, expiryInMilliSeconds);

        String sb = SIMPLE_STRING.getValue() + "OK" + TERMINATE.getValue();

        return sb;
    }

    private void setExpiryInMilliSeconds(int expiryInMilliSeconds) {

    }
}
