package i2f.extension.antlr4.tpl.impl.exception.impl;

import i2f.extension.antlr4.tpl.impl.exception.TplControlException;

/**
 * @author Ice2Faith
 * @date 2025/2/25 9:01
 */
public class TplContinueException extends TplControlException {
    public TplContinueException() {
    }

    public TplContinueException(String message) {
        super(message);
    }

    public TplContinueException(String message, Throwable cause) {
        super(message, cause);
    }

    public TplContinueException(Throwable cause) {
        super(cause);
    }

    public TplContinueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
