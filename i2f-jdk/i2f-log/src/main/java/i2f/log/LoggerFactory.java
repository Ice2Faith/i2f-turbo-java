package i2f.log;

import i2f.log.holder.LogHolder;
import i2f.log.logger.DefaultLogger;
import i2f.log.spi.SpiLoggerProvider;
import i2f.lru.LruMap;
import i2f.trace.ThreadTrace;

import java.lang.reflect.Method;
import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2024/7/1 9:59
 * @desc
 */
public class LoggerFactory {
    public static LruMap<String, ILogger> CACHE = new LruMap<>(1024);
    private static final AtomicBoolean spiTest = new AtomicBoolean(false);
    private static volatile SpiLoggerProvider spiProvider = null;

    public static ILogger getLogger(String location) {
        ILogger logger = CACHE.get(location);
        if (logger != null) {
            return logger;
        }

        if (!spiTest.getAndSet(true)) {
            ServiceLoader<SpiLoggerProvider> loader = ServiceLoader.load(SpiLoggerProvider.class);
            for (SpiLoggerProvider provider : loader) {
                if (provider.test()) {
                    spiProvider = provider;
                    break;
                }
            }
            if (spiProvider != null) {
                System.out.println("spi logger apply: " + spiProvider.getName());
            } else {
                System.out.println("default logger apply.");
            }
        }

        if (logger == null) {
            if (spiProvider != null) {
                logger = spiProvider.getLogger(location);
            }
        }

        if (logger == null) {
            logger = new DefaultLogger(location, LogHolder.getDecider(), LogHolder.getWriter());
        }
        CACHE.put(location, logger);
        return logger;
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
