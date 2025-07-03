package i2f.proxy;

import i2f.proxy.impl.interfaces.JdkDynamicProxyInvocationHandlerAdapter;
import i2f.proxy.impl.normal.JdkProxyInvocationHandlerAdapter;
import i2f.proxy.std.IProxyHandler;
import i2f.proxy.std.IProxyInvocationHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author Ice2Faith
 * @date 2022/3/26 19:52
 * @desc
 */
public class JdkProxyUtil {
    public static <T> T proxy(T srcObj, IProxyInvocationHandler handler) {
        return proxy(srcObj, new JdkProxyInvocationHandlerAdapter<>(srcObj, handler));
    }

    public static <T> T proxy(Class<T> interfaces, IProxyInvocationHandler handler) {
        return proxy(interfaces, new JdkDynamicProxyInvocationHandlerAdapter(handler));
    }

    public static <T> T proxy(T srcObj, IProxyHandler handler) {
        return proxy(srcObj, IProxyInvocationHandler.of(handler));
    }

    public static <T> T proxy(Class<T> interfaces, IProxyHandler handler) {
        return proxy(interfaces, IProxyInvocationHandler.of(handler));
    }

    public static <T> T proxy(T srcObj, InvocationHandler handler) {
        Class<?> clazz = srcObj.getClass();
        Class<?>[] interfaces = clazz.getInterfaces();
        return (T) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                interfaces, handler);
    }

    public static <T> T proxy(Class<T> clazz, InvocationHandler handler) {
        Class<?>[] interfaces = new Class<?>[]{clazz};
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                interfaces, handler);
    }
}
