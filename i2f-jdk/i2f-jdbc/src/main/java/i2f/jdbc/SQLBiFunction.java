package i2f.jdbc;

import java.sql.SQLException;

/**
 * @author Ice2Faith
 * @date 2024/4/25 16:18
 * @desc
 */
@FunctionalInterface
public interface SQLBiFunction<T, U, R> {
    R apply(T t, U u) throws SQLException;
}
