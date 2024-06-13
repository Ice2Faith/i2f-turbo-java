package i2f.codec.bytes.basex;

import i2f.codec.bytes.IStringByteCodec;

/**
 * @author Ice2Faith
 * @date 2024/6/13 20:32
 * @desc
 */
public class Base64UrlStringByteCodec implements IStringByteCodec {
    public static final Base64UrlStringByteCodec INSTANCE = new Base64UrlStringByteCodec();

    @Override
    public String encode(byte[] data) {
        return Base64.encodeUrl(data);
    }

    @Override
    public byte[] decode(String enc) {
        return Base64.decodeUrl(enc);
    }
}
