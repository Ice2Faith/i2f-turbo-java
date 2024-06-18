package i2f.otpauth.impl;

import javax.crypto.Mac;

/**
 * @author Ice2Faith
 * @date 2024/6/13 9:16
 * @desc
 */
public class SteamAuthenticator extends TotpAuthenticator {
    public static final String IMPL_TYPE = "steam";
    public static final String Alphabet = "23456789BCDFGHJKMNPQRTVWXY";

    public SteamAuthenticator(byte[] secret) {
        super(secret);
        digits = 5;
    }

    public SteamAuthenticator(byte[] secret, String algorithm) {
        super(secret, algorithm);
        digits = 5;
    }

    public static String truncateSteam(long code, int digits) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < digits; i++) {
            char ch = Alphabet.charAt((int) (code % Alphabet.length()));
            builder.append(ch);
            code /= Alphabet.length();
        }
        return builder.toString();
    }

    @Override
    public String getImplType() {
        return IMPL_TYPE;
    }

    @Override
    public String generateCode(byte[] counterBytes) {
        try {
            Mac mac = getMac(secret, algorithm);
            long code = compute(mac, counterBytes);

            return truncateSteam(code, digits);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
