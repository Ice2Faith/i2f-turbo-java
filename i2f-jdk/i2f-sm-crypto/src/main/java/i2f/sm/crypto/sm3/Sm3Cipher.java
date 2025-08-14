package i2f.sm.crypto.sm3;

import i2f.sm.crypto.exception.SmException;
import i2f.sm.crypto.sm2.Sm3InnerCipher;
import i2f.sm.crypto.util.CipUtils;

/**
 * @author Ice2Faith
 * @date 2025/8/13 18:39
 */
public class Sm3Cipher {
    public static enum Mode {
        SM3, // default
        HMAC
    }

    public static String sm3(String input, Mode mode, String key) {
        byte[] inputArr = CipUtils.utf8ToArray(input);
        byte[] keyArr = key == null || key.isEmpty() ? null : CipUtils.hexToArray(key);
        byte[] retArr = sm3(inputArr, mode, keyArr);
        return CipUtils.arrayToHex(retArr);
    }

    public static byte[] sm3(byte[] input, Mode mode, byte[] key) {
        if (mode == Mode.HMAC) {
            if (key == null) {
                throw new SmException("invalid key");
            }
            return Sm3InnerCipher.hmac(input, key);
        }
        return Sm3InnerCipher.sm3(input);
    }
}
