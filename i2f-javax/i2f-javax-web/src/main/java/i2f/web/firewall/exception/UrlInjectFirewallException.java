package i2f.web.firewall.exception;

import i2f.firewall.exception.FirewallException;

/**
 * @author Ice2Faith
 * @date 2023/8/28 10:34
 * @desc URL注入攻击异常
 */
public class UrlInjectFirewallException extends FirewallException {
    public UrlInjectFirewallException() {
    }

    public UrlInjectFirewallException(String message) {
        super(message);
    }

    public UrlInjectFirewallException(String message, Throwable cause) {
        super(message, cause);
    }

    public UrlInjectFirewallException(Throwable cause) {
        super(cause);
    }
}
