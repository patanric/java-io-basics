package ch.riccardo.patane.io.cObjectSerialization.model;

import java.io.ObjectStreamException;
import java.io.Serializable;

public class PersonProxy implements Serializable {

    private String name;

    public PersonProxy(String name) {
        this.name = name;
    }

    /**
     * This readResolve() method is used for deserialization.
     * The corresponding writeReplace() method used for serialization is in the Person class.
     *
     * @return the Person class filled with data from the PersonProxy
     * @throws ObjectStreamException
     */
    private Object readResolve() throws ObjectStreamException {
        String[] strings = this.name.split("-");
        return new Person(strings[0], Integer.parseInt(strings[1]), "ign F3");
    }
}
