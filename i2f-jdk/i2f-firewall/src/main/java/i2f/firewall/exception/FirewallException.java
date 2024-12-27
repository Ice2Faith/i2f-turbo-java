package i2f.firewall.exception;

/**
 * @author Ice2Faith
 * @date 2023/8/28 10:14
 * @desc
 */
public class FirewallException extends IllegalArgumentException {
    public FirewallException() {
    }

    public FirewallException(String message) {
        super(message);
    }

    public FirewallException(String message, Throwable cause) {
        super(message, cause);
    }

    public FirewallException(Throwable cause) {
        super(cause);
    }
}
