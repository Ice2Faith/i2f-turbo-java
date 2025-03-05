package i2f.jdbc.procedure.context;

import i2f.convert.obj.ObjectConvertor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author Ice2Faith
 * @date 2025/2/17 16:09
 */
public class ContextFunctions {
    public static String replace(String str, String target, String replacement) {
        if (str == null) {
            return str;
        }
        return str.replace(target, replacement);
    }

    public static String regexReplace(String str, String regex, String replacement) {
        if (str == null) {
            return str;
        }
        return Pattern.compile(regex).matcher(str).replaceAll(replacement);
    }

    public static String regexReplaceIgnoreCase(String str, String regex, String replacement) {
        if (str == null) {
            return str;
        }
        return Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(str).replaceAll(replacement);
    }

    public static boolean regexLike(String str, String regex) {
        if (str == null) {
            return false;
        }
        return str.matches(regex);
    }

    public static boolean regexLikeIgnoreCase(String str, String regex) {
        if (str == null) {
            return false;
        }
        return Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(str).matches();
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

    public static Object date_add(Object date, String unit, long interval) {
        if (date == null) {
            return null;
        }
        if (unit == null || unit.isEmpty()) {
            unit = ChronoUnit.DAYS.toString();
        }
        ChronoUnit chronoUnit = ChronoUnit.valueOf(unit.trim().toUpperCase());
        LocalDateTime v = (LocalDateTime) ObjectConvertor.tryConvertAsType(date, LocalDateTime.class);
        LocalDateTime rv = v.plus(interval, chronoUnit);
        Class<?> rawType = date.getClass();
        return ObjectConvertor.tryConvertAsType(rv, rawType);
    }

    public static Date to_date(String str, String pattern) {
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

}
