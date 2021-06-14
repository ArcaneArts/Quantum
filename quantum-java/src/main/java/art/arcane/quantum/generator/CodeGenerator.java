package art.arcane.quantum.generator;

import art.arcane.quantum.data.DataClass;

import java.io.File;
import java.io.IOException;

public interface CodeGenerator {
    public void generate(DataClass dataClass, File srcRoot) throws IOException;
}
