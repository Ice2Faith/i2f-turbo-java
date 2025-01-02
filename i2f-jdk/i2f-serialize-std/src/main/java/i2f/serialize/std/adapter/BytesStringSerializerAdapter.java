package i2f.serialize.std.adapter;

import i2f.serialize.std.bytes.IBytesObjectSerializer;
import i2f.serialize.std.exception.SerializeException;
import i2f.serialize.std.str.IStringObjectSerializer;

/**
 * 适配器
 * 将BytesSerializer转换为StringSerializer
 */
public class BytesStringSerializerAdapter implements IStringObjectSerializer {
    private IBytesObjectSerializer serializer;
    private String charset = "UTF-8";

    public BytesStringSerializerAdapter(IBytesObjectSerializer serializer) {
        this.serializer = serializer;
    }

    public BytesStringSerializerAdapter(IBytesObjectSerializer serializer, String charset) {
        this.serializer = serializer;
        this.charset = charset;
    }

    public byte[] string2bytes(String str) {
        try {
            return str.getBytes(charset);
        } catch (Exception e) {
            throw new SerializeException(e.getMessage(), e);
        }
    }

    public String bytes2string(byte[] bytes) {
        try {
            return new String(bytes, charset);
        } catch (Exception e) {
            throw new SerializeException(e.getMessage(), e);
        }
    }

    @Override
    public String serialize(Object data) {
        byte[] bytes = serializer.serialize(data);
        return bytes2string(bytes);
    }

    @Override
    public Object deserialize(String enc) {
        byte[] bytes = string2bytes(enc);
        return serializer.deserialize(bytes);
    }

    @Override
    public Object deserialize(String enc, Class<?> clazz) {
        byte[] bytes = string2bytes(enc);
        return serializer.deserialize(bytes, clazz);
    }

    @Override
    public Object deserialize(String enc, Object type) {
        byte[] bytes = string2bytes(enc);
        return serializer.deserialize(bytes, type);
    }

}
