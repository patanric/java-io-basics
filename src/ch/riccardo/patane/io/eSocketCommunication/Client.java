package ch.riccardo.patane.io.eSocketCommunication;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class Client {

    public static void main(String[] args) throws IOException {

        SocketAddress endpoint = new InetSocketAddress("localhost", 8005);
        Socket socket = new Socket();
        socket.connect(endpoint);

        OutputStream os = socket.getOutputStream();
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(os));
        pw.println("getMoons");
        pw.flush();

        InputStream is = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        LineNumberReader lnr = new LineNumberReader(isr);
        int number = Integer.parseInt(lnr.readLine());
        for (int i = 0; i < number; i++) {
            System.out.println(lnr.readLine());
        }

        socket.close();
    }
}
