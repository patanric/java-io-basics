package ch.riccardo.patane.io.bByteStreams;

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
import java.util.stream.IntStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * With the GZIPOutputStream we can compress the current byte stream, but we cannot build a ZIP archive.
 * See {@link PlayWithZipFiles} for a demo on how to build ZIP archives containing files and folders.
 */
public class PlayWithByteStreamsCompressed {

    public static void main(String[] args) {

        String pathName = "files/io/ints.bin.gz";

        // Write to file:
        try (OutputStream os = new FileOutputStream(pathName);
             GZIPOutputStream gzos = new GZIPOutputStream(os);
             DataOutputStream dos = new DataOutputStream(gzos)) {

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

        // Read from file
        try (InputStream is = new FileInputStream(pathName);
             GZIPInputStream gzis = new GZIPInputStream(is);
             DataInputStream dis = new DataInputStream(gzis)) {

            List<Integer> list = new ArrayList<>();
            try {
                while (true) {
                    list.add(dis.readInt());
                }
            } catch (EOFException ignored) {
            }

            System.out.println("Number of ints: " + list.size());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
