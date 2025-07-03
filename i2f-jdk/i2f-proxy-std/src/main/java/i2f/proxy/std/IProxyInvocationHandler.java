package i2f.proxy.std;

import i2f.invokable.IInvokable;
import i2f.proxy.std.impl.ProxyHandlerAdapter;

/**
 * @author Ice2Faith
 * @date 2025/7/3 8:55
 */
@FunctionalInterface
public interface IProxyInvocationHandler {
    static IProxyInvocationHandler of(IProxyHandler handler) {
        return new ProxyHandlerAdapter(handler);
    }

    Object invoke(Object ivkObj, IInvokable invokable, Object... args) throws Throwable;
}
