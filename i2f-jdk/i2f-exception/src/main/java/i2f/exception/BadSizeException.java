package i2f.exception;

/**
 * @author Ice2Faith
 * @date 2024/4/22 16:46
 * @desc
 */
public class BadSizeException extends RuntimeException {
    public BadSizeException() {
    }

    public BadSizeException(String message) {
        super(message);
    }

    public BadSizeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadSizeException(Throwable cause) {
        super(cause);
    }

    public BadSizeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
