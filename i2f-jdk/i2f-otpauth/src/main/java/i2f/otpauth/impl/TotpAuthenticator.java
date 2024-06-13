package i2f.otpauth.impl;

/**
 * @author Ice2Faith
 * @date 2024/6/12 21:43
 * @desc
 */
public class TotpAuthenticator extends HmacOtpAuthenticator {
    public static final String IMPL_TYPE = "totp";


    public TotpAuthenticator(byte[] secret) {
        super(secret);
    }

    public TotpAuthenticator(byte[] secret, String algorithm) {
        super(secret, algorithm);
    }

    @Override
    public String getImplType() {
        return IMPL_TYPE;
    }


    @Override
    public String generate() {
        try {
            long counter = System.currentTimeMillis() / 1000;
            byte[] counterBytes = getCounterBytes(counter, period);
            return generateCode(counterBytes);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public int aliveSeconds() {
        try {
            long counter = System.currentTimeMillis() / 1000;
            byte[] counterBytes = getCounterBytes(counter, period);
            String nowCode = generateCode(counterBytes);

            for (int i = 0; i < period; i++) {
                counter++;
                counterBytes = getCounterBytes(counter, period);
                String testCode = generateCode(counterBytes);
                if (!nowCode.equalsIgnoreCase(testCode)) {
                    return i;
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return -1;
    }

}
