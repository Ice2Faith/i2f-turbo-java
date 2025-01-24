package i2f.jdbc.procedure.signal.impl;

import i2f.jdbc.procedure.signal.SignalException;

/**
 * @author Ice2Faith
 * @date 2025/1/24 8:52
 */
public class ControlSignalException extends SignalException {
    public ControlSignalException() {
    }

    public ControlSignalException(String message) {
        super(message);
    }

    public ControlSignalException(String message, Throwable cause) {
        super(message, cause);
    }

    public ControlSignalException(Throwable cause) {
        super(cause);
    }

    public ControlSignalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
