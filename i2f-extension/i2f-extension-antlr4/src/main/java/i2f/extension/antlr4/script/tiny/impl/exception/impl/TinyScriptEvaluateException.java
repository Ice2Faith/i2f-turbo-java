package i2f.extension.antlr4.script.tiny.impl.exception.impl;

import i2f.extension.antlr4.script.tiny.impl.exception.TinyScriptThrowException;

/**
 * @author Ice2Faith
 * @date 2025/2/25 9:00
 */
public class TinyScriptEvaluateException extends TinyScriptThrowException {
    public TinyScriptEvaluateException() {
    }

    public TinyScriptEvaluateException(String message) {
        super(message);
    }

    public TinyScriptEvaluateException(String message, Throwable cause) {
        super(message, cause);
    }

    public TinyScriptEvaluateException(Throwable cause) {
        super(cause);
    }

    public TinyScriptEvaluateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
