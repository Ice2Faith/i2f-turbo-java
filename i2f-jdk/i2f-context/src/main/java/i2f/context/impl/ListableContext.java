package i2f.context.impl;

import i2f.context.IWritableContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2024/8/9 9:38
 * @desc
 */
public class ListableContext implements IWritableContext {
    protected final CopyOnWriteArrayList<Object> list = new CopyOnWriteArrayList<>();

    @Override
    public void addBean(Object bean) {
        synchronized (list) {
            if (!list.contains(bean)) {
                list.add(bean);
            }
        }
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        Object first = null;
        Object equal = null;
        for (Object item : list) {
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
        for (Object item : list) {
            Class<?> type = item.getClass();
            if (clazz.equals(type) || clazz.isAssignableFrom(type)) {
                ret.add((T) item);
            }
        }
        return ret;
    }

    @Override
    public List<Object> getAllBeans() {
        return new ArrayList<>(list);
    }

    @Override
    public void removeBean(Object bean) {
        if (bean == null) {
            return;
        }
        list.remove(bean);
    }
}
