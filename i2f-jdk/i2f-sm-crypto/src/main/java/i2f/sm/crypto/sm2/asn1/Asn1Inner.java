package i2f.sm.crypto.sm2.asn1;

import java.math.BigInteger;

/**
 * @author Ice2Faith
 * @date 2025/8/13 18:42
 */
public class Asn1Inner {
    public static String bigintToValue(BigInteger bigint) {
        String h = bigint.toString(16);
        if (h.charAt(0) != '-') {
            // 正数
            if (h.length() % 2 == 1) {
                h = "0" + h; // 补齐到整字节
            } else if (!h.matches("^[0-7].*$")) {
                h = "00" + h; // 非0开头，则补一个全0字节
            }
        } else {
            // 负数
            h = h.substring(1);

            int len = h.length();
            if (len % 2 == 1) {
                len += 1; // 补齐到整字节
            } else if (!h.matches("^[0-7].*$")) {
                len += 2; // 非0开头，则补一个全0字节
            }

            StringBuilder mask = new StringBuilder();
            for (int i = 0; i < len; i++) {
                mask.append("f");
            }
            BigInteger iMask = new BigInteger(mask.toString(), 16);

            // 对绝对值取反，加1
            BigInteger iH = iMask.xor(bigint).add(BigInteger.ONE);
            h = iH.toString(16).replace("-", "");
        }
        return h;
    }

    /**
     * 获取 l 占用字节数
     */
    public static int getLenOfL(String str, int start) {
        if (Integer.parseInt(str.charAt(start + 2) + "") < 8) {
            return 1; // l 以0开头，则表示短格式，只占一个字节
        }
        return Integer.parseInt(str.substring(start + 2, 2)) & 0x07f + 1; // 长格式，取第一个字节后7位作为长度真正占用字节数，再加上本身
    }

    /**
     * 获取 l
     */
    public static int getL(String str, int start) {
        // 获取 l
        int len = getLenOfL(str, start);
        String l = str.substring(start + 2, len * 2);

        if (l == null || l.isEmpty()) {
            return -1;
        }
        BigInteger bigint = Integer.parseInt(l.charAt(0) + "") < 8 ? new BigInteger(l, 16) : new BigInteger(l.substring(2), 16);

        return bigint.intValue();
    }

    /**
     *
     * 获取 v 的位置
     */
    public static int getStartOfV(String str,int start) {
        int len = getLenOfL(str, start);
        return start + (len + 1) * 2;
    }
}
