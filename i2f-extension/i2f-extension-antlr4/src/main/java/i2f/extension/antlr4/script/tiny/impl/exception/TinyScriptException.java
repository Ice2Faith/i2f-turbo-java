package i2f.extension.antlr4.script.tiny.impl.exception;

/**
 * @author Ice2Faith
 * @date 2025/3/5 9:48
 */
public class TinyScriptException extends RuntimeException {
    public TinyScriptException() {
    }

    public TinyScriptException(String message) {
        super(message);
    }

    public TinyScriptException(String message, Throwable cause) {
        super(message, cause);
    }

    public TinyScriptException(Throwable cause) {
        super(cause);
    }

    public TinyScriptException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
