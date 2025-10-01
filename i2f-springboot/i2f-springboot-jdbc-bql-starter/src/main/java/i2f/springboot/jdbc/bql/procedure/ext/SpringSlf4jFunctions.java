package i2f.springboot.jdbc.bql.procedure.ext;

import i2f.jdbc.procedure.context.ContextHolder;
import i2f.lru.LruMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2025/9/17 9:56
 */
public class SpringSlf4jFunctions {
    protected static final LruMap<String, Logger> LOGGER_MAP = new LruMap<>(512);

    public static String log_text(Object... objs) {
        String text = String.join(", ", Arrays.asList(objs).stream().map(e -> String.valueOf(e)).collect(Collectors.toList()));
        return text;
    }

    public static Logger logger(Object location) {
        String loc = (location == null ? SpringSlf4jFunctions.class.getName() : String.valueOf(location));
        Logger ret = LOGGER_MAP.get(loc);
        if (ret != null) {
            return ret;
        }
        ret = LoggerFactory.getLogger(loc);
        LOGGER_MAP.put(loc, ret);
        return ret;
    }

    public static Logger logger() {
        return logger(ContextHolder.TRACE_FILE.get());
    }

    public static void log(Object... objs) {
        logger().info(log_text(objs));
    }

    public static void log_info(Object... objs) {
        logger().info(log_text(objs));
    }

    public static void log_warn(Object... objs) {
        logger().warn(log_text(objs));
    }

    public static void log_error(Object... objs) {
        logger().error(log_text(objs));
    }

    public static void log_debug(Object... objs) {
        logger().debug(log_text(objs));
    }

    public static void log_trace(Object... objs) {
        logger().trace(log_text(objs));
    }
}
