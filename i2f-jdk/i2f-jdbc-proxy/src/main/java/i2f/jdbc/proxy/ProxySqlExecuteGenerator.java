package i2f.jdbc.proxy;

import i2f.jdbc.proxy.handler.ProxyRenderSqlHandler;
import i2f.jdbc.proxy.provider.JdbcInvokeContextProvider;
import i2f.jdbc.proxy.provider.ProxyRenderSqlProvider;

import java.lang.reflect.Proxy;

/**
 * @author Ice2Faith
 * @date 2024/6/6 8:54
 * @desc
 */
public class ProxySqlExecuteGenerator {
    public static <T, CTX> T proxy(Class<T> clazz, ProxyRenderSqlHandler<CTX> sqlHandler) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{clazz},
                sqlHandler);
    }

    public static <T, CTX> T proxy(Class<T> clazz, JdbcInvokeContextProvider<CTX> contextProvider,
                                   ProxyRenderSqlProvider sqlProvider) {
        return proxy(clazz, new ProxyRenderSqlHandler<>(clazz, contextProvider, sqlProvider));
    }
}
