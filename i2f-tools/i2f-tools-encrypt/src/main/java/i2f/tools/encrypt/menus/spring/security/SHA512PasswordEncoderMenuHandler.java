package i2f.tools.encrypt.menus.spring.security;

import i2f.tools.encrypt.IMenuHandler;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SHA512PasswordEncoderMenuHandler implements IMenuHandler {
    public static PasswordEncoder encoder = new MessageDigestPasswordEncoder("SHA-512");

    @Override
    public String name() {
        return "pe-sha-512";
    }

    @Override
    public void execute(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println(name() + ": require least once argument.");
            System.out.println(name() + " [...string]");
            System.out.println(name() + " hello");
            System.out.println(name() + " hello world");
            return;
        }
        for (String item : args) {
            String encode = encoder.encode(item);
            System.out.println(item + " ==> " + encode);
        }
    }
}
