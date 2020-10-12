package ch.riccardo.patane.nio2;

import java.io.IOException;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

/**
 * This is an example that shows how to implement the FileVisitor interface to do more complicated operations
 * on a FileSystem.
 */
public class VisitingDirectories {

    public static void main(String[] args) throws IOException {
        // count # of empty directories
        // count # files / type

        Path path = Paths.get(URI.create("file:///home/riccardo/IdeaProjects"));
        CustomFileVisitor visitor = new CustomFileVisitor();
        Files.walkFileTree(path, visitor);
        System.out.println("visitor.getEmptyDirs() = " + visitor.getEmptyDirs());
        System.out.println("File types:");
        visitor.getFileTypes().forEach((key, value) -> System.out.println(key + " - " + value));

    }

    static class CustomFileVisitor implements FileVisitor<Path> {

        private long emptyDirs = 0L;
        private final Map<String, Long> fileTypes = new HashMap<>();

        public long getEmptyDirs() {
            return emptyDirs;
        }

        public Map<String, Long> getFileTypes() {
            return fileTypes;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dir);
            boolean hasNext = directoryStream.iterator().hasNext();
            if (!hasNext) {
                emptyDirs++;
                return FileVisitResult.SKIP_SUBTREE;
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            String type = Files.probeContentType(file);
            fileTypes.merge(type, 1L, Long::sum);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }
    }

}
