package i2f.database.type;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Ice2Faith
 * @date 2025/9/1 19:08
 * @desc
 */
@FunctionalInterface
public interface DatabaseTypeDetector {
    default DatabaseType detect(Connection conn) throws SQLException {
        String url = conn.getMetaData().getURL();
        return detect(url);
    }

    DatabaseType detect(String jdbcUrl);
}
