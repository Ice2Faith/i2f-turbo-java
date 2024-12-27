package i2f.web.firewall.exception;

import i2f.firewall.exception.FirewallException;

/**
 * @author Ice2Faith
 * @date 2023/8/28 10:34
 * @desc 文件上传下载攻击异常
 */
public class FileSuffixFirewallException extends FirewallException {
    public FileSuffixFirewallException() {
    }

    public FileSuffixFirewallException(String message) {
        super(message);
    }

    public FileSuffixFirewallException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileSuffixFirewallException(Throwable cause) {
        super(cause);
    }
}
