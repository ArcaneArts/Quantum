package art.arcane.quantum.data.type;

import art.arcane.quantum.api.QuantumException;
import art.arcane.quantum.util.GeneralType;
import art.arcane.quill.collections.KList;
import art.arcane.quill.collections.KMap;
import art.arcane.quill.logging.L;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ListType extends DataType {
    private static final KMap<GeneralType, ListType> basicTypes = new KMap<GeneralType, ListType>()
            .qput(GeneralType.BOOL, new ListType(DataType.ofBool()))
            .qput(GeneralType.INT, new ListType(DataType.ofInt()))
            .qput(GeneralType.BYTE, new ListType(DataType.ofByte()))
            .qput(GeneralType.DOUBLE, new ListType(DataType.ofDouble()))
            .qput(GeneralType.FLOAT, new ListType(DataType.ofFloat()))
            .qput(GeneralType.LONG, new ListType(DataType.ofLong()))
            .qput(GeneralType.SHORT, new ListType(DataType.ofShort()))
            .qput(GeneralType.STRING, new ListType(DataType.ofString()));
    private final DataType elementType;

    public ListType(DataType elementType)
    {
        super(GeneralType.LIST);
        this.elementType = elementType;
    }

    public static DataType of(Class<?> c, KList<Class<?>> innerTypes) throws QuantumException {
        if(innerTypes.isEmpty())
        {
            L.f("Cannot process types for list");
            throw new QuantumException("Cannot parse list, no inner types annotation!");
        }

        return new ListType(DataType.of(innerTypes.pop(), innerTypes));
    }

    public static ListType ofInt() { return of(GeneralType.INT); }
    public static ListType ofByte() { return of(GeneralType.BYTE); }
    public static ListType ofBool() { return of(GeneralType.BOOL); }
    public static ListType ofDouble() { return of(GeneralType.DOUBLE); }
    public static ListType ofFloat() { return of(GeneralType.FLOAT); }
    public static ListType ofLong() { return of(GeneralType.LONG); }
    public static ListType ofShort() { return of(GeneralType.SHORT); }
    public static ListType ofString() { return of(GeneralType.STRING); }

    public static ListType of(GeneralType type) {
        return basicTypes.get(type);
    }
}
