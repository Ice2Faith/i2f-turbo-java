package org.slf4j.impl;

import org.slf4j.spi.MDCAdapter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2024/7/23 19:45
 * @desc
 */
public class SimpleMdcAdapter implements MDCAdapter {
    private final ThreadLocal<Map<String, String>> copyOnThreadLocal = new ThreadLocal<>();
    public static final int WRITE_OPERATION = 1;
    public static final int MAP_COPY_OPERATION = 2;
    private final ThreadLocal<Integer> lastOperation = new ThreadLocal<>();

    public SimpleMdcAdapter() {
    }

    private Integer getAndSetLastOperation(int operation) {
        Integer lastOp = this.lastOperation.get();
        this.lastOperation.set(operation);
        return lastOp;
    }

    private boolean wasLastOpReadOrNull(Integer lastOp) {
        return lastOp == null || lastOp == MAP_COPY_OPERATION;
    }

    private synchronized Map<String, String> duplicateAndInsertNewMap(Map<String, String> oldMap) {
        Map<String, String> newMap = Collections.synchronizedMap(new HashMap<>());
        if (oldMap != null) {
            newMap.putAll(oldMap);
        }

        this.copyOnThreadLocal.set(newMap);
        return newMap;
    }

    @Override
    public void put(String key, String val) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }
        Map<String, String> oldMap = this.copyOnThreadLocal.get();
        Integer lastOp = this.getAndSetLastOperation(WRITE_OPERATION);
        if (!this.wasLastOpReadOrNull(lastOp) && oldMap != null) {
            oldMap.put(key, val);
        } else {
            Map<String, String> newMap = this.duplicateAndInsertNewMap(oldMap);
            newMap.put(key, val);
        }
    }

    @Override
    public void remove(String key) {
        if (key == null) {
            return;
        }
        Map<String, String> oldMap = this.copyOnThreadLocal.get();
        if (oldMap == null) {
            return;
        }
        Integer lastOp = this.getAndSetLastOperation(WRITE_OPERATION);
        if (this.wasLastOpReadOrNull(lastOp)) {
            Map<String, String> newMap = this.duplicateAndInsertNewMap(oldMap);
            newMap.remove(key);
        } else {
            oldMap.remove(key);
        }
    }

    @Override
    public void clear() {
        this.lastOperation.set(WRITE_OPERATION);
        this.copyOnThreadLocal.remove();
    }

    @Override
    public String get(String key) {
        Map<String, String> map = this.copyOnThreadLocal.get();
        return map != null && key != null ? map.get(key) : null;
    }

    public Map<String, String> getPropertyMap() {
        this.lastOperation.set(MAP_COPY_OPERATION);
        return this.copyOnThreadLocal.get();
    }

    public Set<String> getKeys() {
        Map<String, String> map = this.getPropertyMap();
        return map != null ? map.keySet() : null;
    }

    @Override
    public Map<String, String> getCopyOfContextMap() {
        Map<String, String> hashMap = this.copyOnThreadLocal.get();
        return hashMap == null ? null : new HashMap<>(hashMap);
    }

    @Override
    public void setContextMap(Map<String, String> contextMap) {
        this.lastOperation.set(WRITE_OPERATION);
        Map<String, String> newMap = Collections.synchronizedMap(new HashMap<>());
        newMap.putAll(contextMap);
        this.copyOnThreadLocal.set(newMap);
    }
}
