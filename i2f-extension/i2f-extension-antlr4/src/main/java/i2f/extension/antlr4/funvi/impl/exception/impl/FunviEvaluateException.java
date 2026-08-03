package i2f.extension.antlr4.funvi.impl.exception.impl;

import i2f.extension.antlr4.funvi.impl.exception.FunviThrowException;

/**
 * @author Ice2Faith
 * @date 2025/2/25 9:00
 */
public class FunviEvaluateException extends FunviThrowException {
    public FunviEvaluateException() {
    }

    public FunviEvaluateException(String message) {
        super(message);
    }

    public FunviEvaluateException(String message, Throwable cause) {
        super(message, cause);
    }

    public FunviEvaluateException(Throwable cause) {
        super(cause);
    }

    public FunviEvaluateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
