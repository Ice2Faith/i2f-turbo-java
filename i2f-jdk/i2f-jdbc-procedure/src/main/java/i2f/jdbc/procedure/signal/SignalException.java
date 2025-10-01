package i2f.jdbc.procedure.signal;

import i2f.jdbc.procedure.context.ContextHolder;

/**
 * @author Ice2Faith
 * @date 2025/1/20 11:32
 */
public class SignalException extends RuntimeException {
    protected String message;
    protected String location;
    protected boolean hasLogout = false;

    {
        location = ContextHolder.TRACE_LOCATION.get();
    }

    protected SignalException() {
    }

    protected SignalException(String message) {
        super(message);
        this.message = message;
    }

    protected SignalException(String message, Throwable cause) {
        super(message, cause);
        this.message = message + " with cause by " + cause.getClass() + " : " + cause.getMessage();
    }

    protected SignalException(Throwable cause) {
        super(cause);
        this.message = "cause by " + cause.getClass() + " : " + cause.getMessage();
    }

    protected SignalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.message = message + " with cause by " + cause.getClass() + " : " + cause.getMessage();
    }

    @Override
    public String getMessage() {
        return (message == null ? super.getMessage() : message) + " , location at " + location;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean hasLogout() {
        return this.hasLogout;
    }

    public void setHasLogout(boolean hasLogout) {
        this.hasLogout = hasLogout;
    }

}
