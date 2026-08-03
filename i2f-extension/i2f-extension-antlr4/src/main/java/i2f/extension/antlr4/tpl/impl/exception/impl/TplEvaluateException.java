package i2f.extension.antlr4.tpl.impl.exception.impl;

import i2f.extension.antlr4.tpl.impl.exception.TplThrowException;

/**
 * @author Ice2Faith
 * @date 2025/2/25 9:00
 */
public class TplEvaluateException extends TplThrowException {
    public TplEvaluateException() {
    }

    public TplEvaluateException(String message) {
        super(message);
    }

    public TplEvaluateException(String message, Throwable cause) {
        super(message, cause);
    }

    public TplEvaluateException(Throwable cause) {
        super(cause);
    }

    public TplEvaluateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
