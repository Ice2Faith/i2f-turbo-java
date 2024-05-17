package i2f.exception;

/**
 * @author Ice2Faith
 * @date 2024/4/22 16:46
 * @desc
 */
public class ReflectException extends RuntimeException {
    public ReflectException() {
    }

    public ReflectException(String message) {
        super(message);
    }

    public ReflectException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectException(Throwable cause) {
        super(cause);
    }

    public ReflectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
