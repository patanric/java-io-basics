package ch.riccardo.patane.nio.aAsyncBuffer;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class ReadWriteBuffer {

    public static void main(String[] args) throws IOException {

        // fill Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 1024);
        byteBuffer.putInt(10);
        byteBuffer.putInt(20);
        byteBuffer.putInt(30);

        System.out.println("byteBuffer.position() = " + byteBuffer.position());
        System.out.println("byteBuffer.limit() = " + byteBuffer.limit());

        byteBuffer.flip();

        Path path = Paths.get("files/nio/ints.bin");
        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {

            // WRITE to file
            fileChannel.write(byteBuffer);

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Files.size(path) = " + Files.size(path));


        // READ the file back to Buffer
        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ)) {

            byteBuffer.clear();
            fileChannel.read(byteBuffer);

        } catch (IOException e) {
            e.printStackTrace();
        }

        byteBuffer.flip();
        System.out.println("byteBuffer.position() = " + byteBuffer.position());
        System.out.println("byteBuffer.limit() = " + byteBuffer.limit());

        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        List<Integer> ints = new ArrayList<>();
        try {
            while (true) {
                ints.add(intBuffer.get());
            }
        } catch (BufferUnderflowException e) {}

        System.out.println("ints.size() = " + ints.size());
        ints.forEach(System.out::println);
    }
}
