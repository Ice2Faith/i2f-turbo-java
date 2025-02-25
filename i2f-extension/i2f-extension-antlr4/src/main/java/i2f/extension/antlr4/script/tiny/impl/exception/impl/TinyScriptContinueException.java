package i2f.extension.antlr4.script.tiny.impl.exception.impl;

import i2f.extension.antlr4.script.tiny.impl.exception.TinyScriptControlException;

/**
 * @author Ice2Faith
 * @date 2025/2/25 9:01
 */
public class TinyScriptContinueException extends TinyScriptControlException {
    public TinyScriptContinueException() {
    }

    public TinyScriptContinueException(String message) {
        super(message);
    }

    public TinyScriptContinueException(String message, Throwable cause) {
        super(message, cause);
    }

    public TinyScriptContinueException(Throwable cause) {
        super(cause);
    }

    public TinyScriptContinueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
