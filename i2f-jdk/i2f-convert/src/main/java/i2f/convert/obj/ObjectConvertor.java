package i2f.convert.obj;

import i2f.typeof.TypeOf;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Clob;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.concurrent.*;
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
    public static final Map<String, SimpleDateFormat> simpleFormatMap;
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
            Map<String, SimpleDateFormat> formatMap = new ConcurrentHashMap<>();
            Map<String, DateTimeFormatter> formatterMap = new ConcurrentHashMap<>();
            for (String dateFormat : dateFormats) {
                formatMap.put(dateFormat, new SimpleDateFormat(dateFormat));
                formatterMap.put(dateFormat, DateTimeFormatter.ofPattern(dateFormat));
            }
            dateFormaterMap = formatterMap;
            simpleFormatMap = formatMap;
        }
    }

    public static boolean isNumericType(Class<?> clazz) {
        for (Class<?> item : bigDecimalTypeConverterMap.keySet()) {
            if (TypeOf.typeOf(clazz, item)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBooleanType(Class<?> clazz) {
        for (Class<?> item : boolTypeConverterMap.keySet()) {
            if (TypeOf.typeOf(clazz, item)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isCharType(Class<?> clazz) {
        for (Class<?> item : charTypeConverterMap.keySet()) {
            if (TypeOf.typeOf(clazz, item)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDateType(Class<?> clazz) {
        for (Class<?> item : dateTypeConverterMap.keySet()) {
            if (TypeOf.typeOf(clazz, item)) {
                return true;
            }
        }
        return false;
    }

    public static String stringifyReader(Reader reader, String nullAs) throws IOException {
        if (reader == null) {
            return nullAs;
        }
        try {
            StringBuilder builder = new StringBuilder();
            char[] buff = new char[4096];
            int len = 0;
            while ((len = reader.read(buff)) > 0) {
                builder.append(buff, 0, len);
            }
            return builder.toString();
        } finally {
            reader.close();
        }
    }

    public static String stringify(Object obj, String nullAs) {
        if (obj == null) {
            return nullAs;
        }
        if (obj instanceof byte[]) {
            obj = new ByteArrayInputStream((byte[]) obj);
        } else if (obj instanceof char[]) {
            obj = new CharArrayReader((char[]) obj);
        }
        Class<?> clazz = obj.getClass();
        if (TypeOf.typeOf(clazz, Clob.class)) {
            Clob clob = (Clob) obj;
            try (Reader reader = clob.getCharacterStream()) {
                return stringifyReader(reader, nullAs);
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        } else if (TypeOf.typeOf(clazz, Reader.class)) {
            Reader reader = (Reader) obj;
            try {
                return stringifyReader(reader, nullAs);
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        } else if (TypeOf.typeOf(clazz, InputStream.class)) {
            InputStream is = (InputStream) obj;
            Reader reader = new InputStreamReader(is);
            try {
                return stringifyReader(reader, nullAs);
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        } else if (clazz.isArray()) {
            StringBuilder builder = new StringBuilder();
            int len = Array.getLength(obj);
            builder.append("[");
            for (int i = 0; i < len; i++) {
                if (i > 0) {
                    builder.append(", ");
                }
                String str = stringify(Array.get(obj, i), nullAs);
                builder.append(str);
            }
            builder.append("]");
            return builder.toString();
        } else {
            return String.valueOf(obj);
        }
    }

    public static Object tryConvertAsType(Object val, Class<?> targetType) {
        if (val == null) {
            if (TypeOf.typeOfAny(targetType, Boolean.class, boolean.class)) {
                return false;
            }
            return val;
        }

        Class<?> sourceType = val.getClass();
        // 类型匹配
        if (TypeOf.typeOf(sourceType, targetType)) {
            return val;
        }
        // 目标类型为 String ，都能转
        if (TypeOf.typeOfAny(targetType, String.class, CharSequence.class, Appendable.class, char[].class, Reader.class, InputStream.class, byte[].class)) {
            String str = stringify(val, null);
            if (str == null) {
                return null;
            }
            if (TypeOf.typeOf(targetType, char[].class)) {
                return str.toCharArray();
            } else if (TypeOf.typeOf(targetType, Appendable.class)) {
                if (TypeOf.typeOf(targetType, StringBuilder.class)) {
                    StringBuilder builder = new StringBuilder(str);
                    return builder;
                } else if (TypeOf.typeOf(targetType, StringBuffer.class)) {
                    StringBuffer buffer = new StringBuffer(str);
                    return buffer;
                }
            } else if (TypeOf.typeOfAny(targetType, Reader.class)) {
                return new StringReader(str);
            } else if (TypeOf.typeOfAny(targetType, InputStream.class)) {
                return new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
            } else if (TypeOf.typeOfAny(targetType, byte[].class)) {
                return str.getBytes(StandardCharsets.UTF_8);
            } else {
                return str;
            }
        }

        // 源类型为String，目标类型为可转类型
        if (TypeOf.typeOfAny(sourceType, String.class, CharSequence.class, Appendable.class, char[].class, Reader.class, InputStream.class, byte[].class)) {
            String str = stringify(val, null);

            if (TypeOf.typeOf(targetType, char[].class)) {
                return str.toCharArray();
            } else if (TypeOf.typeOf(targetType, Appendable.class)) {
                if (TypeOf.typeOf(targetType, StringBuilder.class)) {
                    StringBuilder builder = new StringBuilder(str);
                    return builder;
                } else if (TypeOf.typeOf(targetType, StringBuffer.class)) {
                    StringBuffer buffer = new StringBuffer(str);
                    return buffer;
                }
            } else if (TypeOf.typeOfAny(targetType, Reader.class)) {
                return new StringReader(str);
            } else if (TypeOf.typeOfAny(targetType, InputStream.class)) {
                return new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
            } else if (TypeOf.typeOfAny(targetType, byte[].class)) {
                return str.getBytes(StandardCharsets.UTF_8);
            }
        }


        // 原始和目标都是 Number
        Class<?>[] numericTypes = bigDecimalTypeConverterMap.keySet().toArray(new Class<?>[0]);
        if (TypeOf.typeOfAny(sourceType, numericTypes)
                &&
                TypeOf.typeOfAny(targetType, numericTypes)) {
            BigDecimal decimal = (val instanceof BigDecimal) ? ((BigDecimal) val) : new BigDecimal(String.valueOf(val));
            Function<BigDecimal, ?> func = bigDecimalTypeConverterMap.get(targetType);
            if (func == null) {
                for (Map.Entry<Class<?>, Function<BigDecimal, ?>> entry : bigDecimalTypeConverterMap.entrySet()) {
                    Class<?> itemClass = entry.getKey();
                    if (TypeOf.typeOf(itemClass, targetType)) {
                        func = entry.getValue();
                        break;
                    }
                }
            }
            if (func != null) {
                return func.apply(decimal);
            }

        }

        // 原始和目标都是 Boolean
        Class<?>[] boolTypes = boolTypeConverterMap.keySet().toArray(new Class<?>[0]);
        if (TypeOf.typeOfAny(sourceType, boolTypes)
                &&
                TypeOf.typeOfAny(targetType, boolTypes)) {
            boolean ok = (val == null) ? false : (Boolean) val;
            Function<Boolean, ?> func = boolTypeConverterMap.get(targetType);
            if (func == null) {
                for (Map.Entry<Class<?>, Function<Boolean, ?>> entry : boolTypeConverterMap.entrySet()) {
                    Class<?> itemClass = entry.getKey();
                    if (TypeOf.typeOf(itemClass, targetType)) {
                        func = entry.getValue();
                        break;
                    }
                }
            }
            if (func != null) {
                return func.apply(ok);
            }
        }

        // 原始和目标都是 Char
        Class<?>[] charTypes = charTypeConverterMap.keySet().toArray(new Class<?>[0]);
        if (TypeOf.typeOfAny(sourceType, charTypes)
                &&
                TypeOf.typeOfAny(targetType, charTypes)) {
            char ch = (val == null) ? 0 : (Character) val;
            Function<Character, ?> func = charTypeConverterMap.get(targetType);
            if (func == null) {
                for (Map.Entry<Class<?>, Function<Character, ?>> entry : charTypeConverterMap.entrySet()) {
                    Class<?> itemClass = entry.getKey();
                    if (TypeOf.typeOf(itemClass, targetType)) {
                        func = entry.getValue();
                        break;
                    }
                }
            }
            if (func != null) {
                return func.apply(ch);
            }
        }


        // 日期时间类型的互转
        Class<?>[] dateTypes = dateTypeConverterMap.keySet().toArray(new Class<?>[0]);
        if (TypeOf.typeOfAny(sourceType, dateTypes)
                &&
                TypeOf.typeOfAny(targetType, dateTypes)) {
            Instant ins = null;
            Function<Object, Instant> sourceFunc = date2InstantConverterMap.get(sourceType);
            if (sourceFunc == null) {
                for (Map.Entry<Class<?>, Function<Object, Instant>> entry : date2InstantConverterMap.entrySet()) {
                    Class<?> itemClass = entry.getKey();
                    if (TypeOf.typeOf(itemClass, sourceType)) {
                        sourceFunc = entry.getValue();
                        break;
                    }
                }
            }
            if (sourceFunc != null) {
                ins = sourceFunc.apply(val);
            }

            if (ins != null) {
                Function<Instant, ?> func = dateTypeConverterMap.get(targetType);
                if (func == null) {
                    for (Map.Entry<Class<?>, Function<Instant, ?>> entry : dateTypeConverterMap.entrySet()) {
                        Class<?> itemClass = entry.getKey();
                        if (TypeOf.typeOf(itemClass, targetType)) {
                            func = entry.getValue();
                            break;
                        }
                    }
                }
                if (func != null) {
                    return func.apply(ins);
                }
            }

        }

        // 字符串字面值处理
        String valStr = stringify(val, null);
        if (TypeOf.typeOfAny(targetType, numericTypes)) {
            BigDecimal decimal = null;
            if (valStr.matches("(\\+|\\-)?([1-9]([0-9]+)?|0)")) {
                decimal = new BigDecimal(String.valueOf(val));
            } else if (valStr.matches("(0x|0X)[a-fA-F0-9]+")) {
                Long num = Long.valueOf(valStr.substring(2), 16);
                decimal = new BigDecimal(num);
            } else if (valStr.matches("0([0-9]+)?")) {
                Long num = Long.valueOf(valStr.substring(1), 8);
                decimal = new BigDecimal(String.valueOf(num));
            } else if (valStr.matches("(0b|0B)[0-1]+")) {
                Long num = Long.valueOf(valStr.substring(2), 2);
                decimal = new BigDecimal(num);
            } else if (valStr.matches("(\\+|\\-)?\\d+(\\.\\d+)?")) {
                decimal = new BigDecimal(valStr);
            }

            if (decimal != null) {
                Function<BigDecimal, ?> func = bigDecimalTypeConverterMap.get(targetType);
                if (func == null) {
                    for (Map.Entry<Class<?>, Function<BigDecimal, ?>> entry : bigDecimalTypeConverterMap.entrySet()) {
                        Class<?> itemClass = entry.getKey();
                        if (TypeOf.typeOf(itemClass, targetType)) {
                            func = entry.getValue();
                            break;
                        }
                    }
                }
                if (func != null) {
                    return func.apply(decimal);
                }
            }
        }

        // boolean 的宽泛转换
        if (TypeOf.typeOfAny(targetType, boolTypes)) {
            return toBoolean(val);
        }

        // 字符串字面值
        if (TypeOf.typeOfAny(targetType, boolTypes)) {
            Boolean bl = tryParseBoolean(valStr);
            if (bl != null) {
                return bl;
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

        // 其他特殊目标类型的处理
        // 处理URL
        if (TypeOf.typeOf(targetType, URL.class)) {
            if (TypeOf.typeOf(sourceType, File.class)) {
                try {
                    File file = (File) val;
                    return file.toURI().toURL();
                } catch (Exception e) {

                }
            }
            try {
                return new URL(valStr);
            } catch (Exception e) {

            }
        }
        // 处理URI
        if (TypeOf.typeOf(targetType, URI.class)) {
            if (TypeOf.typeOf(sourceType, File.class)) {
                try {
                    File file = (File) val;
                    return file.toURI();
                } catch (Exception e) {

                }
            }
            try {
                return new URI(valStr);
            } catch (Exception e) {

            }
        }
        // 处理Charset
        if (TypeOf.typeOf(targetType, Charset.class)) {
            try {
                return Charset.forName(valStr);
            } catch (Exception e) {

            }
        }
        // 处理File
        if (TypeOf.typeOf(targetType, File.class)) {
            if (TypeOf.typeOf(sourceType, URL.class)) {
                URL url = (URL) val;
                return new File(url.getFile());
            }
            if (TypeOf.typeOf(sourceType, URI.class)) {
                URI uri = (URI) val;
                return new File(uri.getPath());
            }
            return new File(valStr);
        }
        // 处理目标Enum
        if (targetType.isEnum()) {
            Object[] enums = targetType.getEnumConstants();
            if (enums != null) {
                boolean isIntVal = TypeOf.isBigIntegerCompatibleType(sourceType);
                int intVal = -1;
                if (isIntVal) {
                    intVal = new BigDecimal(valStr).intValue();
                }
                for (Object item : enums) {
                    Enum<?> enu = (Enum<?>) item;
                    String name = enu.name();
                    int ordinal = enu.ordinal();
                    if (isIntVal) {
                        if (ordinal == intVal) {
                            return enu;
                        }
                    } else {
                        if (name.equalsIgnoreCase(valStr)) {
                            return enu;
                        }
                    }
                }
            }
        }
        // 处理源Enum
        if (sourceType.isEnum()) {
            Enum<?> enu = (Enum<?>) val;
            String name = enu.name();
            int ordinal = enu.ordinal();
            if (TypeOf.isBigDecimalCompatibleType(targetType)) {
                return tryConvertAsType(ordinal, targetType);
            } else if (TypeOf.typeOf(targetType, String.class)) {
                return name;
            }
        }

        // 处理目标位Class
        if (TypeOf.typeOf(targetType, Class.class)) {
            try {
                Class<?> clazz = Class.forName(valStr);
                if (clazz != null) {
                    return clazz;
                }
            } catch (Throwable e) {
            }
            try {
                Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(valStr);
                if (clazz != null) {
                    return clazz;
                }
            } catch (Throwable e) {
            }
        }

        // 处理MessageDigest
        if (TypeOf.typeOf(targetType, MessageDigest.class)) {
            try {
                MessageDigest md = MessageDigest.getInstance(valStr);
                if (md != null) {
                    return md;
                }
            } catch (Exception e) {

            }
        }

        // 处理MessageDigest
        if (TypeOf.typeOf(targetType, Mac.class)) {
            try {
                Mac md = Mac.getInstance(valStr);
                if (md != null) {
                    return md;
                }
            } catch (Exception e) {

            }
        }

        // 处理Cipher
        if (TypeOf.typeOf(targetType, Cipher.class)) {
            try {
                Cipher md = Cipher.getInstance(valStr);
                if (md != null) {
                    return md;
                }
            } catch (Exception e) {

            }
        }

        // 处理InetAddress
        if (TypeOf.typeOf(targetType, Inet4Address.class)) {
            try {
                InetAddress inet = Inet4Address.getByName(valStr);
                return inet;
            } catch (Exception e) {

            }
        }
        if (TypeOf.typeOf(targetType, Inet6Address.class)) {
            try {
                InetAddress inet = Inet6Address.getByName(valStr);
                return inet;
            } catch (Exception e) {

            }
        }

        // 处理Locale
        if (TypeOf.typeOf(targetType, Locale.class)) {
            try {
                Locale loc = new Locale(valStr);
                return loc;
            } catch (Exception e) {

            }
        }

        // 处理Map
        if (TypeOf.typeOf(targetType, Map.class)) {
            if (val == null) {
                return val;
            }
            if (val instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) val;
                if (TypeOf.instanceOf(map, targetType)) {
                    return val;
                }
                Map dstMap = null;
                if (HashMap.class.equals(targetType)) {
                    dstMap = new HashMap();
                } else if (LinkedHashMap.class.equals(targetType)) {
                    dstMap = new LinkedHashMap();
                } else if (TreeMap.class.equals(targetType)) {
                    dstMap = new TreeMap();
                } else if (ConcurrentHashMap.class.equals(targetType)) {
                    dstMap = new ConcurrentHashMap();
                } else if (IdentityHashMap.class.equals(targetType)) {
                    dstMap = new IdentityHashMap();
                } else if (WeakHashMap.class.equals(targetType)) {
                    dstMap = new WeakHashMap();
                } else if (Hashtable.class.equals(targetType)) {
                    dstMap = new Hashtable();
                } else if (Properties.class.equals(targetType)) {
                    dstMap = new Properties();
                }
                if (dstMap != null) {
                    dstMap.putAll(map);
                    return dstMap;
                }
            }
        }

        // 处理Collection
        if (TypeOf.typeOf(targetType, Collection.class)) {
            if (val == null) {
                return val;
            }
            Collection col = null;
            if (val instanceof Collection) {
                col = (Collection<?>) val;
            }
            if (val.getClass().isArray()) {
                col = new ArrayList<>();
                int len = Array.getLength(val);
                for (int i = 0; i < len; i++) {
                    col.add(Array.get(val, i));
                }
            }

            if (col != null) {
                if (TypeOf.instanceOf(col, targetType)) {
                    return val;
                }
                Collection dstCol = null;
                if (ArrayList.class.equals(targetType)) {
                    dstCol = new ArrayList();
                } else if (LinkedList.class.equals(targetType)) {
                    dstCol = new LinkedList();
                } else if (Vector.class.equals(targetType)) {
                    dstCol = new Vector();
                } else if (CopyOnWriteArrayList.class.equals(targetType)) {
                    dstCol = new CopyOnWriteArrayList();
                } else if (HashSet.class.equals(targetType)) {
                    dstCol = new HashSet();
                } else if (LinkedHashSet.class.equals(targetType)) {
                    dstCol = new LinkedHashSet();
                } else if (CopyOnWriteArraySet.class.equals(targetType)) {
                    dstCol = new CopyOnWriteArraySet();
                } else if (TreeSet.class.equals(targetType)) {
                    dstCol = new TreeSet();
                } else if (Stack.class.equals(targetType)) {
                    dstCol = new Stack();
                } else if (PriorityQueue.class.equals(targetType)) {
                    dstCol = new PriorityQueue();
                } else if (LinkedBlockingQueue.class.equals(targetType)) {
                    dstCol = new LinkedBlockingQueue();
                } else if (ConcurrentLinkedQueue.class.equals(targetType)) {
                    dstCol = new ConcurrentLinkedQueue();
                }

                if (dstCol != null) {
                    dstCol.addAll(col);
                    return dstCol;
                }
            }
        }

        // 处理数组
        if (targetType.isArray()) {
            if (val == null) {
                return val;
            }
            Class<?> elemType = targetType.getComponentType();
            List list = null;
            if (val instanceof Collection) {
                if (list == null) {
                    list = new ArrayList();
                }
                list.addAll((Collection<?>) val);
            }
            if (val.getClass().isArray()) {
                if (list == null) {
                    list = new ArrayList();
                }
                int len = Array.getLength(val);
                for (int i = 0; i < len; i++) {
                    list.add(Array.get(val, i));
                }
            }
            if (list != null) {
                Object ret = Array.newInstance(elemType, list.size());
                int idx = 0;
                for (Object item : list) {
                    Array.set(ret, idx, item);
                    idx++;
                }
                return ret;
            }
        }

        Object ret = tryConvertAsTypeWithConstructor(val, targetType);
        if (ret != val) {
            return ret;
        }

        return val;
    }


    public static boolean toBoolean(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        if (obj instanceof Number) {
            BigDecimal num = new BigDecimal(String.valueOf(obj));
            return num.compareTo(BigDecimal.ZERO) != 0;
        }
        if (obj instanceof String) {
            if (!"".equals(obj)) {
                return true;
            }
        }
        if (obj instanceof Collection) {
            Collection<?> col = (Collection<?>) obj;
            if (!col.isEmpty()) {
                return true;
            }
        }
        if (obj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) obj;
            if (!map.isEmpty()) {
                return true;
            }
        }
        if (obj.getClass().isArray()) {
            if (Array.getLength(obj) > 0) {
                return true;
            }
        }
        return true;
    }

    public static Object tryConvertAsTypeWithConstructor(Object val, Class<?> targetType) {
        if (val == null) {
            return val;
        }
        Class<?> clazz = val.getClass();
        Constructor<?>[] constructors = targetType.getConstructors();
        for (Constructor<?> item : constructors) {
            int count = item.getParameterCount();
            if (count == 0) {
                continue;
            }
            Class<?> paramType = item.getParameterTypes()[0];
            if (TypeOf.typeOf(clazz, paramType)) {
                try {
                    return item.newInstance(val);
                } catch (Exception e) {
                }
            }
        }
        Method[] methods = targetType.getMethods();
        for (Method item : methods) {
            int mod = item.getModifiers();
            if (!Modifier.isPublic(mod)) {
                continue;
            }
            if (!Modifier.isStatic(mod)) {
                continue;
            }
            int count = item.getParameterCount();
            if (count != 1) {
                continue;
            }
            Class<?> returnType = item.getReturnType();
            if (!TypeOf.typeOf(returnType, targetType)) {
                continue;
            }
            Class<?> paramType = item.getParameterTypes()[0];
            if (TypeOf.typeOf(clazz, paramType)) {
                try {
                    return item.invoke(null, val);
                } catch (Exception e) {
                }
            }
        }
        return val;
    }


    public static Boolean tryParseBoolean(String valStr) {
        if (valStr == null) {
            return null;
        }
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
        if ("y".equals(str)) {
            return true;
        }
        if ("n".equals(str)) {
            return false;
        }
        if ("t".equals(str)) {
            return true;
        }
        if ("f".equals(str)) {
            return false;
        }
        return null;
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


    public static SimpleDateFormat getSimpleFormatter(String patten) {
        if (simpleFormatMap.containsKey(patten)) {
            return simpleFormatMap.get(patten);
        }
        SimpleDateFormat fmt = new SimpleDateFormat(patten);
        simpleFormatMap.put(patten, fmt);
        return simpleFormatMap.get(patten);
    }

    public static DateTimeFormatter getDateTimeFormatter(String patten) {
        if (dateFormaterMap.containsKey(patten)) {
            return dateFormaterMap.get(patten);
        }
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(patten);
        dateFormaterMap.put(patten, fmt);
        return dateFormaterMap.get(patten);
    }

    public synchronized static String formatDate(String patten, Date date) {
        return getSimpleFormatter(patten).format(date);
    }

    public synchronized static Date parseDate(String patten, String date) throws ParseException {
        return getSimpleFormatter(patten).parse(date);
    }

    public static String formatDate(String patten, TemporalAccessor date) {
        return getDateTimeFormatter(patten).format(date);
    }

    public static String formatDate(String patten, LocalDate date) {
        return date.format(getDateTimeFormatter(patten));
    }

    public static String formatDate(String patten, LocalTime date) {
        return date.format(getDateTimeFormatter(patten));
    }

    public static String formatDate(String patten, LocalDateTime date) {
        return date.format(getDateTimeFormatter(patten));
    }

    public static LocalDate parseLocalDate(String patten, String date) {
        return LocalDate.parse(date, getDateTimeFormatter(patten));
    }

    public static LocalTime parseLocalTime(String patten, String date) {
        return LocalTime.parse(date, getDateTimeFormatter(patten));
    }

    public static LocalDateTime parseLocalDateTime(String patten, String date) {
        return LocalDateTime.parse(date, getDateTimeFormatter(patten));
    }

    public static LocalDateTime parseLocalDateTime(String date) {
        Exception ex = null;
        for (String patten : dateFormats) {
            try {
                return date2LocalDateTime(parseDate(patten, date));
            } catch (Exception e) {
                ex = e;
            }
        }
        if (ex instanceof RuntimeException) {
            throw (RuntimeException) ex;
        } else {
            throw new IllegalArgumentException(ex);
        }
    }

    public static LocalDate parseLocalDate(String date) {
        return parseLocalDateTime(date).toLocalDate();
    }

    public static LocalTime parseLocalTime(String date) {
        return parseLocalDateTime(date).toLocalTime();
    }

    public static LocalDateTime date2LocalDateTime(Date val) {
        Instant instant = new Date(val.getTime()).toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    public static Date localDateTime2Date(LocalDateTime val) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = ((LocalDateTime) val).atZone(zone).toInstant();
        return Date.from(instant);
    }
}
