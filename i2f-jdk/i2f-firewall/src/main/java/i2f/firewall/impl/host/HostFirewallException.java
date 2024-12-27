package i2f.firewall.impl.host;


import i2f.firewall.exception.FirewallException;

/**
 * @author Ice2Faith
 * @date 2023/8/31 16:30
 * @desc
 */
public class HostFirewallException extends FirewallException {
    public HostFirewallException() {
    }

    public HostFirewallException(String message) {
        super(message);
    }

    public HostFirewallException(String message, Throwable cause) {
        super(message, cause);
    }

    public HostFirewallException(Throwable cause) {
        super(cause);
    }
}
