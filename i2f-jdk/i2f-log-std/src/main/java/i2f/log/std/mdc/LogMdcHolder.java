package i2f.log.std.mdc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2025/7/3 15:36
 */
public class LogMdcHolder {
    public static final String TRACE_ID = "traceId";
    protected static final ThreadLocal<Map<String, Object>> MDC = InheritableThreadLocal.withInitial(() -> new HashMap<>());
    public static final CopyOnWriteArraySet<LogMdcChangeListener> MDC_CHANGE_LISTENERS = new CopyOnWriteArraySet<>();

    protected static void notifyListeners(LogMdcChangeType type, String key, Object value, Object oldValue, Map<String, Object> mdcMap) {
        for (LogMdcChangeListener item : MDC_CHANGE_LISTENERS) {
            if (item == null) {
                continue;
            }
            item.change(type, key, value, oldValue, mdcMap);
        }
    }

    public static Map<String, Object> getMdc() {
        Map<String, Object> map = MDC.get();
        if (map == null) {
            map = new HashMap<>();
            MDC.set(map);
        }
        return map;
    }

    public static void set(String key, Object value) {
        Map<String, Object> map = getMdc();
        Object oldValue = map.get(key);
        map.put(key, value);
        notifyListeners(LogMdcChangeType.UPDATE, key, value, oldValue, map);
    }

    public static void remove(String key) {
        Map<String, Object> map = getMdc();
        Object oldValue = map.get(key);
        map.remove(key);
        notifyListeners(LogMdcChangeType.REMOVE, key, null, oldValue, map);
    }

    public static void clear() {
        Map<String, Object> map = getMdc();
        MDC.remove();
        notifyListeners(LogMdcChangeType.CLEAR, null, null, map, null);
    }

    public static <T> T get(String key) {
        Map<String, Object> map = getMdc();
        return (T) map.get(key);
    }

    public static <T> T getOrDefault(String key, T defVal) {
        T obj = get(key);
        if (obj == null) {
            return defVal;
        }
        return obj;
    }

    public static <T> T getOrDefault(String key, Supplier<T> supplier) {
        T obj = get(key);
        if (obj == null) {
            return supplier.get();
        }
        return obj;
    }

    public static String newTraceId() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    public static String getTraceId() {
        return get(TRACE_ID);
    }

    public static String getOrNewTraceId() {
        String ret = get(TRACE_ID);
        if (ret == null) {
            ret = newTraceId();
            set(TRACE_ID, ret);
        }
        return ret;
    }

    public static void setTraceId(String traceId) {
        set(TRACE_ID, traceId);
    }

    public static void removeTraceId() {
        remove(TRACE_ID);
    }
}
