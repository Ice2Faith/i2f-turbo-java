package i2f.mixin.impl;


import i2f.convert.obj.ObjectConvertor;
import i2f.io.stream.StreamUtil;
import i2f.match.regex.RegexUtil;
import i2f.mixin.MixinProxyFactory;
import i2f.text.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Ice2Faith
 * @date 2026/5/14 8:54
 * @desc
 */
public interface StringMixins {

    default String newline() {
        return "\n";
    }

    default String sharp() {
        return "#";
    }

    default String dollar() {
        return "$";
    }

    default boolean is_empty(String str) {
        return StringUtils.isEmpty(str);
    }

    default boolean not_empty(String str) {
        return !is_empty(str);
    }

    default String if_empty(String obj, String defVal) {
        if (is_empty(obj)) {
            return defVal;
        }
        return obj;
    }

    default String format(String format, Object... obj) {
        if (format == null) {
            return null;
        }
        return String.format(format, obj);
    }

    default Appendable append(Appendable appendable, Object... objs) {
        for (Object o : objs) {
            try {
                appendable.append(String.valueOf(o));
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return appendable;
    }

    default int length(CharSequence sequence) {
        if (sequence == null) {
            return -1;
        }
        return sequence.length();
    }

    default String[] split(String str, String sep) {
        if (is_empty(str)) {
            return new String[0];
        }
        return str.split(sep);
    }

    default String[] split(String str, String sep, int limit) {
        if (is_empty(str)) {
            return new String[0];
        }
        return str.split(sep, limit);
    }

    default String replace(String str, String src, String dst) {
        if (str == null || src == null) {
            return str;
        }
        return str.replace(src, dst);
    }

    default String replace_all(String str, String src, String dst) {
        if (str == null || src == null) {
            return str;
        }
        return str.replaceAll(src, dst);
    }

    default boolean contains(String str, String sub) {
        if (str == null && sub == null) {
            return true;
        }
        if (sub == null || sub.isEmpty()) {
            return true;
        }
        if (str == null) {
            return false;
        }
        return str.contains(sub);
    }

    default String to_pascal(String str) {
        if (is_empty(str)) {
            return str;
        }
        return StringUtils.toPascal(str);
    }

    default String to_camel(String str) {
        if (is_empty(str)) {
            return str;
        }
        return StringUtils.toCamel(str);
    }

    default String to_underscore(String str) {
        if (is_empty(str)) {
            return str;
        }
        if (str.contains("_")) {
            return str.trim();
        }
        return StringUtils.toUnderScore(str);
    }

    default String to_snake(String str) {
        if (is_empty(str)) {
            return str;
        }
        if (str.contains("-")) {
            return str.trim();
        }
        return StringUtils.toSnake(str);
    }

    default String to_property_case(String str) {
        if (is_empty(str)) {
            return str;
        }
        if (str.contains(".")) {
            return str.trim();
        }
        return StringUtils.toPropertyCase(str);
    }

    default String to_path_case(String str) {
        if (is_empty(str)) {
            return str;
        }
        if (str.contains("/")) {
            return str.trim();
        }
        return StringUtils.toPathCase(str);
    }

    default String to_colon_case(String str) {
        if (is_empty(str)) {
            return str;
        }
        if (str.contains(":")) {
            return str.trim();
        }
        return StringUtils.toColonCase(str);
    }

    default String to_url_encoded(String str) {
        if (is_empty(str)) {
            return str;
        }
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {

        }
        return str;
    }

    default String to_base64(String str) {
        if (str == null) {
            return null;
        }
        try {
            return Base64.getEncoder().encodeToString(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {

        }
        return str;
    }

    default int index_of(Object oStr, Object oSstr) {
        return index_of(oStr, oSstr, 0);
    }

    default int index_of(Object oStr, Object oSstr, int fromIndex) {
        if (oStr == null) {
            return -1;
        }
        if (oSstr == null) {
            return -1;
        }
        String sstr = String.valueOf(oSstr);
        if (sstr.isEmpty()) {
            return 0;
        }
        String str = String.valueOf(oStr);
        return str.indexOf(sstr, fromIndex);
    }

    default int last_index_of(Object oStr, Object oSstr) {
        if (oStr == null) {
            return -1;
        }
        if (oSstr == null) {
            return -1;
        }
        String sstr = String.valueOf(oSstr);
        if (sstr.isEmpty()) {
            return 0;
        }
        String str = String.valueOf(oStr);
        return str.lastIndexOf(sstr);
    }

    default int last_index_of(Object oStr, Object oSstr, int fromIndex) {
        if (oStr == null) {
            return -1;
        }
        if (oSstr == null) {
            return -1;
        }
        String sstr = String.valueOf(oSstr);
        if (sstr.isEmpty()) {
            return 0;
        }
        String str = String.valueOf(oStr);
        return str.lastIndexOf(sstr, fromIndex);
    }

    default String replace(Object str, Object target) {
        return replace(str, target, "");
    }

    default String replace(Object oStr, Object oTarget, Object oReplacement) {
        if (oStr == null) {
            return null;
        }
        String str = String.valueOf(oStr);
        if (oTarget == null) {
            return str;
        }
        if (oReplacement == null) {
            oReplacement = "";
        }
        String target = String.valueOf(oTarget);
        String replacement = String.valueOf(oReplacement);
        return str.replace(target, replacement);
    }

    default String join(Object obj) {
        return join(obj, ",", false, false);
    }

    default String join(Object obj, Object separator) {
        return join(obj, separator, false, false);
    }

    default String join(Object obj, Object separator, boolean ignoreNull) {
        return join(obj, separator, ignoreNull, false);
    }

    default String join(Object obj, Object separator, boolean ignoreNull, boolean ignoreEmpty) {
        if (obj == null) {
            if (ignoreNull) {
                return "";
            }
            return String.valueOf(obj);
        }
        Class<?> clazz = obj.getClass();
        if (clazz.isArray()) {
            StringBuilder builder = new StringBuilder();
            int len = Array.getLength(obj);
            boolean isFirst = true;
            for (int i = 0; i < len; i++) {
                Object item = Array.get(obj, i);
                if (item == null) {
                    if (ignoreNull) {
                        continue;
                    }
                }
                String str = String.valueOf(item);
                if (str.isEmpty() && ignoreEmpty) {
                    continue;
                }
                if (!isFirst) {
                    if (separator != null) {
                        builder.append(separator);
                    }
                }
                builder.append(str);
                isFirst = false;
            }
            return builder.toString();
        } else if (obj instanceof Map) {
            Map<?, ?> iterable = (Map<?, ?>) obj;
            StringBuilder builder = new StringBuilder();
            boolean isFirst = true;
            for (Map.Entry<?, ?> item : iterable.entrySet()) {
                if (item == null) {
                    if (ignoreNull) {
                        continue;
                    }
                }
                String str = String.valueOf(item);
                if (str.isEmpty() && ignoreEmpty) {
                    continue;
                }
                if (!isFirst) {
                    if (separator != null) {
                        builder.append(separator);
                    }
                }
                builder.append(str);
                isFirst = false;
            }
            return builder.toString();
        } else if (obj instanceof Iterable) {
            Iterable<?> iterable = (Iterable<?>) obj;
            StringBuilder builder = new StringBuilder();
            boolean isFirst = true;
            for (Object item : iterable) {
                if (item == null) {
                    if (ignoreNull) {
                        continue;
                    }
                }
                String str = String.valueOf(item);
                if (str.isEmpty() && ignoreEmpty) {
                    continue;
                }
                if (!isFirst) {
                    if (separator != null) {
                        builder.append(separator);
                    }
                }
                builder.append(str);
                isFirst = false;
            }
            return builder.toString();
        } else if (obj instanceof Iterator) {
            Iterator<?> iterator = (Iterator<?>) obj;
            StringBuilder builder = new StringBuilder();
            boolean isFirst = true;
            while (iterator.hasNext()) {
                Object item = iterator.next();
                if (item == null) {
                    if (ignoreNull) {
                        continue;
                    }
                }
                String str = String.valueOf(item);
                if (str.isEmpty() && ignoreEmpty) {
                    continue;
                }
                if (!isFirst) {
                    if (separator != null) {
                        builder.append(separator);
                    }
                }
                builder.append(str);
                isFirst = false;
            }
            return builder.toString();
        } else if (obj instanceof Enumeration) {
            Enumeration<?> enumeration = (Enumeration<?>) obj;
            StringBuilder builder = new StringBuilder();
            boolean isFirst = true;
            while (enumeration.hasMoreElements()) {
                Object item = enumeration.nextElement();
                if (item == null) {
                    if (ignoreNull) {
                        continue;
                    }
                }
                String str = String.valueOf(item);
                if (str.isEmpty() && ignoreEmpty) {
                    continue;
                }
                if (!isFirst) {
                    if (separator != null) {
                        builder.append(separator);
                    }
                }
                builder.append(str);
                isFirst = false;
            }
            return builder.toString();
        }
        String str = String.valueOf(obj);
        return str;
    }

    default String trim(String str) {
        if (str == null) {
            return str;
        }
        return str.trim();
    }

    default String trim_empty_lines(String str) {
        if (str == null) {
            return str;
        }
        String[] lines = str.split("\n");
        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            builder.append(line).append("\n");
        }
        return builder.toString();
    }

    default String init_capital(Object obj) {
        if (obj == null) {
            return null;
        }
        String str = String.valueOf(obj);
        return RegexUtil.regexFindAndReplace(str, "[a-zA-Z0-9\\$@']", s -> {
            return first_upper(s);
        });
    }

    default String first_upper(String str) {
        if (str == null) {
            return str;
        }
        if (str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    default String first_lower(String str) {
        if (str == null) {
            return str;
        }
        if (str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    default String upper(String str) {
        if (str == null) {
            return str;
        }
        return str.toUpperCase();
    }

    default String lower(String str) {
        if (str == null) {
            return str;
        }
        return str.toLowerCase();
    }

    default String chr(int ascii) {
        char ch = (char) ascii;
        return "" + ch;
    }

    default int char_code(String str, int index) {
        return str.charAt(index);
    }

    default String rtrim(String str) {
        return rtrim(str, null);
    }

    default String rtrim(String str, String substr) {
        if (str == null) {
            return str;
        }
        if (substr == null) {
            int idx = str.length() - 1;
            while (idx >= 0) {
                char ch = str.charAt(idx);
                if (!Character.isWhitespace(ch)) {
                    break;
                }
                idx--;
            }
            if (idx >= 0) {
                return str.substring(0, idx + 1);
            }
            return str;
        } else {
            if (substr.isEmpty()) {
                return str;
            }
            while (str.endsWith(substr)) {
                str = str.substring(0, str.length() - substr.length());
            }
            return str;
        }
    }

    default String ltrim(String str) {
        return ltrim(str, null);
    }

    default String ltrim(String str, String substr) {
        if (str == null) {
            return str;
        }
        if (substr == null) {
            int idx = 0;
            while (idx < str.length()) {
                char ch = str.charAt(idx);
                if (!Character.isWhitespace(ch)) {
                    break;
                }
                idx++;
            }
            if (idx >= 0) {
                return str.substring(idx);
            }
            return str;
        } else {
            if (substr.isEmpty()) {
                return str;
            }
            while (str.startsWith(substr)) {
                str = str.substring(substr.length());
            }
            return str;
        }
    }

    default String lpad(Object str, int len) {
        return lpad(str, len, null);
    }

    default String lpad(Object str, int len, Object padStr) {
        if (str == null) {
            return null;
        }
        String mainStr = String.valueOf(str);
        if (mainStr.length() >= len) {
            return mainStr;
        }
        if (padStr == null) {
            padStr = " ";
        }
        String padStr2 = String.valueOf(padStr);
        if (padStr2.isEmpty()) {
            padStr2 = " ";
        }
        int diffLen = len - mainStr.length();
        int j = 0;
        StringBuilder builder = new StringBuilder();
        while (diffLen > 0) {
            builder.append(padStr2.charAt(j));
            j = (j + 1) % padStr2.length();
            diffLen--;
        }
        return builder + mainStr;
    }

    default String rpad(Object str, int len) {
        return rpad(str, len, null);
    }

    default String rpad(Object str, int len, Object padStr) {
        if (str == null) {
            return null;
        }
        String mainStr = String.valueOf(str);
        if (mainStr.length() >= len) {
            return mainStr;
        }
        if (padStr == null) {
            padStr = " ";
        }
        String padStr2 = String.valueOf(padStr);
        if (padStr2.isEmpty()) {
            padStr2 = " ";
        }
        int diffLen = len - mainStr.length();
        int j = 0;
        StringBuilder builder = new StringBuilder();
        while (diffLen > 0) {
            builder.append(padStr2.charAt(j));
            j = (j + 1) % padStr2.length();
            diffLen--;
        }
        return mainStr + builder;
    }

    default String to_char(Object obj) {
        return to_char(obj, null);
    }

    default String to_char(Object obj, String pattern) {
        if (obj == null) {
            return null;
        }
        if (pattern == null || pattern.isEmpty()) {
            return String.valueOf(obj);
        }
        Class<?> clazz = obj.getClass();
        if (ObjectConvertor.isDateType(clazz)) {
            return MixinProxyFactory.getMixinInstance(DateMixins.class).date_format(obj, pattern);
        } else if (ObjectConvertor.isNumericType(clazz)) {
            BigDecimal num = (BigDecimal) ObjectConvertor.tryConvertAsType(obj, BigDecimal.class);
            if (pattern.contains("%")) {
                return String.format(pattern, num.doubleValue());
            } else if (RegexUtil.getPattern("(?i)(fm)?([09]+)(\\.[09]+)?").matcher(pattern).matches()) {
                // oracle dialect of regex pattern: (fm)?([09]+)(\.[09]+)?
                // such: 99999.99
                boolean isTrim = false;
                pattern = pattern.toLowerCase();
                if (pattern.startsWith("fm")) {
                    pattern = pattern.substring(2);
                    isTrim = true;
                }

                String[] arr = pattern.split("\\.", 2);
                String intPattern = arr[0];
                String facPattern = arr.length > 1 ? arr[1] : null;

                boolean isNeg = num.compareTo(BigDecimal.ZERO) < 0;
                num = num.abs();
                if (facPattern != null) {
                    num = (BigDecimal) MixinProxyFactory.getMixinInstance(MathMixins.class).round(num, facPattern.length());
                }

                String text = num.toPlainString();
                arr = text.split("\\.", 2);
                String intNum = arr[0];
                String facNum = arr.length > 1 ? arr[1] : null;
                if (intNum.length() > intPattern.length()) {
                    // overflow
                    return repeat("#", intPattern.length()) + (facPattern == null ? "" : repeat("#", facPattern.length()));
                }
                String ret = (isNeg ? "-" : "")
                        + repeat((isTrim ? "" : (intPattern.startsWith("0") ? "0" : " ")), intPattern.length() - intNum.length())
                        + intNum;
                if (facPattern != null) {
                    if (facNum == null) {
                        if (!isTrim) {
                            ret += "." + repeat("0", facPattern.length());
                        }
                    } else {
                        ret += "."
                                + facNum
                                + repeat((isTrim ? "" : "0"), facPattern.length() - facNum.length());
                    }
                }
                return ret;
            } else {
                return String.format(pattern, num.doubleValue());
            }
        }
        return String.valueOf(obj);
    }

    default String repeat(CharSequence str, int count) {
        if (count <= 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            builder.append(str);
        }
        return builder.toString();
    }

    default String to_string(Object obj) {
        return to_char(obj, null);
    }

    default String to_string(Object obj, String pattern) {
        return to_char(obj, pattern);
    }

    default String escape_sql_string(Object obj) {
        if (obj == null) {
            return "''";
        }
        return "'" + String.valueOf(obj).replace("'", "''") + "'";
    }

    default String descape_sql_string(Object obj) {
        if (obj == null) {
            return null;
        }
        String str = String.valueOf(obj);
        if (str.startsWith("'")) {
            str = str.substring(1, str.length() - 1);
        }
        return str.replace("''", "'");
    }

    default String left(Object obj, int len) {
        if (obj == null) {
            return null;
        }
        String str = String.valueOf(obj);
        if (len < 0) {
            len = str.length() + len;
        }
        if (len >= str.length()) {
            return str;
        }
        return str.substring(0, len);
    }

    default String right(Object obj, int len) {
        if (obj == null) {
            return null;
        }
        String str = String.valueOf(obj);
        if (len < 0) {
            len = str.length() + len;
        }
        if (len >= str.length()) {
            return str;
        }
        return str.substring(str.length() - len);
    }

    default String reverse(Object str) {
        if (str == null) {
            return null;
        }
        String s = String.valueOf(str);
        StringBuilder builder = new StringBuilder();
        for (int i = s.length() - 1; i >= 0; i--) {
            builder.append(s.charAt(i));
        }
        return builder.toString();
    }

    default String concat(Object... args) {
        return concat_ws(null, args);
    }

    default String concat(Iterable<?> args) {
        return concat_ws(null, args);
    }

    default String concat_ws(Object separator, Object... args) {
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for (Object item : args) {
            if (!isFirst) {
                if (separator != null) {
                    builder.append(separator);
                }
            }
            builder.append(item);
            isFirst = false;
        }
        return builder.toString();
    }

    default String concat_ws(Object separator, Iterable<Object> args) {
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for (Object item : args) {
            if (!isFirst) {
                if (separator != null) {
                    builder.append(separator);
                }
            }
            builder.append(item);
            isFirst = false;
        }
        return builder.toString();
    }

    default String join(Object separator, Object... args) {
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for (Object item : args) {
            if (!isFirst) {
                if (separator != null) {
                    builder.append(separator);
                }
            }
            builder.append(item);
            isFirst = false;
        }
        return builder.toString();
    }

    default String join(Object separator, Iterable<?> args) {
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for (Object item : args) {
            if (!isFirst) {
                if (separator != null) {
                    builder.append(separator);
                }
            }
            builder.append(item);
            isFirst = false;
        }
        return builder.toString();
    }

    default int instr(Object obj, Object sub) {
        String str = null;
        if (obj != null) {
            str = String.valueOf(obj);
        }
        String sstr = null;
        if (sub != null) {
            sstr = String.valueOf(sub);
        }
        if (str == null || sstr == null) {
            return -1;
        }
        return str.indexOf(sstr);
    }

    default String substr(Object obj, int index) {
        return substr(obj, index, -1);
    }

    default String substr(Object obj, int index, int len) {
        String str = null;
        if (obj != null) {
            str = String.valueOf(obj);
        }
        if (str == null) {
            return str;
        }
        if (index >= str.length()) {
            return "";
        }
        if (len >= 0) {
            int endIndex = index + len;
            if (endIndex >= str.length()) {
                return str.substring(index);
            }
            return str.substring(index, index + len);
        } else {
            return str.substring(index);
        }
    }

    default String substring(Object obj, int beginIndex) {
        return substring(obj, beginIndex, -1);
    }

    default String substring(Object obj, int beginIndex, int endIndex) {
        String str = null;
        if (obj != null) {
            str = String.valueOf(obj);
        }
        if (str == null) {
            return str;
        }
        if (beginIndex >= str.length()) {
            return "";
        }
        if (endIndex >= 0) {
            if (endIndex >= str.length()) {
                return str.substring(beginIndex);
            }
            return str.substring(beginIndex, endIndex);
        } else {
            return str.substring(beginIndex);
        }
    }

    default String substrb(Object str, int index) {
        return substrb(str, index, -1);
    }

    default String substrb(Object str, int index, int len) {
        String ret = substr(str, index, len);
        if (ret == null || ret.isEmpty()) {
            return ret;
        }
        if (len < 0) {
            return ret;
        }
        byte[] bytes = ret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length <= len) {
            return ret;
        }
        char[] arr = ret.toCharArray();
        int count = 0;
        StringBuilder builder = new StringBuilder();
        for (char ch : arr) {
            if (ch >= 0 && ch <= 127) {
                count += 1;
            } else {
                String s = ch + "";
                count += s.getBytes(StandardCharsets.UTF_8).length;
            }
            if (count >= len) {
                break;
            }
            builder.append(ch);
        }
        return builder.toString();
    }

    default int substr_count(Object obj, Object substr) {
        if (obj == null || substr == null) {
            return -1;
        }
        String str = String.valueOf(obj);
        String sstr = String.valueOf(obj);
        ArrayList<String> arr = split_literal(str, sstr);
        return arr.size() - 1;
    }

    default String substr2_index(Object obj, Object substr) {
        return substr2_index(obj, substr, 0);
    }

    default String substr2_index(Object obj, Object substr, int beginIndex) {
        if (obj == null) {
            return null;
        }
        int endIndex = instr(obj, substr);
        if (beginIndex < 0) {
            beginIndex = 0;
        }
        if (endIndex < beginIndex) {
            endIndex = -1;
        }
        return substring(obj, beginIndex, endIndex);
    }

    default String substr2_index_end(Object obj, Object substr) {
        return substr2_index_end(obj, substr, 0);
    }

    default String substr2_index_end(Object obj, Object substr, int beginIndex) {
        if (obj == null) {
            return null;
        }
        int endIndex = instr(obj, substr);
        if (endIndex < 0) {
            return "";
        }
        if (beginIndex < 0) {
            beginIndex = 0;
        }
        if (endIndex < beginIndex) {
            endIndex = -1;
        }
        String ret = substring(obj, beginIndex, endIndex);
        return ret + substr;
    }

    default String substr_index(Object obj, Object substr, int len) {
        int idx = instr(obj, substr);
        if (idx < 0) {
            return "";
        }
        if (len < 0) {
            return substr(obj, idx);
        }
        return substr(obj, idx, len);
    }

    default String substr_index(Object obj, Object substr) {
        return substr_index(obj, substr, -1);
    }

    default String substr_index_end(Object obj, Object substr, int len) {
        int idx = instr(obj, substr);
        if (idx < 0) {
            return "";
        }
        int l = MixinProxyFactory.getMixinInstance(ObjectMixins.class).length(substr);
        if (len < 0) {
            return substr(obj, idx + l);
        }
        return substr(obj, idx + l, len);
    }

    default String substr_index_end(Object obj, Object substr) {
        return substr_index_end(obj, substr, -1);
    }

    default boolean contains(Object obj, Object substr) {
        if (obj == null || substr == null) {
            return false;
        }
        String sstr = (String) ObjectConvertor.tryConvertAsType(substr, String.class);
        if (sstr == null || sstr.isEmpty()) {
            return true;
        }
        String str = (String) ObjectConvertor.tryConvertAsType(obj, String.class);
        if (str == null) {
            return false;
        }
        return str.contains(sstr);
    }

    default boolean like(Object obj, Object substr) {
        return contains(obj, substr);
    }

    default boolean starts_with(Object obj, Object substr) {
        return starts_with(obj, substr, 0);
    }

    default boolean starts_with(Object obj, Object substr, int offset) {
        if (obj == null || substr == null) {
            return false;
        }
        String sstr = (String) ObjectConvertor.tryConvertAsType(substr, String.class);
        if (sstr == null || sstr.isEmpty()) {
            return true;
        }
        String str = (String) ObjectConvertor.tryConvertAsType(obj, String.class);
        if (str == null) {
            return false;
        }
        return str.startsWith(sstr, offset);
    }

    default boolean starts(Object obj, String substr) {
        return starts_with(obj, substr);
    }

    default boolean starts(Object obj, String substr, int offset) {
        return starts_with(obj, substr, offset);
    }

    default boolean ends_with(Object obj, Object substr) {
        return ends_with(obj, substr, 0);
    }

    default boolean ends_with(Object obj, Object substr, int offset) {
        if (obj == null || substr == null) {
            return false;
        }
        String sstr = (String) ObjectConvertor.tryConvertAsType(substr, String.class);
        if (sstr == null || sstr.isEmpty()) {
            return true;
        }
        String str = (String) ObjectConvertor.tryConvertAsType(obj, String.class);
        if (str == null) {
            return false;
        }
        return starts_with(str, sstr, str.length() - sstr.length() - offset);
    }

    default boolean ends(Object obj, Object substr) {
        return ends_with(obj, substr);
    }

    default boolean ends(Object obj, Object substr, int offset) {
        return ends_with(obj, substr, offset);
    }

    default ArrayList<String> split_literal(Object str, Object literal) {
        return split_literal(str, literal, -1);
    }

    default ArrayList<String> split(Object str, Object literal) {
        return split_literal(str, literal);
    }

    default ArrayList<String> split_literal(Object oStr, Object oLiteral, int limit) {
        if (oStr == null) {
            return new ArrayList<>();
        }
        String str = String.valueOf(oStr);
        if (oLiteral == null) {
            return new ArrayList<>(Collections.singletonList(str));
        }
        String literal = String.valueOf(oLiteral);
        String[] arr = RegexUtil.getPattern(literal, Pattern.LITERAL).split(str, limit);
        return new ArrayList<>(Arrays.asList(arr));
    }

    default ArrayList<String> split(Object str, Object literal, int limit) {
        return split_literal(str, literal, limit);
    }

    default String encode_url(Object obj) {
        try {
            String str = String.valueOf(obj);
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    default String decode_url(Object obj) {
        try {
            String str = String.valueOf(obj);
            return URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    default String encode_base64(Object obj) {
        try {
            String str = String.valueOf(obj);
            return Base64.getEncoder().encodeToString(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    default String decode_base64(Object obj) {
        try {
            String str = String.valueOf(obj);
            return new String(Base64.getDecoder().decode(str), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    default String md5(Object data) {
        return mds("MD5", data, "hex", null);
    }

    default String sha1(Object data) {
        return mds("SHA-1", data, "hex", null);
    }

    default String sha256(Object data) {
        return mds("SHA-256", data, "hex", null);
    }

    default String sha384(Object data) {
        return mds("SHA-384", data, "hex", null);
    }

    default String sha512(Object data) {
        return mds("SHA-512", data, "hex", null);
    }

    default String mds(String algorithm, Object data) {
        return mds(algorithm, data, "hex", null);
    }

    default String mds(String algorithm, Object data, String format) {
        return mds(algorithm, data, format, null);
    }

    default String mds(String algorithm, Object data, String format, String provider) {
        if (data == null) {
            return null;
        }
        if (provider != null) {
            provider = provider.trim();
        }
        MessageDigest md = null;
        if (provider != null && !provider.isEmpty()) {
            try {
                md = MessageDigest.getInstance(algorithm, provider);
            } catch (Exception e) {

            }
        }
        if (md == null) {
            try {
                MessageDigest.getInstance(algorithm);
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalArgumentException("cannot find message digest algorithm:" + algorithm + " , with error: " + e.getMessage(), e);
            }
        }
        byte[] bytes = null;
        try {
            if (data instanceof byte[]) {
                bytes = (byte[]) data;
            } else if (data instanceof InputStream) {
                InputStream is = (InputStream) data;
                bytes = StreamUtil.readBytes(is, true);
            } else if (data instanceof Reader) {
                Reader reader = (Reader) data;
                bytes = StreamUtil.readString(reader, true).getBytes(StandardCharsets.UTF_8);
            } else if (data instanceof String
                    || data instanceof CharSequence
                    || data instanceof Appendable) {
                String str = String.valueOf(data);
                bytes = str.getBytes(StandardCharsets.UTF_8);
            } else {
                String str = (String) ObjectConvertor.tryConvertAsType(data, String.class);
                bytes = str.getBytes(StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new IllegalStateException("message digest convert data to byte[] error :" + e.getMessage(), e);
        }
        byte[] hex = md.digest(bytes);
        if ("base64".equalsIgnoreCase(format)) {
            return Base64.getEncoder().encodeToString(hex);
        } else {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < hex.length; i++) {
                builder.append(String.format("%02x", (0x0ff) & hex[i]));
            }
            return builder.toString();
        }
    }
}
