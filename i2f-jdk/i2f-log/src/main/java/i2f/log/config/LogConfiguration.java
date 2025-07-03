package i2f.log.config;

import i2f.log.decide.ILogDecider;
import i2f.log.decide.impl.DefaultClassNamePattenLogDecider;
import i2f.log.holder.LogHolder;
import i2f.log.std.enums.LogLevel;
import i2f.log.stdout.StdoutRedirectPrintStream;
import i2f.log.writer.DefaultBroadcastLogWriter;
import i2f.log.writer.ILogWriter;
import i2f.log.writer.impl.LocalFilePlanTextLogWriter;
import i2f.log.writer.impl.StdoutPlanTextLogWriter;
import i2f.reflect.ReflectResolver;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/7/3 9:47
 * @desc
 */
public class LogConfiguration {
    /**
     * 在配置中指定的writer注册之后，允许接受一个字符串参数params
     * 配置中指定的参数，将会在实例化writer之后，调用下面的成员方法进行设置params
     * public void setParams(String params);
     */
    public static final String CONFIG_WRITER_SET_PARAMS_METHOD_NAME = "setParams";

    /**
     * 默认配置文件的路径，会同时查找classpath与当前路径下的这些文件
     * 先找到任意一个结束
     */
    public static final String[] LOG_CONFIG_FILES = {
            "log.properties",
            "resources/log.properties",
            "config/log.properties",
            "conf/log.properties"
    };

    public static void config() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = null;
        for (int i = 0; i < LOG_CONFIG_FILES.length; i++) {
            String name = LOG_CONFIG_FILES[i];
            try {
                url = loader.getResource(name);
            } catch (Exception e) {

            }
            if (url != null) {
                break;
            }
            try {
                File file = new File(".", name);
                if (file.exists() && file.isFile()) {
                    url = file.toURI().toURL();
                }
            } catch (Exception e) {

            }
        }
        if (url == null) {
            return;
        }
        try {
            LogProperties properties = PropertiesFileLogPropertiesLoader.load(url);
            config(properties);
        } catch (Exception e) {

        }
    }

    public static void config(LogProperties properties) {
        configStdoutRedirect(properties.getStdoutRedirect());
        configStdoutWriter(properties.getStdoutWriter());
        configFileWriter(properties.getFileWriter());
        configBroadcastWriter(properties.getBroadcastWriter());
        configLoggingLevel(properties.getLoggingLevel());
    }

    public static void configStdoutRedirect(LogProperties.StdoutRedirectProperties prop) {
        if (prop == null) {
            return;
        }
        if (!prop.isEnable()) {
            return;
        }
        StdoutRedirectPrintStream.redirectSysoutSyserr(prop.isKeepConsole(), prop.isUseTrace());
    }

    public static void configStdoutWriter(LogProperties.StdoutWriterProperties prop) {
        if (prop == null) {
            return;
        }
        if (prop.isEnable()) {
            LogHolder.registryWriter(StdoutPlanTextLogWriter.WRITER_NAME, new DefaultBroadcastLogWriter());
        } else {
            LogHolder.removeWriter(StdoutPlanTextLogWriter.WRITER_NAME);
        }
    }

    public static void configFileWriter(LogProperties.FileWriterProperties prop) {
        if (prop == null) {
            return;
        }
        if (!prop.isEnable()) {
            return;
        }
        LocalFilePlanTextLogWriter writer = new LocalFilePlanTextLogWriter();
        writer.setFilePath(prop.getFilePath());
        writer.setApplicationName(prop.getApplicationName());
        writer.setLimitLevel(LogLevel.parse(prop.getLimitLevel()));
        writer.setFileLimitSize(prop.getFileLimitSizeMb() * 1024 * 1024);
        writer.setFileLimitTotalSize(prop.getFileLimitTotalSizeMb() * 1024 * 1024);
        writer.setFileSizeCheckCount(prop.getFileSizeCheckCount());
        LogHolder.registryWriter(LocalFilePlanTextLogWriter.DEFAULT_NAME, writer);
    }

    public static void configBroadcastWriter(LogProperties.BroadcastWriterProperties prop) {
        if (prop == null) {
            return;
        }
        if (!prop.isEnable()) {
            return;
        }
        ILogWriter writer = LogHolder.GLOBAL_WRITER;
        if (!(writer instanceof DefaultBroadcastLogWriter)) {
            return;
        }
        DefaultBroadcastLogWriter broadcastLogWriter = (DefaultBroadcastLogWriter) writer;
        broadcastLogWriter.adjustAsync(prop.isAsync());
        broadcastLogWriter.adjustPoolSize(prop.getParallelism());

        configBroadcastWriterItems(prop.getItems());

    }

    public static void configBroadcastWriterItems(List<LogProperties.LogWriterItemProperties> items) {
        if (items == null) {
            return;
        }
        for (LogProperties.LogWriterItemProperties item : items) {
            if (item == null) {
                continue;
            }
            if (!item.isEnable()) {
                continue;
            }
            String name = item.getName();
            if (name == null) {
                continue;
            }
            name = name.trim();
            if (name.isEmpty()) {
                continue;
            }
            String className = item.getClassName();
            if (className == null) {
                continue;
            }
            className = className.trim();
            if (className.isEmpty()) {
                continue;
            }
            try {
                Class<?> clazz = ReflectResolver.loadClass(className);
                if (clazz == null) {
                    continue;
                }
                Object value = ReflectResolver.getInstance(clazz);
                if (!(value instanceof ILogWriter)) {
                    continue;
                }

                ReflectResolver.invokeSingletonMethod(value, CONFIG_WRITER_SET_PARAMS_METHOD_NAME, item.getParams());
                ILogWriter logWriter = (ILogWriter) value;
                LogHolder.registryWriter(name, logWriter);
            } catch (Exception e) {

            }
        }
    }

    public static void configLoggingLevel(LogProperties.LoggingLevelProperties prop) {
        if (prop == null) {
            return;
        }
        if (!prop.isEnable()) {
            return;
        }
        ILogDecider decider = LogHolder.GLOBAL_DECIDER;
        if (!(decider instanceof DefaultClassNamePattenLogDecider)) {
            return;
        }
        DefaultClassNamePattenLogDecider pattenLogDecider = (DefaultClassNamePattenLogDecider) decider;
        pattenLogDecider.setRootLevel(LogLevel.parse(prop.getRootLevel()));
        configLoggingLevelItems(prop.getItems());
    }

    public static void configLoggingLevelItems(List<LogProperties.LoggingLevelItemProperties> items) {
        if (items == null) {
            return;
        }
        for (LogProperties.LoggingLevelItemProperties item : items) {
            if (item == null) {
                continue;
            }
            if (!item.isEnable()) {
                return;
            }
            String patten = item.getPatten();
            if (patten == null) {
                continue;
            }
            patten = patten.trim();
            if (patten.isEmpty()) {
                continue;
            }
            LogHolder.registryDecideLevel(patten, LogLevel.parse(item.getLevel()));
        }
    }
}
