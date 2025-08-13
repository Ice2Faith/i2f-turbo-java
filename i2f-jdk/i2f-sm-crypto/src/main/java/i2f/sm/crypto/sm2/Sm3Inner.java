package i2f.sm.crypto.sm2;

/**
 * @author Ice2Faith
 * @date 2025/8/13 18:42
 */
public class Sm3Inner {
    public static byte[] sm3(byte[] array) {
        return Sm3InnerCipher.sm3(array);
    }

    public static byte[] hmac(byte[] input, byte[] key) {
        return Sm3InnerCipher.hmac(input, key);
    }
}
