package i2f.jce.std.encrypt.asymmetric;

import i2f.codec.bytes.base64.Base64StringByteCodec;
import i2f.codec.bytes.raw.HexStringByteCodec;
import i2f.jce.std.encrypt.IEncryptor;
import i2f.jce.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.jce.std.signature.ISignatureSigner;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

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

    void setPublicKey(PublicKey publicKey);

    PublicKey getPublicKey();

    void setPrivateKey(PrivateKey privateKey);

    PrivateKey getPrivateKey();

    void setPublicKeyBytes(byte[] publicKeyBytes);

    byte[] getPublicKeyBytes();

    default void setPublicKeyString(String str) {
        setPublicKeyBytes(Base64StringByteCodec.INSTANCE.decode(str));
    }

    default String getPublicKeyString() {
        return Base64StringByteCodec.INSTANCE.encode(getPublicKeyBytes());
    }

    void setPrivateKeyBytes(byte[] privateKeyBytes);

    byte[] getPrivateKeyBytes();

    default void setPrivateKeyString(String str) {
        setPrivateKeyBytes(Base64StringByteCodec.INSTANCE.decode(str));
    }

    default String getPrivateKeyString() {
        return Base64StringByteCodec.INSTANCE.encode(getPrivateKeyBytes());
    }

    void setKeyPair(KeyPair keyPair);

    KeyPair getKeyPair();

    default void setAsymKeyPair(AsymKeyPair keyPair) {
        String publicKey = keyPair.getPublicKey();
        if (publicKey != null && !publicKey.isEmpty()) {
            setPublicKeyString(publicKey);
        }
        String privateKey = keyPair.getPrivateKey();
        if (privateKey != null && !privateKey.isEmpty()) {
            setPrivateKeyString(privateKey);
        }
    }

    default AsymKeyPair getAsymKeyPair() {
        String publicKey = getPublicKeyString();
        String privateKey = getPrivateKeyString();
        return new AsymKeyPair(publicKey, privateKey);
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
