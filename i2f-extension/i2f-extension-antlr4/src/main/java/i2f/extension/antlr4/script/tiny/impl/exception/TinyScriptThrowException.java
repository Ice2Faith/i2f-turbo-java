package i2f.extension.antlr4.script.tiny.impl.exception;

/**
 * @author Ice2Faith
 * @date 2025/2/25 9:00
 */
public class TinyScriptThrowException extends TinyScriptException {
    public TinyScriptThrowException() {
    }

    public TinyScriptThrowException(String message) {
        super(message);
    }

    public TinyScriptThrowException(String message, Throwable cause) {
        super(message, cause);
    }

    public TinyScriptThrowException(Throwable cause) {
        super(cause);
    }

    public TinyScriptThrowException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
