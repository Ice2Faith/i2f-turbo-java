package i2f.context.impl;

import i2f.context.IWritableContext;
import i2f.context.IWritableNamingContext;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Ice2Faith
 * @date 2024/8/9 9:48
 * @desc
 */
public class ListableNamingContext implements IWritableNamingContext, IWritableContext {
    protected final ReadWriteLock lock = new ReentrantReadWriteLock();
    protected final ConcurrentHashMap<String, Object> beanMap = new ConcurrentHashMap<>(64);
    protected final CopyOnWriteArrayList<Object> beanList = new CopyOnWriteArrayList<>();

    public String guessBeanName(Object bean) {
        return bean.getClass().getName();
    }

    @Override
    public void addBean(String name, Object bean) {
        if (bean == null) {
            throw new IllegalArgumentException("bean cannot be null!");
        }
        if (name == null) {
            name = guessBeanName(bean);
        }
        lock.writeLock().lock();
        try {
            if (beanMap.containsKey(name)) {
                throw new IllegalArgumentException("bean [" + name + "] has already exist!");
            }
            beanMap.put(name, bean);
            beanList.add(bean);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public <T> T getBean(String name) {
        return (T) beanMap.get(name);
    }

    @Override
    public <T> Map<String, T> getBeansMap(Class<T> clazz) {
        Map<String, T> ret = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
            Object bean = entry.getValue();
            Class<?> type = bean.getClass();
            if (clazz.equals(type) || clazz.isAssignableFrom(type)) {
                ret.put(entry.getKey(), (T) bean);
            }
        }
        return ret;
    }

    @Override
    public void addBean(Object bean) {
        String name = guessBeanName(bean);
        addBean(name, bean);
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        Object first = null;
        Object equal = null;
        for (Object item : beanList) {
            Class<?> type = item.getClass();
            if (clazz.isAssignableFrom(type)) {
                if (first == null) {
                    first = item;
                }
            }
            if (clazz.equals(type)) {
                if (equal == null) {
                    equal = item;
                }
            }
            if (first != null && equal != null) {
                break;
            }
        }
        if (equal != null) {
            return (T) equal;
        }
        return (T) first;
    }

    @Override
    public <T> List<T> getBeans(Class<T> clazz) {
        List<T> ret = new ArrayList<>();
        for (Object item : beanList) {
            Class<?> type = item.getClass();
            if (clazz.equals(type) || clazz.isAssignableFrom(type)) {
                ret.add((T) item);
            }
        }
        return ret;
    }

    @Override
    public List<Object> getAllBeans() {
        return new ArrayList<>(beanList);
    }

    @Override
    public Map<String, Object> getAllBeansMap() {
        return new LinkedHashMap<>(beanMap);
    }

    @Override
    public void removeBean(Object bean) {
        if (bean == null) {
            return;
        }
        lock.writeLock().lock();
        try {
            String name = null;
            Object mapBean = null;
            for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
                Object value = entry.getValue();
                if (value.equals(bean)) {
                    name = entry.getKey();
                    mapBean = value;
                    break;
                }
            }
            if (name != null) {
                beanMap.remove(name);
                beanList.remove(mapBean);
            }
        } finally {
            lock.writeLock().unlock();
        }

    }

    @Override
    public void removeBean(String name) {
        if (name == null) {
            return;
        }
        lock.writeLock().lock();
        try {
            Object mapBean = beanMap.get(name);
            if (mapBean != null) {
                beanMap.remove(name);
                beanList.remove(mapBean);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
}
