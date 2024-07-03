package i2f.properties.test;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/7/3 9:49
 * @desc
 */
@Data
@NoArgsConstructor
public class TestBeanProperties {
    protected StdoutRedirectProperties stdoutRedirect;
    protected StdoutWriterProperties stdoutWriter;
    protected FileWriterProperties fileWriter;
    protected BroadcastWriterProperties broadcastWriter;
    protected LoggingLevelProperties loggingLevel;

    @Data
    @NoArgsConstructor
    public static class StdoutRedirectProperties {
        protected boolean enable;
        protected boolean keepConsole;
        protected boolean useTrace;
    }

    @Data
    @NoArgsConstructor
    public static class StdoutWriterProperties {
        protected boolean enable;
    }

    @Data
    @NoArgsConstructor
    public static class FileWriterProperties {
        protected boolean enable;
        protected File filePath;
        protected String applicationName;
        protected TestLevel limitLevel;
        protected long fileLimitSizeMb;
        protected long fileLimitTotalSizeMb;
        protected int fileSizeCheckCount;

    }

    @Data
    @NoArgsConstructor
    public static class LogWriterItemProperties {
        protected boolean enable;
        protected String name;
        protected Class<?> className;
        /**
         * 具体用法，查看此字段注释
         * LogConfiguration.CONFIG_WRITER_SET_PARAMS_METHOD_NAME
         */
        protected String params;
    }

    @Data
    @NoArgsConstructor
    public static class BroadcastWriterProperties {
        protected boolean enable;
        protected boolean async;
        protected int parallelism;
        protected List<LogWriterItemProperties> items;
    }

    @Data
    @NoArgsConstructor
    public static class LoggingLevelItemProperties {
        protected boolean enable;
        protected String patten;
        protected TestLevel level;
    }

    @Data
    @NoArgsConstructor
    public static class LoggingLevelProperties {
        protected boolean enable;
        protected TestLevel rootLevel;
        protected List<LoggingLevelItemProperties> items;
    }
}
