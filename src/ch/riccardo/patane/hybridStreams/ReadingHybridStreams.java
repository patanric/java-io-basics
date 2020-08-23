package ch.riccardo.patane.hybridStreams;

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

    public static void main(String[] args) throws IOException {

        int fableNumber = 291;
        int fableNumberOffset = 1;

        String fileName = "files/aesop-compressed.bin";
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
            System.out.println(fableLine);

            int offset = Integer.parseInt(fableLine.substring(0, 7).trim());
            int length = Integer.parseInt(fableLine.substring(7, 15).trim());
            String title = fableLine.substring(15).trim();

            bis.reset();

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
