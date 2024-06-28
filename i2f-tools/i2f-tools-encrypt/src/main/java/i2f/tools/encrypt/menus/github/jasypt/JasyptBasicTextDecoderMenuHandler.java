package i2f.tools.encrypt.menus.github.jasypt;

import i2f.tools.encrypt.IMenuHandler;
import org.jasypt.util.text.BasicTextEncryptor;

/**
 * @author Ice2Faith
 * @date 2023/2/20 16:08
 * @desc
 */
public class JasyptBasicTextDecoderMenuHandler implements IMenuHandler {
    public static BasicTextEncryptor encoder = new BasicTextEncryptor();

    @Override
    public String name() {
        return "jasypt-txt-de";
    }

    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println(name() + ": require least once argument.");
            System.out.println(name() + " [password] [...string]");
            System.out.println(name() + " hello");
            System.out.println(name() + " hello world");
            return;
        }
        encoder.setPassword(args[0]);
        for (int i = 1; i < args.length; i++) {
            String item = args[i];
            if (item.startsWith("ENC(")) {
                item = item.substring("ENC(".length(), item.length() - 1);
            }
            String encode = encoder.decrypt(item);
            System.out.println(item + " ==> (pass=" + args[0] + ") ==> " + encode);
        }
    }
}
