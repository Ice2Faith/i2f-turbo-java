package i2f.proxy.impl;


import i2f.proxy.IProxyHandler;
import i2f.proxy.IProxyProvider;

import java.lang.reflect.Proxy;

/**
 * @author Ice2Faith
 * @date 2022/3/25 20:24
 * @desc
 */
public class JdkDynamicProxyProvider implements IProxyProvider {
    @Override
    public <T> T proxy(Object obj, IProxyHandler handler) {
        Class<?>[] interfaces = new Class<?>[]{(Class<?>) obj};
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                interfaces, new JdkDynamicProxyHandlerAdapter(handler));
    }

    public <T> T proxyNative(Class<T> interfaces, IProxyHandler handler) {
        return proxy(interfaces, handler);
    }
}
