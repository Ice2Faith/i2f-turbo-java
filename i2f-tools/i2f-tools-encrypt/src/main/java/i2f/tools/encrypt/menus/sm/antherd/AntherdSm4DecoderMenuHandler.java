package i2f.tools.encrypt.menus.sm.antherd;


import i2f.extension.jce.sm.antherd.encrypt.symmetric.Sm4Encryptor;
import i2f.tools.encrypt.IMenuHandler;

/**
 * @author Ice2Faith
 * @date 2023/2/20 17:04
 * @desc
 */
public class AntherdSm4DecoderMenuHandler implements IMenuHandler {
    @Override
    public String name() {
        return "antherd-sm4-de";
    }

    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println(name() + ": require least one argument.");
            System.out.println(name() + " [password] [...string]");
            System.out.println(name() + " hello");
            System.out.println(name() + " hello world");
            return;
        }
        Sm4Encryptor encryptor = new Sm4Encryptor(args[0]);
        for (int i = 1; i < args.length; i++) {
            String item = args[i];
            String encode = encryptor.decrypt(item);
            System.out.println(item + "==> (pass=" + args[0] + ") ==> " + encode);
        }
    }
}
