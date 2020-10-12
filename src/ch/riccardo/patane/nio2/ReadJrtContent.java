package ch.riccardo.patane.nio2;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * In JRT there are two top-level directories: modules and packages.
 * <p>
 * JRT was introduced in the Java language due to the Project Jigsaw module system. The removal of rt.jar had several
 * implications that had to be overcome. The addition of the JRT file system was the solution.
 * Prior to Java 9 the JRT file system was not available.
 */
public class ReadJrtContent {

    public static void main(String[] args) throws IOException {

        FileSystem jrtFS = FileSystems.getFileSystem(URI.create("jrt:/"));
        Path path = jrtFS.getPath("/");

        boolean exists = Files.exists(path);
        System.out.println("exists = " + exists);

        Stream<Path> pathStream = Files.find(path, Integer.MAX_VALUE, (p, attr) -> true);
        System.out.println("pathStream.count() = " + pathStream.count());

        Stream<Path> pathStream2 = Files.find(path, 1, (p, attr) -> true);
        pathStream2.forEach(System.out::println);

    }

}
