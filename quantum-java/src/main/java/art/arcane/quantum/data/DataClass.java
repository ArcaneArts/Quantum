package art.arcane.quantum.data;

import art.arcane.quantum.QuantumFabric;
import art.arcane.quantum.api.Quantum;
import art.arcane.quantum.api.QuantumException;
import art.arcane.quill.collections.KList;
import art.arcane.quill.logging.L;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@Data
public class DataClass {
    private final String canonicalName;
    private final DataField[] fields;
    private final DataMethod[] methods;
    private transient final String name;

    public DataClass(String canonicalName, DataField[] fields, DataMethod[] methods)
    {
        this.canonicalName = canonicalName;
        this.fields = fields;
        this.methods = methods;
        this.name = KList.from(canonicalName.split("\\Q.\\E")).popLast();
    }

    public static DataClass readJavaClass(Class<?> c) throws QuantumException {
        QuantumFabric.status("Reading Class " + c.getCanonicalName());
        String realThreadName = Thread.currentThread().getName();
        Thread.currentThread().setName("Compile " + c.getName());
        try
        {
            L.v("Processing " + c.getCanonicalName());
            if(!c.isAnnotationPresent(Quantum.class))
            {
                L.w("Cannot process class " + c.getCanonicalName() + " it's not annotated with @Quantum");
                throw new QuantumException("Cannot process class " + c.getCanonicalName() + " it's not annotated with @Quantum");
            }

            KList<DataField> fields = new KList<>();
            Object dummyInstnace = c.getConstructor().newInstance();

            for(Field i : c.getDeclaredFields())
            {
                i.setAccessible(true);

                if(Modifier.isStatic(i.getModifiers()) || Modifier.isTransient(i.getModifiers()))
                {
                    L.v("  Skipping Field " + i.getName() + " due to being either static or transient");
                    continue;
                }

                L.v("  Processing Field " + i.getName());
                fields.add(DataField.readJavaField(i, dummyInstnace));
            }

            KList<DataMethod> methods = new KList<>();

            for(Method i : c.getDeclaredMethods())
            {
                i.setAccessible(true);

                if(Modifier.isStatic(i.getModifiers()) || !Modifier.isPublic(i.getModifiers()))
                {
                    L.v("  Skipping Method " + i.getName() + " due to being either static or non-public");
                }

                methods.add(DataMethod.readJavaMethod(i));
            }

            Thread.currentThread().setName(realThreadName);
            return new DataClass(c.getCanonicalName(), fields.toArray(new DataField[0]), methods.toArray(new DataMethod[0]));
        }

        catch(Throwable e)
        {
            Thread.currentThread().setName(realThreadName);
            throw new QuantumException("Failed with " + e.getMessage(), e);
        }
    }

    public String getName() {
        return name;
    }
}
