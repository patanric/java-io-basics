package ch.riccardo.patane.hybridStreams;

import ch.riccardo.patane.hybridStreams.model.Fable;
import ch.riccardo.patane.hybridStreams.model.FableData;

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

        // write header
        pw.println("Aesop's Fables");
        pw.printf("%d\n", fables.size());
        for (Fable fable : fables) {
            pw.printf("%7d %7d %s\n", 0, 0, fable.getTitle());
        }

        pw.flush();
        pw.close();
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

        // write header
        pw.println("Aesop's Fables");
        pw.printf("%d\n", fables.size());
        for (FableData fableData : fableDataList) {
            pw.printf("%7d %7d %s\n", fableData.getOffset(), fableData.getLength(), fableData.getFable().getTitle());
        }
        pw.flush();

        baos.write(textBaos.toByteArray());
        baos.close();

        try (OutputStream os = new FileOutputStream("files/aesop-compressed.bin")) {
            os.write(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        pw.close();
    }

}
