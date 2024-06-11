package i2f.text;

import java.util.List;
import java.util.Vector;

/**
 * @author Ice2Faith
 * @date 2024/6/11 10:59
 * @desc
 */
public class StringUtils {
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean nonEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean hasText(CharSequence str) {
        if (str == null) {
            return false;
        }
        int len = str.length();
        for (int i = 0; i < len; i++) {
            if (!Character.isWhitespace(str.charAt(str.charAt(i)))) {
                return true;
            }
        }
        return false;
    }

    public static boolean nonText(CharSequence str) {
        return !hasText(str);
    }

    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    public static String trimLeft(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        int len = str.length();
        boolean keep = false;
        for (int i = 0; i < len; i++) {
            char ch = str.charAt(i);
            if (!keep) {
                if (!Character.isWhitespace(ch)) {
                    keep = true;
                }
            }
            if (keep) {
                builder.append(ch);
            }
        }

        return builder.toString();
    }

    public static String trimRight(String str) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        int idx = len - 1;
        for (int i = idx; i >= 0; i--) {
            char ch = str.charAt(i);
            if (!Character.isWhitespace(ch)) {
                break;
            }
        }
        return str.substring(0, idx + 1);
    }

    public static String trimAll(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        int len = str.length();
        for (int i = 0; i < len; i++) {
            char ch = str.charAt(i);
            if (!Character.isWhitespace(ch)) {
                builder.append(ch);
            }
        }
        return builder.toString();
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
        if (str.contains("_")) {
            return str.trim();
        }
        StringBuffer buffer = new StringBuffer();
        char[] arr = str.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] >= 'A' && arr[i] <= 'Z') {
                if (i > 0) {
                    buffer.append("_");
                }
                buffer.append((char) (arr[i] | 32));
            } else {
                buffer.append(arr[i]);
            }
        }
        return buffer.toString();
    }

    public static String trim(String str, List<String> trimPrefixes, List<String> trimSuffixes) {
        return trim(str, true, trimPrefixes, trimSuffixes, null, null);
    }

    public static String trim(String str, List<String> trimPrefixes, List<String> trimSuffixes, String appendPrefix, String appendSuffix) {
        return trim(str, true, trimPrefixes, trimSuffixes, appendPrefix, appendSuffix);
    }

    public static String trim(String str, boolean trimWhiteSpace, List<String> trimPrefixes, List<String> trimSuffixes, String appendPrefix, String appendSuffix) {
        if (str == null) {
            return null;
        }
        if (trimPrefixes != null && !trimPrefixes.isEmpty()) {
            if (trimWhiteSpace) {
                str = str.trim();
            }
            for (String item : trimPrefixes) {
                if (str.startsWith(item)) {
                    str = str.substring(item.length());
                    break;
                }
            }
        }
        if (trimSuffixes != null && !trimSuffixes.isEmpty()) {
            if (trimWhiteSpace) {
                str = str.trim();
            }
            for (String item : trimSuffixes) {
                if (str.endsWith(item)) {
                    str = str.substring(0, str.length() - item.length());
                    break;
                }
            }
        }
        if (appendPrefix != null) {
            if (trimWhiteSpace) {
                str = str.trim();
            }
            if (!str.isEmpty()) {
                str = appendPrefix + str;
            }
        }
        if (appendSuffix != null) {
            if (trimWhiteSpace) {
                str = str.trim();
            }
            if (!str.isEmpty()) {
                str = str + appendSuffix;
            }
        }
        return str;
    }

    public static String joins(String separator, String... strs) {
        String ret = null;
        for (String str : strs) {
            ret = join(ret, separator, str);
        }
        return ret;
    }

    public static String join(String head, String separator, String tail) {
        if (isEmpty(separator)) {
            if (isEmpty(head)) {
                return tail;
            }
            if (isEmpty(tail)) {
                return head;
            }
            return head + tail;
        }
        if (isEmpty(head)) {
            if (isEmpty(tail)) {
                return "";
            } else {
                if (tail.startsWith(separator)) {
                    return tail.substring(separator.length());
                } else {
                    return tail;
                }
            }
        } else {
            if (isEmpty(tail)) {
                if (head.endsWith(separator)) {
                    return head.substring(0, head.length() - separator.length());
                } else {
                    return head;
                }
            } else {
                if (head.endsWith(separator)) {
                    head = head.substring(0, head.length() - separator.length());
                }
                if (tail.startsWith(separator)) {
                    tail = tail.substring(separator.length());
                }
                return head + separator + tail;
            }
        }
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
                if ("".equals(item)) {
                    continue;
                }
            }
            result.add(item);
        }
        return result.toArray(new String[0]);
    }
}
