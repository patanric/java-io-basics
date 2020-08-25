package ch.riccardo.patane.dHybridStreams;

import ch.riccardo.patane.dHybridStreams.model.Fable;
import ch.riccardo.patane.dHybridStreams.model.FableData;
import ch.riccardo.patane.dHybridStreams.util.AesopReader;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public class WritingHybridStreams {

    public static void main(String[] args) throws IOException {

        AesopReader aesopReader = new AesopReader();
        List<Fable> fables = aesopReader.readFable("files/aesop.txt");

        System.out.println("# of fables: " + fables.size());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(baos);

        // Write header with zeroes as placeholder for the offsets and the lengths.
        writeStaticTopOfHeader(fables, pw);
        int index = 1;
        for (Fable fable : fables) {
            pw.printf("%3d %7d %7d %s\n", index++, 0, 0, fable.getTitle());
        }
        pw.flush();

        // Compress and write the fable texts in the same file as the header.
        int textOffset = baos.size();
        ByteArrayOutputStream textBaos = new ByteArrayOutputStream();
        List<FableData> fableDataList = new ArrayList<>();
        int offset = textOffset;
        for (Fable fable : fables) {
            ByteArrayOutputStream fableBaos = new ByteArrayOutputStream();

            try (GZIPOutputStream gzos = new GZIPOutputStream(fableBaos)) {
                gzos.write(fable.getText().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            int length = fableBaos.size();
            textBaos.write(fableBaos.toByteArray());

            FableData fableData = new FableData(fable, offset, length);
            offset += length;

            fableDataList.add(fableData);
        }
        textBaos.close();


        baos = new ByteArrayOutputStream();
        pw = new PrintWriter(baos);

        // Replace header placeholder with real data.
        writeStaticTopOfHeader(fables, pw);
        index = 1;
        for (FableData fableData : fableDataList) {
            pw.printf("%3d %7d %7d %s\n", index++, fableData.getOffset(), fableData.getLength(), fableData.getFable().getTitle());
        }
        pw.flush();
        pw.close();

        baos.write(textBaos.toByteArray());
        baos.close();

        try (OutputStream os = new FileOutputStream("files/aesop-compressed.bin")) {
            os.write(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeStaticTopOfHeader(List<Fable> fables, PrintWriter pw) {
        pw.println("Aesop's Fables");
        pw.printf("Number of fables: %d\n", fables.size());
        pw.println("---------------------");
        pw.printf("%3s %7s %7s %s\n", "#", "Offset", "Length", "Title");
    }

}
