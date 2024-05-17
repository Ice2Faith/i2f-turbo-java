package i2f.exception;

/**
 * @author Ice2Faith
 * @date 2024/4/22 16:46
 * @desc
 */
public class DisconnectException extends RuntimeException {
    public DisconnectException() {
    }

    public DisconnectException(String message) {
        super(message);
    }

    public DisconnectException(String message, Throwable cause) {
        super(message, cause);
    }

    public DisconnectException(Throwable cause) {
        super(cause);
    }

    public DisconnectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
