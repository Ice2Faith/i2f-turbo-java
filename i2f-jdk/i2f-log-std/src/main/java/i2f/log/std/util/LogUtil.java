package i2f.log.std.util;

import i2f.clock.SystemClock;
import i2f.log.std.ILogger;
import i2f.log.std.data.LogData;
import i2f.log.std.enums.LogLevel;
import i2f.log.std.mdc.LogMdcHolder;
import i2f.lru.LruMap;
import i2f.trace.ThreadTrace;

import java.lang.reflect.Array;
import java.util.Date;

/**
 * @author Ice2Faith
 * @date 2025/7/3 16:15
 */
public class LogUtil {
    public static LruMap<String, String> CACHE_LOCATION = new LruMap<>(1024);

    public static String formatMsg(String format, Object... args) {
        int placeHolderCount = 0;
        if (format != null) {
            for (int i = 0; i < format.length(); i++) {
                char ch = format.charAt(i);
                if (ch == '%') {
                    placeHolderCount++;
                }
            }
            try {
                format = String.format(format, args);
                if (placeHolderCount >= args.length) {
                    return format;
                }
            } catch (Exception e) {
            }
        }

        StringBuilder builder = new StringBuilder();
        builder.append(format);
        formatArgs(builder, placeHolderCount, -1, args);
        return builder.toString();
    }

    public static StringBuilder formatArgs(StringBuilder builder, int startIndex, int endIndex, Object... args) {
        if (args == null || args.length == 0) {
            return builder;
        }
        if (endIndex < 0) {
            endIndex = Integer.MAX_VALUE;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int i = startIndex; i < args.length && i < endIndex; i++) {
            Object item = args[i];
            builder.append((i == startIndex) ? "" : ", ")
                    .append("[").append(i).append("]")
                    .append("(").append(item == null ? "" : item.getClass().getSimpleName()).append(")");
            append(builder, item);
        }
        return builder;
    }

    public static StringBuilder append(StringBuilder builder, Object obj) {
        if (obj == null) {
            return builder.append(obj);
        }
        if (!obj.getClass().isArray()) {
            return builder.append(obj);
        }
        int len = Array.getLength(obj);
        builder.append("[");
        for (int i = 0; i < len; i++) {
            builder.append((i == 0) ? "" : ", ");
            append(builder, Array.get(obj, i));
        }
        builder.append("]");
        return builder;
    }

    public static LogData newLogData(LogLevel level, String location) {
        LogData data = new LogData();
        data.setLocation(location);
        data.setLevel(level);
        data.setDate(new Date(SystemClock.currentTimeMillis()));
        Thread thread = Thread.currentThread();
        data.setThreadName(thread.getName());
        data.setThreadId(thread.getId());
        if (level.level() >= LogLevel.DEBUG.level()) {
            StackTraceElement elem = ThreadTrace.beforeTrace(ILogger.class.getName())[0];
            data.setClassName(elem.getClassName());
            data.setMethodName(elem.getMethodName());
            data.setFileName(elem.getFileName());
            data.setLineNumber(elem.getLineNumber());
        }
        data.setTraceId(LogMdcHolder.getTraceId());
        return data;
    }


    public static String truncateString(String location, int len) {
        if (location == null) {
            return null;
        }
        if (location.isEmpty()) {
            return location;
        }
        if (location.length() < len) {
            return location;
        }
        String str = location.substring(location.length() - len);
        if (str.length() > 1) {
            str = "*" + str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static String truncateLocation(String location, int len) {
        if (location == null) {
            return null;
        }
        if (location.isEmpty()) {
            return location;
        }
        if (location.length() <= len) {
            return location;
        }
        String cacheKey = len + "#" + location;
        String cacheValue = CACHE_LOCATION.get(cacheKey);
        if (cacheValue != null) {
            return cacheValue;
        }
        String[] arr = location.split("\\.");
        int idx = arr.length - 1;
        int diffLen = 0;
        while (idx >= 0) {
            int sumLen = idx * 2 + (arr.length - idx - 1);
            for (int j = idx; j < arr.length; j++) {
                sumLen += arr[j].length();
            }
            if (sumLen >= len) {
                diffLen = sumLen - len;
                break;
            }
            idx--;
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i < idx) {
                builder.append(arr[i].substring(0, 1));
                builder.append(".");
            } else if (i == idx) {
                if (diffLen > 0 && diffLen < arr[i].length()) {
                    String str = arr[i].substring(0, arr[i].length() - diffLen);
                    if (str.length() > 1) {
                        str = str.substring(0, str.length() - 1) + "*";
                    }
                    builder.append(str);
                } else {
                    builder.append(arr[i]);
                }
                builder.append(".");
            } else {
                builder.append(arr[i]);
                builder.append(".");
            }
        }
        String str = builder.toString();
        str = str.substring(0, str.length() - 1);
        if (str.length() > len) {
            str = str.substring(str.length() - len);
        }
        CACHE_LOCATION.put(cacheKey, str);
        return str;
    }
}
