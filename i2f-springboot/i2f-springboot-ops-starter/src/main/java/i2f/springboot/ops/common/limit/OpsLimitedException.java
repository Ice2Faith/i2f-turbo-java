package i2f.springboot.ops.common.limit;

/**
 * @author Ice2Faith
 * @date 2026/11/21 17:42
 * @desc
 */
public class OpsLimitedException extends RuntimeException {
    public OpsLimitedException() {
    }

    public OpsLimitedException(String message) {
        super(message);
    }

    public OpsLimitedException(String message, Throwable cause) {
        super(message, cause);
    }

    public OpsLimitedException(Throwable cause) {
        super(cause);
    }
}
