package data.requests;

import java.util.List;

import static enums.DataType.*;

public class EchoRequestHandler implements IRequestHandler {
    private RedisRequest redisRequest;

    public EchoRequestHandler() {
    }

    @Override
    public String handle(RedisRequest redisRequest) {
        StringBuilder sb = new StringBuilder();
        sb.append(BULK_STRING);

        List<String> elements = redisRequest.getElements();
        for (int i = 0; i < elements.size(); i++) {
            String s = elements.get(i);
            if(i == 0) {
                sb.append(s.length());
                sb.append(TERMINATE);
            }

            sb.append(s);
            sb.append(TERMINATE);
        }

        return sb.toString();
    }
}
