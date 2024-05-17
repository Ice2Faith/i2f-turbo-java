package i2f.exception;

/**
 * @author Ice2Faith
 * @date 2024/4/22 16:46
 * @desc
 */
public class ConcurrentException extends RuntimeException {
    public ConcurrentException() {
    }

    public ConcurrentException(String message) {
        super(message);
    }

    public ConcurrentException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConcurrentException(Throwable cause) {
        super(cause);
    }

    public ConcurrentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
