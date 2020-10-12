package ch.riccardo.patane.nio2;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static java.nio.file.StandardOpenOption.WRITE;

/**
 * WARNING: works only on Linux, not on Windows.
 * Please DELETE the archive-test.zip file in your home directory after running this method.
 */
public class JarFileSystemOperations {

    private static final String HOME_DIR = System.getProperty("user.home");
    private static final String ZIP_FILE_NAME = "archive-test.zip";
    private static final Path PATH = Paths.get(HOME_DIR + "/" + ZIP_FILE_NAME);

    public static void main(String[] args) throws IOException {

        URI zipURI = URI.create("jar:file://" + PATH);
        Map<String, String> options = new HashMap<>();
        options.put("create", "true");
        // If needed you can put encoding into the options map.

        boolean fileExists = Files.exists(PATH);
        if (fileExists) {
            System.out.println("Deleting file: " + PATH);
            Files.delete(PATH);
        }

        System.out.println("Creating new file: " + PATH);
        System.out.println("Please delete test file '" + PATH + "' right now.");
        try (FileSystem zipFS = FileSystems.newFileSystem(zipURI, options)) {
            Path dir = zipFS.getPath("files/");
            zipFS.provider().createDirectory(dir);
            Path aesop = Paths.get("files/io/aesop.txt");
            Path target = zipFS.getPath("files/aesop-compressed.txt");
            Files.copy(aesop, target);

            Path binDir = zipFS.getPath("bin/");
            zipFS.provider().createDirectory(binDir);
            Path binFile = zipFS.getPath("bin/ints.bin");

            OutputStream os = zipFS.provider().newOutputStream(binFile, CREATE_NEW, WRITE);
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeInt(10);
            dos.writeInt(20);
            dos.writeInt(30);
            dos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
