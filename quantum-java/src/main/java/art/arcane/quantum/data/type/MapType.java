package art.arcane.quantum.data.type;

import art.arcane.quantum.api.QuantumException;
import art.arcane.quantum.util.GeneralType;
import art.arcane.quill.collections.KList;
import art.arcane.quill.logging.L;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MapType extends DataType {
    private final DataType keyType;
    private final DataType valueType;

    public MapType(DataType keyType, DataType valueType)
    {
        super(GeneralType.MAP);
        this.keyType = keyType;
        this.valueType = valueType;
    }

    public static MapType of(Class<?> c, KList<Class<?>> innerTypes) throws QuantumException {
        if(innerTypes.isEmpty())
        {
            L.f("Cannot process types");
            throw new QuantumException("Cannot parse map, no inner types annotation!");
        }

        if(innerTypes.size() < 2)
        {
            L.f("Cannot process types");
            throw new QuantumException("Cannot parse map, Not enough inner types!");
        }

        return new MapType(DataType.of(innerTypes.pop(), innerTypes), DataType.of(innerTypes.pop(), innerTypes));
    }
}
