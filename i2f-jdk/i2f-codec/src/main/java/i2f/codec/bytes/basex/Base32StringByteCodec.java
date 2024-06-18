package i2f.codec.bytes.basex;

import i2f.codec.bytes.IStringByteCodec;

/**
 * @author Ice2Faith
 * @date 2024/6/13 20:32
 * @desc
 */
public class Base32StringByteCodec implements IStringByteCodec {
    public static final Base32StringByteCodec INSTANCE = new Base32StringByteCodec();

    @Override
    public String encode(byte[] data) {
        return Base32.encode(data);
    }

    @Override
    public byte[] decode(String enc) {
        return Base32.decode(enc);
    }
}
