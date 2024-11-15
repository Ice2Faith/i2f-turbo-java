package i2f.hash.impl;


import i2f.serialize.bytes.IBytesObjectSerializer;

/**
 * @author ltb
 * @date 2022/4/27 15:05
 * @desc
 */
public class ElfHashProvider<T> extends IByteArrayHashProvider<T> {
    public ElfHashProvider(IBytesObjectSerializer serializer) {
        super(serializer);
    }

    @Override
    public long hashBytes(byte[] data) {
        long hash = 0;
        long x = 0;

        for (int i = 0; i < data.length; i++) {
            hash = (hash << 4) + (data[i]);
            if ((x = hash & 0xF0000000L) != 0) {
                hash ^= (x >>> 24);
            }
            hash &= ~x;
        }

        return hash;
    }
}
