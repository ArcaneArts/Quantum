package art.arcane.quantum.api;

public class QuantumException extends Exception{
    public QuantumException(String message) {
        super(message);
    }

    public QuantumException(String message, Throwable t) {
        super(message, t);
    }
}
