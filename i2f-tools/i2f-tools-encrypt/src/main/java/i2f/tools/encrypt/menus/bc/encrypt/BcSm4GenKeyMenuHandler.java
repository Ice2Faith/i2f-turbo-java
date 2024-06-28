package i2f.tools.encrypt.menus.bc.encrypt;


import i2f.codec.CodecUtil;
import i2f.extension.jce.bc.encrypt.symmetric.BcSymmetricEncryptor;
import i2f.extension.jce.bc.encrypt.symmetric.Sm4Type;
import i2f.tools.encrypt.IMenuHandler;

import javax.crypto.SecretKey;

/**
 * @author Ice2Faith
 * @date 2023/2/20 17:04
 * @desc
 */
public class BcSm4GenKeyMenuHandler implements IMenuHandler {
    @Override
    public String name() {
        return "bc-sm4-keygen";
    }

    @Override
    public void execute(String[] args) throws Exception {
        SecretKey key = BcSymmetricEncryptor.genKey(Sm4Type.DEFAULT);
        String str = CodecUtil.toHexString(key.getEncoded());
        System.out.println("==> " + str);
    }
}
