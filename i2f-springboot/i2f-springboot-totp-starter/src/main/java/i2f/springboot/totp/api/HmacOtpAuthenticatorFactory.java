package i2f.springboot.totp.api;

import i2f.otpauth.impl.HmacOtpAuthenticator;

/**
 * @author Ice2Faith
 * @date 2024/6/27 11:05
 * @desc
 */
public interface HmacOtpAuthenticatorFactory {
    HmacOtpAuthenticator getAuthenticator(byte[] secret);
}
