package i2f.trace.mdc;

import i2f.trace.mdc.manager.MdcManager;
import i2f.trace.mdc.manager.impl.DefaultMdcManager;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * @author Ice2Faith
 * @date 2026/4/15 14:26
 * @desc
 */
public class MdcHolder {
    protected static final MdcManager manager = findMdcManager();
    public static final String MANAGER_CLASS_PROPERTY = "mdc.manager";

    protected static MdcManager findMdcManager() {
        String prop = System.getProperty(MANAGER_CLASS_PROPERTY);
        if (prop != null && !prop.isEmpty()) {
            prop = prop.trim();
            Class<?> clazz = null;
            if (clazz == null) {
                try {
                    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                    clazz = classLoader.loadClass(prop);
                } catch (Throwable e) {

                }
            }
            if (clazz == null) {
                try {
                    ClassLoader classLoader = MdcHolder.class.getClassLoader();
                    clazz = classLoader.loadClass(prop);
                } catch (Throwable e) {

                }
            }
            if (clazz == null) {
                try {
                    clazz = Class.forName(prop);
                } catch (Throwable e) {

                }
            }
            if (clazz != null) {
                try {
                    Constructor<?> constructor = clazz.getConstructor();
                    return (MdcManager) constructor.newInstance();
                } catch (Throwable e) {

                }

                try {
                    return (MdcManager) clazz.newInstance();
                } catch (Throwable e) {

                }

            }
        }
        MdcManager first = null;
        MdcManager slf4j = null;
        MdcManager log4j = null;
        ServiceLoader<MdcManager> list = ServiceLoader.load(MdcManager.class);
        for (MdcManager manager : list) {
            if (first == null) {
                first = manager;
            }
            String lowerFullName = manager.getClass().getName().toLowerCase();
            if (lowerFullName.contains("slf4j")) {
                slf4j = manager;
            }
            if (lowerFullName.contains("log4j")) {
                log4j = manager;
            }
        }
        if (slf4j != null) {
            return slf4j;
        }
        if (log4j != null) {
            return log4j;
        }
        if (first != null) {
            return first;
        }

        return new DefaultMdcManager();
    }

    public static MdcManager manager() {
        return manager;
    }

    public static void put(String key, String value) {
        manager.put(key, value);
    }

    public static String get(String key) {
        return manager.get(key);
    }

    public static void remove(String key) {
        manager.remove(key);
    }

    public static void clear() {
        manager.clear();
    }

    public static Map<String, String> copyOf() {
        return manager.copyOf();
    }

    public static void replaceAs(Map<String, String> map) {
        manager.replaceAs(map);
    }
}
