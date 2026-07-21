package i2f.ai.std.tool;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Ice2Faith
 * @date 2026/7/21 11:21
 * @desc
 */
public class ToolCallContextHolder {
    protected static final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();
    protected static final ThreadLocal<Map<String, Object>> LOCAL = new InheritableThreadLocal<>();

    public static void put(String key, Object value) {
        LOCK.writeLock().lock();
        try {
            Map<String, Object> map = LOCAL.get();
            if (map == null) {
                map = new HashMap<>();
                LOCAL.set(map);
            }
            map.put(key, value);
        } finally {
            LOCK.writeLock().unlock();
        }
    }

    public static <T> T get(String key) {
        LOCK.readLock().lock();
        try {
            Map<String, Object> map = LOCAL.get();
            if (map == null) {
                return null;
            }
            return (T) map.get(key);
        } finally {
            LOCK.readLock().unlock();
        }
    }

    public static void remove(String key) {
        LOCK.writeLock().lock();
        try {
            Map<String, Object> map = LOCAL.get();
            if (map == null) {
                return;
            }
            map.remove(key);
        } finally {
            LOCK.writeLock().unlock();
        }
    }

    public static void clear() {
        LOCK.writeLock().lock();
        try {
            Map<String, Object> map = LOCAL.get();
            if (map == null) {
                return;
            }
            LOCAL.set(null);
        } finally {
            LOCK.writeLock().unlock();
        }
    }

    public static Map<String, Object> copyOf() {
        LOCK.readLock().lock();
        try {
            Map<String, Object> map = LOCAL.get();
            if (map == null) {
                return new HashMap<>();
            }
            return new HashMap<>(map);
        } finally {
            LOCK.readLock().unlock();
        }
    }

    public static void replaceAs(Map<String, Object> vars) {
        if (vars == null) {
            return;
        }
        LOCK.writeLock().lock();
        try {
            LOCAL.set(new HashMap<>(vars));
        } finally {
            LOCK.writeLock().unlock();
        }
    }
}
