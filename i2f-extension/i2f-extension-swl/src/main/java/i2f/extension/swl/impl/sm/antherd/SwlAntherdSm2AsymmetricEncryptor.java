package i2f.extension.swl.impl.sm.antherd;

import com.antherd.smcrypto.sm2.Keypair;
import i2f.crypto.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.extension.jce.sm.antherd.encrypt.asymmetric.Sm2Encryptor;
import i2f.swl.consts.SwlCode;
import i2f.swl.exception.SwlException;
import i2f.swl.std.ISwlAsymmetricEncryptor;

/**
 * @author Ice2Faith
 * @date 2024/7/10 9:45
 * @desc
 */
public class SwlAntherdSm2AsymmetricEncryptor implements ISwlAsymmetricEncryptor {
    private Sm2Encryptor encryptor = new Sm2Encryptor();

    @Override
    public AsymKeyPair generateKeyPair() {
        Keypair keypair = Sm2Encryptor.genKey();
        return new AsymKeyPair(keypair.getPublicKey(), keypair.getPrivateKey());
    }

    @Override
    public AsymKeyPair getKeyPair() {
        return encryptor.getAsymKeyPair();
    }

    @Override
    public void setKeyPair(AsymKeyPair key) {
        encryptor.setAsymKeyPair(key);
    }

    @Override
    public String getPublicKey() {
        return encryptor.getPublicKeyString();
    }

    @Override
    public void setPublicKey(String publicKey) {
        encryptor.setPublicKeyString(publicKey);
    }

    @Override
    public String getPrivateKey() {
        return encryptor.getPrivateKeyString();
    }

    @Override
    public void setPrivateKey(String privateKey) {
        encryptor.setPrivateKeyString(privateKey);
    }

    @Override
    public String encrypt(String data) {
        try {
            return encryptor.encrypt(data);
        } catch (Exception e) {
            throw new SwlException(SwlCode.ASYMMETRIC_ENCRYPT_EXCEPTION.code(), e.getMessage(), e);
        }
    }

    @Override
    public String decrypt(String data) {
        try {
            return encryptor.decrypt(data);
        } catch (Exception e) {
            throw new SwlException(SwlCode.ASYMMETRIC_DECRYPT_EXCEPTION.code(), e.getMessage(), e);
        }
    }

    @Override
    public String sign(String data) {
        try {
            return encryptor.sign(data);
        } catch (Exception e) {
            throw new SwlException(SwlCode.ASYMMETRIC_SIGN_EXCEPTION.code(), e.getMessage(), e);
        }
    }

    @Override
    public boolean verify(String sign, String data) {
        try {
            return encryptor.verify(sign, data);
        } catch (Exception e) {
            throw new SwlException(SwlCode.ASYMMETRIC_VERIFY_EXCEPTION.code(), e.getMessage(), e);
        }
    }
}
