package i2f.text;


/**
 * @author Ice2Faith
 * @date 2022/3/19 15:30
 * @desc
 */
public class SensibleStringUtil {
    public static String hideSensibleInfo(String str, int keepStartLen) {
        return hideSensibleInfo(str, keepStartLen, 0, -1);
    }

    public static String hideSensibleInfo(String str, int keepStartLen, int keepEndLen) {
        return hideSensibleInfo(str, keepStartLen, keepEndLen, -1);
    }

    public static String hideSensibleInfo(String str, int keepStartLen, int keepEndLen, int maxLen) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        String start = str.substring(0, keepStartLen);
        String end = str.substring(str.length() - keepEndLen);
        String mid = str.substring(keepStartLen, str.length() - keepEndLen);
        if (maxLen >= 0) {
            int llen = maxLen - keepStartLen - keepEndLen;
            if (llen >= 0) {
                mid = mid.substring(0, llen);
            }
        }
        int mlen = mid.length();
        StringBuilder builder = new StringBuilder(str.length());
        builder.append(start);
        for (int i = 0; i < mlen; i++) {
            builder.append("*");
        }
        builder.append(end);
        return builder.toString();
    }
}
