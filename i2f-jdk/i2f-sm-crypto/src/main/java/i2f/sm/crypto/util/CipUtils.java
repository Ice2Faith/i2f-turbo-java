package i2f.sm.crypto.util;

import i2f.codec.bytes.charset.CharsetStringByteCodec;
import i2f.codec.bytes.raw.HexStringByteCodec;

/**
 * @author Ice2Faith
 * @date 2025/8/13 20:57
 * @desc
 */
public class CipUtils {

    /**
     * 16 进制串转字节数组
     */
    public static byte[] hexToArray(String str) {
        return HexStringByteCodec.INSTANCE.decode(str);
    }

    /**
     * 字节数组转 16 进制串
     */
    public static String arrayToHex(byte[] arr) {
        return HexStringByteCodec.INSTANCE.encode(arr).toLowerCase();
    }

    /**
     * utf8 串转字节数组
     */
    public static byte[] utf8ToArray(String str) {
        return CharsetStringByteCodec.UTF8.decode(str);
    }

    /**
     * 字节数组转 utf8 串
     */
    public static String arrayToUtf8(byte[] arr) {
        return CharsetStringByteCodec.UTF8.encode(arr);
    }

    /**
     * 32 比特循环左移
     */
    public static int rotl(int x, int n) {
        int s = n & 31;
        return (x << s) | (x >>> (32 - s));
    }

    /**
     * 补全16进制字符串
     */
    public static String leftPad(String input, int num) {
        if (input.length() >= num) {
            return input;
        }
        int diffLen = num - input.length();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < diffLen; i++) {
            builder.append('0');
        }
        builder.append(input);
        return builder.toString();
    }

}
