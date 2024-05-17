package i2f.exception;

/**
 * @author Ice2Faith
 * @date 2024/4/22 16:46
 * @desc
 */
public class NativeException extends RuntimeException {
    public NativeException() {
    }

    public NativeException(String message) {
        super(message);
    }

    public NativeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NativeException(Throwable cause) {
        super(cause);
    }

    public NativeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
