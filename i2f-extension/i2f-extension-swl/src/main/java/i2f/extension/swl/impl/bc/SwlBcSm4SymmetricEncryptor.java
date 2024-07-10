package i2f.extension.swl.impl.bc;

import i2f.codec.bytes.charset.CharsetStringByteCodec;
import i2f.codec.bytes.raw.HexStringByteCodec;
import i2f.extension.jce.bc.encrypt.symmetric.Sm4Type;
import i2f.jce.jdk.encrypt.symmetric.SymmetricEncryptor;
import i2f.jce.jdk.encrypt.symmetric.SymmetricType;
import i2f.jce.std.encrypt.symmetric.ISymmetricEncryptor;
import i2f.swl.consts.SwlCode;
import i2f.swl.exception.SwlException;
import i2f.swl.std.ISwlSymmetricEncryptor;

import javax.crypto.SecretKey;

/**
 * @author Ice2Faith
 * @date 2024/7/10 19:28
 * @desc
 */
public class SwlBcSm4SymmetricEncryptor implements ISwlSymmetricEncryptor {
    private SymmetricType symmetricType= Sm4Type.DEFAULT;
    private ISymmetricEncryptor encryptor=new SymmetricEncryptor(symmetricType);
    @Override
    public String generateKey() {
        try {
            SecretKey key = SymmetricEncryptor.genKey(symmetricType);
            byte[] encoded = key.getEncoded();
            return HexStringByteCodec.INSTANCE.encode(encoded);
        } catch (Exception e) {
            throw new SwlException(SwlCode.SYMMETRIC_INVALID_KEY_EXCEPTION.code(),e.getMessage(),e);
        }
    }

    @Override
    public String getKey() {
        try {
            byte[] encoded = encryptor.getKeyBytes();
            return HexStringByteCodec.INSTANCE.encode(encoded);
        } catch (Exception e) {
            throw new SwlException(SwlCode.SYMMETRIC_INVALID_KEY_EXCEPTION.code(),e.getMessage(),e);
        }
    }

    @Override
    public void setKey(String key) {
        byte[] bytes = HexStringByteCodec.INSTANCE.decode(key);
        encryptor.setKeyBytes(bytes);
    }

    @Override
    public String encrypt(String data) {
        try {
            byte[] bytes = CharsetStringByteCodec.UTF8.decode(data);
            byte[] encrypt = encryptor.encrypt(bytes);
            return HexStringByteCodec.INSTANCE.encode(encrypt);
        } catch (Exception e) {
            throw new SwlException(SwlCode.ASYMMETRIC_ENCRYPT_EXCEPTION.code(), e.getMessage(),e);
        }
    }

    @Override
    public String decrypt(String data) {
        try {
            byte[] bytes = HexStringByteCodec.INSTANCE.decode(data);
            byte[] encrypt = encryptor.decrypt(bytes);
            return CharsetStringByteCodec.UTF8.encode(encrypt);
        } catch (Exception e) {
            throw new SwlException(SwlCode.ASYMMETRIC_ENCRYPT_EXCEPTION.code(), e.getMessage(),e);
        }
    }
}
