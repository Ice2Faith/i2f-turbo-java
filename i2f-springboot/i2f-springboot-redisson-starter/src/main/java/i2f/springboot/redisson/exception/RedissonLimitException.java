package i2f.springboot.redisson.exception;

/**
 * @author Ice2Faith
 * @date 2026/9/22 16:10
 * @desc
 */
public class RedissonLimitException extends RuntimeException {
    public RedissonLimitException() {
    }

    public RedissonLimitException(String message) {
        super(message);
    }

    public RedissonLimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedissonLimitException(Throwable cause) {
        super(cause);
    }
}
