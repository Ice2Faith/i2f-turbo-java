package i2f.extension.antlr4.funvi.impl.exception.impl;

import i2f.extension.antlr4.funvi.impl.exception.FunviControlException;

/**
 * @author Ice2Faith
 * @date 2025/2/25 9:01
 */
public class FunviBreakException extends FunviControlException {
    public FunviBreakException() {
    }

    public FunviBreakException(String message) {
        super(message);
    }

    public FunviBreakException(String message, Throwable cause) {
        super(message, cause);
    }

    public FunviBreakException(Throwable cause) {
        super(cause);
    }

    public FunviBreakException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
