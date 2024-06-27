package i2f.springboot.totp.api.impl;

import i2f.otpauth.impl.HmacOtpAuthenticator;
import i2f.otpauth.impl.MicrosoftAuthenticator;
import i2f.otpauth.impl.SteamAuthenticator;
import i2f.otpauth.impl.TotpAuthenticator;
import i2f.springboot.totp.api.HmacOtpAuthenticatorFactory;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Ice2Faith
 * @date 2024/6/27 11:07
 * @desc
 */
@Data
@ConditionalOnMissingBean(HmacOtpAuthenticatorFactory.class)
@ConditionalOnExpression("${i2f.springboot.totp.factory.enable:true}")
@ConfigurationProperties(prefix = "i2f.springboot.totp.factory")
public class DefaultTotpAuthenticatorFactory implements HmacOtpAuthenticatorFactory {
    private String type = "totp";
    protected String algorithm = "SHA1";
    protected int digits = 6;

    @Override
    public HmacOtpAuthenticator getAuthenticator(byte[] secret) {
        if ("totp".equalsIgnoreCase(type)) {
            return new TotpAuthenticator(secret);
        }
        if ("microsoft".equalsIgnoreCase(type)) {
            return new MicrosoftAuthenticator(secret);
        } else if ("steam".equalsIgnoreCase(type)) {
            return new SteamAuthenticator(secret);
        }
        HmacOtpAuthenticator ret = new TotpAuthenticator(secret);
        if (algorithm != null && !algorithm.isEmpty()) {
            ret.setAlgorithm(algorithm);
        }
        if (digits > 0) {
            ret.setDigits(digits);
        }
        return ret;
    }
}
