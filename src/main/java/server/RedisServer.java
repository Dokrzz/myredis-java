package server;

import data.requests.*;
import resp.Parser;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import java.util.logging.Logger;


public class RedisServer {

    private static final int DEFAULT_REDIS_PORT = 6379;

    static Logger logger = Logger.getLogger(RedisServer.class.getName());


    public void serve() throws IOException {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress("0.0.0.0", DEFAULT_REDIS_PORT));

            Selector selector = Selector.open();

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            logger.info("Redis server listening on port: " + DEFAULT_REDIS_PORT);

            while (true) {
                selector.select();

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();

                while (it.hasNext()) {
                    SelectionKey key = it.next();

                    if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
                        acceptNewConnection(key, selector);
                    } else if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
                        String incomingMsg = readNewMessage(key);
                        RedisRequest request = Parser.parse(incomingMsg);

                        writeResponse(key, request);
                    }

                    it.remove();
                }
            }
        }
    }

    private static void acceptNewConnection(SelectionKey key, Selector selector) throws IOException {

        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        SocketChannel sc = ssc.accept();

        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_READ);
    }

    private static String readNewMessage(SelectionKey key) throws IOException {
        // Read the data
        SocketChannel sc = (SocketChannel) key.channel();

        ByteBuffer readBuffer = ByteBuffer.allocate(256);
        sc.read(readBuffer);

        return new String(readBuffer.array()).trim();
    }

    private static void writeResponse(SelectionKey key, RedisRequest redisRequest) throws IOException {
        SocketChannel sc = (SocketChannel) key.channel();

        logger.info(redisRequest.toString());

        String responseMessage = switch(redisRequest.getCommand()) {
            case PING -> {
                var requestHandler = new PingRequestHandler();
                yield requestHandler.handle(redisRequest);
            }
            case ECHO -> {
                var requestHandler = new EchoRequestHandler();
                yield requestHandler.handle(redisRequest);
            }
            case SET -> {
                var requestHandler = new SetRequestHandler();
                yield requestHandler.handle(redisRequest);
            }
            case GET -> {
                var requestHandler = new GetRequestHandler();
                yield requestHandler.handle(redisRequest);
            }
            default ->  "";
        };

        ByteBuffer writeBuffer = ByteBuffer.wrap(responseMessage.getBytes());
        sc.write(writeBuffer);
    }
}
