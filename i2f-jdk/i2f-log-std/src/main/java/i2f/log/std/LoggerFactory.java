package i2f.log.std;

import i2f.log.std.logger.impl.StdioLogger;
import i2f.log.std.provider.LoggerProvider;
import i2f.lru.LruMap;
import i2f.trace.ThreadTrace;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2024/7/1 9:59
 * @desc
 */
public class LoggerFactory {
    public static final String LOGGER_PROVIDER_CLASS_NAME = "logger.provider.class";
    public static final String LOGGER_PROVIDER_SPI_SELECT_NAME = "logger.provider.spi.select.name";
    public static final String LOGGER_DEFAULT_CONFIG_PROPERTIES = "META-INF/log.properties";

    protected static LruMap<String, ILogger> CACHE = new LruMap<>(1024);
    protected static final AtomicBoolean hasFindProvider = new AtomicBoolean(false);
    public static volatile LoggerProvider loggerProvider = null;

    public static ILogger getLogger(String location) {
        ILogger logger = CACHE.get(location);
        if (logger != null) {
            return logger;
        }

        if (!hasFindProvider.getAndSet(true)) {
            if (loggerProvider == null) {
                loggerProvider = loadProvider();
            }
        }

        if (logger == null) {
            if (loggerProvider != null) {
                logger = loggerProvider.getLogger(location);
            }
        }

        if (logger == null) {
            logger = new StdioLogger(location);
        }
        CACHE.put(location, logger);
        return logger;
    }

    public static LoggerProvider loadProvider() {
        LoggerProvider ret = null;
        try {
            // 从系统配置读取
            ret = loadProviderByClassName(System.getProperty(LOGGER_PROVIDER_CLASS_NAME));
            if (ret != null) {
                System.out.println("system property logger apply: " + ret.getName());
                return ret;
            }
        } catch (Exception e) {

        }

        // 加载默认配置文件
        Properties properties = new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(LOGGER_DEFAULT_CONFIG_PROPERTIES));
        } catch (Exception e) {
        }

        try {
            // 从默认配置文件读取
            ret = loadProviderByClassName(properties.getProperty(LOGGER_PROVIDER_CLASS_NAME));
            if (ret != null) {
                System.out.println("default config properties logger apply: " + ret.getName());
                return ret;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 从SPI中读取
        ret = loadProviderBySpi(System.getProperty(LOGGER_PROVIDER_SPI_SELECT_NAME));
        if (ret != null) {
            System.out.println("spi logger apply: " + ret.getName());
            return ret;
        }

        // 从默认配置文件读取
        ret = loadProviderBySpi(properties.getProperty(LOGGER_PROVIDER_SPI_SELECT_NAME));
        if (ret != null) {
            System.out.println("spi logger apply: " + ret.getName());
            return ret;
        }

        return ret;
    }

    public static LoggerProvider loadProviderBySpi(String selectName) {
        LoggerProvider ret = null;
        LoggerProvider firstProvider = null;
        ServiceLoader<LoggerProvider> loader = ServiceLoader.load(LoggerProvider.class);
        for (LoggerProvider provider : loader) {
            if (firstProvider == null) {
                firstProvider = provider;
            }
            String name = provider.getName();
            if (name == null) {
                name = "";
            }
            if (name.equalsIgnoreCase(selectName)) {
                ret = provider;
                break;
            }
        }
        if (ret != null) {
            return ret;
        }

        return firstProvider;
    }

    public static LoggerProvider loadProviderByClassName(String className) {
        LoggerProvider ret = null;
        if (className != null) {
            className = className.trim();
            if (!className.isEmpty()) {
                Class<?> clazz = null;
                if (clazz == null || !LoggerProvider.class.isAssignableFrom(clazz)) {
                    try {
                        clazz = Class.forName(className);
                    } catch (Throwable e) {

                    }
                }
                if (clazz == null || !LoggerProvider.class.isAssignableFrom(clazz)) {
                    try {
                        clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
                    } catch (Throwable e) {

                    }
                }
                if (clazz != null && LoggerProvider.class.isAssignableFrom(clazz)) {
                    if (ret == null) {
                        try {
                            Constructor<?> constructor = clazz.getConstructor();
                            ret = (LoggerProvider) constructor.newInstance();
                        } catch (Throwable e) {

                        }
                    }
                    if (ret == null) {
                        try {
                            ret = (LoggerProvider) clazz.newInstance();
                        } catch (Throwable e) {

                        }
                    }
                }
            }
        }
        return ret;
    }

    public static ILogger getLogger(Class<?> location) {
        return getLogger(location.getName());
    }

    public static ILogger getLogger(Method method) {
        return getLogger(method.getDeclaringClass().getName() + "." + method.getName());
    }

    public static ILogger getLogger(Class<?> clazz, Method method) {
        return getLogger(clazz.getName() + "." + method.getName());
    }

    public static ILogger getLogger() {
        StackTraceElement[] elems = ThreadTrace.beforeTrace(LoggerFactory.class.getName());
        return getLogger(elems[0].getClassName() + "." + elems[0].getMethodName());
    }

}
