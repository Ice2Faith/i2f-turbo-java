package i2f.web.firewall.exception;

import i2f.firewall.exception.FirewallException;

/**
 * @author Ice2Faith
 * @date 2023/8/28 10:34
 * @desc 利用web-trace进行链路跟踪攻击异常
 */
public class HttpMethodFirewallException extends FirewallException {
    public HttpMethodFirewallException() {
    }

    public HttpMethodFirewallException(String message) {
        super(message);
    }

    public HttpMethodFirewallException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpMethodFirewallException(Throwable cause) {
        super(cause);
    }
}
