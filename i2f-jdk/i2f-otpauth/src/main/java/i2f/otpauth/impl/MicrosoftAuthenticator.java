package i2f.otpauth.impl;

/**
 * @author Ice2Faith
 * @date 2024/6/13 9:16
 * @desc
 */
public class MicrosoftAuthenticator extends TotpAuthenticator {
    public MicrosoftAuthenticator(byte[] secret) {
        super(secret);
    }

    public MicrosoftAuthenticator(byte[] secret, String algorithm) {
        super(secret, algorithm);
    }
}
