package art.arcane.quantum.test;

import art.arcane.quantum.api.Quantum;
import art.arcane.quantum.api.QuantumTypes;
import art.arcane.quill.collections.KList;
import art.arcane.quill.collections.KMap;

import java.util.List;
import java.util.Map;

@Quantum
public class QuantumTest {
    private String aString = "default";
    private int anpInt = 1;
    private double apDouble = 2;
    private short apShort = 3;
    private boolean apBoolean = false;
    private long apLong = 4;
    private byte apByte = 5;
    private float apFloat = 6;
    private Integer anInt = 7;
    private Double aDouble = 8D;
    private Short aShort = 9;
    private Boolean aBoolean = true;
    private Long aLong = 10l;
    private Byte aByte = 11;
    private Float aFloat = 12f;
    private final int someReadOnlyValue = 36;
    private QuantumTest self;

    @QuantumTypes(Integer.class)
    private List<Integer> intList;

    @QuantumTypes(String.class)
    private KList<String> intkList;

    @QuantumTypes({Long.class, Double.class})
    private Map<Long, Double> mapTest;

    @QuantumTypes({Float.class, Byte.class})
    private KMap<Float, Byte> kmapTest;

    @QuantumTypes({String.class, List.class, Byte.class})
    private KMap<String, KList<Byte>> nestedTest1;

    public void methodVoid()
    {

    }

    public int methodInt(int v)
    {
        return v;
    }

    @QuantumTypes({String.class, Integer.class, String.class})
    public KMap<String, Integer> typeMethod(String f, KList<String> g)
    {
        return null;
    }

    @QuantumTypes({String.class, String.class, KList.class, Double.class})
    public KList<String> typeMethod2(KMap<String, KList<Double>> map)
    {
        return null;
    }
}
