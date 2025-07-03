package i2f.proxy.impl.interfaces;


import i2f.proxy.JdkProxyUtil;
import i2f.proxy.std.IProxyInvocationHandler;
import i2f.proxy.std.IProxyProvider;

/**
 * @author Ice2Faith
 * @date 2022/3/25 20:24
 * @desc
 */
public class JdkDynamicProxyProvider implements IProxyProvider {
    @Override
    public <T> T proxy(Object obj, IProxyInvocationHandler handler) {
        return JdkProxyUtil.proxy((Class<T>) obj, handler);
    }

}
