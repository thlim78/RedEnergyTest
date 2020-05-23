package simplenem12.exception;

public class InvalidEnergyUnitException extends RuntimeException {
    public InvalidEnergyUnitException(String message) {
        super(message);
    }
    public InvalidEnergyUnitException(String message, Throwable cause) {
        super(message, cause);
    }
}
