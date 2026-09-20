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
import java.util.Iterator;
import java.util.Set;

class Main {

    private static int REDIS_PORT = 6379;

    static void main(String[] args) throws IOException {
        serve(REDIS_PORT);
  }

  private static void serve(int port) throws IOException {
      try(ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
          serverSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
          serverSocketChannel.configureBlocking(true);
          serverSocketChannel.bind(new InetSocketAddress("0.0.0.0", port));

          Selector selector = Selector.open();

          serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

          System.out.println("Redis server listening on port: " + port);

          while(true) {
              selector.select();

              Set<SelectionKey> selectionKeys = selector.selectedKeys();
              Iterator<SelectionKey> it = selectionKeys.iterator();

              while(it.hasNext()) {
                  SelectionKey key = it.next();

                  if((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
                      ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                      SocketChannel sc = ssc.accept();

                      sc.configureBlocking(false);
                      sc.register(selector, SelectionKey.OP_READ);
                  }

                  else if((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
                      // Read the data
                      SocketChannel sc = (SocketChannel) key.channel();

                      ByteBuffer readBuffer = ByteBuffer.allocate(256);
                      sc.read(readBuffer);

                      String inMessage = new String(readBuffer.array()).trim();
                      System.out.println("The message is: " + inMessage);
                  }

                  it.remove();
              }
          }
      }
  }
}
