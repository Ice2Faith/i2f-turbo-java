package i2f.jdbc.procedure.signal.impl;

import i2f.jdbc.procedure.signal.SignalException;

/**
 * @author Ice2Faith
 * @date 2025/1/20 11:33
 */
public class ContinueSignalException extends SignalException {
    public ContinueSignalException() {
    }

    public ContinueSignalException(String message) {
        super(message);
    }

    public ContinueSignalException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContinueSignalException(Throwable cause) {
        super(cause);
    }

    public ContinueSignalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
