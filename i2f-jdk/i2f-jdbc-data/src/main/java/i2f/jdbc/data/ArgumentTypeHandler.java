package i2f.jdbc.data;

import java.sql.PreparedStatement;

/**
 * @author Ice2Faith
 * @date 2025/3/20 14:02
 */
@FunctionalInterface
public interface ArgumentTypeHandler {
    void handle(PreparedStatement stat, int index, Object value);
}
