package i2f.proxy;

/**
 * @author Ice2Faith
 * @date 2022/3/25 20:23
 * @desc
 */
public interface IProxyProvider {
    <T> T proxy(Object obj, IProxyHandler handler);
}
