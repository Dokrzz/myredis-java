import server.RedisServer;

import java.io.IOException;

class Main {




    static void main(String[] args) throws IOException {
        RedisServer redisServer = new RedisServer();
        redisServer.serve();
    }


}
