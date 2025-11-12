package i2f.springboot.limit.exception;

/**
 * @author Ice2Faith
 * @date 2025/11/11 17:30
 */
public class LimitException extends RuntimeException{
    public LimitException() {
    }

    public LimitException(String message) {
        super(message);
    }

    public LimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public LimitException(Throwable cause) {
        super(cause);
    }
}
