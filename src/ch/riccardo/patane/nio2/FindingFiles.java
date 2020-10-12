package ch.riccardo.patane.nio2;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.stream.Stream;

public class FindingFiles {

    private static final Path PATH_IDEA_PROJECTS = Paths.get(System.getProperty("user.home") + "/IdeaProjects");

    public static void main(String[] args) throws IOException {

        Path path = Paths.get(URI.create("file://" + PATH_IDEA_PROJECTS));
        boolean exists = Files.exists(path);
        System.out.println("Directory " + PATH_IDEA_PROJECTS + " exists: " + exists);

        Stream<Path> pathStream = Files.find(path, Integer.MAX_VALUE, (p, attr) -> true);
        System.out.println("File count total = " + pathStream.count());

        Stream<Path> pathStream2 = Files.find(path, Integer.MAX_VALUE, (p, attr) -> p.toString().endsWith(".iml"));
        System.out.println("File count ending with .iml = " + pathStream2.count());

        Calendar c = GregorianCalendar.getInstance();
        c.set(2020, Calendar.MARCH, 24, 0, 0, 0);
        long date = c.getTimeInMillis();
        Stream<Path> pathStream3 = Files.find(path, Integer.MAX_VALUE, (p, attr) -> attr.creationTime().toMillis() > date);
        System.out.println("File count newer than " + c.getTime().toString() +" = " + pathStream3.count());

    }
}
