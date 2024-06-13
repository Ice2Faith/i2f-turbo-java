package i2f.otpauth;

/**
 * @author Ice2Faith
 * @date 2024/6/13 8:32
 * @desc
 */
public interface OtpAuthenticator {
    String PROTOCOL_PREFIX = "otpauth://";

    String generate();

    String makeQrUrl(String account, String issuer);

    default boolean verify(String code) {
        String gen = generate();
        return gen.equalsIgnoreCase(code);
    }

}
