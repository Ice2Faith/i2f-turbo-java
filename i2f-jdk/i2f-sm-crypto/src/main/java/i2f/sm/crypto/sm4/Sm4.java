package i2f.sm.crypto.sm4;


import i2f.sm.crypto.util.CipUtils;

/**
 * @author Ice2Faith
 * @date 2025/8/13 18:21
 */
public class Sm4 {
    public static byte[] generateKey() {
        return Sm4Cipher.generateKey();
    }

    public static String generateHexKey() {
        byte[] hex = generateKey();
        return CipUtils.arrayToHex(hex);
    }

    public static byte[] encrypt(byte[] inArray, byte[] key) {
        return encrypt(inArray, key, null, null, null);
    }

    public static byte[] encrypt(byte[] inArray, byte[] key,
                                 Sm4Cipher.Padding padding,
                                 Sm4Cipher.Mode mode,
                                 byte[] iv) {
        return Sm4Cipher.sm4(inArray, key, Sm4Cipher.CryptFlag.ENCRYPT, padding, mode, iv);
    }

    public static byte[] decrypt(byte[] inArray, byte[] key) {
        return decrypt(inArray, key, null, null, null);
    }

    public static byte[] decrypt(byte[] inArray, byte[] key,
                                 Sm4Cipher.Padding padding,
                                 Sm4Cipher.Mode mode,
                                 byte[] iv) {
        return Sm4Cipher.sm4(inArray, key, Sm4Cipher.CryptFlag.DECRYPT, padding, mode, iv);
    }

    public static String encrypt(String inArray, String key) {
        return encrypt(inArray, key, null, null, null);
    }

    public static String encrypt(String inArray, String key,
                                 Sm4Cipher.Padding padding,
                                 Sm4Cipher.Mode mode,
                                 String iv) {
        return Sm4Cipher.sm4(inArray, key, Sm4Cipher.CryptFlag.ENCRYPT, padding, mode, iv);
    }

    public static String decrypt(String inArray, String key) {
        return decrypt(inArray, key, null, null, null);
    }

    public static String decrypt(String inArray, String key,
                                 Sm4Cipher.Padding padding,
                                 Sm4Cipher.Mode mode,
                                 String iv) {
        return Sm4Cipher.sm4(inArray, key, Sm4Cipher.CryptFlag.DECRYPT, padding, mode, iv);
    }
}
