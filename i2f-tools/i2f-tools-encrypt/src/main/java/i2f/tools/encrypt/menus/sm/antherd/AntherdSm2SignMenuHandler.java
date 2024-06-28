package i2f.tools.encrypt.menus.sm.antherd;


import i2f.extension.jce.sm.antherd.encrypt.asymmetric.Sm2Encryptor;
import i2f.tools.encrypt.IMenuHandler;

public class AntherdSm2SignMenuHandler implements IMenuHandler {

    @Override
    public String name() {
        return "antherd-sm2-sign";
    }

    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println(name() + ": require least one argument.");
            System.out.println(name() + " [privateKey] [...string]");
            System.out.println(name() + " hello");
            System.out.println(name() + " hello world");
            return;
        }
        Sm2Encryptor encryptor = new Sm2Encryptor(null, args[0]);
        for (int i = 1; i < args.length; i++) {
            String item = args[i];
            String encode = encryptor.sign(item);
            System.out.println(item + "==> (pass=" + args[0] + ") ==> " + encode);
        }
    }
}
