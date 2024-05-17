package i2f.exception;

/**
 * @author Ice2Faith
 * @date 2024/4/22 16:46
 * @desc
 */
public class UnSupportException extends RuntimeException {
    public UnSupportException() {
    }

    public UnSupportException(String message) {
        super(message);
    }

    public UnSupportException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnSupportException(Throwable cause) {
        super(cause);
    }

    public UnSupportException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
