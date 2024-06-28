package i2f.tools.encrypt.menus.bc.encrypt;


import i2f.codec.CodecUtil;
import i2f.extension.jce.bc.encrypt.asymmetric.BcSm2Encryptor;
import i2f.tools.encrypt.IMenuHandler;

public class BcSm2VerifyMenuHandler implements IMenuHandler {

    @Override
    public String name() {
        return "bc-sm2-verify";
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
        BcSm2Encryptor encryptor = new BcSm2Encryptor(null, args[0]);
        for (int i = 2; i < args.length; i++) {
            String item = args[i];
            boolean ok = encryptor.verify(CodecUtil.ofHexString(args[1]), CodecUtil.toUtf8(item));
            System.out.println("==> (pass=" + args[0] + ",sign=" + args[0] + ") ==> " + ok);
        }
    }
}
