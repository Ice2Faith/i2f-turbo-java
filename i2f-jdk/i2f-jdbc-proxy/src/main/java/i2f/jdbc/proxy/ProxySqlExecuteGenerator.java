package i2f.jdbc.proxy;

import i2f.jdbc.proxy.handler.ProxyRenderSqlHandler;
import i2f.jdbc.proxy.provider.ProxyRenderSqlProvider;
import i2f.jdbc.std.context.JdbcInvokeContextProvider;
import i2f.proxy.JdkProxyUtil;

/**
 * @author Ice2Faith
 * @date 2024/6/6 8:54
 * @desc
 */
public class ProxySqlExecuteGenerator {
    public static <T> T proxy(Class<T> clazz, ProxyRenderSqlHandler sqlHandler) {
        return JdkProxyUtil.proxy(clazz, sqlHandler);
    }

    public static <T> T proxy(Class<T> clazz, JdbcInvokeContextProvider<?> contextProvider,
                              ProxyRenderSqlProvider sqlProvider) {
        return proxy(clazz, new ProxyRenderSqlHandler(clazz, contextProvider, sqlProvider));
    }
}
