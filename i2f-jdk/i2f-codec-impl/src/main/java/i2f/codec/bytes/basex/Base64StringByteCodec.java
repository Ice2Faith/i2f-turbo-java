package i2f.codec.bytes.basex;

import i2f.codec.std.bytes.IStringByteCodec;

/**
 * @author Ice2Faith
 * @date 2024/6/13 20:32
 * @desc
 */
public class Base64StringByteCodec implements IStringByteCodec {
    public static final Base64StringByteCodec INSTANCE = new Base64StringByteCodec();

    @Override
    public String encode(byte[] data) {
        return Base64.encode(data);
    }

    @Override
    public byte[] decode(String enc) {
        return Base64.decode(enc);
    }
}
