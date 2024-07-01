package i2f.log.writer.impl;

import i2f.clock.SystemClock;
import i2f.log.enums.LogLevel;
import i2f.log.writer.AbsPlainTextLogWriter;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ice2Faith
 * @date 2024/7/1 10:58
 * @desc
 */
@Data
@NoArgsConstructor
public class LocalFilePlanTextLogWriter extends AbsPlainTextLogWriter implements Closeable {
    public static final String PROPERTY_APPLICATION = "logging.application";
    private LogLevel limitLevel = LogLevel.INFO;
    private volatile long fileLimitSize = 200 * 1024 * 1024;
    private volatile int fileSizeCheckCount = 100;
    private volatile File logFile;
    private volatile PrintStream ps;
    private AtomicInteger counter = new AtomicInteger(0);

    private AtomicBoolean initialed = new AtomicBoolean(false);

    public void init() {
        if (initialed.getAndSet(true)) {
            return;
        }
        if (logFile == null) {
            String applicationName = System.getProperty(PROPERTY_APPLICATION);
            if (applicationName == null || applicationName.isEmpty()) {
                applicationName = "noappname";
            }
            logFile = new File("./logs/" + applicationName + ".log");
        }
        if (!logFile.getParentFile().exists()) {
            logFile.getParentFile().mkdirs();
        }
    }

    @Override
    public void write(LogLevel level, String text) {
        if (level.level() > limitLevel.level()) {
            return;
        }
        init();
        try {
            if (ps == null) {
                ps = new PrintStream(new FileOutputStream(logFile, true));
            }
            ps.println(text);
            ps.flush();
            int flag = counter.updateAndGet(e -> (e + 1) % fileSizeCheckCount);
            if (flag == 0) {
                if (logFile.length() >= fileLimitSize) {
                    ps.close();
                    String name = logFile.getName();
                    int idx = name.lastIndexOf(".");
                    String suffix = "";
                    if (idx >= 0) {
                        suffix = name.substring(idx);
                        name = name.substring(0, idx);
                    }
                    name = name + "-" + new SimpleDateFormat("yyyyMMdd-HHmmss-SSS").format(new Date(SystemClock.currentTimeMillis()));
                    logFile.renameTo(new File(logFile.getParentFile(), name + suffix));
                    ps = new PrintStream(new FileOutputStream(logFile, true));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        if (ps != null) {
            ps.close();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        close();
    }
}
