package i2f.jce.std.encrypt;

import i2f.codec.bytes.base64.Base64StringByteCodec;
import i2f.codec.bytes.raw.HexStringByteCodec;

/**
 * @author Ice2Faith
 * @date 2024/3/27 9:12
 * @desc
 */
public interface IEncryptor {
    byte[] encrypt(byte[] data) throws Exception;

    byte[] decrypt(byte[] data) throws Exception;

    default String encryptAsBase64(byte[] data) throws Exception {
        return Base64StringByteCodec.INSTANCE.encode(encrypt(data));
    }

    default byte[] decryptByBase64(String data) throws Exception {
        return decrypt(Base64StringByteCodec.INSTANCE.decode(data));
    }

    default String encryptAsHex(byte[] data) throws Exception {
        return HexStringByteCodec.INSTANCE.encode(encrypt(data));
    }

    default byte[] decryptByHex(String data) throws Exception {
        return decrypt(HexStringByteCodec.INSTANCE.decode(data));
    }
}
