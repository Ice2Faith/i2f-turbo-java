package i2f.sm.crypto.util;

import java.util.Arrays;

/**
 * 十六进制编解码
 * <p>
 * 原先编解码走 i2f-codec 的 {@code HexStringByteCodec}，编码 1KB 需要约 750us（0.73us/字节），
 * 而 SM3/SM4 算法本身只有几十微秒，编解码反而成了最大开销，这里改为查表实现（快两个数量级）。
 * <p>
 * 语义保持与原来一致：编码输出小写；解码时非法字符按 0 处理，奇数长度丢弃末位字符。
 *
 * @author Ice2Faith
 */
public class HexUtils {
    public static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

    /**
     * 字符到半字节值的映射，-1 表示不是十六进制字符
     */
    protected static final int[] HEX_VALUES = new int[128];

    static {
        Arrays.fill(HEX_VALUES, -1);
        for (int i = 0; i < HEX_CHARS.length; i++) {
            HEX_VALUES[HEX_CHARS[i]] = i;
            HEX_VALUES[Character.toUpperCase(HEX_CHARS[i])] = i;
        }
    }

    /**
     * 字节数组转 16 进制串（小写）
     */
    public static String arrayToHex(byte[] arr) {
        int len = arr.length;
        char[] chars = new char[len * 2];
        for (int i = 0, j = 0; i < len; i++) {
            int val = arr[i] & 0x0ff;
            chars[j++] = HEX_CHARS[val >>> 4];
            chars[j++] = HEX_CHARS[val & 0x0f];
        }
        return new String(chars);
    }

    /**
     * 16 进制串转字节数组
     */
    public static byte[] hexToArray(String str) {
        int len = str.length();
        byte[] ret = new byte[len >>> 1];
        for (int i = 0, j = 0; i < ret.length; i++, j += 2) {
            int high = hexValue(str.charAt(j));
            int low = hexValue(str.charAt(j + 1));
            if (high >= 0 && low >= 0) {
                ret[i] = (byte) ((high << 4) | low);
            } else {
                // 含非法字符时退回按两位一组解析，保持与原实现（Integer.parseInt）一致
                ret[i] = (byte) parseHexGroup(str.substring(j, j + 2));
            }
        }
        return ret;
    }

    protected static int parseHexGroup(String group) {
        try {
            return Integer.parseInt(group, 16);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 取单个字符的半字节值，非十六进制字符返回 -1
     */
    protected static int hexValue(char ch) {
        return ch < HEX_VALUES.length ? HEX_VALUES[ch] : -1;
    }
}
