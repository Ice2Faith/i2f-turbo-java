package i2f.sm.crypto.swl;

import i2f.sm.crypto.std.SmCryptoSm4Encryptor;
import i2f.swl.consts.SwlCode;
import i2f.swl.exception.SwlException;
import i2f.swl.std.ISwlSymmetricEncryptor;

/**
 * @author Ice2Faith
 * @date 2024/7/10 9:58
 * @desc
 */
public class SwlSmCryptoSm4SymmetricEncryptor implements ISwlSymmetricEncryptor {
    private SmCryptoSm4Encryptor encryptor = new SmCryptoSm4Encryptor();

    @Override
    public String generateKey() {
        try {
            return SmCryptoSm4Encryptor.genKey();
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
        try {
            return encryptor.encrypt(data);
        } catch (Exception e) {
            throw new SwlException(SwlCode.SYMMETRIC_ENCRYPT_EXCEPTION.code(), e.getMessage(), e);
        }
    }

    @Override
    public String decrypt(String data) {
        try {
            return encryptor.decrypt(data);
        } catch (Exception e) {
            throw new SwlException(SwlCode.SYMMETRIC_DECRYPT_EXCEPTION.code(), e.getMessage(), e);
        }
    }
}
