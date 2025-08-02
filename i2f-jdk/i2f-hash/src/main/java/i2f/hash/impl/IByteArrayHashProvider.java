package i2f.hash.impl;


import i2f.hash.IHashProvider;
import i2f.serialize.std.bytes.IBytesObjectSerializer;

/**
 * @author Ice2Faith
 * @date 2022/4/27 15:09
 * @desc
 */
public abstract class IByteArrayHashProvider<T> implements IHashProvider<T> {
    protected IBytesObjectSerializer serializer;

    public IByteArrayHashProvider(IBytesObjectSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public long hash(T obj) {
        if (obj == null) {
            return 0;
        }
        byte[] data = serializer.serialize(obj);
        return hashBytes(data);
    }

    public abstract long hashBytes(byte[] data);
}
