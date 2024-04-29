package i2f.proxy.impl;

import i2f.proxy.IProxyHandler;
import i2f.proxy.IProxyProvider;

import java.lang.reflect.Proxy;

/**
 * @author Ice2Faith
 * @date 2022/3/25 20:24
 * @desc
 */
public class JdkProxyProvider implements IProxyProvider {
    @Override
    public <T> T proxy(Object obj, IProxyHandler handler) {
        Class<?> clazz = obj.getClass();
        Class<?>[] interfaces = clazz.getInterfaces();
        return (T) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                interfaces, new JdkProxyHandlerAdapter<>(obj, handler));
    }

    public <T> T proxyNative(T obj, IProxyHandler handler) {
        return proxy(obj, handler);
    }
}
