package i2f.web.firewall.exception;

import i2f.firewall.exception.FirewallException;

/**
 * @author Ice2Faith
 * @date 2023/8/28 10:34
 * @desc 敏感文件攻击异常
 */
public class FileNameFirewallException extends FirewallException {
    public FileNameFirewallException() {
    }

    public FileNameFirewallException(String message) {
        super(message);
    }

    public FileNameFirewallException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileNameFirewallException(Throwable cause) {
        super(cause);
    }
}
