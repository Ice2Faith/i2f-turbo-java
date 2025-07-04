package i2f.jdbc.handler;

import java.sql.PreparedStatement;
import java.sql.SQLType;

/**
 * @author Ice2Faith
 * @date 2025/4/2 9:20
 */
@FunctionalInterface
public interface StatementParameterSetHandler {
    boolean set(PreparedStatement stat, int index, Object obj, SQLType jdbcType, Class<?> javaTye);
}
