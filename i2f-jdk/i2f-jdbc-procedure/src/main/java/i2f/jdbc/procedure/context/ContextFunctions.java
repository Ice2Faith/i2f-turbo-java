package i2f.jdbc.procedure.context;

import i2f.clock.SystemClock;
import i2f.convert.obj.ObjectConvertor;
import i2f.io.stream.StreamUtil;
import i2f.match.regex.RegexUtil;
import i2f.match.regex.data.RegexMatchItem;
import i2f.reflect.ReflectResolver;
import i2f.text.StringUtils;
import i2f.uid.SnowflakeLongUid;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2025/2/17 16:09
 */
public interface ContextFunctions {
    ContextFunctions INSTANCE = new ContextFunctions() {
    };
    String[][] ORACLE_REGEX_REPLACE_MAPPING = {
            {"[:alpha:]", "a-zA-Z"},
            {"[:alnum:]", "a-zA-Z0-9"},
            {"[:alphanum:]", "a-zA-Z0-9"},
            {"[:punct:]", "\\!-\\&\\(-/:-@\\[-`\\{-\\~'"},
            {"[:digit:]", "0-9"},
            {"[:lower:]", "a-z"},
            {"[:upper:]", "A-Z"},
            {"[:blank:]", "\\s"},
            {"[:grah:]", "\\S"},

    };
    String[][] CHRONO_UNIT_MAPPING = {
            {"day", "DAYS"},
            {"dd", "DAYS"},
            {"month", "MONTHS"},
            {"mon", "MONTHS"},
            {"mm", "MONTHS"},
            {"year", "YEARS"},
            {"yyyy", "YEARS"},
            {"minute", "MINUTES"},
            {"min", "MINUTES"},
            {"mi", "MINUTES"},
            {"second", "SECONDS"},
            {"sec", "SECONDS"},
            {"ss", "SECONDS"},
            {"hour", "HOURS"},
            {"hh", "HOURS"},
            {"mill", "MILLIS"},
            {"ms", "MILLIS"},
            {"sss", "MILLIS"},
            {"week", "WEEKS"},
            {"ww", "WEEKS"},
            {"micro", "MICROS"},
            {"nano", "NANOS"},
            {"ns", "NANOS"},

    };
    MathContext MATH_CONTEXT = new MathContext(20, RoundingMode.HALF_UP);
    BigDecimal NUM_0_5 = new BigDecimal("0.5");

    default String convertOracleRegexExpression(String regex) {
        if (regex == null) {
            return null;
        }
        for (String[] arr : ORACLE_REGEX_REPLACE_MAPPING) {
            regex = regex.replace(arr[0], arr[1]);
        }
        return regex;
    }

    default String convertOracleRegexReplacement(String replacement) {
        if (replacement == null) {
            return null;
        }
        replacement = RegexUtil.regexFindAndReplace(replacement, "[\\\\]\\d+", (str) -> {
            return "$" + str.substring(1);
        });
        return replacement;
    }

    default void print(Object... objs) {
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for (Object obj : objs) {
            if (!isFirst) {
                builder.append(", ");
            }
            builder.append(obj);
            isFirst = false;
        }
        System.out.print(builder.toString());
    }

    default void println(Object... objs) {
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for (Object obj : objs) {
            if (!isFirst) {
                builder.append(", ");
            }
            builder.append(obj);
            isFirst = false;
        }
        System.out.println(builder.toString());
    }

    default String sys_env(String key) {
        return System.getenv(key);
    }

    default String jvm(String key) {
        String prop = System.getProperty(key);
        return prop;
    }

    default void gc() {
        System.gc();
    }

    default void exit(int status) {
        System.exit(status);
    }

    default void yield() {
        Thread.yield();
    }

    default long thread_id() {
        return Thread.currentThread().getId();
    }

    default String thread_name() {
        return Thread.currentThread().getName();
    }

    default boolean isnull(Object obj) {
        return obj == null;
    }

    default boolean is_empty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String
                || obj instanceof CharSequence
                || obj instanceof Appendable) {
            return String.valueOf(obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof Iterator) {
            return !((Iterator) obj).hasNext();
        }
        if (obj instanceof Enumeration) {
            return !((Enumeration) obj).hasMoreElements();
        }
        return false;
    }

    default boolean is_blank(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String
                || obj instanceof CharSequence
                || obj instanceof Appendable) {
            String str = String.valueOf(obj);
            char[] arr = str.toCharArray();
            for (int i = 0; i < arr.length; i++) {
                char ch = arr[i];
                if (!Character.isWhitespace(ch)) {
                    return false;
                }
            }
            return true;
        }
        return is_empty(obj);
    }

    default List<Object> list_of(Object... arr) {
        return new ArrayList<>(Arrays.asList(arr));
    }

    default Map<Object, Object> map_of(Object... arr) {
        Map<Object, Object> ret = new LinkedHashMap<>();
        for (int i = 0; i < arr.length; i += 2) {
            if (i + 1 < arr.length) {
                ret.put(arr[i], arr[i + 1]);
            } else {
                ret.put(arr[i], null);
            }
        }
        return ret;
    }

    default int index_of(String str, String sstr) {
        if (str == null) {
            return -1;
        }
        if (sstr == null) {
            return -1;
        }
        if (sstr.isEmpty()) {
            return 0;
        }
        return str.indexOf(sstr);
    }

    default int last_index_of(String str, String sstr) {
        if (str == null) {
            return -1;
        }
        if (sstr == null) {
            return -1;
        }
        if (sstr.isEmpty()) {
            return 0;
        }
        return str.lastIndexOf(sstr);
    }

    default String replace(String str, String target) {
        return replace(str, target, "");
    }

    default String replace(String str, String target, String replacement) {
        if (str == null) {
            return str;
        }
        if (target == null) {
            return str;
        }
        if (replacement == null) {
            replacement = "";
        }
        return str.replace(target, replacement);
    }

    default String regex_replace(String str, String regex) {
        return regex_replace(str, regex, "");
    }

    default String regex_replace(String str, String regex, String replacement) {
        return regex_replace(str, regex, replacement, -1);
    }

    default String regex_replace(String str, String regex, String replacement, int occurrence) {
        if (str == null) {
            return str;
        }
        if (regex == null) {
            return str;
        }
        if (replacement == null) {
            replacement = "";
        }
        regex = convertOracleRegexExpression(regex);
        replacement = convertOracleRegexReplacement(replacement);
        if (occurrence <= 0) {
            return str.replaceAll(regex, replacement);
        } else {
            AtomicInteger count = new AtomicInteger(0);
            final String reg = regex;
            final String rep = replacement;
            return RegexUtil.regexFindAndReplace(str, regex, (s) -> {
                count.incrementAndGet();
                if (count.get() == occurrence) {
                    return s.replaceFirst(reg, rep);
                }
                return s;
            });
        }
    }

    default String regexp_replace(String str, String regex) {
        return regexp_replace(str, regex, "");
    }

    default String regexp_replace(String str, String regex, String replacement) {
        return regex_replace(str, regex, replacement);
    }

    default String regexp_replace(String str, String regex, String replacement, int occurrence) {
        return regex_replace(str, regex, replacement, occurrence);
    }

    default boolean regex_like(String str, String regex) {
        if (str == null) {
            return false;
        }
        if (regex == null) {
            return false;
        }
        regex = convertOracleRegexExpression(regex);
        return str.matches(regex);
    }

    default boolean regexp_like(String str, String regex) {
        return regex_like(str, regex);
    }

    default List<String> regex_find(String str, String regex) {
        if (str == null) {
            return null;
        }
        if (regex == null) {
            return new ArrayList<>();
        }
        return RegexUtil.regexFinds(str, regex)
                .stream()
                .map(e -> e.getMatchStr())
                .collect(Collectors.toList());
    }

    default boolean regex_contains(String str, String regex) {
        if (str == null) {
            return false;
        }
        if (regex == null) {
            return false;
        }
        regex = convertOracleRegexExpression(regex);
        List<RegexMatchItem> list = RegexUtil.regexFinds(str, regex);
        if (!list.isEmpty()) {
            return true;
        }
        return false;
    }

    default boolean regexp_contains(String str, String regex) {
        return regex_contains(str, regex);
    }

    default int regex_index(String str, String regex) {
        if (str == null) {
            return -1;
        }
        if (regex == null) {
            return -1;
        }
        regex = convertOracleRegexExpression(regex);
        List<RegexMatchItem> list = RegexUtil.regexFinds(str, regex);
        if (!list.isEmpty()) {
            RegexMatchItem item = list.get(0);
            return item.getIdxStart();
        }
        return -1;
    }

    default int regexp_index(String str, String regex) {
        return regex_index(str, regex);
    }

    default String regex_extra(String str, String regex) {
        if (str == null) {
            return null;
        }
        if (regex == null) {
            return null;
        }
        regex = convertOracleRegexExpression(regex);
        List<RegexMatchItem> list = RegexUtil.regexFinds(str, regex);
        if (!list.isEmpty()) {
            RegexMatchItem item = list.get(0);
            return item.getMatchStr();
        }
        return null;
    }

    default String regexp_extra(String str, String regex) {
        return regex_extra(str, regex);
    }

    default String regex_find_join(String str, String regex) {
        return regex_find_join(str, regex, ",");
    }

    default String regex_find_join(String str, String regex, Object separator) {
        List<String> list = regex_find(str, regex);
        return join(list, separator);
    }

    default String to_camel(String str) {
        return StringUtils.toCamel(str);
    }

    default String to_pascal(String str) {
        return StringUtils.toPascal(str);
    }

    default String to_underscore(String str) {
        return StringUtils.toUnderScore(str);
    }

    default String to_snake(String str) {
        return StringUtils.toSnake(str);
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

    default int length(Object obj) {
        if (obj == null) {
            return -1;
        }
        if (obj instanceof CharSequence
                || obj instanceof String
                || obj instanceof Appendable) {
            return String.valueOf(obj).length();
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).size();
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).size();
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj);
        }
        throw new IllegalArgumentException("length(obj) function cannot support type:" + obj.getClass());
    }

    default int lengthb(Object obj) {
        if (obj == null) {
            return -1;
        }
        if (obj instanceof CharSequence
                || obj instanceof String
                || obj instanceof Appendable) {
            String str = String.valueOf(obj);
            return str.getBytes(StandardCharsets.UTF_8).length;
        }
        return length(obj);
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

    default Object nvl(Object v1, Object v2) {
        return ifnull(v1, v2);
    }

    default Object ifnull(Object v1, Object v2) {
        return v1 != null ? v1 : v2;
    }

    default Object if_empty(Object v1, Object v2) {
        return is_empty(v1) ? v2 : v1;
    }

    default Object evl(Object v1, Object v2) {
        return if_empty(v1, v2);
    }

    default Object if_blank(Object v1, Object v2) {
        return is_blank(v1) ? v2 : v1;
    }

    default Object bvl(Object v1, Object v2) {
        return if_blank(v1, v2);
    }

    default Object if2(Object cond, Object trueVal, Object falseVal) {
        if (cond instanceof Boolean) {
            Boolean ok = (Boolean) cond;
            if (ok) {
                return trueVal;
            } else {
                return falseVal;
            }
        }
        if (to_boolean(cond)) {
            return trueVal;
        } else {
            return falseVal;
        }
    }

    default Object nvl2(Object cond, Object trueVal, Object falseVal) {
        return if2(cond, trueVal, falseVal);
    }

    default Object decode(Object target, Object... args) {
        int i = 0;
        while (i + 1 < args.length) {
            if (Objects.equals(target, args[i])) {
                return args[i + 1];
            }
            i += 2;
        }
        if (args.length % 2 != 0) {
            return args[args.length - 1];
        }
        return target;
    }

    default Object cast(Object val, Object type) {
        if (type == null) {
            throw new ClassCastException("cannot cast value to type null");
        }
        if (val == null) {
            return null;
        }
        Class<?> clazz = type.getClass();
        if (type instanceof Class) {
            clazz = (Class<?>) type;
        } else {
            try {
                Class<?> clz = ReflectResolver.loadClass(String.valueOf(type));
                if (clz != null) {
                    clazz = clz;
                }
            } catch (Exception e) {

            }
        }
        return ObjectConvertor.tryConvertAsType(val, clazz);
    }

    default Object convert(Object val, Object type) {
        return cast(val, type);
    }

    default ChronoUnit chrono_unit(String unit) {
        if (unit == null || unit.isEmpty()) {
            return ChronoUnit.DAYS;
        }
        unit = unit.trim();
        try {
            ChronoUnit ret = ChronoUnit.valueOf(unit.toUpperCase());
            if (ret != null) {
                return ret;
            }
        } catch (IllegalArgumentException e) {

        }

        for (String[] arr : CHRONO_UNIT_MAPPING) {
            if (arr[0].equalsIgnoreCase(unit)) {
                try {
                    ChronoUnit ret = ChronoUnit.valueOf(unit.toUpperCase());
                    if (ret != null) {
                        return ret;
                    }
                } catch (IllegalArgumentException e) {

                }
            }
        }
        return ChronoUnit.DAYS;
    }

    default Object date_sub(Object date, String unit, long interval) {
        return date_add(date, unit, -interval);
    }

    default Object date_add(Object date, String unit, long interval) {
        if (date == null) {
            return null;
        }
        ChronoUnit chronoUnit = chrono_unit(unit);
        LocalDateTime v = (LocalDateTime) ObjectConvertor.tryConvertAsType(date, LocalDateTime.class);
        LocalDateTime rv = v.plus(interval, chronoUnit);
        Class<?> rawType = date.getClass();
        return ObjectConvertor.tryConvertAsType(rv, rawType);
    }

    default Date to_date(Object obj) {
        if (obj instanceof Date) {
            return (Date) obj;
        }
        return ObjectConvertor.tryParseDate(String.valueOf(obj));
    }

    default Date to_date(Object obj, String pattern) {
        String str = null;
        if (obj == null) {
            str = null;
        } else if (obj instanceof CharSequence
                || obj instanceof Appendable
                || obj instanceof String) {
            str = String.valueOf(obj);
        } else {
            str = String.valueOf(obj);
        }
        if (str == null || str.isEmpty()) {
            return null;
        }
        if (pattern == null || pattern.isEmpty()) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        try {
            return new SimpleDateFormat(pattern).parse(str);
        } catch (ParseException e) {
            Object ret = ObjectConvertor.tryConvertAsType(str, Date.class);
            if (ret instanceof Date) {
                return (Date) ret;
            }
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    default String date_format(Object date, String pattern) {
        if (date == null) {
            return null;
        }
        if (pattern == null || pattern.isEmpty()) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        if (date instanceof Date) {
            return new SimpleDateFormat(pattern).format(date);
        } else if (date instanceof Temporal) {
            return DateTimeFormatter.ofPattern(pattern).format((Temporal) date);
        }
        throw new IllegalArgumentException("un-support date type[" + date.getClass() + "] format!");
    }

    default Object last_day(Object date) {
        if (date == null) {
            return null;
        }

        LocalDateTime v = (LocalDateTime) ObjectConvertor.tryConvertAsType(date, LocalDateTime.class);
        LocalDateTime rv = v.withDayOfMonth(1).plusMonths(1).plusDays(-1);
        Class<?> rawType = date.getClass();
        return ObjectConvertor.tryConvertAsType(rv, rawType);
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
            return date_format(obj, pattern);
        } else if (ObjectConvertor.isNumericType(clazz)) {
            BigDecimal num = (BigDecimal) ObjectConvertor.tryConvertAsType(obj, BigDecimal.class);
            return String.format(pattern, num.doubleValue());
        }
        return String.valueOf(obj);
    }

    default String to_string(Object obj) {
        return to_char(obj, null);
    }

    default String to_string(Object obj, String pattern) {
        return to_char(obj, pattern);
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

    default String uuid() {
        return UUID.randomUUID().toString();
    }

    default long snowflake_id() {
        return SnowflakeLongUid.getId();
    }

    default Date sysdate() {
        return new Date(SystemClock.currentTimeMillis());
    }

    default Date now() {
        return new Date(SystemClock.currentTimeMillis());
    }

    default long timestamp() {
        return SystemClock.currentTimeSeconds();
    }

    default Date timestamp_to_date(Object ts) {
        long lts = -1;
        if (ts == null) {
            return null;
        }
        if (ts instanceof Long) {
            lts = (Long) ts;
        } else {
            lts = Long.parseLong(String.valueOf(ts));
        }
        return new Date(lts * 1000);
    }

    default Long date_to_timestamp(Object date) {
        if (date == null) {
            return null;
        }
        Date dt = (Date) ObjectConvertor.tryConvertAsType(date, Date.class);
        return dt.getTime() / 1000;
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

    default Object add_months(Object date, long interval) {
        return date_add(date, ChronoUnit.MONTHS.name(), interval);
    }

    default Object add_days(Object date, long interval) {
        return date_add(date, ChronoUnit.DAYS.name(), interval);
    }

    default Object trunc(Object obj) {
        if (obj == null) {
            return null;
        }
        Class<?> clazz = obj.getClass();
        if (ObjectConvertor.isNumericType(clazz)) {
            return trunc(obj, 0);
        } else if (ObjectConvertor.isDateType(clazz)) {
            return trunc(obj, "");
        }
        throw new IllegalArgumentException("un-support trunc apply to type, expect date/number, but found :" + clazz);
    }

    default Object trunc(Object date, String format) {
        if (date == null) {
            return null;
        }
        if (format == null || format.isEmpty()) {
            format = "dd";
        }
        Object time = ObjectConvertor.tryConvertAsType(date, LocalDateTime.class);
        if ((!(time instanceof LocalDateTime))) {
            throw new IllegalArgumentException("date cannot cast as date type, of type :" + date.getClass());
        }
        LocalDateTime obj = (LocalDateTime) time;
        if ("dd".equalsIgnoreCase(format)
                || "day".equalsIgnoreCase(format)
                || "days".equalsIgnoreCase(format)) {
            obj = obj.withNano(0)
                    .withSecond(0)
                    .withMinute(0)
                    .withHour(0);
        } else if ("MM".equals(format)
                || "mon".equalsIgnoreCase(format)
                || "month".equalsIgnoreCase(format)
                || "months".equalsIgnoreCase(format)) {
            obj = obj.withNano(0)
                    .withSecond(0)
                    .withMinute(0)
                    .withHour(0)
                    .withDayOfMonth(1);
        } else if ("yyyy".equalsIgnoreCase(format)
                || "year".equalsIgnoreCase(format)
                || "years".equalsIgnoreCase(format)) {
            obj = obj.withNano(0)
                    .withSecond(0)
                    .withMinute(0)
                    .withHour(0)
                    .withDayOfMonth(1)
                    .withMonth(1);
        } else if ("HH".equalsIgnoreCase(format) || "hh24".equalsIgnoreCase(format)
                || "hour".equalsIgnoreCase(format)
                || "hours".equalsIgnoreCase(format)) {
            obj = obj.withNano(0)
                    .withSecond(0)
                    .withMinute(0);
        } else if ("mm".equals(format) || "mi".equalsIgnoreCase(format)
                || "min".equalsIgnoreCase(format)
                || "minute".equalsIgnoreCase(format)
                || "minutes".equalsIgnoreCase(format)) {
            obj = obj.withNano(0)
                    .withSecond(0);
        } else if ("ss".equalsIgnoreCase(format)
                || "sec".equalsIgnoreCase(format)
                || "second".equalsIgnoreCase(format)
                || "seconds".equalsIgnoreCase(format)) {
            obj = obj.withNano(0);
        } else if ("SSS".equalsIgnoreCase(format)
                || "mill".equalsIgnoreCase(format)
                || "mills".equalsIgnoreCase(format)
                | "ms".equalsIgnoreCase(format)) {
            obj = obj.withNano(obj.getNano() / 1000_000 * 1000_000);
        } else if ("week".equalsIgnoreCase(format)
                || "iw".equalsIgnoreCase(format)) {
            DayOfWeek week = obj.getDayOfWeek();
            int day = week.getValue();
            obj = obj.plusDays(day - 1);
        } else {
            throw new IllegalArgumentException("un-support date trunc format :" + format);
        }

        return ObjectConvertor.tryConvertAsType(obj, date.getClass());
    }

    default Object trunc(Object number, Integer precision) {
        if (number == null) {
            return null;
        }
        if (precision == null) {
            precision = 0;
        }
        Object num = ObjectConvertor.tryConvertAsType(number, BigDecimal.class);
        if (!(num instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number.getClass());
        }

        BigDecimal obj = (BigDecimal) num;
        if (precision == 0) {
            obj = new BigDecimal(obj.toBigInteger());
        } else if (precision > 0) {
            BigDecimal scale = BigDecimal.TEN;
            scale = scale.pow(precision, MATH_CONTEXT);
            obj = obj.multiply(scale, MATH_CONTEXT);
            obj = new BigDecimal(obj.toBigInteger());
            obj = obj.divide(scale, MATH_CONTEXT);
        } else {
            precision = -precision;
            BigDecimal scale = BigDecimal.TEN;
            scale = scale.pow(precision, MATH_CONTEXT);
            obj = obj.divide(scale, MATH_CONTEXT);
            obj = new BigDecimal(obj.toBigInteger());
            obj = obj.multiply(scale, MATH_CONTEXT);
        }

        return ObjectConvertor.tryConvertAsType(obj, number.getClass());
    }

    default Object round(Object number) {
        return round(number, 0);
    }

    default Object round(Object number, Integer precision) {
        if (number == null) {
            return null;
        }
        if (precision == null) {
            precision = 0;
        }
        Object num = ObjectConvertor.tryConvertAsType(number, BigDecimal.class);
        if (!(num instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number.getClass());
        }

        BigDecimal obj = (BigDecimal) num;
        if (precision == 0) {
            obj = new BigDecimal(obj.add(NUM_0_5).toBigInteger());
        } else if (precision > 0) {
            BigDecimal scale = BigDecimal.TEN;
            scale = scale.pow(precision, MATH_CONTEXT);
            obj = obj.multiply(scale, MATH_CONTEXT).add(NUM_0_5);
            obj = new BigDecimal(obj.toBigInteger());
            obj = obj.divide(scale, MATH_CONTEXT);
        } else {
            precision = -precision;
            BigDecimal scale = BigDecimal.TEN;
            scale = scale.pow(precision, MATH_CONTEXT);
            obj = obj.divide(scale, MATH_CONTEXT).add(NUM_0_5);
            obj = new BigDecimal(obj.toBigInteger());
            obj = obj.multiply(scale, MATH_CONTEXT);
        }

        return ObjectConvertor.tryConvertAsType(obj, number.getClass());
    }

    default String concat(Object... args) {
        StringBuilder builder = new StringBuilder();
        for (Object item : args) {
            builder.append(item);
        }
        return builder.toString();
    }

    default String concat(Iterable<?> args) {
        StringBuilder builder = new StringBuilder();
        for (Object item : args) {
            builder.append(item);
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

    default Object first_day(Object date) {
        if (date == null) {
            return null;
        }
        Object obj = ObjectConvertor.tryConvertAsType(date, LocalDateTime.class);
        if ((!(obj instanceof LocalDateTime))) {
            throw new IllegalArgumentException("date cannot cast as date type, of type :" + date.getClass());
        }
        LocalDateTime time = (LocalDateTime) obj;
        time = time
                .withDayOfMonth(1);
        return ObjectConvertor.tryConvertAsType(time, date.getClass());
    }

    default int day(Object date) {
        return extract(ChronoUnit.DAYS.name(), date);
    }

    default int month(Object date) {
        return extract(ChronoUnit.MONTHS.name(), date);
    }

    default int year(Object date) {
        return extract(ChronoUnit.YEARS.name(), date);
    }

    default int hour(Object date) {
        return extract(ChronoUnit.HOURS.name(), date);
    }

    default int minute(Object date) {
        return extract(ChronoUnit.MINUTES.name(), date);
    }

    default int second(Object date) {
        return extract(ChronoUnit.SECONDS.name(), date);
    }

    default int week(Object date) {
        return extract(ChronoUnit.WEEKS.name(), date);
    }

    default int extract(String fmt, Object date) {
        if (date == null) {
            return -1;
        }
        Object obj = ObjectConvertor.tryConvertAsType(date, LocalDateTime.class);
        if ((!(obj instanceof LocalDateTime))) {
            throw new IllegalArgumentException("date cannot cast as date type, of type :" + date.getClass());
        }
        LocalDateTime time = (LocalDateTime) obj;
        ChronoUnit unit = chrono_unit(fmt);
        if (unit == ChronoUnit.YEARS) {
            return time.getYear();
        } else if (unit == ChronoUnit.MONTHS) {
            return time.getMonth().getValue();
        } else if (unit == ChronoUnit.DAYS) {
            return time.getDayOfMonth();
        } else if (unit == ChronoUnit.HOURS) {
            return time.getHour();
        } else if (unit == ChronoUnit.MINUTES) {
            return time.getMinute();
        } else if (unit == ChronoUnit.SECONDS) {
            return time.getSecond();
        } else if (unit == ChronoUnit.MILLIS) {
            return time.getNano() / 1000 / 1000;
        } else if (unit == ChronoUnit.MICROS) {
            return time.getNano() / 1000;
        } else if (unit == ChronoUnit.NANOS) {
            return time.getNano();
        } else if (unit == ChronoUnit.WEEKS) {
            return time.getDayOfWeek().getValue();
        }
        return -1;
    }

    default BigDecimal to_number(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof BigDecimal) {
            return (BigDecimal) obj;
        }
        return (BigDecimal) ObjectConvertor.tryConvertAsType(obj, BigDecimal.class);
    }

    default Integer to_int(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Integer) {
            return (int) obj;
        }
        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }
        return (Integer) ObjectConvertor.tryConvertAsType(obj, Integer.class);
    }

    default Long to_long(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Long) {
            return (long) obj;
        }
        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        }
        return (Long) ObjectConvertor.tryConvertAsType(obj, Long.class);
    }

    default boolean to_boolean(Object obj) {
        return ObjectConvertor.toBoolean(obj);
    }

    default void sleep(long seconds) {
        if (seconds >= 0) {
            try {
                Thread.sleep(seconds * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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

    default String substr_index(Object obj, String substr, int len) {
        int idx = instr(obj, substr);
        if (idx < 0) {
            return "";
        }
        return substr(obj, idx, len);
    }

    default ArrayList<String> splitRegex(String str, String regex) {
        return splitRegex(str, regex, -1);
    }

    default ArrayList<String> splitRegex(String str, String regex, int limit) {
        if (str == null) {
            return new ArrayList<>();
        }
        if (regex == null) {
            return new ArrayList<>(Collections.singletonList(str));
        }
        String[] arr = Pattern.compile(regex).split(str, limit);
        return new ArrayList<>(Arrays.asList(arr));
    }

    default ArrayList<String> splitLiteral(String str, String regex) {
        return splitLiteral(str, regex, -1);
    }

    default ArrayList<String> splitLiteral(String str, String regex, int limit) {
        if (str == null) {
            return new ArrayList<>();
        }
        if (regex == null) {
            return new ArrayList<>(Collections.singletonList(str));
        }
        String[] arr = Pattern.compile(regex, Pattern.LITERAL).split(str, limit);
        return new ArrayList<>(Arrays.asList(arr));
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

    default boolean ends(Object obj, Object substr) {
        return ends_with(obj, substr);
    }

    default boolean starts_with(Object obj, Object substr) {
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
        return str.startsWith(sstr);
    }

    default boolean starts(Object obj, String substr) {
        return starts_with(obj, substr);
    }

    default boolean ends_with(Object obj, Object substr) {
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
        return str.endsWith(sstr);
    }

    default Object neg(Object number) {
        if (number == null) {
            return null;
        }
        Object num = ObjectConvertor.tryConvertAsType(number, BigDecimal.class);
        if (!(num instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number.getClass());
        }

        BigDecimal obj = (BigDecimal) num;
        obj = obj.negate(MATH_CONTEXT);

        return ObjectConvertor.tryConvertAsType(obj, number.getClass());
    }

    default Object abs(Object number) {
        if (number == null) {
            return null;
        }
        Object num = ObjectConvertor.tryConvertAsType(number, BigDecimal.class);
        if (!(num instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number.getClass());
        }

        BigDecimal obj = (BigDecimal) num;
        obj = obj.abs(MATH_CONTEXT);

        return ObjectConvertor.tryConvertAsType(obj, number.getClass());
    }

    default Object ln(Object number) {
        if (number == null) {
            return null;
        }
        Object num = ObjectConvertor.tryConvertAsType(number, BigDecimal.class);
        if (!(num instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number.getClass());
        }

        BigDecimal obj = (BigDecimal) num;
        obj = BigDecimal.valueOf(Math.log(obj.doubleValue()));

        return ObjectConvertor.tryConvertAsType(obj, number.getClass());
    }

    default Object add(Object number1, Object number2) {
        if (number1 == null) {
            return null;
        }
        if (number2 == null) {
            return null;
        }
        Object num1 = ObjectConvertor.tryConvertAsType(number1, BigDecimal.class);
        if (!(num1 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number1.getClass());
        }
        Object num2 = ObjectConvertor.tryConvertAsType(number2, BigDecimal.class);
        if (!(num2 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number2.getClass());
        }

        BigDecimal b1 = (BigDecimal) num1;
        BigDecimal b2 = (BigDecimal) num2;
        b1 = b1.add(b2, MATH_CONTEXT);

        return ObjectConvertor.tryConvertAsType(b1, number1.getClass());
    }

    default Object sub(Object number1, Object number2) {
        if (number1 == null) {
            return null;
        }
        if (number2 == null) {
            return null;
        }
        Object num1 = ObjectConvertor.tryConvertAsType(number1, BigDecimal.class);
        if (!(num1 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number1.getClass());
        }
        Object num2 = ObjectConvertor.tryConvertAsType(number2, BigDecimal.class);
        if (!(num2 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number2.getClass());
        }

        BigDecimal b1 = (BigDecimal) num1;
        BigDecimal b2 = (BigDecimal) num2;
        b1 = b1.subtract(b2, MATH_CONTEXT);

        return ObjectConvertor.tryConvertAsType(b1, number1.getClass());
    }

    default Object mul(Object number1, Object number2) {
        if (number1 == null) {
            return null;
        }
        if (number2 == null) {
            return null;
        }
        Object num1 = ObjectConvertor.tryConvertAsType(number1, BigDecimal.class);
        if (!(num1 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number1.getClass());
        }
        Object num2 = ObjectConvertor.tryConvertAsType(number2, BigDecimal.class);
        if (!(num2 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number2.getClass());
        }

        BigDecimal b1 = (BigDecimal) num1;
        BigDecimal b2 = (BigDecimal) num2;
        b1 = b1.multiply(b2, MATH_CONTEXT);

        return ObjectConvertor.tryConvertAsType(b1, number1.getClass());
    }

    default Object div(Object number1, Object number2) {
        if (number1 == null) {
            return null;
        }
        if (number2 == null) {
            return null;
        }
        Object num1 = ObjectConvertor.tryConvertAsType(number1, BigDecimal.class);
        if (!(num1 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number1.getClass());
        }
        Object num2 = ObjectConvertor.tryConvertAsType(number2, BigDecimal.class);
        if (!(num2 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number2.getClass());
        }

        BigDecimal b1 = (BigDecimal) num1;
        BigDecimal b2 = (BigDecimal) num2;
        b1 = b1.divide(b2, MATH_CONTEXT);

        return ObjectConvertor.tryConvertAsType(b1, number1.getClass());
    }

    default Object mod(Object number1, Object number2) {
        if (number1 == null) {
            return null;
        }
        if (number2 == null) {
            return null;
        }
        Object num1 = ObjectConvertor.tryConvertAsType(number1, BigInteger.class);
        if (!(num1 instanceof BigInteger)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number1.getClass());
        }
        Object num2 = ObjectConvertor.tryConvertAsType(number2, BigInteger.class);
        if (!(num2 instanceof BigInteger)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number2.getClass());
        }

        BigInteger b1 = (BigInteger) num1;
        BigInteger b2 = (BigInteger) num2;
        b1 = b1.mod(b2);

        return ObjectConvertor.tryConvertAsType(b1, number1.getClass());
    }

    default Object pow(Object number1, Object number2) {
        if (number1 == null) {
            return null;
        }
        if (number2 == null) {
            return null;
        }
        Object num1 = ObjectConvertor.tryConvertAsType(number1, BigDecimal.class);
        if (!(num1 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number1.getClass());
        }
        Object num2 = ObjectConvertor.tryConvertAsType(number2, BigDecimal.class);
        if (!(num2 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number2.getClass());
        }

        BigDecimal b1 = (BigDecimal) num1;
        BigDecimal b2 = (BigDecimal) num2;
        b1 = BigDecimal.valueOf(Math.pow(b1.doubleValue(), b2.doubleValue()));

        return ObjectConvertor.tryConvertAsType(b1, number1.getClass());
    }

    default Object sin(Object number1) {
        if (number1 == null) {
            return null;
        }
        Object num1 = ObjectConvertor.tryConvertAsType(number1, BigDecimal.class);
        if (!(num1 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number1.getClass());
        }


        BigDecimal b1 = (BigDecimal) num1;
        b1 = BigDecimal.valueOf(Math.sin(b1.doubleValue()));

        return ObjectConvertor.tryConvertAsType(b1, number1.getClass());
    }

    default Object cos(Object number1) {
        if (number1 == null) {
            return null;
        }
        Object num1 = ObjectConvertor.tryConvertAsType(number1, BigDecimal.class);
        if (!(num1 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number1.getClass());
        }


        BigDecimal b1 = (BigDecimal) num1;
        b1 = BigDecimal.valueOf(Math.cos(b1.doubleValue()));

        return ObjectConvertor.tryConvertAsType(b1, number1.getClass());
    }

    default Object tan(Object number1) {
        if (number1 == null) {
            return null;
        }
        Object num1 = ObjectConvertor.tryConvertAsType(number1, BigDecimal.class);
        if (!(num1 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number1.getClass());
        }


        BigDecimal b1 = (BigDecimal) num1;
        b1 = BigDecimal.valueOf(Math.tan(b1.doubleValue()));

        return ObjectConvertor.tryConvertAsType(b1, number1.getClass());
    }

    default Object asin(Object number1) {
        if (number1 == null) {
            return null;
        }
        Object num1 = ObjectConvertor.tryConvertAsType(number1, BigDecimal.class);
        if (!(num1 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number1.getClass());
        }


        BigDecimal b1 = (BigDecimal) num1;
        b1 = BigDecimal.valueOf(Math.asin(b1.doubleValue()));

        return ObjectConvertor.tryConvertAsType(b1, number1.getClass());
    }

    default Object acos(Object number1) {
        if (number1 == null) {
            return null;
        }
        Object num1 = ObjectConvertor.tryConvertAsType(number1, BigDecimal.class);
        if (!(num1 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number1.getClass());
        }


        BigDecimal b1 = (BigDecimal) num1;
        b1 = BigDecimal.valueOf(Math.acos(b1.doubleValue()));

        return ObjectConvertor.tryConvertAsType(b1, number1.getClass());
    }

    default Object atan(Object number1) {
        if (number1 == null) {
            return null;
        }
        Object num1 = ObjectConvertor.tryConvertAsType(number1, BigDecimal.class);
        if (!(num1 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number1.getClass());
        }


        BigDecimal b1 = (BigDecimal) num1;
        b1 = BigDecimal.valueOf(Math.atan(b1.doubleValue()));

        return ObjectConvertor.tryConvertAsType(b1, number1.getClass());
    }

    default Object sqrt(Object number1) {
        if (number1 == null) {
            return null;
        }
        Object num1 = ObjectConvertor.tryConvertAsType(number1, BigDecimal.class);
        if (!(num1 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number1.getClass());
        }


        BigDecimal b1 = (BigDecimal) num1;
        b1 = BigDecimal.valueOf(Math.sqrt(b1.doubleValue()));

        return ObjectConvertor.tryConvertAsType(b1, number1.getClass());
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
