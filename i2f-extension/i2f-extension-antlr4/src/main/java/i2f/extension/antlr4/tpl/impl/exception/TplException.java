package i2f.extension.antlr4.tpl.impl.exception;

/**
 * @author Ice2Faith
 * @date 2025/3/5 9:48
 */
public class TplException extends RuntimeException {
    public TplException() {
    }

    public TplException(String message) {
        super(message);
    }

    public TplException(String message, Throwable cause) {
        super(message, cause);
    }

    public TplException(Throwable cause) {
        super(cause);
    }

    public TplException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
