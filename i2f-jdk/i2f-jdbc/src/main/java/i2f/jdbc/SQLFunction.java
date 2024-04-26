package i2f.jdbc;

import java.sql.SQLException;

/**
 * @author Ice2Faith
 * @date 2024/4/25 16:18
 * @desc
 */
@FunctionalInterface
public interface SQLFunction<T, R> {
    R apply(T t) throws SQLException;
}
