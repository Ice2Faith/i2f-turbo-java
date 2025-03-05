package i2f.extension.antlr4.script.tiny.impl.exception;

/**
 * @author Ice2Faith
 * @date 2025/2/25 9:00
 */
public class TinyScriptEvaluateException extends TinyScriptException{
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
