package art.arcane.quantum.data.type;

import art.arcane.quantum.util.GeneralType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClassType extends DataType {
    private final String canonicalName;

    public ClassType(String canonicalName)
    {
        super(GeneralType.TYPE);
        this.canonicalName = canonicalName;
    }
}
