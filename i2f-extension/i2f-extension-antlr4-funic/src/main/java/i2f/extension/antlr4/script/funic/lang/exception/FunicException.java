package i2f.extension.antlr4.script.funic.lang.exception;

/**
 * @author Ice2Faith
 * @date 2025/3/5 9:48
 */
public class FunicException extends RuntimeException {
    public FunicException() {
    }

    public FunicException(String message) {
        super(message);
    }

    public FunicException(String message, Throwable cause) {
        super(message, cause);
    }

    public FunicException(Throwable cause) {
        super(cause);
    }

    public FunicException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
