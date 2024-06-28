package i2f.tools.encrypt.menus.sm.antherd;


import i2f.extension.jce.sm.antherd.encrypt.asymmetric.Sm2Encryptor;
import i2f.tools.encrypt.IMenuHandler;

public class AntherdSm2VerifyMenuHandler implements IMenuHandler {

    @Override
    public String name() {
        return "antherd-sm2-verify";
    }

    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 3) {
            System.out.println(name() + ": require least one argument.");
            System.out.println(name() + " [publicKey] [sign] [...data]");
            System.out.println(name() + " hello");
            System.out.println(name() + " hello world");
            return;
        }
        Sm2Encryptor encryptor = new Sm2Encryptor(null, args[0]);
        for (int i = 2; i < args.length; i++) {
            String item = args[i];
            boolean ok = encryptor.verify(args[1], item);
            System.out.println("==> (pass=" + args[0] + ",sign=" + args[0] + ") ==> " + ok);
        }
    }
}
