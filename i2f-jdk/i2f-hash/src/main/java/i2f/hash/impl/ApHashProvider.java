package i2f.hash.impl;


import i2f.serialize.bytes.IBytesObjectSerializer;

/**
 * @author ltb
 * @date 2022/4/27 15:05
 * @desc
 */
public class ApHashProvider<T> extends IByteArrayHashProvider<T> {
    public ApHashProvider(IBytesObjectSerializer serializer) {
        super(serializer);
    }

    @Override
    public long hashBytes(byte[] data) {
        long hash = 0xAAAAAAAA;

        for (int i = 0; i < data.length; i++) {
            hash ^= ((i & 1) == 0) ? ((hash << 7) ^ (data[i]) * (hash >>> 3)) :
                    (~((hash << 11) + (data[i]) ^ (hash >> 5)));
        }

        return hash;
    }
}
