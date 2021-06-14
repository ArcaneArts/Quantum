package art.arcane.quantum.loader;

import art.arcane.quantum.api.QuantumException;
import art.arcane.quantum.data.DataClass;
import art.arcane.quill.collections.KList;

import java.io.IOException;

public interface DataLoader<T>
{
    public KList<DataClass> getData();

    public void loadAll(String... packages) throws IOException, QuantumException;

    public DataClass load(T data) throws QuantumException;
}
