package i2f.extension.antlr4.script.tiny.impl.exception;

/**
 * @author Ice2Faith
 * @date 2025/2/25 9:00
 */
public class TinyScriptControlException extends TinyScriptException {
    public TinyScriptControlException() {
    }

    public TinyScriptControlException(String message) {
        super(message);
    }

    public TinyScriptControlException(String message, Throwable cause) {
        super(message, cause);
    }

    public TinyScriptControlException(Throwable cause) {
        super(cause);
    }

    public TinyScriptControlException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
