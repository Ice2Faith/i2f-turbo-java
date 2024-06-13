package i2f.otpauth.impl;

import i2f.bytes.ByteUtil;
import i2f.codec.bytes.basex.Base32;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Ice2Faith
 * @date 2024/6/12 21:43
 * @desc
 */
public class HotpAuthenticator extends HmacOtpAuthenticator {
    public static final String IMPL_TYPE = "hotp";
    protected volatile AtomicLong counter = new AtomicLong(0);

    public HotpAuthenticator(long counter, byte[] secret) {
        super(secret);
        this.counter.set(counter);
    }

    public HotpAuthenticator(long counter, byte[] secret, String algorithm) {
        super(secret, algorithm);
        this.counter.set(counter);
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
                        + "&counter=" + counter;
            }
            return PROTOCOL_PREFIX + implType + "/" +
                    urlEncode(issuer + ":" + account)
                    + "?secret=" + urlEncode(secretBase32)
                    + "&issuer=" + urlEncode(issuer)
                    + "&algorithm=" + urlEncode(algorithm)
                    + "&digits=" + digits
                    + "&counter=" + counter;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public String generate() {
        try {
            long count = counter.incrementAndGet();
            byte[] counterBytes = ByteUtil.toBigEndian(count);
            return generateCode(counterBytes);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }


}
