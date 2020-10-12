package ch.riccardo.patane.nio2;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.spi.FileSystemProvider;
import java.util.List;

public class DiskFileSystemOperations {

    public static void main(String[] args) throws IOException {

        List<FileSystemProvider> fileSystemProviders = FileSystemProvider.installedProviders();
        fileSystemProviders.forEach(fileSystemProvider -> System.out.println(fileSystemProvider.getScheme()));

        FileSystemProvider linuxFSP = fileSystemProviders.get(0);

        // Two ways to get the same FileSystem
        FileSystem fileSystem = FileSystems.getDefault();
        FileSystem fileSystem2 = FileSystems.getFileSystem(URI.create("file:///"));
        System.out.println("Two ways of creating the same FS? " + (fileSystem == fileSystem2));

        // These are 3 alternative ways to get the same Path. A Path is always bound to a FileSystem.
        //Path dir = Paths.get("/home/riccardo/tmp/new-dir");
        //Path dir = Paths.get(URI.create("file:///home/riccardo/tmp/new-dir"));
        //Path dir = fileSystem.getPath("/home/riccardo/tmp/new-dir");
        //linuxFSP.createDirectory(dir);

        // Root Directories are just declared elements. It does not say whether there is something to read through it or not.
        Iterable<Path> rootDirectories = fileSystem.getRootDirectories();
        rootDirectories.forEach(System.out::println);

        // File Stores is really about the mounted stores in the machine.
        Iterable<FileStore> fileStores = fileSystem.getFileStores();
        FileStore next = fileStores.iterator().next();
        System.out.println("next = " + next);


    }

}
