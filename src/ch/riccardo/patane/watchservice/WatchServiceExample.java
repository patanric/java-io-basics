package ch.riccardo.patane.watchservice;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

/**
 * Do not forget to delete the test folder after stopping this program.
 */
public class WatchServiceExample {

    private static final String HOME_DIR = System.getProperty("user.home");
    private static final Path HOME_PATH = Paths.get(HOME_DIR);
    private static final Path TEST_PATH = Paths.get(HOME_DIR + "/test");

    public static void main(String[] args) throws IOException, InterruptedException {

        FileSystem fileSystem = HOME_PATH.getFileSystem();

        if (!Files.exists(TEST_PATH)) {
            System.out.println("Creating '" + TEST_PATH + "' directory." );
            fileSystem.provider().createDirectory(TEST_PATH);
        }

        WatchService watchService = fileSystem.newWatchService();
        WatchKey watchKey = TEST_PATH.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);

        while (watchKey.isValid()) {
            WatchKey take = watchService.take();
            List<WatchEvent<?>> watchEvents = take.pollEvents();
            for (WatchEvent<?> event : watchEvents) {
                WatchEvent.Kind<?> kind = event.kind();
                Path path = (Path) event.context();
                if (OVERFLOW == kind) {
                    continue;
                }
                if (ENTRY_CREATE == kind) {
                    System.out.println("Entry created:" + path + " - " + Files.probeContentType(path));
                }
                if (ENTRY_MODIFY == kind) {
                    System.out.println("Entry modified:" + path + " - " + Files.probeContentType(path));
                }
                if (ENTRY_DELETE == kind) {
                    System.out.println("Entry deleted:" + path + " - " + Files.probeContentType(path));
                }
            }
            take.reset();
        }
        System.out.println("watchKey is invalid");
    }

}
