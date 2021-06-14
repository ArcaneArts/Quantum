package art.arcane.quantum.data.type;

import art.arcane.quantum.QuantumFabric;
import art.arcane.quantum.api.QuantumException;
import art.arcane.quantum.api.QuantumTypes;
import art.arcane.quantum.util.GeneralType;
import art.arcane.quill.collections.KList;
import art.arcane.quill.collections.KMap;
import art.arcane.quill.logging.L;
import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;
import java.util.Map;

@Data
public class DataType {
    private static final KMap<GeneralType, DataType> basicTypes = new KMap<GeneralType, DataType>()
            .qput(GeneralType.BOOL, new DataType(GeneralType.BOOL))
            .qput(GeneralType.INT, new DataType(GeneralType.INT))
            .qput(GeneralType.BYTE, new DataType(GeneralType.BYTE))
            .qput(GeneralType.DOUBLE, new DataType(GeneralType.DOUBLE))
            .qput(GeneralType.FLOAT, new DataType(GeneralType.FLOAT))
            .qput(GeneralType.LONG, new DataType(GeneralType.LONG))
            .qput(GeneralType.SHORT, new DataType(GeneralType.SHORT))
            .qput(GeneralType.NULL, new DataType(GeneralType.NULL))
            .qput(GeneralType.STRING, new DataType(GeneralType.STRING));

    private final GeneralType type;

    public DataType()
    {
        this(GeneralType.NULL);
    }

    public DataType(GeneralType type)
    {
        this.type = type;
    }

    public static DataType of(Field i) throws QuantumException {
        KList<Class<?>> g = new KList<>();

        QuantumTypes t = i.getDeclaredAnnotation(QuantumTypes.class);
        if(t != null)
        {
            g.add(t.value());
        }

        return of(i.getType(), g);
    }

    public static DataType of(Class<?> c, KList<Class<?>> innerTypes) throws QuantumException {
        if(c.equals(int.class) || c.equals(Integer.class))
        {
            return ofInt();
        }

        if(c.equals(long.class) || c.equals(Long.class))
        {
            return ofLong();
        }

        if(c.equals(double.class) || c.equals(Double.class))
        {
            return ofDouble();
        }

        if(c.equals(short.class) || c.equals(Short.class))
        {
            return ofShort();
        }

        if(c.equals(byte.class) || c.equals(Byte.class))
        {
            return ofByte();
        }

        if(c.equals(boolean.class) || c.equals(Boolean.class))
        {
            return ofBool();
        }

        if(c.equals(String.class))
        {
            return ofString();
        }

        if(c.equals(float.class) || c.equals(Float.class))
        {
            return ofFloat();
        }

        if(c.isAssignableFrom(List.class) || List.class.isAssignableFrom(c))
        {
            return ListType.of(c, innerTypes);
        }

        if(c.isAssignableFrom(Map.class) || Map.class.isAssignableFrom(c))
        {
            return MapType.of(c, innerTypes);
        }

        return new ClassType(c.getCanonicalName());
    }

    public ListType asList() {
        return ListType.of(getType());
    }

    public static DataType ofNull() { return of(GeneralType.NULL); }
    public static DataType ofInt() { return of(GeneralType.INT); }
    public static DataType ofByte() { return of(GeneralType.BYTE); }
    public static DataType ofBool() { return of(GeneralType.BOOL); }
    public static DataType ofDouble() { return of(GeneralType.DOUBLE); }
    public static DataType ofFloat() { return of(GeneralType.FLOAT); }
    public static DataType ofLong() { return of(GeneralType.LONG); }
    public static DataType ofShort() { return of(GeneralType.SHORT); }
    public static DataType ofString() { return of(GeneralType.STRING); }
    public static DataType of(GeneralType g) { return basicTypes.get(g); }
}
