package i2f.sm.crypto.sm4;

import com.sun.istack.internal.Nullable;

/**
 * @author Ice2Faith
 * @date 2025/8/13 18:21
 */
public class Sm4 {
    public static byte[] encrypt(byte[] inArray, byte[] key) {
        return encrypt(inArray, key, null, null, null);
    }

    public static byte[] encrypt(byte[] inArray, byte[] key,
                                 @Nullable Sm4Cipher.Padding padding,
                                 @Nullable Sm4Cipher.Mode mode,
                                 @Nullable byte[] iv) {
        return Sm4Cipher.sm4(inArray, key, Sm4Cipher.CryptFlag.ENCRYPT, padding, mode, iv);
    }

    public static byte[] decrypt(byte[] inArray, byte[] key) {
        return decrypt(inArray, key, null, null, null);
    }

    public static byte[] decrypt(byte[] inArray, byte[] key,
                                 @Nullable Sm4Cipher.Padding padding,
                                 @Nullable Sm4Cipher.Mode mode,
                                 @Nullable byte[] iv) {
        return Sm4Cipher.sm4(inArray, key, Sm4Cipher.CryptFlag.DECRYPT, padding, mode, iv);
    }

    public static String encrypt(String inArray, String key) {
        return encrypt(inArray, key, null, null, null);
    }

    public static String encrypt(String inArray, String key,
                                 @Nullable Sm4Cipher.Padding padding,
                                 @Nullable Sm4Cipher.Mode mode,
                                 @Nullable String iv) {
        return Sm4Cipher.sm4(inArray, key, Sm4Cipher.CryptFlag.ENCRYPT, padding, mode, iv);
    }

    public static String decrypt(String inArray, String key) {
        return decrypt(inArray, key, null, null, null);
    }

    public static String decrypt(String inArray, String key,
                                 @Nullable Sm4Cipher.Padding padding,
                                 @Nullable Sm4Cipher.Mode mode,
                                 @Nullable String iv) {
        return Sm4Cipher.sm4(inArray, key, Sm4Cipher.CryptFlag.DECRYPT, padding, mode, iv);
    }
}
