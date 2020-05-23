package simplenem12.exception;

public class InvalidQualityException extends RuntimeException {
    public InvalidQualityException(String message) {
        super(message);
    }
    public InvalidQualityException(String message, Throwable cause) {
        super(message, cause);
    }
}
