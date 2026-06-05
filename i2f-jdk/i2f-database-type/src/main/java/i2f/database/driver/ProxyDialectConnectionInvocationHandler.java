package i2f.database.driver;

import lombok.Data;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Driver;

/**
 * @author Ice2Faith
 * @date 2026/6/5 21:10
 * @desc
 */
@Data
public class ProxyDialectConnectionInvocationHandler implements InvocationHandler {
    protected final Connection connection;
    protected final Driver driver;
    protected final ProxyDialectJdbcUrlMeta meta;

    public ProxyDialectConnectionInvocationHandler(Connection connection, Driver driver, ProxyDialectJdbcUrlMeta meta) {
        this.connection = connection;
        this.driver = driver;
        this.meta = meta;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String name = method.getName();
        if (ProxyDialectConnectionFeature.METHOD_REAL_CONNECTION.equals(name)) {
            return connection;
        } else if (ProxyDialectConnectionFeature.METHOD_DIALECT.equals(name)) {
            return meta.getDialectType();
        } else if (ProxyDialectConnectionFeature.METHOD_REAL_URL.equals(name)) {
            return meta.getRealJdbcUrl();
        } else if (ProxyDialectConnectionFeature.METHOD_REAL_DRIVER.equals(name)) {
            return driver;
        }

        return method.invoke(connection, args);
    }
}
