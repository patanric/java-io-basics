package ch.riccardo.patane.nio.bSocket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class Server {

    public static void main(String[] args) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(13579));

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            System.out.println("Waiting for events...");
            int numberOfEvents = selector.select();
            System.out.printf("Got %d events\n", numberOfEvents);

            Set<SelectionKey> keys = selector.selectedKeys();
            for (SelectionKey key : keys) {

                // This if statement accepts incoming connection requests.
                // It just accepts all requests. In a real production you may want to check whether the request
                // is legit.
                if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
                    System.out.println("Accepting connection.");
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = channel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    keys.remove(key);
                }

                // This if statement reads the incoming message.
                if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
                    System.out.println("Read content");
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    socketChannel.read(byteBuffer);
                    byteBuffer.flip();
                    CharBuffer charBuffer = StandardCharsets.UTF_8.decode(byteBuffer);
                    String result = new String(charBuffer.array());

                    System.out.println("result = " + result);

                    keys.remove(key);
                    key.cancel();
                    socketChannel.close();

                }
            }
        }

    }

}
