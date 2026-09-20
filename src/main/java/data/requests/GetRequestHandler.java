package data.requests;

import data.CacheStore;

import java.util.List;
import java.util.Optional;

import static enums.DataType.*;


public class GetRequestHandler implements IRequestHandler {

    @Override
    public String handle(RedisRequest redisRequest) {
        List<String> elements = redisRequest.getElements();

        if(elements.size() != 1) {
            throw new RuntimeException();
        }

        String key = elements.getFirst();

        StringBuilder sb = new StringBuilder();
        sb.append(BULK_STRING.getValue());

        Optional<String> valueOptional = CacheStore.get(key);
        String value = valueOptional.orElse("");

        int size;
        if(!value.isEmpty()) {
            sb.append(value.length());
        }
        else {
            sb.append(-1);
        }

        sb.append(TERMINATE.getValue());

        if(!value.isEmpty()) {
            sb.append(value);
            sb.append(TERMINATE.getValue());
        }

        return sb.toString();

    }
}
