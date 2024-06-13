package i2f.jce.std.signature;


import i2f.array.ArrayUtil;

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
}
