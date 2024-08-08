package i2f.swl.core.key;

import i2f.jce.std.encrypt.asymmetric.key.AsymKeyPair;

/**
 * @author Ice2Faith
 * @date 2024/8/4 21:42
 * @desc
 */
public interface SwlKeyManager {
    AsymKeyPair getSelfKeyPair();

    String getDefaultSelfAsymSign();

    void setDefaultSelfAsymSign(String selfAsymSign);

    AsymKeyPair getSelfKeyPair(String selfAsymSign);

    void setSelfKeyPair(String selfAsymSign, AsymKeyPair keyPair);

    String getOtherPublicKey();

    String getDefaultOtherAsymSign();

    void setDefaultOtherAsymSign(String otherAsymSign);

    String getOtherPublicKey(String otherAsymSign);

    void setOtherPublicKey(String otherAsymSign, String publicKey);
}
