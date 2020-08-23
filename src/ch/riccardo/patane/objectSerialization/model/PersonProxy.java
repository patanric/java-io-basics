package ch.riccardo.patane.objectSerialization.model;

import java.io.ObjectStreamException;
import java.io.Serializable;

public class PersonProxy implements Serializable {

    private String name;

    public PersonProxy(String name) {
        this.name = name;
    }

    /**
     * This readResolve() method is used for deserialization.
     * The writeReplace() method used for serialization is in the Person class.
     *
     * @return
     * @throws ObjectStreamException
     */
    private Object readResolve() throws ObjectStreamException {
        String[] strings = this.name.split("-");
        return new Person(strings[0], Integer.parseInt(strings[1]), "ign F3");
    }
}
