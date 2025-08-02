package i2f.tools.encrypt.menus.bc.encrypt;


import i2f.codec.CodecUtil;
import i2f.crypto.std.encrypt.symmetric.ISymmetricEncryptor;
import i2f.extension.jce.bc.encrypt.symmetric.BcSymmetricEncryptor;
import i2f.extension.jce.bc.encrypt.symmetric.Sm4Type;
import i2f.tools.encrypt.IMenuHandler;

import javax.crypto.SecretKey;

/**
 * @author Ice2Faith
 * @date 2023/2/20 17:04
 * @desc
 */
public class BcSm4DecoderMenuHandler implements IMenuHandler {
    @Override
    public String name() {
        return "bc-sm4-de";
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
        SecretKey key = BcSymmetricEncryptor.keyOf(Sm4Type.DEFAULT, CodecUtil.ofHexString(args[0]));
        ISymmetricEncryptor encryptor = new BcSymmetricEncryptor(Sm4Type.DEFAULT, key);
        for (int i = 1; i < args.length; i++) {
            String item = args[i];
            String encode = CodecUtil.ofUtf8(encryptor.decrypt(CodecUtil.ofHexString(item)));
            System.out.println(item + "==> (pass=" + args[0] + ") ==> " + encode);
        }
    }
}
