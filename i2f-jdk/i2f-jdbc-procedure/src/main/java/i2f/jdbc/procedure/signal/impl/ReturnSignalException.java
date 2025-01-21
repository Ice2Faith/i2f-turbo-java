package i2f.jdbc.procedure.signal.impl;

import i2f.jdbc.procedure.signal.SignalException;

/**
 * @author Ice2Faith
 * @date 2025/1/20 11:33
 */
public class ReturnSignalException extends SignalException {
    public ReturnSignalException() {
    }

    public ReturnSignalException(String message) {
        super(message);
    }

    public ReturnSignalException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReturnSignalException(Throwable cause) {
        super(cause);
    }

    public ReturnSignalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
