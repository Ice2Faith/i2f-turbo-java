package i2f.crypto.std.signature;


import i2f.array.ArrayUtil;
import i2f.codec.bytes.base64.Base64StringByteCodec;
import i2f.codec.bytes.raw.HexStringByteCodec;

/**
 * @author Ice2Faith
 * @date 2024/3/28 10:54
 * @desc
 */
public interface ISignatureSigner {
    byte[] sign(byte[] data) throws Exception;

    default boolean verify(byte[] sign, byte[] data) throws Exception {
        byte[] cmp = sign(data);
        return ArrayUtil.equal(sign, cmp);
    }

    default String signAsBase64(byte[] data) throws Exception {
        return Base64StringByteCodec.INSTANCE.encode(sign(data));
    }

    default boolean verifyByBase64(String sign, byte[] data) throws Exception {
        return verify(Base64StringByteCodec.INSTANCE.decode(sign), data);
    }

    default String signAsHex(byte[] data) throws Exception {
        return HexStringByteCodec.INSTANCE.encode(sign(data));
    }

    default boolean verifyByHex(String sign, byte[] data) throws Exception {
        return verify(HexStringByteCodec.INSTANCE.decode(sign), data);
    }
}
