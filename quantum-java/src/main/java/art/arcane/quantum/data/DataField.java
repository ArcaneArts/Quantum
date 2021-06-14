package art.arcane.quantum.data;

import art.arcane.quantum.QuantumFabric;
import art.arcane.quantum.api.QuantumException;
import art.arcane.quantum.api.QuantumReadOnly;
import art.arcane.quantum.data.type.DataType;
import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@Data
public class DataField {
    private final String name;
    private final DataType type;
    private final boolean readOnly;
    private final Object defaultObject;

    public DataField(String name, DataType type, boolean readOnly, Object defaultObject)
    {
        this.name = name;
        this.type = type;
        this.readOnly = readOnly;
        this.defaultObject = defaultObject;
    }

    public DataField(String name, DataType type, Object defaultObject)
    {
        this(name, type, false, defaultObject);
    }

    public static DataField readJavaField(Field i, Object instance) throws QuantumException {

        i.setAccessible(true);
        QuantumFabric.status("Reading Field " + i.getName() + " in " + i.getDeclaringClass().getCanonicalName());
        try {
            return new DataField(i.getName(), DataType.of(i),  Modifier.isFinal(i.getModifiers()) || i.isAnnotationPresent(QuantumReadOnly.class), i.get(instance));
        } catch (IllegalAccessException e) {
            throw new QuantumException("Reflection Error", e);
        }
    }
}
