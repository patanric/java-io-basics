package ch.riccardo.patane.io.cObjectSerialization;

import ch.riccardo.patane.io.cObjectSerialization.model.Person;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * This class serializes and deserializes data objects. Notice that this class does NOT decide HOW to serialize.
 * How the serialization is done can be seen in the {@link Person} class.
 */
public class DataObjects {

    public static void main(String[] args) {

        String fileName = "files/io/person.bin";

        Person p1 = new Person("Nalleli", 41, "ign F1");
        Person p2 = new Person("Maria Luisa", 62, "ign F2");

        List<Person> persons = new ArrayList<>();
        persons.add(p1);
        persons.add(p2);

        // Write file
        try (OutputStream os = new FileOutputStream(fileName);
             ObjectOutputStream oos = new ObjectOutputStream(os)) {

            oos.writeObject(persons);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Verify file size
        try {
            System.out.println(fileName + ": " + Files.size(Paths.get(fileName)) + " Bytes");
        } catch (IOException ignored) { }

        // Read file
        try (InputStream is = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(is)) {

            @SuppressWarnings("unchecked")
            List<Person> list = (List<Person>) ois.readObject();
            list.forEach(System.out::println);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
