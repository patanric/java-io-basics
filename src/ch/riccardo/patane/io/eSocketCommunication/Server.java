package ch.riccardo.patane.io.eSocketCommunication;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(8005);
        while (true) {
            System.out.println("Waiting for requests.");

            Socket socket = serverSocket.accept();
            System.out.println("Request accepted.");

            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            LineNumberReader lnr = new LineNumberReader(isr);
            String request = lnr.readLine();
            System.out.println("Request: " + request);

            if (request.equals("getMoons")) {
                OutputStream os = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(os));
                pw.println("3");
                pw.println("Moon");
                pw.println("Io");
                pw.println("Europa");
                pw.flush();
            }
        }
    }
}
