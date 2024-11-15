package i2f.hash.impl;


import i2f.serialize.bytes.IBytesObjectSerializer;

/**
 * @author ltb
 * @date 2022/4/27 15:05
 * @desc
 */
public class BkdrHashProvider<T> extends IByteArrayHashProvider<T> {
    public BkdrHashProvider(IBytesObjectSerializer serializer) {
        super(serializer);
    }

    @Override
    public long hashBytes(byte[] data) {
        long seed = 131; /* 31 131 1313 13131 131313 etc.. */
        long hash = 0;

        for (int i = 0; i < data.length; i++) {
            hash = (hash * seed) + (data[i]);
        }

        return hash;
    }
}
