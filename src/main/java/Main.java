import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

class Main {

    final private static int REDIS_PORT = 6379;
    final private static String msgPostfix = "\r\n";
    final private static String msgPrefix = "$";


    static void main(String[] args) throws IOException {
        serve(REDIS_PORT);
    }

    private static void serve(int port) throws IOException {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress("0.0.0.0", port));

            Selector selector = Selector.open();

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Redis server listening on port: " + port);

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
                        System.out.println(incomingMsg);

                        writeResponse(key);
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

    private static void writeResponse(SelectionKey key) throws IOException {
        SocketChannel sc = (SocketChannel) key.channel();
        byte[] responseMsg = "PONG".getBytes();
        int responseByteCount = responseMsg.length;

        String responseBytes = msgPrefix + responseByteCount + msgPostfix;
        String responseMessage = "PONG" + msgPostfix;

        ByteBuffer writeBuffer = ByteBuffer.wrap(responseBytes.getBytes());
        sc.write(writeBuffer);

        writeBuffer.clear();

        writeBuffer = ByteBuffer.wrap(responseMessage.getBytes());
        sc.write(writeBuffer);
    }
}
