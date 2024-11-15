package i2f.hash.impl;

import i2f.serialize.bytes.IBytesObjectSerializer;

/**
 * @author ltb
 * @date 2022/4/27 15:05
 * @desc
 */
public class SdbmHashProvider<T> extends IByteArrayHashProvider<T> {
    public SdbmHashProvider(IBytesObjectSerializer serializer) {
        super(serializer);
    }

    @Override
    public long hashBytes(byte[] data) {
        long hash = 0;

        for (int i = 0; i < data.length; i++) {
            hash = (data[i]) + (hash << 6) + (hash << 16) - hash;
        }

        return hash;
    }
}
