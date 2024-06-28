package i2f.tools.encrypt.menus.bc.encrypt;


import i2f.codec.CodecUtil;
import i2f.extension.jce.bc.digest.md.BcMessageDigester;
import i2f.tools.encrypt.IMenuHandler;

public class BcSm3MenuHandler implements IMenuHandler {

    @Override
    public String name() {
        return "bc-sm3";
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
            String encode = CodecUtil.toHexString(BcMessageDigester.SM3.digest(CodecUtil.toUtf8(item)));
            System.out.println(item + "==> " + encode);
        }
    }
}
