package i2f.lifecycle.exception;


/**
 * @author ltb
 * @date 2022/4/27 11:19
 * @desc
 */
public class LifeCycleException extends RuntimeException {
    public LifeCycleException() {
    }

    public LifeCycleException(String message) {
        super(message);
    }

    public LifeCycleException(String message, Throwable cause) {
        super(message, cause);
    }

    public LifeCycleException(Throwable cause) {
        super(cause);
    }
}
