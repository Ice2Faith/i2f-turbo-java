package i2f.tools.encrypt.menus.jce;


import i2f.codec.CodecUtil;
import i2f.jce.jdk.encrypt.Encryptor;
import i2f.jce.jdk.encrypt.asymmetric.RsaType;
import i2f.tools.encrypt.IMenuHandler;

import java.security.KeyPair;

public class RsaKeyGenMenuHandler implements IMenuHandler {

    @Override
    public String name() {
        return "rsa-keygen";
    }

    @Override
    public void execute(String[] args) throws Exception {
        KeyPair keyPair = Encryptor.genKeyPair(RsaType.DEFAULT, null, null);
        System.out.println("publicKey" + " ==> " + CodecUtil.toBase64(keyPair.getPublic().getEncoded()));
        System.out.println("privateKey" + " ==> " + CodecUtil.toBase64(keyPair.getPrivate().getEncoded()));
    }
}
