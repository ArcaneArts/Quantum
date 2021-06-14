package art.arcane.quantum;

import art.arcane.quantum.data.DataClass;
import art.arcane.quantum.generator.CodeGenerator;
import art.arcane.quantum.generator.JavaCodeGenerator;
import art.arcane.quantum.loader.DataLoader;
import art.arcane.quantum.loader.JavaRuntimeDataLoader;
import art.arcane.quill.collections.KList;
import art.arcane.quill.collections.KMap;
import art.arcane.quill.logging.L;
import com.google.gson.Gson;
import com.sun.source.doctree.ThrowsTree;

import java.io.File;
import java.io.IOException;

public class QuantumFabric
{
    public static final Gson gson = new Gson();
    public static final KMap<Long, String> workerStatus = new KMap<>();

    public static void status(String msg)
    {
        workerStatus.put(Thread.currentThread().getId(), msg);
    }

    public static void main(String[] a)
    {
        try
        {
            KList<String> packages = new KList<>();
            DataLoader<?> loader = new JavaRuntimeDataLoader();
            packages.add("art.arcane.quantum.test");
            for(String i : a)
            {
                if(i.startsWith("-"))
                {
                    continue;
                }

                packages.add(i);
            }

            if(packages.isNotEmpty())
            {
                loader.loadAll(packages.toArray(new String[0]));
            }

            else {
                L.v("Nothing to do. Exiting");
            }

            CodeGenerator cg = new JavaCodeGenerator("test.pkg");

            for(DataClass i : loader.getData())
            {
                L.i("GEN " + i.getCanonicalName());
                cg.generate(i, new File("root/generated"));
            }

            L.i("All Tasks Complete.");
        }

        catch(Throwable e)
        {
            L.ex(e);

            for(long i : QuantumFabric.workerStatus.k())
            {
                L.f("Thread: " + i + " Last Status -> " + QuantumFabric.workerStatus.get(i));
            }
        }

        L.flush();
        System.exit(0);
    }
}
