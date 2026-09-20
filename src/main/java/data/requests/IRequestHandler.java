package data.requests;

public interface IRequestHandler {

    public String handle(RedisRequest redisRequest);
}
