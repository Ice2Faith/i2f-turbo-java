package i2f.extension.antlr4.funvi.impl.exception;

/**
 * @author Ice2Faith
 * @date 2025/2/25 9:00
 */
public class FunviControlException extends FunviException {
    public FunviControlException() {
    }

    public FunviControlException(String message) {
        super(message);
    }

    public FunviControlException(String message, Throwable cause) {
        super(message, cause);
    }

    public FunviControlException(Throwable cause) {
        super(cause);
    }

    public FunviControlException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
