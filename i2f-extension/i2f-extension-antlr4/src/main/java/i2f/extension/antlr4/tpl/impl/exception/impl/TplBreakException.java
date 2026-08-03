package i2f.extension.antlr4.tpl.impl.exception.impl;

import i2f.extension.antlr4.tpl.impl.exception.TplControlException;

/**
 * @author Ice2Faith
 * @date 2025/2/25 9:01
 */
public class TplBreakException extends TplControlException {
    public TplBreakException() {
    }

    public TplBreakException(String message) {
        super(message);
    }

    public TplBreakException(String message, Throwable cause) {
        super(message, cause);
    }

    public TplBreakException(Throwable cause) {
        super(cause);
    }

    public TplBreakException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
