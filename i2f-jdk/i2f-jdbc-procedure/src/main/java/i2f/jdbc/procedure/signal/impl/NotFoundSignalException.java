package i2f.jdbc.procedure.signal.impl;

/**
 * @author Ice2Faith
 * @date 2025/1/24 8:52
 */
public class NotFoundSignalException extends ThrowSignalException {
    public NotFoundSignalException() {
    }

    public NotFoundSignalException(String message) {
        super(message);
    }

    public NotFoundSignalException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundSignalException(Throwable cause) {
        super(cause);
    }

    public NotFoundSignalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
