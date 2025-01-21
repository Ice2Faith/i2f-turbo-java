package i2f.jdbc.procedure.signal;

/**
 * @author Ice2Faith
 * @date 2025/1/20 11:32
 */
public class SignalException extends RuntimeException {
    public SignalException() {
    }

    public SignalException(String message) {
        super(message);
    }

    public SignalException(String message, Throwable cause) {
        super(message, cause);
    }

    public SignalException(Throwable cause) {
        super(cause);
    }

    public SignalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
