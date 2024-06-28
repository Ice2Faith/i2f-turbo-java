package i2f.jce.std.encrypt.asymmetric;

import i2f.codec.bytes.base64.Base64StringByteCodec;
import i2f.codec.bytes.raw.HexStringByteCodec;
import i2f.jce.std.encrypt.IEncryptor;
import i2f.jce.std.signature.ISignatureSigner;

/**
 * @author Ice2Faith
 * @date 2024/3/27 8:55
 * @desc
 */
public interface IAsymmetricEncryptor extends IEncryptor, ISignatureSigner {

    default byte[] privateEncrypt(byte[] data) throws Exception {
        throw new UnsupportedOperationException("unsupported asymmetric private encrypt mode.");
    }

    default byte[] publicDecrypt(byte[] data) throws Exception {
        throw new UnsupportedOperationException("unsupported asymmetric public decrypt mode.");
    }

    @Override
    default byte[] sign(byte[] data) throws Exception {
        return privateEncrypt(data);
    }

    default String privateEncryptAsBase64(byte[] data) throws Exception {
        return Base64StringByteCodec.INSTANCE.encode(privateEncrypt(data));
    }

    default byte[] publicDecryptByBase64(String data) throws Exception {
        return publicDecrypt(Base64StringByteCodec.INSTANCE.decode(data));
    }

    default String privateEncryptAsHex(byte[] data) throws Exception {
        return HexStringByteCodec.INSTANCE.encode(privateEncrypt(data));
    }

    default byte[] publicDecryptByHex(String data) throws Exception {
        return publicDecrypt(HexStringByteCodec.INSTANCE.decode(data));
    }

}
