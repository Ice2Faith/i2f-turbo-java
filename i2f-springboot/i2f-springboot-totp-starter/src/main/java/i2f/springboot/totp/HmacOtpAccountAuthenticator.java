package i2f.springboot.totp;

import i2f.codec.bytes.basex.Base32;
import i2f.otpauth.impl.HmacOtpAuthenticator;
import i2f.springboot.totp.api.HmacOtpAccountKeyProvider;
import i2f.springboot.totp.api.HmacOtpAuthenticatorFactory;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Ice2Faith
 * @date 2024/6/27 10:50
 * @desc
 */
@Data
@Slf4j
@NoArgsConstructor
public class HmacOtpAccountAuthenticator implements InitializingBean {
    @Autowired(required = false)
    private HmacOtpAccountKeyProvider accountKeyProvider;

    @Autowired
    private HmacOtpAuthenticatorFactory authenticatorFactory;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (accountKeyProvider == null) {
            throw new IllegalStateException("you must implement on bean of " + HmacOtpAccountKeyProvider.class.getName() + " to get account bind key, such from database or others.");
        }
    }

    public String generateRandomKey() {
        byte[] bytes = HmacOtpAuthenticator.generateSecretKey();
        return generateKey(bytes);
    }

    public String generateKey(byte[] bytes) {
        return Base32.encode(bytes);
    }

    public HmacOtpAuthenticator getAuthenticator(String account) {
        String totpKey = accountKeyProvider.getKey(account);
        if (totpKey == null) {
            throw new IllegalStateException("account [" + account + "] missing totp key");
        }
        return getAuthenticatorBySecretKey(totpKey);
    }

    public HmacOtpAuthenticator getAuthenticatorBySecretKey(String totpKey) {
        if (totpKey == null) {
            throw new IllegalStateException("missing totp key");
        }
        byte[] secret = Base32.decode(totpKey);
        return authenticatorFactory.getAuthenticator(secret);
    }

    public boolean verify(String account, String code) {
        HmacOtpAuthenticator authenticator = getAuthenticator(account);
        return authenticator.verify(code);
    }

    public String generate(String account) {
        HmacOtpAuthenticator authenticator = getAuthenticator(account);
        return authenticator.generate();
    }

    public String makeUrl(String account, String issuer) {
        HmacOtpAuthenticator authenticator = getAuthenticator(account);
        return authenticator.makeQrUrl(account, issuer);
    }
}
