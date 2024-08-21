package i2f.thread.local;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/8/21 11:30
 * @desc
 */
public class ThreadLocalUtil {

    public static final String THREAD_LOCAL_MAP_CLASS_NAME = "java.lang.ThreadLocal$ThreadLocalMap";

    public static void clearAllThreadLocals(Thread thread) {
        Object map = getThreadLocalMap(thread);
        clearAllThreadLocalMap(map);
    }

    public static void clearAllInheritableThreadLocals(Thread thread) {
        Object map = getInheritableThreadLocalMap(thread);
        clearAllThreadLocalMap(map);
    }

    public static void clearAllThreadLocalMap(Object map) {
        if (!isThreadLocalMap(map)) {
            return;
        }
        List<ThreadLocal<?>> threadLocals = parseThreadLocalMap(map);
        Class<?> mapClass = map.getClass();
        try {
            Method method = mapClass.getDeclaredMethod("remove", ThreadLocal.class);
            method.setAccessible(true);
            for (ThreadLocal<?> threadLocal : threadLocals) {
                method.invoke(map, threadLocal);
            }
        } catch (Exception e) {

        }
    }

    public static List<ThreadLocal<?>> getThreadLocals(Thread thread) {
        Object map = getThreadLocalMap(thread);
        return parseThreadLocalMap(map);
    }

    public static List<ThreadLocal<?>> getInheritableThreadLocals(Thread thread) {
        Object map = getInheritableThreadLocalMap(thread);
        return parseThreadLocalMap(map);
    }

    public static boolean isThreadLocalMap(Object map) {
        if (map == null) {
            return false;
        }
        Class<?> mapClass = map.getClass();
        if (THREAD_LOCAL_MAP_CLASS_NAME.equals(mapClass.getName())) {
            return true;
        }
        return false;
    }

    public static List<ThreadLocal<?>> parseThreadLocalMap(Object map) {
        List<ThreadLocal<?>> ret = new ArrayList<>();
        if (map == null) {
            return ret;
        }
        if (!isThreadLocalMap(map)) {
            return ret;
        }
        try {
            Class<?> mapClass = map.getClass();
            Field field = mapClass.getDeclaredField("table");
            field.setAccessible(true);
            Object table = field.get(map);
            int len = Array.getLength(table);
            for (int i = 0; i < len; i++) {
                Object ref = Array.get(table, i);
                if (ref == null) {
                    continue;
                }
                Class<?> refClass = ref.getClass();
                Method method = refClass.getMethod("get");
                Object local = method.invoke(ref);
                if (local != null) {
                    ret.add((ThreadLocal<?>) local);
                }
            }
        } catch (Exception e) {

        }

        return ret;
    }

    public static Object getThreadLocalMap(Thread thread) {
        if (thread == null) {
            return null;
        }
        try {
            Field field = Thread.class.getDeclaredField("threadLocals");
            field.setAccessible(true);
            return field.get(thread);
        } catch (Exception e) {
        }
        return null;
    }

    public static Object getInheritableThreadLocalMap(Thread thread) {
        if (thread == null) {
            return null;
        }
        try {
            Field field = Thread.class.getDeclaredField("inheritableThreadLocals");
            field.setAccessible(true);
            return field.get(thread);
        } catch (Exception e) {
        }
        return null;
    }
}
