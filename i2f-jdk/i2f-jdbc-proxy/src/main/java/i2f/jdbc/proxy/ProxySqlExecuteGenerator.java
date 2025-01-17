package i2f.jdbc.proxy;

import i2f.jdbc.proxy.handler.ProxyRenderSqlHandler;
import i2f.jdbc.proxy.provider.ProxyRenderSqlProvider;
import i2f.jdbc.std.context.JdbcInvokeContextProvider;

import java.lang.reflect.Proxy;

/**
 * @author Ice2Faith
 * @date 2024/6/6 8:54
 * @desc
 */
public class ProxySqlExecuteGenerator {
    public static <T> T proxy(Class<T> clazz, ProxyRenderSqlHandler sqlHandler) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{clazz},
                sqlHandler);
    }

    public static <T> T proxy(Class<T> clazz, JdbcInvokeContextProvider<?> contextProvider,
                              ProxyRenderSqlProvider sqlProvider) {
        return proxy(clazz, new ProxyRenderSqlHandler(clazz, contextProvider, sqlProvider));
    }
}
