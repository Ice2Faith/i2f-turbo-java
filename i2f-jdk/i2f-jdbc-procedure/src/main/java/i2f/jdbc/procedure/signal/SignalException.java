package i2f.jdbc.procedure.signal;

import i2f.jdbc.procedure.context.ContextHolder;

/**
 * @author Ice2Faith
 * @date 2025/1/20 11:32
 */
public class SignalException extends RuntimeException {
    protected String message;
    protected String location;
    {
        location= ContextHolder.TRACE_LOCATION.get()+":"+ContextHolder.TRACE_LINE.get();
    }
    protected SignalException() {
    }

    protected SignalException(String message) {
        super(message);
        this.message=message;
    }

    protected SignalException(String message, Throwable cause) {
        super(message, cause);
        this.message=message+" with cause by "+cause.getClass()+" : "+cause.getMessage();
    }

    protected SignalException(Throwable cause) {
        super(cause);
        this.message="cause by "+cause.getClass()+" : "+cause.getMessage();
    }

    protected SignalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.message=message+" with cause by "+cause.getClass()+" : "+cause.getMessage();
    }

    public void setMessage(String message){
        this.message=message;
    }

    @Override
    public String getMessage() {
        return (message==null?super.getMessage():message)+" , location at "+location;
    }
}
