package i2f.proxy.impl.normal;

import i2f.proxy.JdkProxyUtil;
import i2f.proxy.std.IProxyInvocationHandler;
import i2f.proxy.std.IProxyProvider;

/**
 * @author Ice2Faith
 * @date 2022/3/25 20:24
 * @desc
 */
public class JdkProxyProvider implements IProxyProvider {
    @Override
    public <T> T proxy(Object obj, IProxyInvocationHandler handler) {
        return (T) JdkProxyUtil.proxy(obj, handler);
    }

}
