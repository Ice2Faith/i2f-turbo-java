package i2f.springboot.encrypt.property.impl;


import i2f.codec.CodecUtil;
import i2f.jce.jdk.encrypt.symmetric.AesType;
import i2f.jce.jdk.encrypt.symmetric.SymmetricEncryptor;
import i2f.jce.jdk.supports.SecureRandomAlgorithm;
import i2f.springboot.encrypt.property.core.ITextEncryptor;
import i2f.springboot.encrypt.property.core.PrefixPropertyDecryptor;

/**
 * @author Ice2Faith
 * @date 2022/6/7 9:59
 * @desc
 */
public class AesPropertyDecryptor extends PrefixPropertyDecryptor implements ITextEncryptor {
    public static final String AES_PREFIX = "aes.";
    protected SymmetricEncryptor encryptor;

    public AesPropertyDecryptor(String key) {
        super(AES_PREFIX);
        try {
            encryptor = SymmetricEncryptor.genKeyEncryptor(AesType.ECB_PKCS5Padding, key.getBytes(), null, SecureRandomAlgorithm.SHA1PRNG.text());
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public String decryptText(String text) {
        try {
            byte[] data = encryptor.decrypt(CodecUtil.ofHexString(text));
            return CodecUtil.ofUtf8(data);
        } catch (Exception e) {
            return text;
        }
    }

    @Override
    public String encrypt(String text) {
        try {
            byte[] enc = encryptor.encrypt(CodecUtil.toUtf8(text));
            return AES_PREFIX + CodecUtil.toHexString(enc);
        } catch (Exception e) {
            return AES_PREFIX + text;
        }
    }
}
