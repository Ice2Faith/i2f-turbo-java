package i2f.jdbc.procedure.signal.impl;

/**
 * @author Ice2Faith
 * @date 2025/1/20 11:33
 */
public class BreakSignalException extends ControlSignalException {
    public BreakSignalException() {
    }

    public BreakSignalException(String message) {
        super(message);
    }

    public BreakSignalException(String message, Throwable cause) {
        super(message, cause);
    }

    public BreakSignalException(Throwable cause) {
        super(cause);
    }

    public BreakSignalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
