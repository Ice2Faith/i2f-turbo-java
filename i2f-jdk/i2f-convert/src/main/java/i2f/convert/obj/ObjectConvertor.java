package i2f.convert.obj;

import i2f.typeof.TypeOf;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/4/26 10:20
 * @desc
 */
public class ObjectConvertor {

    public static final Map<Class<?>, Function<BigDecimal, ?>> bigDecimalTypeConverterMap;
    public static final Map<Class<?>, Function<Boolean, ?>> boolTypeConverterMap;
    public static final Map<Class<?>, Function<Character, ?>> charTypeConverterMap;
    public static final Map<Class<?>, Function<Object, Instant>> date2InstantConverterMap;
    public static final Map<Class<?>, Function<Instant, ?>> dateTypeConverterMap;
    public static final String[] dateFormats = {
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
            "yyyy-MM-dd'T'HH:mm:ss SSSZ",
            "yyyy-MM-dd'T'HH:mm:ss.SSS",
            "yyyy-MM-dd'T'HH:mm:ss SSS",
            "yyyy-MM-dd'T'HH:mm:ssZ",
            "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd HH:mm:ss.SSS",
            "yyyy-MM-dd HH:mm:ss SSS",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd HH:mm",
            "yyyy-MM-dd",
            "yyyy-MM",
            "yyyy",
            "yyyy/MM/dd HH:mm:ss.SSS",
            "yyyy/MM/dd HH:mm:ss SSS",
            "yyyy/MM/dd HH:mm:ss",
            "yyyy/MM/dd HH:mm",
            "yyyy/MM/dd",
            "yyyy/MM",
            "yyyy年MM月dd HH时mm分ss秒.SSS",
            "yyyy年MM月dd HH时mm分ss秒 SSS",
            "yyyy年MM月dd HH时mm分ss秒",
            "yyyy年MM月dd HH时mm分",
            "yyyy年MM月dd",
            "yyyy年MM月",
            "yyyy年",
            "yyyyMMddHHmmssSSS",
            "yyyyMMddHHmmss",
            "yyyyMMddHHmm",
            "yyyyMMdd",
            "yyyyMM",
            "HH:mm:ss.SSS",
            "HH:mm:ss SSS",
            "HH:mm:ss",
            "HH:mm",
            "HH时mm分ss秒.SSS",
            "HH时mm分ss秒 SSS",
            "HH时mm分ss秒",
            "HH时mm分",
            "HHmmssSSS",
            "HHmmss",
            "HHmm"
    };
    public static final Map<String, DateTimeFormatter> dateFormaterMap;

    static {

        if (true) {
            Map<Class<?>, Function<BigDecimal, ?>> map = new LinkedHashMap<>();
            map.put(int.class, BigDecimal::intValue);
            map.put(Integer.class, BigDecimal::intValue);
            map.put(short.class, Number::shortValue);
            map.put(Short.class, Number::shortValue);
            map.put(long.class, Number::longValue);
            map.put(Long.class, Number::longValue);
            map.put(byte.class, Number::byteValue);
            map.put(Byte.class, Number::byteValue);
            map.put(float.class, Number::floatValue);
            map.put(Float.class, Number::floatValue);
            map.put(double.class, Number::doubleValue);
            map.put(Double.class, Number::doubleValue);
            map.put(BigInteger.class, BigDecimal::toBigInteger);
            map.put(BigDecimal.class, v -> v);
            map.put(AtomicInteger.class, v -> new AtomicInteger(v.intValue()));
            map.put(AtomicLong.class, v -> new AtomicLong(v.longValue()));

            bigDecimalTypeConverterMap = Collections.unmodifiableMap(map);
        }


        if (true) {
            Map<Class<?>, Function<Boolean, ?>> map = new LinkedHashMap<>();

            map.put(boolean.class, v -> v);
            map.put(Boolean.class, v -> v);

            boolTypeConverterMap = Collections.unmodifiableMap(map);
        }

        if (true) {
            Map<Class<?>, Function<Character, ?>> map = new LinkedHashMap<>();
            map.put(char.class, v -> v);
            map.put(Character.class, v -> v);

            charTypeConverterMap = Collections.unmodifiableMap(map);
        }

        if (true) {
            Map<Class<?>, Function<Object, Instant>> map = new LinkedHashMap<>();
            map.put(Date.class, v -> {
                return ((Date) v).toInstant();
            });
            map.put(java.sql.Date.class, v -> {
                return ((java.sql.Date) v).toInstant();
            });
            map.put(Time.class, v -> {
                return ((Time) v).toInstant();
            });
            map.put(Timestamp.class, v -> {
                return ((Timestamp) v).toInstant();
            });
            map.put(LocalDateTime.class, v -> {
                LocalDateTime dt = (LocalDateTime) v;
                return dt.toInstant(ZoneId.systemDefault().getRules().getOffset(dt));
            });
            map.put(LocalDate.class, v -> {
                LocalDateTime dt = ((LocalDate) v).atStartOfDay();
                return dt.toInstant(ZoneId.systemDefault().getRules().getOffset(dt));
            });
            map.put(LocalTime.class, v -> {
                LocalDateTime dt = LocalDate.now().atTime((LocalTime) v);
                return dt.toInstant(ZoneId.systemDefault().getRules().getOffset(dt));
            });
            map.put(Calendar.class, v -> {
                Date time = ((Calendar) v).getTime();
                return time.toInstant();
            });
            map.put(Long.class, v -> {
                Long ts = ((Long) v);
                if (String.valueOf(ts).length() == 10) {
                    return Instant.ofEpochSecond(ts);
                }
                return Instant.ofEpochMilli(ts);
            });
            map.put(Instant.class, v -> {
                Instant ts = ((Instant) v);
                return ts;
            });
            map.put(Clock.class, v -> {
                Clock ts = ((Clock) v);
                return ts.instant();
            });

            date2InstantConverterMap = Collections.unmodifiableMap(map);
        }

        if (true) {
            Map<Class<?>, Function<Instant, ?>> map = new LinkedHashMap<>();

            map.put(Date.class, Date::from);
            map.put(java.sql.Date.class, v -> {
                return new java.sql.Date(v.toEpochMilli());
            });
            map.put(Time.class, Time::from);
            map.put(Timestamp.class, Timestamp::from);
            map.put(LocalDateTime.class, v -> {
                return v.atZone(ZoneId.systemDefault()).toLocalDateTime();
            });
            map.put(LocalDate.class, v -> {
                return v.atZone(ZoneId.systemDefault()).toLocalDateTime().toLocalDate();
            });
            map.put(LocalTime.class, v -> {
                return v.atZone(ZoneId.systemDefault()).toLocalDateTime().toLocalTime();
            });
            map.put(Calendar.class, v -> {
                Date dt = Date.from(v);
                Calendar ret = Calendar.getInstance();
                ret.setTime(dt);
                return ret;
            });
            map.put(Long.class, v -> {
                return v.getEpochSecond();
            });
            map.put(Instant.class, v -> {
                return v;
            });
            map.put(Clock.class, v -> {
                return Clock.fixed(v, ZoneId.systemDefault());
            });

            dateTypeConverterMap = Collections.unmodifiableMap(map);
        }

        if (true) {
            Map<String, DateTimeFormatter> map = new LinkedHashMap<>();
            for (String dateFormat : dateFormats) {
                map.put(dateFormat, DateTimeFormatter.ofPattern(dateFormat));
            }
            dateFormaterMap = Collections.unmodifiableMap(map);
        }
    }

    public static String stringify(Object obj,String nullAs){
        if(obj==null){
            return nullAs;
        }
        Class<?> clazz=obj.getClass();
        if(clazz.isArray()){
            StringBuilder builder = new StringBuilder();
            int len= Array.getLength(obj);
            builder.append("[");
            for (int i = 0; i < len; i++) {
                if(i>0){
                    builder.append(", ");
                }
                String str=stringify(Array.get(obj,i),nullAs);
                builder.append(str);
            }
            builder.append("]");
            return builder.toString();
        }else {
            return String.valueOf(obj);
        }
    }

    public static Object tryConvertAsType(Object val, Class<?> targetType) {
        if (val == null) {
            return val;
        }

        Class<?> clazz = val.getClass();
        // 类型匹配
        if (TypeOf.typeOf(clazz, targetType)) {
            return val;
        }
        // 目标类型为 String ，都能转
        if (TypeOf.typeOf(targetType, String.class)) {
            return stringify(val,null);
        }

        // 原始和目标都是 Number
        Class<?>[] numericTypes = bigDecimalTypeConverterMap.keySet().toArray(new Class<?>[0]);
        if (TypeOf.typeOfAny(clazz, numericTypes)
                &&
                TypeOf.typeOfAny(clazz, numericTypes)) {
            BigDecimal decimal = new BigDecimal(String.valueOf(val));
            for (Map.Entry<Class<?>, Function<BigDecimal, ?>> entry : bigDecimalTypeConverterMap.entrySet()) {
                Class<?> itemClass = entry.getKey();
                if (TypeOf.typeOf(itemClass, targetType)) {
                    return entry.getValue().apply(decimal);
                }
            }
        }

        // 原始和目标都是 Boolean
        Class<?>[] boolTypes = boolTypeConverterMap.keySet().toArray(new Class<?>[0]);
        if (TypeOf.typeOfAny(clazz, boolTypes)
                &&
                TypeOf.typeOfAny(clazz, boolTypes)) {
            boolean ok = (val == null) ? false : (Boolean) val;
            for (Map.Entry<Class<?>, Function<Boolean, ?>> entry : boolTypeConverterMap.entrySet()) {
                Class<?> itemClass = entry.getKey();
                if (TypeOf.typeOf(itemClass, targetType)) {
                    return entry.getValue().apply(ok);
                }
            }
        }

        // 原始和目标都是 Char
        Class<?>[] charTypes = charTypeConverterMap.keySet().toArray(new Class<?>[0]);
        if (TypeOf.typeOfAny(clazz, charTypes)
                &&
                TypeOf.typeOfAny(clazz, charTypes)) {
            char ch = (val == null) ? 0 : (Character) val;
            for (Map.Entry<Class<?>, Function<Character, ?>> entry : charTypeConverterMap.entrySet()) {
                Class<?> itemClass = entry.getKey();
                if (TypeOf.typeOf(itemClass, targetType)) {
                    return entry.getValue().apply(ch);
                }
            }
        }


        // 日期时间类型的互转
        Class<?>[] dateTypes = dateTypeConverterMap.keySet().toArray(new Class<?>[0]);
        if (TypeOf.typeOfAny(clazz, dateTypes)
                &&
                TypeOf.typeOfAny(clazz, dateTypes)) {
            Instant ins = null;
            for (Map.Entry<Class<?>, Function<Object, Instant>> entry : date2InstantConverterMap.entrySet()) {
                Class<?> itemClass = entry.getKey();
                if (TypeOf.typeOf(itemClass, clazz)) {
                    ins = entry.getValue().apply(val);
                    break;
                }
            }

            if (ins != null) {
                for (Map.Entry<Class<?>, Function<Instant, ?>> entry : dateTypeConverterMap.entrySet()) {
                    Class<?> itemClass = entry.getKey();
                    if (TypeOf.typeOf(itemClass, targetType)) {
                        return entry.getValue().apply(ins);
                    }
                }
            }

        }

        // 字符串字面值处理
        String valStr = String.valueOf(val);
        if (TypeOf.typeOfAny(targetType, numericTypes)) {
            BigDecimal decimal = null;
            if (valStr.matches("[1-9]([0-9]+)?")) {
                decimal = new BigDecimal(String.valueOf(val));
            } else if (valStr.matches("(0x|0X)[a-fA-F0-9]+")) {
                Long num = Long.valueOf(valStr.substring(2), 16);
                decimal = new BigDecimal(num);
            } else if (valStr.matches("0([0-9]+)?")) {
                Long num = Long.valueOf(valStr.substring(2), 8);
                decimal = new BigDecimal(String.valueOf(num));
            } else if (valStr.matches("(0b|0B)[0-1]+")) {
                Long num = Long.valueOf(valStr.substring(2), 2);
                decimal = new BigDecimal(num);
            } else if (valStr.matches("\\d+(\\.\\d+)?")) {
                decimal = new BigDecimal(valStr);
            }

            if (decimal != null) {
                for (Map.Entry<Class<?>, Function<BigDecimal, ?>> entry : bigDecimalTypeConverterMap.entrySet()) {
                    Class<?> itemClass = entry.getKey();
                    if (TypeOf.typeOf(itemClass, targetType)) {
                        return entry.getValue().apply(decimal);
                    }
                }
            }
        }

        // 字符串字面值
        if (TypeOf.typeOfAny(targetType, boolTypes)) {
            String str = valStr.toLowerCase();
            if ("true".equals(str)) {
                return true;
            }
            if ("false".equals(str)) {
                return false;
            }
            if ("1".equals(str)) {
                return true;
            }
            if ("0".equals(str)) {
                return false;
            }
        }

        // 字符串字面值
        if (TypeOf.typeOfAny(targetType, charTypes)) {
            if (valStr.length() == 1) {
                char ch = valStr.charAt(0);
                return ch;
            }
        }

        // 字符串字面值
        if (TypeOf.typeOfAny(targetType, dateTypes)) {
            Date date = tryParseDate(valStr);
            if (date != null) {
                Instant ins = date2InstantConverterMap.get(Date.class).apply(date);
                return dateTypeConverterMap.get(targetType).apply(ins);
            }
        }

        return val;
    }

    public static Date tryParseDate(String valStr) {

        Date date = null;
        for (String format : dateFormats) {
            try {
                SimpleDateFormat fmt = new SimpleDateFormat(format);
                date = fmt.parse(valStr);
                if (date != null) {
                    break;
                }
            } catch (Exception e) {

            }
        }
        return date;
    }

    public static LocalDate tryParseLocalDate(String valStr) {

        LocalDate date = null;
        for (String format : dateFormats) {
            try {
                date = LocalDate.parse(valStr, dateFormaterMap.get(format));
                if (date != null) {
                    break;
                }
            } catch (Exception e) {

            }
        }
        return date;
    }

    public static LocalDateTime tryParseLocalDateTime(String valStr) {

        LocalDateTime date = null;
        for (String format : dateFormats) {
            try {
                date = LocalDateTime.parse(valStr, dateFormaterMap.get(format));
                if (date != null) {
                    break;
                }
            } catch (Exception e) {

            }
        }
        return date;
    }

    public static LocalTime tryParseLocalTime(String valStr) {

        LocalTime date = null;
        for (String format : dateFormats) {
            try {
                date = LocalTime.parse(valStr, dateFormaterMap.get(format));
                if (date != null) {
                    break;
                }
            } catch (Exception e) {

            }
        }
        return date;
    }
}
