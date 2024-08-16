package i2f.swl.core.key.ttl;

import i2f.jce.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.swl.core.key.SwlKeyManager;

/**
 * @author Ice2Faith
 * @date 2024/8/8 22:14
 * @desc
 */
public interface SwlTtlKeyManager extends SwlKeyManager {
    boolean preferSetAndTtl();

    void setSelfKeyPairWithTtl(String selfAsymSign, AsymKeyPair keyPair, long ttlSeconds);

    void setOtherPublicKeyWithTtl(String otherAsymSign, String publicKey, long ttlSeconds);

    void setSelfTtl(String selfAsymSign, long ttlSeconds);

    void setOtherTtl(String otherAsymSign, long ttlSeconds);
}
