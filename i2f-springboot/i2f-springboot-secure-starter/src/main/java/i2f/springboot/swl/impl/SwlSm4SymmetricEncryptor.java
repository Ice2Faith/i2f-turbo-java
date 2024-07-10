package i2f.springboot.swl.impl;

import i2f.extension.jce.sm.antherd.encrypt.symmetric.Sm4Encryptor;
import i2f.springboot.swl.consts.SwlCode;
import i2f.springboot.swl.exception.SwlException;
import i2f.springboot.swl.std.ISwlSymmetricEncryptor;

/**
 * @author Ice2Faith
 * @date 2024/7/10 9:58
 * @desc
 */
public class SwlSm4SymmetricEncryptor implements ISwlSymmetricEncryptor {
    private Sm4Encryptor encryptor = new Sm4Encryptor();

    @Override
    public String generateKey() {
        try {
            return Sm4Encryptor.genKey();
        } catch (Exception e) {
            throw new SwlException(SwlCode.SYMMETRIC_INVALID_KEY_EXCEPTION.code(), e.getMessage(), e);
        }
    }

    @Override
    public String getKey() {
        return encryptor.getKeyString();
    }

    @Override
    public void setKey(String key) {
        encryptor.setKeyString(key);
    }

    @Override
    public String encrypt(String data) {
        return encryptor.encrypt(data);
    }

    @Override
    public String decrypt(String data) {
        return encryptor.decrypt(data);
    }
}
