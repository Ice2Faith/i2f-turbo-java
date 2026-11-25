package i2f.spring.aop;

import java.lang.reflect.Proxy;

/**
 * @author Ice2Faith
 * @date 2026/5/27 15:28
 * @desc
 */
public class ProxyUtils {

    public static <T> Class<T> getProxyRealClass(T bean) {
        if (bean == null) {
            return null;
        }
        return (Class<T>) getProxyRealClass(bean.getClass());
    }

    public static Class<?> getProxyRealClass(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        while (true) {
            boolean hasWrap = false;
            if (isProxyByJdk(clazz)) {
                clazz = unwrapClassByJdk(clazz);
                hasWrap = true;
            }
            if (isProxyBySpringCglib(clazz)) {
                clazz = unwrapClassBySpringCglib(clazz);
                hasWrap = true;
            }
            if (isProxyByCglib(clazz)) {
                clazz = unwrapClassByCglib(clazz);
                hasWrap = true;
            }
            if (!hasWrap) {
                break;
            }
        }
        return clazz;
    }

    public static boolean isProxyByJdk(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        return Proxy.isProxyClass(clazz);
    }

    public static Class<?> unwrapClassByJdk(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        if (!isProxyByJdk(clazz)) {
            return clazz;
        }
        Class<?>[] arr = clazz.getInterfaces();
        if (arr.length == 1) {
            return arr[0];
        }
        for (Class<?> item : arr) {
            String name = item.getName();
            if (name.startsWith("org.springframework.")) {
                continue;
            }
            return item;
        }
        return arr[0];
    }

    public static final String SPRING_CGLIB_PROXY_CLASS = "$$EnhancerBySpringCGLIB$$";

    public static boolean isProxyBySpringCglib(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        int idx = clazz.getName().indexOf(SPRING_CGLIB_PROXY_CLASS);
        return idx >= 0;
    }

    public static Class<?> unwrapClassBySpringCglib(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        if (!isProxyBySpringCglib(clazz)) {
            return clazz;
        }
        return clazz.getSuperclass();
    }

    public static final String CGLIB_PROXY_CLASS = "$$EnhancerByCGLIB$$";

    public static boolean isProxyByCglib(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        int idx = clazz.getName().indexOf(CGLIB_PROXY_CLASS);
        return idx >= 0;
    }

    public static Class<?> unwrapClassByCglib(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        if (!isProxyByCglib(clazz)) {
            return clazz;
        }
        return clazz.getSuperclass();
    }
}
