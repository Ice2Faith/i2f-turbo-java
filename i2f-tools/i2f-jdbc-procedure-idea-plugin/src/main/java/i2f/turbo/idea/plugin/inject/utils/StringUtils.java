package i2f.turbo.idea.plugin.inject.utils;

import java.util.Vector;

/**
 * @author Ice2Faith
 * @date 2026/4/14 11:00
 * @desc
 */
public class StringUtils {
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean nonEmpty(String str) {
        return !isEmpty(str);
    }

    public static String firstUpper(String str) {
        if (str == null) {
            return null;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String firstLower(String str) {
        if (str == null) {
            return null;
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    public static String toUpper(String str) {
        if (str == null) {
            return null;
        }
        return str.toUpperCase();
    }

    public static String toLower(String str) {
        if (str == null) {
            return null;
        }
        return str.toLowerCase();
    }

    public static String toPascal(String str) {
        if (isEmpty(str)) {
            return str;
        }
        if (!str.contains("_") && !str.contains("-")) {
            return firstUpper(str);
        }
        String[] arr = split(str, true, "_|-", -1, true);
        StringBuffer buffer = new StringBuffer();
        for (String item : arr) {
            buffer.append(firstUpper(item));
        }
        return buffer.toString();
    }

    public static String toCamel(String str) {
        if (isEmpty(str)) {
            return str;
        }
        if (!str.contains("_") && !str.contains("-")) {
            return firstLower(str);
        }
        String[] arr = split(str, true, "_|-", -1, true);
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < arr.length; i++) {
            if (i == 0) {
                buffer.append(firstLower(arr[i]));
            } else {
                buffer.append(firstUpper(arr[i]));
            }
        }
        return buffer.toString();
    }

    public static String toUnderScore(String str) {
        return toLinkCase0(str, "_");
    }

    public static String toSnake(String str) {
        return toLinkCase0(str, "-");
    }

    public static String toPropertyCase(String str) {
        return toLinkCase0(str, ".");
    }

    public static String toPathCase(String str) {
        return toLinkCase0(str, "/");
    }

    public static String toColonCase(String str) {
        return toLinkCase0(str, ":");
    }

    public static String toLinkCase0(String str, String separator) {
        if (str.contains(separator)) {
            return str.trim();
        }
        StringBuffer buffer = new StringBuffer();
        char[] arr = str.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] >= 'A' && arr[i] <= 'Z') {
                if (i > 0) {
                    buffer.append(separator);
                }
                buffer.append((char) (arr[i] | 32));
            } else {
                buffer.append(arr[i]);
            }
        }
        return buffer.toString();
    }


    public static String[] split(String str, String regex, boolean removeEmpty) {
        return split(str, false, regex, -1, removeEmpty);
    }

    public static String[] split(String str, boolean trimBefore, String regex, int limit, boolean removeEmpty) {
        String[] ret = new String[]{};
        if (str == null) {
            return ret;
        }
        if (trimBefore) {
            str = str.trim();
        }
        ret = str.split(regex, limit);
        Vector<String> result = new Vector<>();
        for (String item : ret) {
            if (removeEmpty) {
                if (item.isEmpty()) {
                    continue;
                }
            }
            result.add(item);
        }
        return result.toArray(new String[0]);
    }
}
