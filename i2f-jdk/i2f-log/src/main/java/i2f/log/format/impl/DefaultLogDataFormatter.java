package i2f.log.format.impl;

import i2f.console.color.ConsoleElement;
import i2f.console.color.ConsoleOutput;
import i2f.console.color.impl.ConsoleColor;
import i2f.log.format.ILogDataFormatter;
import i2f.log.std.data.LogData;
import i2f.log.std.enums.LogLevel;
import i2f.lru.LruMap;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/7/3 9:35
 * @desc
 */
@Data
@NoArgsConstructor
public class DefaultLogDataFormatter implements ILogDataFormatter {
    public static LruMap<String, String> CACHE_LOCATION = new LruMap<>(1024);
    protected ThreadLocal<SimpleDateFormat> dateFormat = ThreadLocal.withInitial(() -> new SimpleDateFormat("MM-dd HH:mm:ss.SSS"));
    protected int locationLen = 32;
    protected int threadLen = 12;

    public static final Map<LogLevel, ConsoleElement> logLevelColorMap;

    static {
        Map<LogLevel, ConsoleElement> map = new HashMap<>();
        map.put(LogLevel.OFF, ConsoleColor.BLACK);
        map.put(LogLevel.FATAL, ConsoleColor.BRIGHT_RED);
        map.put(LogLevel.ERROR, ConsoleColor.RED);
        map.put(LogLevel.WARN, ConsoleColor.YELLOW);
        map.put(LogLevel.INFO, ConsoleColor.GREEN);
        map.put(LogLevel.DEBUG, ConsoleColor.BRIGHT_BLACK);
        map.put(LogLevel.TRACE, ConsoleColor.BRIGHT_WHITE);
        map.put(LogLevel.ALL, ConsoleColor.DEFAULT);

        logLevelColorMap = Collections.unmodifiableMap(map);
    }

    public String wrapStdoutText(String str, boolean stdout, ConsoleElement element) {
        if (!stdout) {
            return str;
        }
        if (element == null) {
            return str;
        }
        return ConsoleOutput.toAnsiString(str, element);
    }

    @Override
    public String format(LogData data, boolean stdout) {
        StringBuilder builder = new StringBuilder();
        builder.append(dateFormat.get().format(data.getDate()));
        builder.append(wrapStdoutText(String.format(" [%-5s]", String.valueOf(data.getLevel())), stdout, logLevelColorMap.get(data.getLevel())));
        builder.append(wrapStdoutText(String.format(" [%-" + locationLen + "s]", truncateLocation(data.getLocation(), locationLen)), stdout, ConsoleColor.CYAN));
        builder.append(wrapStdoutText(String.format(" [%-" + threadLen + "s]", truncateString(String.format("%s-%d", data.getThreadName(), data.getThreadId()), threadLen)), stdout, ConsoleColor.MAGENTA));
        builder.append(" - ").append(data.getMsg());
        if (data.getMethodName() != null) {
            builder.append(wrapStdoutText(String.format(" --@%s.%s(%s:%d)", data.getClassName(), data.getMethodName(), data.getFileName(), data.getLineNumber()), stdout, ConsoleColor.BRIGHT_CYAN));
        }
        if (data.getTraceId() != null) {
            builder.append(wrapStdoutText(String.format(" --#%s", data.getTraceId()), stdout, ConsoleColor.BRIGHT_MAGENTA));
        }
        if (data.getEx() != null) {
            Throwable ex = data.getEx();
            builder.append("\nexception : ").append(ex.getMessage());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(bos);
            ex.printStackTrace(ps);
            ps.close();
            builder.append("\n")
                    .append(wrapStdoutText(new String(bos.toByteArray()), stdout, ConsoleColor.RED));
        }
        return builder.toString();
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
