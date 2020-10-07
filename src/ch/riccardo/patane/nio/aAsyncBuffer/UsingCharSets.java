package ch.riccardo.patane.nio.aAsyncBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class UsingCharSets {

    public static void main(String[] args) throws IOException {

        Charset latin1 = StandardCharsets.ISO_8859_1;
        Charset utf8 = StandardCharsets.UTF_8;

        String string = "This is a string with the special character ü. Try to play with latin1 and utf8!";
        System.out.println("String length = " + string.length());
        CharBuffer charBuffer = CharBuffer.allocate(1024 * 1024);

        charBuffer.put(string);
        charBuffer.flip();

        ByteBuffer buffer = utf8.encode(charBuffer);
        Path path = Paths.get("files/nio/utf8.bin");
        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
            fileChannel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("File size = " + Files.size(path));


        buffer.clear();
        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ)) {
            fileChannel.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer.flip();

        charBuffer.clear();
        charBuffer = utf8.decode(buffer);

        System.out.println("charBuffer.position() = " + charBuffer.position());
        System.out.println("charBuffer.limit() = " + charBuffer.limit());

        String result = new String(charBuffer.array());

        System.out.println("text from file = '" + result + "'");
    }

}
