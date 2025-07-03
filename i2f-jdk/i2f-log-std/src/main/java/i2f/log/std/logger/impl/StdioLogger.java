package i2f.log.std.logger.impl;

import i2f.console.color.ConsoleElement;
import i2f.console.color.ConsoleOutput;
import i2f.console.color.impl.ConsoleColor;
import i2f.log.std.data.LogData;
import i2f.log.std.enums.LogLevel;
import i2f.log.std.logger.AbsLogger;
import i2f.log.std.util.LogUtil;
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
 * @date 2025/7/3 15:36
 */
@Data
@NoArgsConstructor
public class StdioLogger extends AbsLogger {
    protected String location;

    public StdioLogger(String location) {
        this.location = location;
    }

    protected ThreadLocal<SimpleDateFormat> dateFormat = ThreadLocal.withInitial(() -> new SimpleDateFormat("MM-dd HH:mm:ss.SSS"));
    protected int locationLen = 32;
    protected int threadLen = 12;

    protected LogLevel enableLevel = LogLevel.ALL;
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

    @Override
    public boolean enableLevel(LogLevel level) {
        return level.level() <= enableLevel.level();
    }

    @Override
    public void writeLogData(LogData data) {
        String str = format(data, true);
        if (data.getLevel().level() < LogLevel.INFO.level()) {
            System.err.println(str);
        } else {
            System.out.println(str);
        }
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

    public String format(LogData data, boolean stdout) {
        StringBuilder builder = new StringBuilder();
        builder.append(dateFormat.get().format(data.getDate()));
        builder.append(wrapStdoutText(String.format(" [%-5s]", String.valueOf(data.getLevel())), stdout, logLevelColorMap.get(data.getLevel())));
        builder.append(wrapStdoutText(String.format(" [%-" + locationLen + "s]", LogUtil.truncateLocation(data.getLocation(), locationLen)), stdout, ConsoleColor.CYAN));
        builder.append(wrapStdoutText(String.format(" [%-" + threadLen + "s]", LogUtil.truncateString(String.format("%s-%d", data.getThreadName(), data.getThreadId()), threadLen)), stdout, ConsoleColor.MAGENTA));
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


}
