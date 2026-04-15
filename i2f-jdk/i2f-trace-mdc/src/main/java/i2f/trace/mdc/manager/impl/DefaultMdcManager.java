package i2f.trace.mdc.manager.impl;

import i2f.trace.mdc.manager.MdcManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Ice2Faith
 * @date 2026/4/15 14:16
 * @desc
 */
public class DefaultMdcManager implements MdcManager {
    public static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    public static final ThreadLocal<Map<String, String>> LOCAL = new InheritableThreadLocal<>();

    @Override
    public void put(String key, String value) {
        lock.writeLock().lock();
        try {
            Map<String, String> map = LOCAL.get();
            if (map == null) {
                map = new HashMap<>();
                LOCAL.set(map);
            }
            map.put(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public String get(String key) {
        lock.readLock().lock();
        try {
            Map<String, String> map = LOCAL.get();
            if (map == null) {
                return null;
            }
            return map.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void remove(String key) {
        lock.writeLock().lock();
        try {
            Map<String, String> map = LOCAL.get();
            if (map == null) {
                return;
            }
            map.remove(key);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void clear() {
        lock.writeLock().lock();
        try {
            Map<String, String> map = LOCAL.get();
            if (map == null) {
                return;
            }
            LOCAL.set(null);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Map<String, String> copyOf() {
        lock.readLock().lock();
        try {
            Map<String, String> map = LOCAL.get();
            if (map == null) {
                return new HashMap<>();
            }
            return new HashMap<>(map);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void replaceAs(Map<String, String> vars) {
        if (vars == null) {
            return;
        }
        lock.writeLock().lock();
        try {
            LOCAL.set(new HashMap<>(vars));
        } finally {
            lock.writeLock().unlock();
        }
    }
}
