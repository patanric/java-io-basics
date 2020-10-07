package ch.riccardo.patane.io.aReaderWriter;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class PlayWithReader {

    public static void main(String[] args) throws IOException {

        // Reader selection for illustration
        Reader reader = null;

        FileReader fileReader = null;
        CharArrayReader charArrayReader = null;
        StringReader stringReader = null;

        BufferedReader bufferedReader = null;
        LineNumberReader lineNumberReader = null;
        PushbackReader pushbackReader = null;


        String file = "files/io/antcrick.txt";

        try (Stream<String> lines = Files.newBufferedReader(Paths.get(file)).lines()) {

            lines.forEach(System.out::println);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
