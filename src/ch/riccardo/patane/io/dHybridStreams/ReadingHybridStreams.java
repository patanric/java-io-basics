package ch.riccardo.patane.io.dHybridStreams;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;

public class ReadingHybridStreams {

    /**
     * Run {@link WritingHybridStreams} before running this method.
     */
    public static void main(String[] args) throws IOException {

        int fableNumber = 255;
        int fableNumberOffset = 3;

        String fileName = "files/io/aesop-compressed.bin";
        int fileSize = (int) Files.size(Paths.get(fileName));

        try (InputStream is = new FileInputStream(fileName);
             BufferedInputStream bis = new BufferedInputStream(is);
             InputStreamReader isr = new InputStreamReader(bis);
             LineNumberReader lnr = new LineNumberReader(isr)) {

            bis.mark(fileSize + 1); // +1 to ensure the mark remains valid after reaching the end of file.

            lnr.readLine();
            while (lnr.getLineNumber() < fableNumber + fableNumberOffset) {
                lnr.readLine();
            }

            String fableLine = lnr.readLine();
            int number = Integer.parseInt(fableLine.substring(0, 3).trim());
            int offset = Integer.parseInt(fableLine.substring(3, 11).trim());
            int length = Integer.parseInt(fableLine.substring(11, 19).trim());
            String title = fableLine.substring(19).trim();
            System.out.printf("%d %s\n", number, title);

            bis.reset();

            // Fast forward to the right byte in the file:
            int totalSkipped = (int) bis.skip(offset);
            while (totalSkipped < offset) {
                totalSkipped += (int) bis.skip(offset - totalSkipped);
            }

            byte[] bytes = new byte[4096];
            bis.read(bytes, 0, length);

            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            GZIPInputStream gzis = new GZIPInputStream(bais);

            byte[] bytes2 = new byte[4096];
            gzis.read(bytes2);
            String text = new String(bytes2);
            System.out.println(text);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
