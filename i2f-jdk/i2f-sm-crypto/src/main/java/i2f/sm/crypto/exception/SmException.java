package i2f.sm.crypto.exception;

/**
 * @author Ice2Faith
 * @date 2025/8/13 17:57
 */
public class SmException extends RuntimeException {
    public SmException() {
    }

    public SmException(String message) {
        super(message);
    }

    public SmException(String message, Throwable cause) {
        super(message, cause);
    }

    public SmException(Throwable cause) {
        super(cause);
    }
}
