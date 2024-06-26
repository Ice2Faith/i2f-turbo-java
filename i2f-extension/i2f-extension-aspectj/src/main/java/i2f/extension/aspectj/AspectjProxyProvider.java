package i2f.extension.aspectj;


import i2f.extension.aspectj.core.AspectjProxy;
import i2f.proxy.IProxyHandler;
import i2f.proxy.IProxyProvider;

/**
 * @author Ice2Faith
 * @date 2022/3/26 19:23
 * @desc
 */
public class AspectjProxyProvider implements IProxyProvider {
    @Override
    public <T> T proxy(Object obj, IProxyHandler handler) {
        return (T) new AspectjProxy(handler);
    }

    public AspectjProxy proxyNative(IProxyHandler handler) {
        return proxy(null, handler);
    }
}
