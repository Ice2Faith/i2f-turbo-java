package i2f.tools.encrypt.menus.spring.security;

import i2f.tools.encrypt.IMenuHandler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BCryptPasswordEncoderMenuHandler implements IMenuHandler {
    public static PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String name() {
        return "pe-bcrypt";
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
