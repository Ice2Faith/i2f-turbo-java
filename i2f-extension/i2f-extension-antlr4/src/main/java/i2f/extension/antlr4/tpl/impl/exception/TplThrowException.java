package i2f.extension.antlr4.tpl.impl.exception;

/**
 * @author Ice2Faith
 * @date 2025/2/25 9:00
 */
public class TplThrowException extends TplException {
    public TplThrowException() {
    }

    public TplThrowException(String message) {
        super(message);
    }

    public TplThrowException(String message, Throwable cause) {
        super(message, cause);
    }

    public TplThrowException(Throwable cause) {
        super(cause);
    }

    public TplThrowException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
