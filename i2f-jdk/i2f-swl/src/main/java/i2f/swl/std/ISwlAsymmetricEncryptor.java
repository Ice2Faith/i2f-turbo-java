package i2f.swl.std;

import i2f.jce.std.encrypt.asymmetric.key.AsymKeyPair;

/**
 * @author Ice2Faith
 * @date 2024/7/9 18:41
 * @desc
 */
public interface ISwlAsymmetricEncryptor {
    AsymKeyPair generateKeyPair();

    AsymKeyPair getKeyPair();

    void setKeyPair(AsymKeyPair key);

    String getPublicKey();

    void setPublicKey(String publicKey);

    String getPrivateKey();

    void setPrivateKey(String privateKey);

    String encrypt(String data);

    String decrypt(String data);

    String sign(String data);

    boolean verify(String sign, String data);
}
