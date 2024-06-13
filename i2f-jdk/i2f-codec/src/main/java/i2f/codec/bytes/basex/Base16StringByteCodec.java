package i2f.codec.bytes.basex;

import i2f.codec.bytes.IStringByteCodec;

/**
 * @author Ice2Faith
 * @date 2024/6/13 20:32
 * @desc
 */
public class Base16StringByteCodec implements IStringByteCodec {
    public static final Base16StringByteCodec INSTANCE = new Base16StringByteCodec();

    @Override
    public String encode(byte[] data) {
        return Base16.encode(data);
    }

    @Override
    public byte[] decode(String enc) {
        return Base16.decode(enc);
    }
}
