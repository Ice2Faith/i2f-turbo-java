package i2f.extension.antlr4.script.funic.lang.exception;

/**
 * @author Ice2Faith
 * @date 2025/2/25 9:00
 */
public class FunicThrowException extends FunicException {
    public FunicThrowException() {
    }

    public FunicThrowException(String message) {
        super(message);
    }

    public FunicThrowException(String message, Throwable cause) {
        super(message, cause);
    }

    public FunicThrowException(Throwable cause) {
        super(cause);
    }

    public FunicThrowException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
