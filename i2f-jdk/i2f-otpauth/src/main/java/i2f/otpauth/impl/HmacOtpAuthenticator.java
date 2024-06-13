package i2f.otpauth.impl;

import i2f.bytes.ByteUtil;
import i2f.codec.bytes.basex.Base32;
import i2f.otpauth.OtpAuthenticator;
import lombok.Data;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;

/**
 * @author Ice2Faith
 * @date 2024/6/13 8:32
 * @desc
 */
@Data
public abstract class HmacOtpAuthenticator implements OtpAuthenticator {
    protected byte[] secret;
    protected String algorithm = "SHA1";
    protected int digits = 6;


    public HmacOtpAuthenticator(byte[] secret) {
        this.secret = secret;
    }

    public HmacOtpAuthenticator(byte[] secret, String algorithm) {
        this.secret = secret;
        this.algorithm = algorithm;
    }

    public static final SecureRandom random = new SecureRandom();

    public static byte[] generateSecretKey() {
        byte[] ret = new byte[16];
        random.nextBytes(ret);
        return ret;
    }

    public static String urlEncode(String str) throws UnsupportedEncodingException {
        return URLEncoder.encode(str, "UTF-8").replaceAll("\\+", "%20");
    }


    public static Mac getMac(byte[] secret, String algorithm) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(secret, "RAW");
        Mac mac = Mac.getInstance("Hmac" + algorithm);
        mac.init(keySpec);
        return mac;
    }

    public static long compute(Mac mac, byte[] counter) throws Exception {
        byte[] hash = mac.doFinal(counter);
        int offset = hash[hash.length - 1] & 0x0f;
        long ret = ((hash[offset] & 0x07F) << 24) |
                ((hash[offset + 1] & 0x0FF) << 16) |
                ((hash[offset + 2] & 0x0FF) << 8) |
                ((hash[offset + 3] & 0x0FF) << 0);
        return ret;
    }


    public static byte[] getCounterBytes(long counter, int period) {
        long window = counter / period;
        byte[] ret = ByteUtil.toBigEndian(window, 8);
        return ret;
    }

    public static String truncate(long code, int digits) {
        String ret = String.format("%0" + digits + "d", code);
        return ret.substring(ret.length() - digits);
    }

    public String generateCode(byte[] counterBytes) {
        try {
            Mac mac = getMac(secret, algorithm);
            long code = compute(mac, counterBytes);

            return truncate(code, digits);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
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
                        + "&digits=" + digits;
            }
            return PROTOCOL_PREFIX + implType + "/" +
                    urlEncode(issuer + ":" + account)
                    + "?secret=" + urlEncode(secretBase32)
                    + "&issuer=" + urlEncode(issuer)
                    + "&algorithm=" + urlEncode(algorithm)
                    + "&digits=" + digits;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public abstract String getImplType();

}
