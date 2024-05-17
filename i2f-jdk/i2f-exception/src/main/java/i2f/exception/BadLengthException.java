package i2f.exception;

/**
 * @author Ice2Faith
 * @date 2024/4/22 16:46
 * @desc
 */
public class BadLengthException extends RuntimeException {
    public BadLengthException() {
    }

    public BadLengthException(String message) {
        super(message);
    }

    public BadLengthException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadLengthException(Throwable cause) {
        super(cause);
    }

    public BadLengthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
