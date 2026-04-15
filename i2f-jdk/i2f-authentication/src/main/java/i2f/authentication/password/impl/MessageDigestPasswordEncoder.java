package i2f.authentication.password.impl;

import i2f.authentication.password.IPasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.regex.Pattern;

/**
 * @author Ice2Faith
 * @date 2025/9/20 17:26
 */
public class MessageDigestPasswordEncoder implements IPasswordEncoder {

    public static final MessageDigestPasswordEncoder SHA_1 = new MessageDigestPasswordEncoder("SHA-1");

    private String algorithm = "SHA-1";
    private String provider;
    private String salt = "";
    private int iteration = 1024;
    private final String separator = "$";
    private final SecureRandom random = new SecureRandom();

    public MessageDigestPasswordEncoder() {
    }

    public MessageDigestPasswordEncoder(String algorithm) {
        this.algorithm = algorithm;
    }

    public MessageDigestPasswordEncoder(String algorithm, String salt) {
        this.algorithm = algorithm;
        this.salt = salt;
    }

    public MessageDigestPasswordEncoder(String algorithm, String provider, String salt) {
        this.algorithm = algorithm;
        this.provider = provider;
        this.salt = salt;
    }

    public MessageDigestPasswordEncoder(String algorithm, String provider, String salt, int iteration) {
        this.algorithm = algorithm;
        this.provider = provider;
        this.salt = salt;
        this.iteration = iteration;
    }

    public static MessageDigest getDigester(String algorithm, String provider) throws Exception {
        if (provider == null || provider.isEmpty()) {
            return MessageDigest.getInstance(algorithm);
        }
        return MessageDigest.getInstance(algorithm, provider);
    }

    public String getSalt(String salt) {
        if (salt != null && !salt.isEmpty()) {
            return salt;
        }
        return generateSalt();
    }

    public String generateSalt() {
        byte[] output = new byte[32];
        random.nextBytes(output);
        return encodeHex(output, null);
    }

    public static String calcSign(MessageDigest digester, String rawPassword, String salt, int iteration) throws Exception {
        byte[] workPasswordBytes = rawPassword.getBytes(StandardCharsets.UTF_8);
        byte[] workSaltBytes = salt.getBytes(StandardCharsets.UTF_8);

        byte[] input = new byte[workPasswordBytes.length + workSaltBytes.length];
        System.arraycopy(workPasswordBytes, 0, input, 0, workPasswordBytes.length);
        System.arraycopy(workSaltBytes, 0, input, workPasswordBytes.length, workSaltBytes.length);

        byte[] output = digester.digest(input);
        for (int i = 0; i < iteration; i++) {
            if (input.length != (output.length + workSaltBytes.length)) {
                input = new byte[output.length + workSaltBytes.length];
            }
            System.arraycopy(output, 0, input, 0, output.length);
            System.arraycopy(workSaltBytes, 0, input, output.length, workSaltBytes.length);

            output = digester.digest(input);
        }
        return encodeHex(output, null);
    }

    public static String encodeHex(byte[] output, String nullVal) {
        if (output == null || output.length == 0) {
            return nullVal;
        }
        StringBuilder builder = new StringBuilder();
        for (byte bt : output) {
            builder.append(String.format("%02x", (int) (bt & 0x0ff)));
        }
        return builder.toString();
    }

    public static byte[] decodeHex(String hex, byte[] nullVal) {
        if (hex == null || hex.isEmpty()) {
            return nullVal;
        }
        int len = hex.length() / 2;
        byte[] ret = new byte[len];
        for (int i = 0; i < len; i++) {
            ret[i] = (byte) (Integer.parseInt(hex.substring(i * 2, (i + 1) * 2), 16) & 0x0ff);
        }
        return ret;
    }

    public static String encodeTextHex(String output, String nullVal) {
        if (output == null || output.isEmpty()) {
            return nullVal;
        }
        return encodeHex(output.getBytes(StandardCharsets.UTF_8), nullVal);
    }

    public static String decodeTextHex(String hex, String nullVal) {
        if (hex == null || hex.isEmpty()) {
            return nullVal;
        }
        byte[] bytes = decodeHex(hex, null);
        if (bytes == null || bytes.length == 0) {
            return nullVal;
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    @Override
    public String encode(String rawPassword) {
        if (rawPassword == null) {
            return null;
        }
        try {
            MessageDigest digester = getDigester(algorithm, provider);
            String workSalt = getSalt(this.salt);
            String sign = calcSign(digester, rawPassword, workSalt, this.iteration);
            return separator + encodeTextHex(algorithm, "")
                    + separator + encodeTextHex(provider, "")
                    + separator + iteration
                    + separator + encodeTextHex(workSalt, "")
                    + separator + sign;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }


    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        try {
            if (encodedPassword.startsWith(separator)) {
                encodedPassword = encodedPassword.substring(separator.length());
            }
            String[] arr = Pattern.compile(separator, Pattern.LITERAL).split(encodedPassword);
            String algorithm = decodeTextHex(arr[0].trim(), null);
            String provider = decodeTextHex(arr[1].trim(), null);
            int iteration = Integer.parseInt(arr[2].trim());
            String workSalt = decodeTextHex(arr[3].trim(), "");
            String sign = arr[4].trim();
            MessageDigest digester = getDigester(algorithm, provider);

            String reSign = calcSign(digester, rawPassword, workSalt, iteration);
            return reSign.equalsIgnoreCase(sign);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
