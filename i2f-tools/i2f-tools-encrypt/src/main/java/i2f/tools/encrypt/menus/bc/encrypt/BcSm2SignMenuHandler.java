package i2f.tools.encrypt.menus.bc.encrypt;


import i2f.codec.CodecUtil;
import i2f.extension.jce.bc.encrypt.asymmetric.BcSm2Encryptor;
import i2f.tools.encrypt.IMenuHandler;

public class BcSm2SignMenuHandler implements IMenuHandler {

    @Override
    public String name() {
        return "bc-sm2-sign";
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
        BcSm2Encryptor encryptor = new BcSm2Encryptor(null, args[0]);
        for (int i = 1; i < args.length; i++) {
            String item = args[i];
            String encode = CodecUtil.toHexString(encryptor.sign(CodecUtil.toUtf8(item)));
            System.out.println(item + "==> (pass=" + args[0] + ") ==> " + encode);
        }
    }
}
