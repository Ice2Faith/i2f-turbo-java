package i2f.extension.swl.impl.bc;

import i2f.code.CodeUtil;
import i2f.codec.bytes.base64.Base64StringByteCodec;
import i2f.codec.bytes.charset.CharsetStringByteCodec;
import i2f.crypto.impl.jdk.encrypt.symmetric.SymmetricType;
import i2f.crypto.std.encrypt.symmetric.ISymmetricEncryptor;
import i2f.extension.jce.bc.encrypt.symmetric.AesType;
import i2f.extension.jce.bc.encrypt.symmetric.BcSymmetricEncryptor;
import i2f.swl.consts.SwlCode;
import i2f.swl.exception.SwlException;
import i2f.swl.std.ISwlSymmetricEncryptor;

/**
 * @author Ice2Faith
 * @date 2024/7/10 19:28
 * @desc
 */
public class SwlBcAes256SymmetricEncryptor implements ISwlSymmetricEncryptor {
    private SymmetricType symmetricType = AesType.ECB_ISO10126Padding;
    private ISymmetricEncryptor encryptor = new BcSymmetricEncryptor(symmetricType);

    @Override
    public String generateKey() {
        try {
            String key = CodeUtil.makeCheckCode(256 / 8);
            return key;
        } catch (Exception e) {
            throw new SwlException(SwlCode.SYMMETRIC_INVALID_KEY_EXCEPTION.code(), e.getMessage(), e);
        }
    }

    @Override
    public String getKey() {
        try {
            byte[] encoded = encryptor.getKeyBytes();
            return CharsetStringByteCodec.UTF8.encode(encoded);
        } catch (Exception e) {
            throw new SwlException(SwlCode.SYMMETRIC_INVALID_KEY_EXCEPTION.code(), e.getMessage(), e);
        }
    }

    @Override
    public void setKey(String key) {
        byte[] bytes = CharsetStringByteCodec.UTF8.decode(key);
        encryptor.setKeyBytes(bytes);
    }

    @Override
    public String encrypt(String data) {
        try {
            byte[] bytes = CharsetStringByteCodec.UTF8.decode(data);
            byte[] encrypt = encryptor.encrypt(bytes);
            return Base64StringByteCodec.INSTANCE.encode(encrypt);
        } catch (Exception e) {
            throw new SwlException(SwlCode.ASYMMETRIC_ENCRYPT_EXCEPTION.code(), e.getMessage(), e);
        }
    }

    @Override
    public String decrypt(String data) {
        try {
            byte[] bytes = Base64StringByteCodec.INSTANCE.decode(data);
            byte[] encrypt = encryptor.decrypt(bytes);
            return CharsetStringByteCodec.UTF8.encode(encrypt);
        } catch (Exception e) {
            throw new SwlException(SwlCode.ASYMMETRIC_ENCRYPT_EXCEPTION.code(), e.getMessage(), e);
        }
    }
}
