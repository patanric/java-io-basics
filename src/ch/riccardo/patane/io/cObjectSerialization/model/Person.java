package ch.riccardo.patane.io.cObjectSerialization.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * This class is used to show various serialization mechanisms:
 * 1. JVM default mechanism
 * 2. writeObject and readObject mechanism to fine grain controlling the serialization.
 * 3. Externalized mechanism to serialize only a object identifier.
 * 4. writeReplace and readResolve mechanism together with a proxy.
 *
 * The current active mechanism is number 2. Comment it to use the default JVM mechanism.
 */
public class Person implements Serializable {

    private String name;
    private int age;
    // transient keyword avoids serialization of field. Static fields neither are serialized because
    // serialization works on objects, not classes.
    private transient String ignoredField;

    public Person() {
    }

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
     * 2. writeObject and readObject mechanism to fine grain controlling the serialization.
     *
     * writeObject and readObject are methods to override the build-in serialization methods.
     * Comment it to use the JVM build in serialization mechanism.
     * Use this if you want to fine grain control the serialization.
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
     * 3. Externalized mechanism to serialize only a object identifier.
     *
     * Implement {@link Externalizable} and uncomment the Override annotations if you want to use these methods.
     * It is best suited if you want to serialize only the primary key and the deserialization party can retrieve the
     * object from a source with the primary key.
     */
//    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        String primaryKey = name + "---" + age;
        out.write(primaryKey.getBytes());
    }

//    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        String line = in.readLine();
        String[] strings = line.split("---");
        this.name = strings[0];
        this.age = Integer.parseInt(strings[1]);
    }
    /* End of Externalizable serialization override. */


    /**
     * 4. writeReplace and readResolve mechanism together with a proxy.
     *
     * writeReplace method is needed in conjunction with a Proxy. In this example, the PersonProxy.
     * The PersonProxy is the object that will be serialized.
     * The readResolve() method for deserialization is present in the actual proxy class.
     *
     *  ==> Since it has precedence over other methods it should be commented if not actually used.
     *
     * @return The PersonProxy that has to be used for serialization instead of the Person class.
     * @throws ObjectStreamException
     */
//    private Object writeReplace() throws ObjectStreamException {
//        return new PersonProxy(this.name + "-" + this.age);
//    }
    /* End of writeReplace serialization override. */
}
