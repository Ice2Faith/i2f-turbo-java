package i2f.jdbc.procedure.context;

import i2f.convert.obj.ObjectConvertor;
import i2f.io.stream.StreamUtil;
import i2f.match.regex.RegexUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
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

/**
 * @author Ice2Faith
 * @date 2025/2/17 16:09
 */
public class ContextFunctions {
    public static final String[][] ORACLE_REGEX_REPLACE_MAPPING = {
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
    public static final String[][] CHRONO_UNIT_MAPPING = {
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
    public static final MathContext MATH_CONTEXT = new MathContext(20, RoundingMode.HALF_UP);
    public static final BigDecimal NUM_0_5 = new BigDecimal("0.5");

    public static String convertOracleRegexExpression(String regex) {
        if (regex == null) {
            return null;
        }
        for (String[] arr : ORACLE_REGEX_REPLACE_MAPPING) {
            regex = regex.replace(arr[0], arr[1]);
        }
        return regex;
    }

    public static String convertOracleRegexReplacement(String replacement) {
        if (replacement == null) {
            return null;
        }
        replacement = RegexUtil.regexFindAndReplace(replacement, "[\\\\]\\d+", (str) -> {
            return "$" + str.substring(1);
        });
        return replacement;
    }

    public static String replace(String str, String target) {
        return replace(str, target, "");
    }

    public static String replace(String str, String target, String replacement) {
        if (str == null) {
            return str;
        }
        return str.replace(target, replacement);
    }

    public static String regex_replace(String str, String regex) {
        return regex_replace(str, regex, "");
    }

    public static String regex_replace(String str, String regex, String replacement) {
        return regex_replace(str, regex, replacement, -1);
    }

    public static String regex_replace(String str, String regex, String replacement, int occurrence) {
        if (str == null) {
            return str;
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

    public static String regexp_replace(String str, String regex) {
        return regexp_replace(str, regex, "");
    }

    public static String regexp_replace(String str, String regex, String replacement) {
        return regex_replace(str, regex, replacement);
    }

    public static String regexp_replace(String str, String regex, String replacement, int occurrence) {
        return regex_replace(str, regex, replacement, occurrence);
    }

    public static boolean regex_like(String str, String regex) {
        if (str == null) {
            return false;
        }
        regex = convertOracleRegexExpression(regex);
        return str.matches(regex);
    }

    public static boolean regexp_like(String str, String regex) {
        return regex_like(str, regex);
    }

    public static String trim(String str) {
        if (str == null) {
            return str;
        }
        return str.trim();
    }

    public static String upper(String str) {
        if (str == null) {
            return str;
        }
        return str.toUpperCase();
    }

    public static String lower(String str) {
        if (str == null) {
            return str;
        }
        return str.toLowerCase();
    }

    public static String chr(int ascii) {
        char ch = (char) ascii;
        return "" + ch;
    }

    public static String rtrim(String str) {
        return rtrim(str, null);
    }

    public static String rtrim(String str, String substr) {
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

    public static int length(Object obj) {
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

    public static int lengthb(Object obj) {
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

    public static String ltrim(String str) {
        return ltrim(str, null);
    }

    public static String ltrim(String str, String substr) {
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

    public static Object nvl(Object v1, Object v2) {
        return v1 != null ? v1 : v2;
    }

    public static Object ifnull(Object v1, Object v2) {
        return nvl(v1, v2);
    }

    public static Object nvl2(Object cond, Object trueVal, Object falseVal) {
        if (cond instanceof Boolean) {
            Boolean ok = (Boolean) cond;
            if (ok) {
                return trueVal;
            } else {
                return falseVal;
            }
        }
        if (cond != null) {
            return trueVal;
        } else {
            return falseVal;
        }
    }

    public static Object if2(Object cond, Object trueVal, Object falseVal) {
        return nvl2(cond, trueVal, falseVal);
    }

    public static Object decode(Object target, Object... args) {
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

    public static Object cast(Object val, Object type) {
        if (type == null) {
            throw new ClassCastException("cannot cast value to type null");
        }
        if (val == null) {
            return null;
        }
        Class<?> clazz = type.getClass();
        if (type instanceof Class) {
            clazz = (Class<?>) type;
        }
        return ObjectConvertor.tryConvertAsType(val, clazz);
    }

    public static ChronoUnit chrono_unit(String unit) {
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

    public static Object date_sub(Object date, String unit, long interval) {
        return date_add(date, unit, -interval);
    }

    public static Object date_add(Object date, String unit, long interval) {
        if (date == null) {
            return null;
        }
        ChronoUnit chronoUnit = chrono_unit(unit);
        LocalDateTime v = (LocalDateTime) ObjectConvertor.tryConvertAsType(date, LocalDateTime.class);
        LocalDateTime rv = v.plus(interval, chronoUnit);
        Class<?> rawType = date.getClass();
        return ObjectConvertor.tryConvertAsType(rv, rawType);
    }

    public static Date to_date(Object obj, String pattern) {
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
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public static String date_format(Object date, String pattern) {
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

    public static Object last_day(Object date) {
        if (date == null) {
            return null;
        }

        LocalDateTime v = (LocalDateTime) ObjectConvertor.tryConvertAsType(date, LocalDateTime.class);
        LocalDateTime rv = v.withDayOfMonth(1).plusMonths(1).plusDays(-1);
        Class<?> rawType = date.getClass();
        return ObjectConvertor.tryConvertAsType(rv, rawType);
    }

    public static String to_char(Object obj) {
        return to_char(obj, null);
    }

    public static String to_char(Object obj, String pattern) {
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

    public static Date sysdate() {
        return new Date();
    }

    public static Date now() {
        return new Date();
    }

    public static Object add_months(Object date, long interval) {
        return date_add(date, ChronoUnit.MONTHS.name(), interval);
    }

    public static Object add_days(Object date, long interval) {
        return date_add(date, ChronoUnit.DAYS.name(), interval);
    }

    public static Object trunc(Object obj) {
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

    public static Object trunc(Object date, String format) {
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

    public static Object trunc(Object number, Integer precision) {
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

    public static Object round(Object number) {
        return round(number, 0);
    }

    public static Object round(Object number, Integer precision) {
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

    public static String concat(Object... args) {
        StringBuilder builder = new StringBuilder();
        for (Object item : args) {
            builder.append(item);
        }
        return builder.toString();
    }

    public static String concat(Iterable<?> args) {
        StringBuilder builder = new StringBuilder();
        for (Object item : args) {
            builder.append(item);
        }
        return builder.toString();
    }

    public static String join(Object separator, Object... args) {
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

    public static String join(Object separator, Iterable<?> args) {
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

    public static Object first_day(Object date) {
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

    public static int extract(String fmt, Object date) {
        if (date == null) {
            return -1;
        }
        Object obj = ObjectConvertor.tryConvertAsType(date, LocalDateTime.class);
        if ((!(obj instanceof LocalDateTime))) {
            throw new IllegalArgumentException("date cannot cast as date type, of type :" + date.getClass());
        }
        LocalDateTime time = (LocalDateTime) obj;
        if ("year".equalsIgnoreCase(fmt)) {
            return time.getYear();
        } else if ("month".equalsIgnoreCase(fmt)) {
            return time.getMonth().getValue();
        } else if ("day".equalsIgnoreCase(fmt)) {
            return time.getDayOfMonth();
        } else if ("hour".equalsIgnoreCase(fmt)) {
            return time.getHour();
        } else if ("minute".equalsIgnoreCase(fmt)) {
            return time.getMinute();
        } else if ("second".equalsIgnoreCase(fmt)) {
            return time.getSecond();
        }
        return -1;
    }

    public static Double to_number(Object obj) {
        if (obj == null) {
            return null;
        }
        return Double.parseDouble(String.valueOf(obj));
    }

    public static void sleep(long seconds) {
        if (seconds >= 0) {
            try {
                Thread.sleep(seconds * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static int instr(String str, String sstr) {
        if (str == null || sstr == null) {
            return -1;
        }
        return str.indexOf(sstr);
    }

    public static String substr(Object obj, int index) {
        return substr(obj, index, -1);
    }

    public static String substr(Object obj, int index, int len) {
        String str = null;
        if (obj != null) {
            if (obj instanceof CharSequence
                    || obj instanceof String
                    || obj instanceof Appendable) {
                str = String.valueOf(obj);
            } else {
                throw new IllegalArgumentException("substr(obj) cannot support type:" + obj.getClass());
            }
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

    public static String substrb(Object str, int index) {
        return substrb(str, index, -1);
    }

    public static String substrb(Object str, int index, int len) {
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

    public static ArrayList<String> splitRegex(String str, String regex) {
        return splitRegex(str, regex, -1);
    }

    public static ArrayList<String> splitRegex(String str, String regex, int limit) {
        String[] arr = Pattern.compile(regex).split(str, limit);
        return new ArrayList<>(Arrays.asList(arr));
    }

    public static ArrayList<String> splitLiteral(String str, String regex) {
        return splitLiteral(str, regex, -1);
    }

    public static ArrayList<String> splitLiteral(String str, String regex, int limit) {
        String[] arr = Pattern.compile(regex, Pattern.LITERAL).split(str, limit);
        return new ArrayList<>(Arrays.asList(arr));
    }

    public static boolean contains(Object obj, Object substr) {
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

    public static boolean like(Object obj, Object substr) {
        return contains(obj, substr);
    }

    public static boolean llike(Object obj, Object substr) {
        return likel(obj, substr);
    }

    public static boolean likel(Object obj, Object substr) {
        return startsWith(obj, substr);
    }

    public static boolean startsWith(Object obj, Object substr) {
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

    public static boolean rlike(Object obj, String substr) {
        return liker(obj, substr);
    }

    public static boolean liker(Object obj, Object substr) {
        return endsWith(obj, substr);
    }

    public static boolean endsWith(Object obj, Object substr) {
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

    public static Object abs(Object number) {
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

    public static String md5(Object data) {
        return mds("MD5", data, "hex", null);
    }

    public static String sha1(Object data) {
        return mds("SHA-1", data, "hex", null);
    }

    public static String sha256(Object data) {
        return mds("SHA-256", data, "hex", null);
    }

    public static String sha384(Object data) {
        return mds("SHA-384", data, "hex", null);
    }

    public static String sha512(Object data) {
        return mds("SHA-512", data, "hex", null);
    }

    public static String mds(String algorithm, Object data) {
        return mds(algorithm, data, "hex", null);
    }

    public static String mds(String algorithm, Object data, String format) {
        return mds(algorithm, data, format, null);
    }

    public static String mds(String algorithm, Object data, String format, String provider) {
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
