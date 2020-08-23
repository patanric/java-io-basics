package ch.riccardo.patane.objectSerialization.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;

public class Person implements Serializable {

    private String name;
    private int age;
    // transient keyword avoids serialization of field.
    private transient String ignoredField;

    public Person(String name, int age, String ignoredField) {
        this.name = name;
        this.age = age;
        this.ignoredField = ignoredField;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    /**
     * writeObject and readObject are methods to override the build-in serialization methods.
     * Use this if you want to fine grain control the serialization.
     *
     * @param oos
     * @throws Exception
     */
    private void writeObject(ObjectOutputStream oos) throws Exception {
        DataOutputStream dos = new DataOutputStream(oos);
        dos.writeUTF(name + "::" + age);
    }

    private void readObject(ObjectInputStream ois) throws Exception {
        DataInputStream dis = new DataInputStream(ois);
        String content = dis.readUTF();
        String[] strings = content.split("::");
        this.name = strings[0];
        this.age = Integer.parseInt(strings[1]);
    }
    /* End of writeObject readObject serialization override. */


    /**
     * writeReplace method is needed in conjunction with a Proxy. In this example, the PersonProxy.
     * The PersonProxy is the object that will be serialized.
     * The readResolve() method for deserialization is present in the actual proxy class.
     *
     *  --> Comment it to se writeObject and readObject (methods above) in action.
     *
     * @return
     * @throws ObjectStreamException
     */
    private Object writeReplace() throws ObjectStreamException {
        return new PersonProxy(this.name + "-" + this.age);
    }
    /* End of writeReplace serialization override. */
}
