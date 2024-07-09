package i2f.springboot.swl.std;

import i2f.jce.std.encrypt.asymmetric.key.AsymKeyPair;

/**
 * @author Ice2Faith
 * @date 2024/7/9 20:35
 * @desc
 */
public interface ISwlStore {
    AsymKeyPair getSelfKeyPair();
    void setSelfKeyPair(AsymKeyPair selfKeyPair);
    String getSelfPrivateKey(String selfAsymSign);

    String getOtherPublicKey(String otherAsymSign);
    void setOtherPublicKey(String otherAsymSign, String publicKey);

    boolean containsNonce(String nonce);
    void setNonce(String nonce,long timeoutSeconds);
}
