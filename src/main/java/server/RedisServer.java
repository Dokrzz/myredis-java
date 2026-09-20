package server;

import resp.Command;
import resp.Parser;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class RedisServer {

    final private static int DEFAULT_REDIS_PORT = 6379;
    final private static String msgPostfix = "\r\n";
    final private static String msgPrefix = "$";
    
    
    public void serve() throws IOException {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress("0.0.0.0", DEFAULT_REDIS_PORT));

            Selector selector = Selector.open();

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Redis server listening on port: " + DEFAULT_REDIS_PORT);

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
                        HashMap<Command, List<String>> commandToArgs = Parser.parse(incomingMsg);

                        writeResponse(key, commandToArgs);
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

    private static void writeResponse(SelectionKey key, HashMap<Command, List<String>> commandToArgs) throws IOException {
        SocketChannel sc = (SocketChannel) key.channel();

        String responseMessage = "";

        if(commandToArgs.containsKey(Command.PING)) {
//            byte[] responseMsg = "PONG".getBytes();
//            int responseByteCount = responseMsg.length;
//
//            String responseBytes = msgPrefix + responseByteCount + msgPostfix;
//            String responseMessage = "+PONG" + msgPostfix;
        }

        else if(commandToArgs.containsKey(Command.ECHO)){
            StringBuilder sb = new StringBuilder();
            sb.append(msgPrefix);

            List<String> arguments = commandToArgs.get(Command.ECHO);


            for (int i = 0; i < arguments.size(); i++) {
                String s = arguments.get(i);
                System.out.println("i -> " + i);
                if(i == 0) {
                    sb.append(s.length());
                    sb.append(msgPostfix);
                    continue;
                }

                sb.append(s);
                sb.append(msgPostfix);
            }

            responseMessage = sb.toString();
        }


        ByteBuffer writeBuffer = ByteBuffer.wrap(responseMessage.getBytes());
        sc.write(writeBuffer);
    }
}
