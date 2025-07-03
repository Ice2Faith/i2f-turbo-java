package i2f.log.config;

import i2f.log.std.enums.LogLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/7/3 9:49
 * @desc
 */
@Data
@NoArgsConstructor
public class LogProperties {
    protected StdoutRedirectProperties stdoutRedirect = new StdoutRedirectProperties();
    protected StdoutWriterProperties stdoutWriter = new StdoutWriterProperties();
    protected FileWriterProperties fileWriter = new FileWriterProperties();
    protected BroadcastWriterProperties broadcastWriter = new BroadcastWriterProperties();
    protected LoggingLevelProperties loggingLevel = new LoggingLevelProperties();

    @Data
    @NoArgsConstructor
    public static class StdoutRedirectProperties {
        protected boolean enable = true;
        protected boolean keepConsole = false;
        protected boolean useTrace = true;
    }

    @Data
    @NoArgsConstructor
    public static class StdoutWriterProperties {
        protected boolean enable = true;
    }

    @Data
    @NoArgsConstructor
    public static class FileWriterProperties {
        protected boolean enable = true;
        protected String filePath = "./logs";
        protected String applicationName;
        protected String limitLevel = LogLevel.INFO.name();
        protected long fileLimitSizeMb = 200;
        protected long fileLimitTotalSizeMb = 5 * 200;
        protected int fileSizeCheckCount = 100;

    }

    @Data
    @NoArgsConstructor
    public static class LogWriterItemProperties {
        protected boolean enable = true;
        protected String name;
        protected String className;
        /**
         * 具体用法，查看此字段注释
         * LogConfiguration.CONFIG_WRITER_SET_PARAMS_METHOD_NAME
         */
        protected String params;
    }

    @Data
    @NoArgsConstructor
    public static class BroadcastWriterProperties {
        protected boolean enable = true;
        protected boolean async = true;
        protected int parallelism = -1;
        protected List<LogWriterItemProperties> items = new ArrayList<>();
    }

    @Data
    @NoArgsConstructor
    public static class LoggingLevelItemProperties {
        protected boolean enable = true;
        protected String patten;
        protected String level = LogLevel.INFO.name();
    }

    @Data
    @NoArgsConstructor
    public static class LoggingLevelProperties {
        protected boolean enable = true;
        protected String rootLevel = LogLevel.INFO.name();
        protected List<LoggingLevelItemProperties> items = new ArrayList<>();
    }
}
