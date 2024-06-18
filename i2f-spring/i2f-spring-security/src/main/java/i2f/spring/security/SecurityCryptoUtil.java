package i2f.spring.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Ice2Faith
 * @date 2024/6/18 11:09
 * @desc
 */
public class SecurityCryptoUtil {
    public static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public static String bCryptEncode(String rawPassword) {
        return encode(bCryptPasswordEncoder, rawPassword);
    }

    public static String encode(PasswordEncoder passwordEncoder, String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public static boolean bCryptMatch(String rawPassword, String encPassword) {
        return match(bCryptPasswordEncoder, rawPassword, encPassword);
    }

    public static boolean match(PasswordEncoder passwordEncoder, String rawPassword, String encPassword) {
        return passwordEncoder.matches(rawPassword, encPassword);
    }
}
