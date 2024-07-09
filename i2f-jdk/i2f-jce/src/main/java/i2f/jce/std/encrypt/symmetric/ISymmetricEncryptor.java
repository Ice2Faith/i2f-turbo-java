package i2f.jce.std.encrypt.symmetric;

import i2f.codec.bytes.raw.HexStringByteCodec;
import i2f.jce.std.encrypt.IEncryptor;

import java.security.Key;

/**
 * @author Ice2Faith
 * @date 2024/3/27 8:49
 * @desc
 */
public interface ISymmetricEncryptor extends IEncryptor {

    void setKey(Key key);

    Key getKey();

    void setKeyBytes(byte[] keyBytes);

    byte[] getKeyBytes();

    default void setKeyString(String str) {
        setKeyBytes(HexStringByteCodec.INSTANCE.decode(str));
    }

    default String getKeyString() {
        return HexStringByteCodec.INSTANCE.encode(getKeyBytes());
    }
}
