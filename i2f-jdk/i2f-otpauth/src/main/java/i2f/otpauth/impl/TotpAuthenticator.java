package i2f.otpauth.impl;

import i2f.codec.bytes.basex.Base32;

/**
 * @author Ice2Faith
 * @date 2024/6/12 21:43
 * @desc totp 是基于时间戳窗口的认证实现
 * 实现允许在指定的 period 时间戳窗口内生成相同的验证码
 * 换句话说就是，每 period 时间内验证码不变，可以多次使用
 * 要求就是请求方和接收方要求比较强的时间戳一致性
 * 比如：period为30的情况下，就要求双方的时间戳差值需要尽量小于30才能验证成功
 */
public class TotpAuthenticator extends HmacOtpAuthenticator {
    public static final String IMPL_TYPE = "totp";
    protected int period = 30;

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
    public String makeQrUrl(String account, String issuer) {
        try {
            String implType = getImplType();
            String secretBase32 = Base32.encode(secret);
            if (issuer == null || issuer.isEmpty()) {
                return PROTOCOL_PREFIX + implType + "/" +
                        urlEncode(account)
                        + "?secret=" + urlEncode(secretBase32)
                        + "&algorithm=" + urlEncode(algorithm)
                        + "&digits=" + digits
                        + "&period=" + period;
            }
            return PROTOCOL_PREFIX + implType + "/" +
                    urlEncode(issuer + ":" + account)
                    + "?secret=" + urlEncode(secretBase32)
                    + "&issuer=" + urlEncode(issuer)
                    + "&algorithm=" + urlEncode(algorithm)
                    + "&digits=" + digits
                    + "&period=" + period;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
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
