package i2f.proxy.handlers.validate;

/**
 * @author Ice2Faith
 * @date 2025/7/23 17:08
 */
public class ValidateException extends RuntimeException {
    public ValidateException() {
    }

    public ValidateException(String message) {
        super(message);
    }

    public ValidateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidateException(Throwable cause) {
        super(cause);
    }
}
