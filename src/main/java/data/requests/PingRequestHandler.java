package data.requests;

import static enums.DataType.*;

public class PingRequestHandler implements IRequestHandler {

    @Override
    public String handle(RedisRequest redisRequest) {
        String responseMessage = "";

        responseMessage = SIMPLE_STRING + "PONG" + TERMINATE;

        return responseMessage;
    }
}
