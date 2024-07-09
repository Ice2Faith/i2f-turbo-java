package i2f.springboot.secure.crypto;


import i2f.codec.CodecUtil;
import i2f.jce.jdk.encrypt.symmetric.SymmetricEncryptor;

/**
 * @author Ice2Faith
 * @date 2021/9/2
 */
public class SymmetricUtil {
    public static final String CHAR_SET = "UTF-8";

    public static String genKey(int size) {
        byte[] bytes = SecureProvider.symmetricKeySupplier.apply(size);
        return CodecUtil.ofUtf8(bytes);
    }

    public static String encrypt(byte[] data, String key) {
        try {
            SymmetricEncryptor encryptor = SecureProvider.symmetricEncryptorSupplier.apply(key.getBytes());
            byte[] sdata = encryptor.encrypt(data);
            return CodecUtil.toBase64(sdata);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(String data, String key) {
        try {
            byte[] sdata = CodecUtil.ofBase64(data);
            SymmetricEncryptor encryptor = SecureProvider.symmetricEncryptorSupplier.apply(key.getBytes());
            return encryptor.decrypt(sdata);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(String data, byte[] key) {
        try {
            byte[] sdata = CodecUtil.ofBase64(data);
            SymmetricEncryptor encryptor = SecureProvider.symmetricEncryptorSupplier.apply(key);
            return encryptor.decrypt(sdata);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encryptJsonBytesAfterBase64(byte[] data, String key) {
        try {
            String json = CodecUtil.toBase64(data);
            return encrypt(json.getBytes(CHAR_SET), key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encryptJsonAfterBase64(String json, String key) {
        try {
            json = CodecUtil.toBase64(json.getBytes(CHAR_SET));
            return encrypt(json.getBytes(CHAR_SET), key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptJsonBeforeBase64(String data, String key) {
        try {
            byte[] sdata = decrypt(data, key);
            String json = new String(sdata, CHAR_SET);
            json = new String(CodecUtil.ofBase64(json), CHAR_SET);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
