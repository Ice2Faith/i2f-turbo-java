package i2f.tools.encrypt.menus.bc.encrypt;


import i2f.codec.CodecUtil;
import i2f.extension.jce.bc.encrypt.asymmetric.BcSm2Encryptor;
import i2f.tools.encrypt.IMenuHandler;

import java.security.KeyPair;

public class BcSm2KeyGenMenuHandler implements IMenuHandler {

    @Override
    public String name() {
        return "bc-sm2-keygen";
    }

    @Override
    public void execute(String[] args) throws Exception {
        KeyPair keyPair = BcSm2Encryptor.genKeyPair();
        System.out.println("publicKey" + " ==> " + CodecUtil.toHexString(keyPair.getPublic().getEncoded()));
        System.out.println("privateKey" + " ==> " + CodecUtil.toHexString(keyPair.getPrivate().getEncoded()));
    }
}
