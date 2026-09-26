package i2f.extension.antlr4.script.tiny.impl.exception.impl;

import i2f.extension.antlr4.script.tiny.impl.exception.TinyScriptControlException;

/**
 * @author Ice2Faith
 * @date 2025/2/25 9:01
 */
public class TinyScriptBreakException extends TinyScriptControlException {
    public TinyScriptBreakException() {
    }

    public TinyScriptBreakException(String message) {
        super(message);
    }

    public TinyScriptBreakException(String message, Throwable cause) {
        super(message, cause);
    }

    public TinyScriptBreakException(Throwable cause) {
        super(cause);
    }

    public TinyScriptBreakException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
