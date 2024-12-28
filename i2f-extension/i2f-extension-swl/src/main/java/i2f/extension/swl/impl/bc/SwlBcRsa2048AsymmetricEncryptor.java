package i2f.extension.swl.impl.bc;

import i2f.codec.bytes.base64.Base64StringByteCodec;
import i2f.codec.bytes.charset.CharsetStringByteCodec;
import i2f.crypto.impl.jdk.encrypt.Encryptor;
import i2f.crypto.impl.jdk.encrypt.asymmetric.AsymmetricType;
import i2f.crypto.impl.jdk.encrypt.asymmetric.RsaType;
import i2f.crypto.std.encrypt.asymmetric.IAsymmetricEncryptor;
import i2f.crypto.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.extension.jce.bc.BcProvider;
import i2f.extension.jce.bc.encrypt.asymmetric.BcAsymmetricEncryptor;
import i2f.swl.consts.SwlCode;
import i2f.swl.exception.SwlException;
import i2f.swl.std.ISwlAsymmetricEncryptor;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author Ice2Faith
 * @date 2024/7/10 18:59
 * @desc
 */
public class SwlBcRsa2048AsymmetricEncryptor implements ISwlAsymmetricEncryptor {
    private AsymmetricType asymmetricType = RsaType.ECB_PKCS1PADDING;
    private IAsymmetricEncryptor encryptor = new BcAsymmetricEncryptor(asymmetricType);

    @Override
    public AsymKeyPair generateKeyPair() {
        try {
            KeyPair keyPair = Encryptor.genKeyPair(asymmetricType, BcProvider.PROVIDER_NAME, null, 2048);
            String publicKey = null;
            PublicKey pubKey = keyPair.getPublic();
            if (pubKey != null) {
                publicKey = Base64StringByteCodec.INSTANCE.encode(pubKey.getEncoded());
            }
            String privateKey = null;
            PrivateKey priKey = keyPair.getPrivate();
            if (priKey != null) {
                privateKey = Base64StringByteCodec.INSTANCE.encode(priKey.getEncoded());
            }
            return new AsymKeyPair(publicKey, privateKey);
        } catch (Exception e) {
            throw new SwlException(SwlCode.ASYMMETRIC_INVALID_KEY_EXCEPTION.code(), e.getMessage(), e);
        }
    }

    @Override
    public AsymKeyPair getKeyPair() {
        KeyPair keyPair = encryptor.getKeyPair();
        String publicKey = null;
        PublicKey pubKey = keyPair.getPublic();
        if (pubKey != null) {
            publicKey = Base64StringByteCodec.INSTANCE.encode(pubKey.getEncoded());
        }
        String privateKey = null;
        PrivateKey priKey = keyPair.getPrivate();
        if (priKey != null) {
            privateKey = Base64StringByteCodec.INSTANCE.encode(priKey.getEncoded());
        }
        return new AsymKeyPair(publicKey, privateKey);
    }

    @Override
    public void setKeyPair(AsymKeyPair key) {
        String publicKey = key.getPublicKey();
        if (publicKey != null) {
            byte[] encoded = Base64StringByteCodec.INSTANCE.decode(publicKey);
            encryptor.setPublicKeyBytes(encoded);
        }
        String privateKey = key.getPrivateKey();
        if (privateKey != null) {
            byte[] encoded = Base64StringByteCodec.INSTANCE.decode(privateKey);
            encryptor.setPrivateKeyBytes(encoded);
        }
    }

    @Override
    public String getPublicKey() {
        byte[] publicKeyBytes = encryptor.getPublicKeyBytes();
        return Base64StringByteCodec.INSTANCE.encode(publicKeyBytes);
    }

    @Override
    public void setPublicKey(String publicKey) {
        encryptor.setPublicKeyBytes(Base64StringByteCodec.INSTANCE.decode(publicKey));
    }

    @Override
    public String getPrivateKey() {
        byte[] privateKeyBytes = encryptor.getPrivateKeyBytes();
        return Base64StringByteCodec.INSTANCE.encode(privateKeyBytes);
    }

    @Override
    public void setPrivateKey(String privateKey) {
        encryptor.setPrivateKeyBytes(Base64StringByteCodec.INSTANCE.decode(privateKey));
    }

    @Override
    public String encrypt(String data) {
        try {
            byte[] bytes = CharsetStringByteCodec.UTF8.decode(data);
            byte[] enc = encryptor.encrypt(bytes);
            return Base64StringByteCodec.INSTANCE.encode(enc);
        } catch (Exception e) {
            throw new SwlException(SwlCode.ASYMMETRIC_ENCRYPT_EXCEPTION.code(), e.getMessage(), e);
        }
    }

    @Override
    public String decrypt(String data) {
        try {
            byte[] bytes = Base64StringByteCodec.INSTANCE.decode(data);
            byte[] enc = encryptor.decrypt(bytes);
            return CharsetStringByteCodec.UTF8.encode(enc);
        } catch (Exception e) {
            throw new SwlException(SwlCode.ASYMMETRIC_DECRYPT_EXCEPTION.code(), e.getMessage(), e);
        }
    }

    @Override
    public String sign(String data) {
        try {
            byte[] bytes = CharsetStringByteCodec.UTF8.decode(data);
            byte[] sign = encryptor.sign(bytes);
            return Base64StringByteCodec.INSTANCE.encode(sign);
        } catch (Exception e) {
            throw new SwlException(SwlCode.ASYMMETRIC_SIGN_EXCEPTION.code(), e.getMessage(), e);
        }
    }

    @Override
    public boolean verify(String sign, String data) {
        try {
            byte[] signBytes = Base64StringByteCodec.INSTANCE.decode(sign);
            byte[] dataBytes = CharsetStringByteCodec.UTF8.decode(data);
            return encryptor.verify(signBytes, dataBytes);
        } catch (Exception e) {
            throw new SwlException(SwlCode.ASYMMETRIC_VERIFY_EXCEPTION.code(), e.getMessage(), e);
        }
    }
}
