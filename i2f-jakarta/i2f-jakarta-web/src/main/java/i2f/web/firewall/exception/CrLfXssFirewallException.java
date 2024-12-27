package i2f.web.firewall.exception;

import i2f.firewall.exception.FirewallException;

/**
 * @author Ice2Faith
 * @date 2023/8/28 10:34
 * @desc 回车换行的XSS攻击异常
 */
public class CrLfXssFirewallException extends FirewallException {
    public CrLfXssFirewallException() {
    }

    public CrLfXssFirewallException(String message) {
        super(message);
    }

    public CrLfXssFirewallException(String message, Throwable cause) {
        super(message, cause);
    }

    public CrLfXssFirewallException(Throwable cause) {
        super(cause);
    }
}
