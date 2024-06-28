package i2f.tools.encrypt.menus.bc.encrypt;


import i2f.codec.CodecUtil;
import i2f.extension.jce.bc.encrypt.asymmetric.BcSm2Encryptor;
import i2f.tools.encrypt.IMenuHandler;

public class BcSm2EncryptMenuHandler implements IMenuHandler {

    @Override
    public String name() {
        return "bc-sm2-encrypt";
    }

    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println(name() + ": require least one argument.");
            System.out.println(name() + " [publicKey] [...string]");
            System.out.println(name() + " hello");
            System.out.println(name() + " hello world");
            return;
        }
        BcSm2Encryptor encryptor = new BcSm2Encryptor(args[0], null);
        for (int i = 1; i < args.length; i++) {
            String item = args[i];
            String encode = CodecUtil.toHexString(encryptor.encrypt(CodecUtil.toUtf8(item)));
            System.out.println(item + "==> (pass=" + args[0] + ") ==> " + encode);
        }
    }
}
