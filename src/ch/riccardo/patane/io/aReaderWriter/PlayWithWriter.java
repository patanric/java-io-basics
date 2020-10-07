package ch.riccardo.patane.io.aReaderWriter;

import java.io.BufferedWriter;
import java.io.CharArrayWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class PlayWithWriter {

    public static void main(String[] args) {

        // Writer selection for illustration
        Writer writer = null;

        FileWriter fileWriter = null;
        CharArrayWriter charArrayWriter = null;
        StringWriter stringWriter = null;

        BufferedWriter bufferedWriter = null;
        PrintWriter printWriter = null;


        Path path = Paths.get("files/io/write-file.txt");

        try (BufferedWriter bw = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            // The PrintWriter does not to be closed because it is bound to the same file as the BufferedWriter.
            PrintWriter pw = new PrintWriter(bw);

            Calendar calendar = GregorianCalendar.getInstance();
            calendar.set(1969, Calendar.JULY, 20);
            pw.printf(Locale.ITALIAN, "Armstrong walked on the moon on: %1$tB %1$tA %1$tY\n", calendar);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
