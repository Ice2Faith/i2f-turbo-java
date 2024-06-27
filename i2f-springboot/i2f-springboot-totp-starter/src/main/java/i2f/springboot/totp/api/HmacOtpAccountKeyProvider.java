package i2f.springboot.totp.api;

/**
 * @author Ice2Faith
 * @date 2024/6/27 10:49
 * @desc
 */
public interface HmacOtpAccountKeyProvider {
    String getKey(String account);
}
