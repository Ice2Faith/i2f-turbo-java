package i2f.container.readonly.exception;

/**
 * @author Ice2Faith
 * @date 2024/4/22 16:46
 * @desc
 */
public class ReadonlyException extends RuntimeException {
    public ReadonlyException() {
    }

    public ReadonlyException(String message) {
        super(message);
    }

    public ReadonlyException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReadonlyException(Throwable cause) {
        super(cause);
    }

    public ReadonlyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
