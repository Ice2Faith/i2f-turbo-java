package i2f.serialize.std.bytes;

import i2f.serialize.std.type.ITypeSerializer;

import java.util.Base64;

/**
 * @author Ice2Faith
 * @date 2023/6/27 17:12
 * @desc
 */
public interface IBytesTypeSerializer<T> extends ITypeSerializer<byte[], T> {

    default String serializeAsBase64(T data) {
        byte[] enc = encode(data);
        if (enc == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(enc);
    }

    default T deserializeByBase64(String enc) {
        if (enc == null) {
            return null;
        }
        byte[] data = Base64.getDecoder().decode(enc);
        return decode(data);
    }
}
