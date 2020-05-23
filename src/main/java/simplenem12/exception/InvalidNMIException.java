package simplenem12.exception;

public class InvalidNMIException extends RuntimeException {
    public InvalidNMIException(String message) {
        super(message);
    }
    public InvalidNMIException(String message, Throwable cause) {
        super(message, cause);
    }
}
