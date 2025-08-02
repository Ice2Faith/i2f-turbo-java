package i2f.tools.encrypt.menus.jce;


import i2f.crypto.impl.jdk.encrypt.Encryptor;
import i2f.crypto.impl.jdk.encrypt.symmetric.AesType;
import i2f.crypto.impl.jdk.encrypt.symmetric.SymmetricEncryptor;
import i2f.tools.encrypt.IMenuHandler;

import javax.crypto.SecretKey;
import java.util.Base64;

/**
 * @author Ice2Faith
 * @date 2023/2/20 17:04
 * @desc
 */
public class AesB64EncoderMenuHandler implements IMenuHandler {
    @Override
    public String name() {
        return "aes-b64-en";
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
        byte[] key = Base64.getDecoder().decode(args[0]);
        SecretKey secretKey = Encryptor.keyOf(key, Encryptor.cipherAlgorithm(AesType.ECB_PKCS5Padding.type()));
        SymmetricEncryptor encryptor = new SymmetricEncryptor(AesType.ECB_PKCS5Padding, secretKey);
        for (int i = 1; i < args.length; i++) {
            String item = args[i];
            String encode = Base64.getEncoder().encodeToString(encryptor.encrypt(Base64.getDecoder().decode(item)));
            System.out.println(item + " ==> (pass=" + args[0] + ") ==> " + encode);
        }
    }
}
