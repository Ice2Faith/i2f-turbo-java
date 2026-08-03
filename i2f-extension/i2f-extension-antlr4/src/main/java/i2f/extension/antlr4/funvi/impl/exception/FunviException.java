package i2f.extension.antlr4.funvi.impl.exception;

/**
 * @author Ice2Faith
 * @date 2025/3/5 9:48
 */
public class FunviException extends RuntimeException {
    public FunviException() {
    }

    public FunviException(String message) {
        super(message);
    }

    public FunviException(String message, Throwable cause) {
        super(message, cause);
    }

    public FunviException(Throwable cause) {
        super(cause);
    }

    public FunviException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
