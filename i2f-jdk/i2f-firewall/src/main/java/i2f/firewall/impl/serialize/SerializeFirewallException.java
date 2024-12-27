package i2f.firewall.impl.serialize;


import i2f.firewall.exception.FirewallException;

/**
 * @author Ice2Faith
 * @date 2023/8/31 16:31
 * @desc
 */
public class SerializeFirewallException extends FirewallException {
    public SerializeFirewallException() {
    }

    public SerializeFirewallException(String message) {
        super(message);
    }

    public SerializeFirewallException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializeFirewallException(Throwable cause) {
        super(cause);
    }
}
