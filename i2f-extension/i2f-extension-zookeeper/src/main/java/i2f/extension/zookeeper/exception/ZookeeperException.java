package i2f.extension.zookeeper.exception;


/**
 * @author Ice2Faith
 * @date 2022/4/13 9:07
 * @desc
 */
public class ZookeeperException extends RuntimeException {
    public ZookeeperException() {
    }

    public ZookeeperException(String message) {
        super(message);
    }

    public ZookeeperException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZookeeperException(Throwable cause) {
        super(cause);
    }
}
