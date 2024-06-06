package i2f.jdbc.proxy.provider;

import java.sql.Connection;

/**
 * @author Ice2Faith
 * @date 2024/6/6 9:10
 * @desc
 */
public interface JdbcInvokeContextProvider<T> {
    T beginContext();

    Connection getConnection(T context);

    void endContext(T context, Connection conn);
}
