package i2f.extension.antlr4.tpl.impl.exception;

/**
 * @author Ice2Faith
 * @date 2025/2/25 9:00
 */
public class TplControlException extends TplException {
    public TplControlException() {
    }

    public TplControlException(String message) {
        super(message);
    }

    public TplControlException(String message, Throwable cause) {
        super(message, cause);
    }

    public TplControlException(Throwable cause) {
        super(cause);
    }

    public TplControlException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
