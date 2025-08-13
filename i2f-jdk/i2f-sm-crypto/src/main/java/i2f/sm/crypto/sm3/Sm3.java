package i2f.sm.crypto.sm3;

/**
 * @author Ice2Faith
 * @date 2025/8/13 18:39
 */
public class Sm3 {
    public static byte[] sm3(byte[] input) {
        return sm3(input, null, null);
    }

    public static byte[] sm3(byte[] input, Sm3Cipher.Mode mode, byte[] key) {
        return Sm3Cipher.sm3(input, mode, key);
    }

    public static String sm3(String input) {
        return sm3(input, null, null);
    }

    public static String sm3(String input, Sm3Cipher.Mode mode, String key) {
        return Sm3Cipher.sm3(input, mode, key);
    }
}
