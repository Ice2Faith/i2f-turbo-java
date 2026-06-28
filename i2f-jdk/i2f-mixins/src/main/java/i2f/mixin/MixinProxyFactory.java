package i2f.mixin;


import i2f.proxy.std.impl.DefaultMethodSmartInvocationHandler;

import java.lang.reflect.Proxy;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2026/5/14 10:38
 * @desc
 */
public class MixinProxyFactory {
    public static final DefaultMethodSmartInvocationHandler HANDLER = new DefaultMethodSmartInvocationHandler();
    private static final ConcurrentHashMap<Class<?>, Object> MIXIN_INSTANCE_MAP = new ConcurrentHashMap<>();

    public static <T> T getMixinInstance(Class<T> clazz) {
        Object ret = MIXIN_INSTANCE_MAP.get(clazz);
        if (ret != null) {
            return (T) ret;
        }

        ret = Proxy.newProxyInstance(clazz.getClassLoader(), findInterfaces(clazz).toArray(new Class[0]), HANDLER);
        MIXIN_INSTANCE_MAP.put(clazz, ret);
        return (T) ret;
    }

    protected static Set<Class<?>> findInterfaces(Class<?> clazz) {
        Set<Class<?>> ret = new LinkedHashSet<>();
        findInterfacesNext(clazz, ret);
        return ret;
    }

    protected static void findInterfacesNext(Class<?> clazz, Set<Class<?>> ret) {
        if (clazz == null) {
            return;
        }
        ret.add(clazz);
        if (Object.class.equals(clazz)) {
            return;
        }
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces == null) {
            return;
        }
        for (Class<?> item : interfaces) {
            findInterfacesNext(item, ret);
        }
    }
}
