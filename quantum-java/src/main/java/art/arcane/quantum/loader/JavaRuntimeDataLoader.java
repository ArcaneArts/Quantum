package art.arcane.quantum.loader;

import art.arcane.quantum.QuantumFabric;
import art.arcane.quantum.api.Quantum;
import art.arcane.quantum.api.QuantumException;
import art.arcane.quantum.data.DataClass;
import art.arcane.quill.collections.KList;
import art.arcane.quill.collections.KMap;
import art.arcane.quill.collections.KSet;
import art.arcane.quill.execution.parallel.BurstExecutor;
import art.arcane.quill.execution.parallel.MultiBurst;
import art.arcane.quill.io.IO;
import art.arcane.quill.tools.JarScanner;
import art.arcane.quill.tools.JarTools;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JavaRuntimeDataLoader implements DataLoader<Class<?>> {
    private KList<DataClass> data = new KList<>();

    @Override
    public KList<DataClass> getData() {
        return data;
    }

    @Override
    public void loadAll(String... packages) throws IOException, QuantumException {
        KMap<String, DataClass> dcm = new KMap<>();
        File storage = new File("root");
        storage.mkdirs();

        for(File i : storage.listFiles())
        {
            if(i.getName().endsWith(".qdc"))
            {
                DataClass dc = QuantumFabric.gson.fromJson(IO.readAll(i), DataClass.class);
                dcm.put(dc.getCanonicalName(), dc);
            }
        }

        KSet<Class<?>> clz = new KSet<>();

        for(String i : packages)
        {
            JarScanner j = new JarScanner(JarTools.getJar(getClass()), i);
            j.scan();
            clz.addAll(j.getClasses());
        }

        List<Class<?>> g = clz.parallelStream().filter((i) -> i.isAnnotationPresent(Quantum.class)).collect(Collectors.toList());

        for(Class<?> i : g)
        {
            dcm.put(i.getCanonicalName(), load(i));
        }

        BurstExecutor e = MultiBurst.burst.burst(dcm.size());

        for(DataClass i : dcm.v())
        {
            IO.writeAll(new File(storage, i.getCanonicalName() + ".qdc"), QuantumFabric.gson.toJson(i));
        }

        e.complete();
        data = dcm.v();
    }

    @Override
    public DataClass load(Class<?> data) throws QuantumException {
        return DataClass.readJavaClass(data);
    }
}
