package ch.riccardo.patane.nio.bSocket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Client {

    public static void main(String[] args) throws IOException {

        InetSocketAddress address = new InetSocketAddress("localhost", 13579);
        SocketChannel socketChannel = SocketChannel.open(address);
        Socket socket = socketChannel.socket();

        CharBuffer charBuffer = CharBuffer.allocate(1024);
        charBuffer.put("REQUEST\n");
        charBuffer.flip();
        ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(charBuffer);
        socketChannel.write(byteBuffer);

        socket.close();
    }

}
