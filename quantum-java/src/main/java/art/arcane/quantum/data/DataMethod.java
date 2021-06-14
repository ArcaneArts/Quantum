package art.arcane.quantum.data;

import art.arcane.quantum.QuantumFabric;
import art.arcane.quantum.api.QuantumException;
import art.arcane.quantum.api.QuantumTypes;
import art.arcane.quantum.data.type.DataType;
import art.arcane.quill.collections.KList;
import lombok.Data;

import java.lang.reflect.Method;

@Data
public class DataMethod {
    private final String name;
    private final DataType[] parameterTypes;
    private final DataType resultType;

    public DataMethod(String name, DataType[] parameterTypes, DataType resultType)
    {
        this.name = name;
        this.parameterTypes = parameterTypes;
        this.resultType = resultType;
    }

    public static DataMethod readJavaMethod(Method i) throws QuantumException {
        QuantumFabric.status("Reading Method " + i.getName() + " in " + i.getDeclaringClass().getCanonicalName());
        KList<Class<?>> types = i.isAnnotationPresent(QuantumTypes.class) ? KList.from(i.getDeclaredAnnotation(QuantumTypes.class).value()) : new KList<>();
        DataType rt = DataType.of(i.getReturnType(), types);
        KList<DataType> params = new KList<>();

        for(Class<?> x : i.getParameterTypes())
        {
            params.add(DataType.of(x, types));
        }

        return new DataMethod(i.getName(), params.toArray(new DataType[0]), rt);
    }
}
