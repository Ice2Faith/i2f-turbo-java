package i2f.firewall.impl.sql;


import i2f.firewall.exception.FirewallException;

/**
 * @author Ice2Faith
 * @date 2023/8/31 16:31
 * @desc
 */
public class SqlFirewallException extends FirewallException {
    public SqlFirewallException() {
    }

    public SqlFirewallException(String message) {
        super(message);
    }

    public SqlFirewallException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlFirewallException(Throwable cause) {
        super(cause);
    }
}
