package i2f.springboot.ops.common;

/**
 * @author Ice2Faith
 * @date 2025/11/1 22:32
 * @desc
 */
public class OpsException extends RuntimeException{
    public OpsException() {
    }

    public OpsException(String message) {
        super(message);
    }

    public OpsException(String message, Throwable cause) {
        super(message, cause);
    }

    public OpsException(Throwable cause) {
        super(cause);
    }
}
