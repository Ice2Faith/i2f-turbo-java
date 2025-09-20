package i2f.spring.security;

import i2f.authentication.password.IPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Ice2Faith
 * @date 2025/9/20 17:38
 */
public class SpringPasswordEncoder implements IPasswordEncoder {
    public static final SpringPasswordEncoder BC=new SpringPasswordEncoder(new BCryptPasswordEncoder());

    protected PasswordEncoder encoder=new BCryptPasswordEncoder();

    public SpringPasswordEncoder() {
    }

    public SpringPasswordEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public String encode(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword,encodedPassword);
    }
}
