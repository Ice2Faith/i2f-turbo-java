package i2f.log.writer.impl;

import i2f.clock.SystemClock;
import i2f.log.enums.LogLevel;
import i2f.log.writer.AbsPlainTextLogWriter;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
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
    public static final String DEFAULT_NAME = "FILE";
    public static final String PROPERTY_APPLICATION = "logging.application";
    private LogLevel limitLevel = LogLevel.INFO;
    private volatile long fileLimitSize = 200 * 1024 * 1024;
    private volatile long fileLimitTotalSize = 5 * 200 * 1024 * 1024;
    private volatile int fileSizeCheckCount = 100;
    private String filePath = "./logs";
    private String applicationName;
    private volatile File logFile;
    private volatile PrintStream ps;
    private AtomicInteger counter = new AtomicInteger(0);

    private AtomicBoolean initialed = new AtomicBoolean(false);

    public void setParams(String params) {
        String[] pairs = params.split("&");
        Map<String, String> map = new LinkedHashMap<>();
        for (String pair : pairs) {
            if (pair.isEmpty()) {
                continue;
            }
            String[] arr = pair.split("=", 2);
            if (arr.length != 2) {
                continue;
            }
            try {
                String name = arr[0].trim();
                if (name.isEmpty()) {
                    continue;
                }
                String value = arr[1].trim();
                value = URLDecoder.decode(value, "UTF-8");
                map.put(name, value);
            } catch (Exception e) {
            }
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String name = entry.getKey().replaceAll("-|_", "");
            String value = entry.getValue();
            try {
                if ("limitLevel".equalsIgnoreCase(name)) {
                    this.limitLevel = LogLevel.parse(value);
                } else if ("filePath".equalsIgnoreCase(name)) {
                    this.filePath = value;
                } else if ("applicationName".equalsIgnoreCase(name)) {
                    this.applicationName = value;
                } else if ("fileLimitSize".equalsIgnoreCase(name)) {
                    this.fileLimitSize = Long.parseLong(value);
                } else if ("fileLimitTotalSize".equalsIgnoreCase(name)) {
                    this.fileLimitTotalSize = Long.parseLong(value);
                } else if ("fileLimitSizeMb".equalsIgnoreCase(name)) {
                    this.fileLimitSize = Long.parseLong(value) * 1024 * 1024;
                } else if ("fileLimitTotalSizeMb".equalsIgnoreCase(name)) {
                    this.fileLimitTotalSize = Long.parseLong(value) * 1024 * 1024;
                } else if ("fileSizeCheckCount".equalsIgnoreCase(name)) {
                    this.fileSizeCheckCount = Integer.parseInt(value);
                }
            } catch (Exception e) {

            }
        }

    }

    public void init() {
        if (initialed.getAndSet(true)) {
            return;
        }
        if (fileLimitSize <= 0) {
            fileLimitSize = 200 * 1024 * 1024;
        }
        if (fileLimitTotalSize <= 0) {
            fileLimitTotalSize = 5 * 200 * 1024 * 1024;
        }
        if (fileSizeCheckCount <= 0) {
            fileSizeCheckCount = 100;
        }
        if (filePath == null || filePath.isEmpty()) {
            filePath = "./logs";
        }
        if (applicationName == null || applicationName.isEmpty()) {
            applicationName = System.getProperty(PROPERTY_APPLICATION);
        }
        if (applicationName == null || applicationName.isEmpty()) {
            applicationName = "noappname";
        }
        if (logFile == null) {
            logFile = new File(filePath, applicationName + ".log");
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

                    File[] files = logFile.getParentFile().listFiles();
                    List<File> list = new ArrayList<>();
                    for (File file : files) {
                        if (!file.isFile()) {
                            continue;
                        }
                        String fileName = file.getName();
                        if (!fileName.endsWith(".log")) {
                            continue;
                        }
                        if (!fileName.matches(".+-\\d{8}-\\d{6}-\\d{3}\\.log")) {
                            continue;
                        }
                        String appName = fileName.substring(0, fileName.length() - (1 + 8 + 1 + 6 + 1 + 3 + 1 + 3));
                        if (applicationName.equals(appName)) {
                            list.add(file);
                        }
                    }

                    list.sort((f1, f2) -> f2.getName().compareTo(f1.getName()));

                    long sumSize = 0;
                    boolean deleteFlag = false;
                    for (File file : list) {
                        if (deleteFlag) {
                            file.delete();
                            continue;
                        }
                        sumSize += file.length();
                        if (sumSize >= fileLimitTotalSize) {
                            deleteFlag = true;
                        }
                    }
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
