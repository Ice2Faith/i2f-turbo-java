package i2f.extension.antlr4.funvi.impl.exception;

/**
 * @author Ice2Faith
 * @date 2025/2/25 9:00
 */
public class FunviThrowException extends FunviException {
    public FunviThrowException() {
    }

    public FunviThrowException(String message) {
        super(message);
    }

    public FunviThrowException(String message, Throwable cause) {
        super(message, cause);
    }

    public FunviThrowException(Throwable cause) {
        super(cause);
    }

    public FunviThrowException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
