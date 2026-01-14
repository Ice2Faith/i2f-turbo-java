package i2f.text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/6/11 10:59
 * @desc
 */
public class StringUtils {
    public static final String SPECIAL_WHITE_SPACE_STR = "\u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200A\u202F\u205F\u3000\u2028\u2029\u0085\u200B\u200C\u200D\u2060\u2063";
    public static final Set<Character> SPECIAL_WHITE_SPACE_CHARS = Collections.unmodifiableSet(((Supplier<Set<Character>>) () -> {
        // unicode 中的特殊格式控制符号，体现为不可见字符，但是在某些编码下，显示为乱码，比如在GBK编码下，就是乱码
        // 这种字符一般发生在从网页中复制，或者从WORD等排版工具中复制时，会带入这种字符
        // 一般的空格为0x20,也就是ascii值32，\\u0020，空格
        char[] arr = SPECIAL_WHITE_SPACE_STR.toCharArray();
        Set<Character> ret = new HashSet<>();
        for (char ch : arr) {
            ret.add(ch);
        }
        return ret;
    }).get());

    public static boolean isSpecialWhiteSpaceChar(char ch) {
        return SPECIAL_WHITE_SPACE_CHARS.contains(ch);
    }

    public static boolean hasSpecialWhiteSpaceChars(String str) {
        if (str == null) {
            return false;
        }
        int len = str.length();
        for (int i = 0; i < len; i++) {
            if (SPECIAL_WHITE_SPACE_CHARS.contains(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static String trimSpecialWhiteSpaceChars(String str) {
        if (str == null) {
            return str;
        }
        StringBuilder builder = new StringBuilder();
        char[] arr = str.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if (!SPECIAL_WHITE_SPACE_CHARS.contains(arr[i])) {
                builder.append(arr[i]);
            }
        }
        return builder.toString();
    }

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
            if (!Character.isWhitespace(str.charAt(i))) {
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

    public static String concatPath(String prefix, String suffix) {
        return concatWith(prefix, suffix, "/");
    }

    public static String concatWith(String prefix, String suffix, String separator) {
        if (prefix == null) {
            prefix = "";
        }
        if (suffix == null) {
            suffix = "";
        }
        if (separator == null) {
            separator = "";
        }
        if (separator.isEmpty()) {
            return prefix + suffix;
        }
        if (prefix.endsWith(separator)) {
            if (suffix.startsWith(separator)) {
                return prefix + suffix.substring(separator.length());
            } else {
                return prefix + suffix;
            }
        } else {
            if (suffix.startsWith(separator)) {
                return prefix + suffix;
            } else {
                return prefix + separator + suffix;
            }
        }
    }

    public static boolean endsWithIgnoreCase(String str, String suffix) {
        return endsWith(str, suffix, false, true);
    }

    public static boolean endsWith(String str, String suffix, boolean ignoreCase) {
        return endsWith(str, suffix, false, ignoreCase);
    }

    public static boolean endsWith(String str, String suffix, boolean trimWhiteSpace, boolean ignoreCase) {
        if (str == null) {
            return false;
        }
        if (suffix == null) {
            return true;
        }
        if (suffix.isEmpty()) {
            return true;
        }
        String cmpStr = str;
        String cmpSuffix = suffix;
        if (trimWhiteSpace) {
            cmpStr = cmpStr.trim();
        }
        if (ignoreCase) {
            cmpStr = cmpStr.toLowerCase();
            cmpSuffix = cmpSuffix.toLowerCase();
        }
        return cmpStr.endsWith(cmpSuffix);
    }

    public static boolean startsWithIgnoreCase(String str, String prefix) {
        return startsWith(str, prefix, false, true);
    }

    public static boolean startsWith(String str, String prefix, boolean ignoreCase) {
        return startsWith(str, prefix, false, ignoreCase);
    }

    public static boolean startsWith(String str, String prefix, boolean trimWhiteSpace, boolean ignoreCase) {
        if (str == null) {
            return false;
        }
        if (prefix == null) {
            return true;
        }
        if (prefix.isEmpty()) {
            return true;
        }
        String cmpStr = str;
        String cmpPrefix = prefix;
        if (trimWhiteSpace) {
            cmpStr = cmpStr.trim();
        }
        if (ignoreCase) {
            cmpStr = cmpStr.toLowerCase();
            cmpPrefix = cmpPrefix.toLowerCase();
        }
        return cmpStr.startsWith(cmpPrefix);
    }

    public static String ensureStartsWith(String str, String prefix) {
        return ensureStartsWith(str, prefix, false);
    }

    public static String ensureStartsWithIgnoreCase(String str, String prefix) {
        return ensureStartsWith(str, prefix, true);
    }

    public static String ensureStartsWith(String str, String prefix, boolean ignoreCase) {
        if (str == null || prefix == null) {
            return str;
        }
        if (prefix.isEmpty()) {
            return str;
        }
        String cmpStr = str;
        String cmpPrefix = prefix;
        if (ignoreCase) {
            cmpStr = str.toLowerCase();
            cmpPrefix = str.toLowerCase();
        }
        if (cmpStr.startsWith(cmpPrefix)) {
            return str;
        }
        return prefix + str;
    }

    public static String ensureEndsWith(String str, String suffix) {
        return ensureEndsWith(str, suffix, false);
    }

    public static String ensureEndsWithIgnoreCase(String str, String suffix) {
        return ensureEndsWith(str, suffix, true);
    }

    public static String ensureEndsWith(String str, String suffix, boolean ignoreCase) {
        if (str == null || suffix == null) {
            return str;
        }
        if (suffix.isEmpty()) {
            return str;
        }
        String cmpStr = str;
        String cmpSuffix = suffix;
        if (ignoreCase) {
            cmpStr = str.toLowerCase();
            cmpSuffix = str.toLowerCase();
        }
        if (cmpStr.startsWith(cmpSuffix)) {
            return str;
        }
        return str + suffix;
    }

    public static String trimPrefixIgnoreCase(String str, String... prefixes) {
        return trim(str, false, true, Arrays.asList(prefixes), null, null, null);
    }

    public static String trimSuffixIgnoreCase(String str, String... suffixes) {
        return trim(str, false, true, null, Arrays.asList(suffixes), null, null);
    }

    public static String trimIgnoreCase(String str, List<String> trimPrefixes, List<String> trimSuffixes) {
        return trim(str, true, true, trimPrefixes, trimSuffixes, null, null);
    }

    public static String trimIgnoreCase(String str, List<String> trimPrefixes, List<String> trimSuffixes, String appendPrefix, String appendSuffix) {
        return trim(str, true, true, trimPrefixes, trimSuffixes, appendPrefix, appendSuffix);
    }

    public static String trimIgnoreCase(String str, boolean trimWhiteSpace, List<String> trimPrefixes, List<String> trimSuffixes, String appendPrefix, String appendSuffix) {
        return trim(str, trimWhiteSpace, true, trimPrefixes, trimSuffixes, appendPrefix, appendSuffix);
    }

    public static String trimPrefix(String str, String... prefixes) {
        return trim(str, false, false, Arrays.asList(prefixes), null, null, null);
    }

    public static String trimSuffix(String str, String... suffixes) {
        return trim(str, false, false, null, Arrays.asList(suffixes), null, null);
    }

    public static String trim(String str, List<String> trimPrefixes, List<String> trimSuffixes) {
        return trim(str, true, false, trimPrefixes, trimSuffixes, null, null);
    }

    public static String trim(String str, List<String> trimPrefixes, List<String> trimSuffixes, String appendPrefix, String appendSuffix) {
        return trim(str, true, false, trimPrefixes, trimSuffixes, appendPrefix, appendSuffix);
    }

    public static String trim(String str, boolean trimWhiteSpace, boolean ignoreCase, List<String> trimPrefixes, List<String> trimSuffixes, String appendPrefix, String appendSuffix) {
        if (str == null) {
            return null;
        }
        String cmpStr = str;
        if (ignoreCase) {
            cmpStr = str.toLowerCase();
        }
        if (trimPrefixes != null && !trimPrefixes.isEmpty()) {
            if (trimWhiteSpace) {
                str = str.trim();
                cmpStr = cmpStr.trim();
            }
            for (String item : trimPrefixes) {
                String cmpItem = item;
                if (ignoreCase) {
                    cmpItem = cmpItem.toLowerCase();
                }
                if (cmpStr.startsWith(cmpItem)) {
                    str = str.substring(item.length());
                    break;
                }
            }
        }
        if (trimSuffixes != null && !trimSuffixes.isEmpty()) {
            if (trimWhiteSpace) {
                str = str.trim();
                cmpStr = cmpStr.trim();
            }
            for (String item : trimSuffixes) {
                String cmpItem = item;
                if (ignoreCase) {
                    cmpItem = cmpItem.toLowerCase();
                }
                if (cmpStr.endsWith(cmpItem)) {
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
                if (item.isEmpty()) {
                    continue;
                }
            }
            result.add(item);
        }
        return result.toArray(new String[0]);
    }

    public static String getFileExtension(String str) {
        String ret = substringAfterLastIndexOf(str, ".", true);
        if (ret == null) {
            return "";
        }
        return ret;
    }

    public static String getFileNameOnly(String str) {
        String ret = substringBeforeLastIndexOf(new File(str).getName(), ".", false);
        if (ret == null) {
            return str;
        }
        return ret;
    }

    public static String substringBeforeLastIndexOf(String str, String idx) {
        return substringBeforeLastIndexOf(str, idx, false);
    }

    public static String substringBeforeLastIndexOf(String str, String idx, boolean withIdx) {
        if (str == null) {
            return null;
        }
        int i = str.lastIndexOf(idx);
        if (i >= 0) {
            if (withIdx) {
                return str.substring(0, i + idx.length());
            } else {
                return str.substring(0, i);
            }
        }
        return null;
    }

    public static String substringAfterLastIndexOf(String str, String idx) {
        return substringAfterLastIndexOf(str, idx, false);
    }

    public static String substringAfterLastIndexOf(String str, String idx, boolean withIdx) {
        if (str == null) {
            return null;
        }
        int i = str.lastIndexOf(idx);
        if (i >= 0) {
            if (withIdx) {
                return str.substring(i);
            } else {
                return str.substring(i + idx.length());
            }
        }
        return null;
    }

    public static String substringBeforeIndexOf(String str, String idx) {
        return substringBeforeIndexOf(str, idx, false);
    }

    public static String substringBeforeIndexOf(String str, String idx, boolean withIdx) {
        if (str == null) {
            return null;
        }
        int i = str.indexOf(idx);
        if (i >= 0) {
            if (withIdx) {
                return str.substring(0, i + idx.length());
            } else {
                return str.substring(0, i);
            }
        }
        return null;
    }

    public static String substringAfterIndexOf(String str, String idx) {
        return substringAfterIndexOf(str, idx, false);
    }

    public static String substringAfterIndexOf(String str, String idx, boolean withIdx) {
        if (str == null) {
            return null;
        }
        int i = str.indexOf(idx);
        if (i >= 0) {
            if (withIdx) {
                return str.substring(i);
            } else {
                return str.substring(i + idx.length());
            }
        }
        return null;
    }

    public static String of(Object obj) {
        return of(obj, "null");
    }

    public static String of(Object obj, String nullAs) {
        if (obj == null) {
            return nullAs;
        }
        Class<?> clazz = obj.getClass();
        if (clazz.isArray()) {
            return ofArray(obj, nullAs);
        } else if (obj instanceof Iterable) {
            Iterable<?> iterable = (Iterable<?>) obj;
            return ofIterable(iterable, nullAs);
        } else if (obj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) obj;
            return ofMap(map, nullAs);
        } else if (obj instanceof Throwable) {
            Throwable ex = (Throwable) obj;
            return ofThrowable(ex, nullAs);
        }
        return String.valueOf(obj);
    }

    public static String ofThrowable(Throwable ex, String nullAs) {
        if (ex == null) {
            return nullAs;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(bos);
        ps.println(ex.getClass().getName() + " : " + ex.getMessage());
        ex.printStackTrace(ps);
        ps.flush();
        ps.close();
        return new String(bos.toByteArray());
    }

    public static String ofMap(Map<?, ?> map, String nullAs) {
        if (map == null) {
            return nullAs;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        boolean isFirst = true;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!isFirst) {
                builder.append(", ");
            }
            builder.append(of(entry.getKey(), nullAs));
            builder.append(" : ");
            builder.append(of(entry.getValue(), nullAs));
            isFirst = false;
        }
        builder.append("}");

        return builder.toString();
    }

    public static String ofIterable(Iterable<?> iterable, String nullAs) {
        if (iterable == null) {
            return nullAs;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        boolean isFirst = true;
        for (Object elem : iterable) {
            if (!isFirst) {
                builder.append(", ");
            }
            builder.append(of(elem, nullAs));
            isFirst = false;
        }
        builder.append("]");
        return builder.toString();
    }

    public static String ofArray(Object arr, String nullAs) {
        if (arr == null) {
            return nullAs;
        }
        Class<?> clazz = arr.getClass();
        if (!clazz.isArray()) {
            return of(arr, nullAs);
        }
        StringBuilder builder = new StringBuilder();
        int len = Array.getLength(arr);
        builder.append("[");
        for (int i = 0; i < len; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            Object elem = Array.get(arr, i);
            builder.append(of(elem, nullAs));
        }
        builder.append("]");
        return builder.toString();
    }

    public static String ofBytes(byte[] bytes, String charset) {
        try {
            return new String(bytes, charset);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public static String ofUtf8(byte[] bytes) {
        return ofBytes(bytes, "UTF-8");
    }

    public static String ofGbk(byte[] bytes) {
        return ofBytes(bytes, "GBK");
    }

    public static byte[] toBytes(String str, String charset) {
        if (str == null) {
            return new byte[0];
        }
        try {
            return str.getBytes(charset);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public static byte[] toUtf8(String str) {
        return toBytes(str, "UTF-8");
    }

    public static byte[] toGbk(String str) {
        return toBytes(str, "GBK");
    }

    public static String convertCharset(String source, String targetCharset, String unrecognizeCharReplacer) {
        if (source == null) {
            return source;
        }
        char[] arr = source.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char ch : arr) {
            String str = "" + ch;
            try {
                byte[] bytes = str.getBytes(targetCharset);
                String rev = new String(bytes, targetCharset);
                if (str.equals(rev)) {
                    builder.append(ch);
                }
            } catch (Exception e) {
                builder.append(unrecognizeCharReplacer);
            }
        }
        return builder.toString();
    }
}
