package i2f.hash.impl;


import i2f.serialize.std.bytes.IBytesObjectSerializer;

/**
 * @author Ice2Faith
 * @date 2022/4/27 15:05
 * @desc
 */
public class DekHashProvider<T> extends IByteArrayHashProvider<T> {
    public DekHashProvider(IBytesObjectSerializer serializer) {
        super(serializer);
    }

    @Override
    public long hashBytes(byte[] data) {
        long hash = data.length;

        for (int i = 0; i < data.length; i++) {
            hash = ((hash << 5) ^ (hash >>> 27)) ^ (data[i]);
        }
        return hash;
    }
}
