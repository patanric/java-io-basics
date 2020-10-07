package ch.riccardo.patane.io.dHybridStreams.util;

import ch.riccardo.patane.io.dHybridStreams.model.Fable;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AesopReader {

    public List<Fable> readFable(String fileName) {

        List<Fable> fables = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileName))) {

            reader.readLine();
            reader.readLine();
            reader.readLine();

            String title = reader.readLine();
            reader.readLine();

            while (title != null) {
                Fable fable = new Fable();
                fable.setTitle(title);

                // text
                boolean done = false;
                String nextLine = reader.readLine();
                while (!done) {
                    fable.addText(nextLine);
                    nextLine = reader.readLine();

                    if (nextLine == null) {
                        done = true;
                    } else if (nextLine.isBlank()) {
                        nextLine = reader.readLine();
                        if (nextLine == null || nextLine.isBlank()) {
                            done = true;
                        }
                    }
                }

                fables.add(fable);

                // title
                if (nextLine != null) {
                    title = reader.readLine();
                    reader.readLine();
                } else {
                    title = null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fables;
    }

}
