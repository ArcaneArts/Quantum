package art.arcane.quantum.generator;

import art.arcane.quantum.api.Entangled;
import art.arcane.quantum.data.DataClass;
import art.arcane.quantum.data.DataField;
import art.arcane.quantum.data.DataMethod;
import art.arcane.quantum.data.type.ClassType;
import art.arcane.quantum.data.type.DataType;
import art.arcane.quantum.data.type.ListType;
import art.arcane.quantum.data.type.MapType;
import art.arcane.quill.collections.KList;
import art.arcane.quill.collections.KMap;
import art.arcane.quill.collections.KSet;
import art.arcane.quill.execution.J;
import art.arcane.quill.io.IO;
import art.arcane.quill.lang.Alphabet;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JavaCodeGenerator implements CodeGenerator {
    private static final String template = loadTemplate();
    private final String outputPackage;

    public JavaCodeGenerator(String outputPackage)
    {
        this.outputPackage = outputPackage;
    }

    private static String loadTemplate() {
        try {
            return IO.readAll(JavaCodeGenerator.class.getResourceAsStream("/template.java"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void generate(DataClass dataClass, File srcRoot) throws IOException {
        File path = new File(srcRoot, outputPackage.replaceAll("\\Q.\\E", "/") + "/" + dataClass.getName() + ".java");
        StringBuilder fields = new StringBuilder();
        StringBuilder methods = new StringBuilder();
        StringBuilder imports = new StringBuilder();
        Set<Class<?>> requestImport = new KSet<>();
        path.getParentFile().mkdirs();

        for(DataField i : dataClass.getFields())
        {
            fields.append(writeField(i, requestImport));
        }

        for(DataMethod i : dataClass.getMethods())
        {
            methods.append(writeMethod(i, requestImport));
        }

        imports.append("import ").append(KList.class.getCanonicalName()).append(";\n");
        imports.append("import ").append(KMap.class.getCanonicalName()).append(";\n");
        imports.append("import ").append(List.class.getCanonicalName()).append(";\n");
        imports.append("import ").append(Map.class.getCanonicalName()).append(";\n");
        imports.append("import ").append(Entangled.class.getCanonicalName()).append(";\n");
        requestImport.forEach((i) -> imports.append("import ").append(i.getCanonicalName()).append(";\n"));

        IO.writeAll(path, template
                .replaceAll("\\Q_CANONICAL_NAME_\\E", dataClass.getCanonicalName())
                .replaceAll("\\Q_NAME_\\E", dataClass.getName())
                .replaceAll("\\Q_PACKAGE_\\E", outputPackage)
                .replaceAll("\\Q//imports\\E", imports.toString())
                .replaceAll("\\Q//fields\\E", fields.toString())
                .replaceAll("\\Q//methods\\E", methods.toString())
        );
    }

    private StringBuilder writeMethod(DataMethod method, Set<Class<?>> requestImport) {
        StringBuilder s = new StringBuilder();

        s.append("    @").append("Entangled").append('\n');
        s.append("    public ").append(writeType(method.getResultType(), requestImport)).append(' ').append(method.getName()).append('(');

        for(int i = 0; i < method.getParameterTypes().length; i++)
        {
            s.append(writeType(method.getParameterTypes()[i], requestImport)).append(' ').append(Alphabet.values()[i].getChar());

            if(i < method.getParameterTypes().length-1)
            {
                s.append(',').append(' ');
            }
        }
        s.append(')').append(' ').append('{').append('\n');
        s.append("        // This is content");
        s.append('\n').append("    }").append('\n').append('\n');

        return s;
    }

    private StringBuilder writeField(DataField field, Set<Class<?>> requestImport) {
        StringBuilder f = writeTypedObject(field, requestImport);
        StringBuilder s = new StringBuilder();
        s.append("    @").append("Entangled").append('\n');
        s.append("    private ").append(writeType(field.getType(), requestImport)).append(' ').append(field.getName());

        if(!f.isEmpty()) {
            s.append(" = ").append(f);
        }

        s.append(';').append('\n');

        return s;
    }

    private StringBuilder writeType(DataType type, Set<Class<?>> requestImport) {
        return writeType(type, requestImport, true);
    }

    private StringBuilder writeTypedObject(DataField field, Set<Class<?>> requestImport)
    {
        StringBuilder s = new StringBuilder();
        Object o = field.getDefaultObject();
        switch(field.getType().getType())
        {
            case STRING -> s.append(o == null ? "" : ('\"' + o.toString() + '\"'));
            case INT, BOOL -> s.append(o == null ? "" : o.toString());
            case DOUBLE -> s.append(o == null ? "" : (o + "D"));
            case LONG -> s.append(o == null ? "" : (o + "L"));
            case SHORT -> s.append(o == null ? "" : "Short.valueOf("+o+")");
            case BYTE -> s.append(o == null ? "" : "Byte.valueOf("+o+")");
            case FLOAT -> s.append(o == null ? "" : (o + "F"));
            case LIST -> s.append("new KList<>()");
            case MAP -> s.append("new KMap<>()");
            case TYPE -> {
                if(o != null)
                {
                    J.attempt(() -> requestImport.add(Class.forName(((ClassType)field.getType()).getCanonicalName())));
                    s.append("new ").append(KList.from(((ClassType)field.getType()).getCanonicalName()
                            .split("\\Q.\\E")).popLast()).append("()");
                }
            }
        }

        return s;
    }

    private StringBuilder writeType(DataType type, Set<Class<?>> requestImport, boolean primitive) {
        StringBuilder s = new StringBuilder();
        switch(type.getType())
        {
            case NULL -> s.append(primitive ? "void" : "Object");
            case STRING -> s.append("String");
            case INT -> s.append(primitive ? "int" : "Integer");
            case DOUBLE -> s.append(primitive ? "double" : "Double");
            case LONG -> s.append(primitive ? "long" : "Long");
            case SHORT -> s.append(primitive ? "short" : "Short");
            case BYTE -> s.append(primitive ? "byte" : "Byte");
            case BOOL -> s.append(primitive ? "boolean" : "Boolean");
            case FLOAT -> s.append(primitive ? "float" : "Float");
            case LIST -> s.append("List").append('<').append(writeType(((ListType)type)
                            .getElementType(), requestImport, false)).append('>');
            case MAP -> s.append("Map").append('<')
                    .append(writeType(((MapType)type).getKeyType(),requestImport,  false)).append(", ")
                    .append(writeType(((MapType)type).getValueType(),requestImport,  false)).append('>');
            case TYPE -> {
                J.attempt(() -> requestImport.add(Class.forName(((ClassType)type).getCanonicalName())));
                s.append(KList.from(((ClassType)type).getCanonicalName().split("\\Q.\\E")).popLast());
            }
        }

        return s;
    }
}
