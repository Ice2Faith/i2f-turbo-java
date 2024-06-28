package i2f.jce.std.digest;

import i2f.array.ArrayUtil;
import i2f.codec.bytes.raw.HexStringByteCodec;
import i2f.jce.std.signature.ISignatureSigner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author Ice2Faith
 * @date 2024/3/27 8:39
 * @desc
 */
public interface IMessageDigester extends ISignatureSigner {
    byte[] digest(InputStream is) throws Exception;

    default byte[] digest(byte[] data) throws Exception {
        return digest(new ByteArrayInputStream(data));
    }

    default String digestAsHex(InputStream is) throws Exception {
        return HexStringByteCodec.INSTANCE.encode(digest(is));
    }

    default String digestAsHex(byte[] data) throws Exception {
        return HexStringByteCodec.INSTANCE.encode(digest(data));
    }

    @Override
    default byte[] sign(byte[] data) throws Exception {
        return digest(data);
    }

    default boolean verify(byte[] sign, InputStream is) throws Exception {
        byte[] res = digest(is);
        return ArrayUtil.equal(res, sign);
    }

    default boolean verify(byte[] sign, byte[] data) throws Exception {
        byte[] res = digest(data);
        return ArrayUtil.equal(res, sign);
    }

    default boolean verifyByHex(String sign, InputStream is) throws Exception {
        return verify(HexStringByteCodec.INSTANCE.decode(sign), is);
    }

    default boolean verifyByHex(String sign, byte[] data) throws Exception {
        return verify(HexStringByteCodec.INSTANCE.decode(sign), data);
    }
}
