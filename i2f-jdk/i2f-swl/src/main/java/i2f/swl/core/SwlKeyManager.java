package i2f.swl.core;

import i2f.jce.std.encrypt.asymmetric.key.AsymKeyPair;

/**
 * @author Ice2Faith
 * @date 2024/8/4 21:42
 * @desc
 */
public interface SwlKeyManager {
    AsymKeyPair getSelfKeyPair(String selfAsymSign);

    void setSelfKeyPair(String selfAsymSign,AsymKeyPair keyPair);

    AsymKeyPair getOtherKeyPair(String otherAsymSign);

    void setOtherKeyPair(String otherAsymSign,AsymKeyPair keyPair);
}
