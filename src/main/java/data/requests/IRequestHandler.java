package data.requests;

public interface IRequestHandler {

    public abstract String handle(RedisRequest redisRequest);

}
