package i2f.exception;


/**
 * @author Ice2Faith
 * @date 2024/4/22 16:45
 * @desc
 */
public class UnHandledException extends ServiceException {
    private Throwable reason;

    public UnHandledException(Throwable reason) {
        super(reason);
        this.reason = reason;
    }

    public UnHandledException(String message, Throwable cause) {
        super(message, cause);
        this.reason = cause;
    }

    public UnHandledException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.reason = cause;
    }

    public Throwable getReason() {
        return reason;
    }
}
