package i2f.extension.antlr4.script.funic.lang.exception;

/**
 * @author Ice2Faith
 * @date 2025/2/25 9:00
 */
public class FunicControlException extends FunicException {
    public FunicControlException() {
    }

    public FunicControlException(String message) {
        super(message);
    }

    public FunicControlException(String message, Throwable cause) {
        super(message, cause);
    }

    public FunicControlException(Throwable cause) {
        super(cause);
    }

    public FunicControlException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
