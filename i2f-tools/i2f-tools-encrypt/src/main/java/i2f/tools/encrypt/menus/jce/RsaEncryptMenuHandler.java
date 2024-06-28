package i2f.tools.encrypt.menus.jce;


import i2f.codec.CodecUtil;
import i2f.jce.jdk.encrypt.Encryptor;
import i2f.jce.jdk.encrypt.asymmetric.AsymmetricEncryptor;
import i2f.jce.jdk.encrypt.asymmetric.RsaType;
import i2f.tools.encrypt.IMenuHandler;

import java.security.PublicKey;

public class RsaEncryptMenuHandler implements IMenuHandler {

    @Override
    public String name() {
        return "rsa-encrypt";
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
        PublicKey key = Encryptor.publicKeyOf(RsaType.DEFAULT, CodecUtil.ofBase64(args[0]));
        AsymmetricEncryptor encryptor = new AsymmetricEncryptor(RsaType.DEFAULT, null, key);
        for (int i = 1; i < args.length; i++) {
            String item = args[i];
            String encode = CodecUtil.toBase64(encryptor.encrypt(CodecUtil.toUtf8(item)));
            System.out.println(item + "==> (pass=" + args[0] + ") ==> " + encode);
        }
    }
}
