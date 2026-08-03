package i2f.extension.antlr4.funvi.impl.exception.impl;

import i2f.extension.antlr4.funvi.impl.exception.FunviControlException;

/**
 * @author Ice2Faith
 * @date 2025/2/25 9:01
 */
public class FunviContinueException extends FunviControlException {
    public FunviContinueException() {
    }

    public FunviContinueException(String message) {
        super(message);
    }

    public FunviContinueException(String message, Throwable cause) {
        super(message, cause);
    }

    public FunviContinueException(Throwable cause) {
        super(cause);
    }

    public FunviContinueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
