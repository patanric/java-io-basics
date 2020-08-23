package ch.riccardo.patane.byteStreams;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.stream.IntStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class PlayWithByteStreams {

    InputStream inputStream = null;
    OutputStream outputStream = null;

    // for disk
    FileInputStream fileInputStream = null;
    FileOutputStream fileOutputStream = null;

    // for in-memory
    ByteArrayInputStream byteArrayInputStream = null;
    ByteArrayOutputStream byteArrayOutputStream = null;

    // for network
//        SocketInputStream socketInputStream = null;

    // adds behaviour
    BufferedInputStream bufferedInputStream = null;
    BufferedOutputStream bufferedOutputStream = null;
    GZIPInputStream gzipInputStream = null;
    GZIPOutputStream gzipOutputStream = null;
    ZipInputStream zipInputStream = null;
    ZipOutputStream zipOutputStream = null;

    // for primitive types
    DataInputStream dataInputStream = null;
    DataOutputStream dataOutputStream = null;

    // for JAR format
    JarInputStream jarInputStream = null;
    JarOutputStream jarOutputStream = null;

    public static void main(String[] args) {

        String pathName = "files/ints.bin";
        try (OutputStream os = new FileOutputStream(pathName);
             DataOutputStream dos = new DataOutputStream(os)) {

            IntStream.range(0, 1000)
                    .forEach(value -> {try{dos.writeInt(value);} catch (IOException ignored){}});

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Path path = Paths.get(pathName);
            long size = Files.size(path);
            System.out.println(path + " size: " + size);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStream is = new FileInputStream(pathName);
             DataInputStream dis = new DataInputStream(is)) {

            List<Integer> list = new ArrayList<>();
            try {
                while (true) {
                    list.add(dis.readInt());
                }
            } catch (EOFException ignored) {
            }
            System.out.println("Number of ints: "+list.size());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
