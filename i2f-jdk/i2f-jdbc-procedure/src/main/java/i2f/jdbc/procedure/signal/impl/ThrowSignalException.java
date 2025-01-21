package i2f.jdbc.procedure.signal.impl;

import i2f.jdbc.procedure.signal.SignalException;

/**
 * @author Ice2Faith
 * @date 2025/1/20 11:33
 */
public class ThrowSignalException extends SignalException {
    public ThrowSignalException() {
    }

    public ThrowSignalException(String message) {
        super(message);
    }

    public ThrowSignalException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThrowSignalException(Throwable cause) {
        super(cause);
    }

    public ThrowSignalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
