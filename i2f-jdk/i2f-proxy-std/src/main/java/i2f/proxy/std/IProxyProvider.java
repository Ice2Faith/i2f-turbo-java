package i2f.proxy.std;

/**
 * @author Ice2Faith
 * @date 2022/3/25 20:23
 * @desc
 */
public interface IProxyProvider {
    <T> T proxy(Object obj, IProxyInvocationHandler handler);

    default <T> T proxy(Object obj, IProxyHandler handler) {
        return proxy(obj, IProxyInvocationHandler.of(handler));
    }
}
