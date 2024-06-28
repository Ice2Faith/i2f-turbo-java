package i2f.tools.encrypt.menus.sm.antherd;


import i2f.extension.jce.sm.antherd.digest.Sm3Digester;
import i2f.tools.encrypt.IMenuHandler;

public class AntherdSm3MenuHandler implements IMenuHandler {

    @Override
    public String name() {
        return "antherd-sm3";
    }

    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println(name() + ": require least one argument.");
            System.out.println(name() + " [...string]");
            System.out.println(name() + " hello");
            System.out.println(name() + " hello world");
            return;
        }

        for (int i = 0; i < args.length; i++) {
            String item = args[i];
            String encode = Sm3Digester.INSTANCE.digest(item);
            System.out.println(item + "==> " + encode);
        }
    }
}
