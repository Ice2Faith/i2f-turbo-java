package i2f.jdbc.std.context;

import java.sql.Connection;

/**
 * @author Ice2Faith
 * @date 2024/6/6 9:10
 * @desc
 */
public interface JdbcInvokeContextProvider<T> {
    T beginContext();

    Connection getConnection(T context);

    default Connection getConnectionInner(Object context) {
        return getConnection((T) context);
    }

    void endContext(T context, Connection conn);

    default void endContextInner(Object context, Connection conn) {
        endContext((T) context, conn);
    }
}
