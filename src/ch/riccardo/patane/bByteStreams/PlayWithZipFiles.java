package ch.riccardo.patane.bByteStreams;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * With ZipOutputStream we can construct a ZIP archive with files and folders in it.
 * This is not possible with the GZIPOutputStream.
 */
public class PlayWithZipFiles {

    public static void main(String[] args) {

        String fileName = "files/archive.zip";

        // Write archive
        try(OutputStream os = new FileOutputStream(fileName);
            ZipOutputStream zos = new ZipOutputStream(os);
            DataOutputStream dos = new DataOutputStream(zos)) {

            ZipEntry dirEntry = new ZipEntry("dir/");
            zos.putNextEntry(dirEntry);
            ZipEntry binFileEntry = new ZipEntry("dir/ints.bin");
            zos.putNextEntry(binFileEntry);
            IntStream.range(0, 1000)
                    .forEach(value -> {try{dos.writeInt(value);} catch (IOException ignored){}});

            ZipEntry otherDirEntry = new ZipEntry("text/");
            zos.putNextEntry(otherDirEntry);
            ZipEntry textFileEntry = new ZipEntry("text/file.txt");
            zos.putNextEntry(textFileEntry);
            dos.writeUTF("This is some text content.");

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read archive
        try(InputStream is = new FileInputStream(fileName);
            ZipInputStream zis = new ZipInputStream(is);
            DataInputStream dis = new DataInputStream(zis)) {

            ZipEntry entry = zis.getNextEntry();
            while (entry != null) {
                if (entry.isDirectory()) {
                    System.out.println("Reading directory " + entry);
                } else {
                    readAndPrintFile(dis, entry);
                }
                entry = zis.getNextEntry();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void readAndPrintFile(DataInputStream dis, ZipEntry entry) throws IOException {
        System.out.println("Reading file " + entry);
        if (entry.getName().endsWith(".bin")) {
            countAndPrintNumberOfIntsInTheFile(dis);

        } else if (entry.getName().endsWith(".txt")) {
            printTextInTheFile(dis);
        }
    }

    private static void countAndPrintNumberOfIntsInTheFile(DataInputStream dis) throws IOException {
        List<Integer> list = new ArrayList<>();
        try {
            while (true) {
                list.add(dis.readInt());
            }
        } catch (EOFException ignored) { }
        System.out.println("Number of ints in the file: "+list.size());
    }

    private static void printTextInTheFile(DataInputStream dis) throws IOException {
        String content = dis.readUTF();
        System.out.println("Content: " + content);
    }

}
