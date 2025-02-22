package i2f.crypto.impl.jdk.signature;

import i2f.crypto.impl.jdk.encrypt.Encryptor;

/**
 * @author Ice2Faith
 * @date 2024/3/27 17:33
 * @desc
 */
public interface SignatureType {

    default String provider() {
        return null;
    }

    String type();

    default String algorithmName() {
        return Encryptor.cipherAlgorithm(type());
    }

    int[] secretBytesLen();
}
