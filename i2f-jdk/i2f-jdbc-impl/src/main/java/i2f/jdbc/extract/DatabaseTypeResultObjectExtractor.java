package i2f.jdbc.extract;

import i2f.database.type.DatabaseType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Ice2Faith
 * @date 2026/7/2 11:13
 * @desc
 */
public abstract class DatabaseTypeResultObjectExtractor implements JdbcResultObjectExtractor {
    @Override
    public boolean support(ResultSet rs) throws SQLException {
        Statement stat = rs.getStatement();
        Connection conn = stat.getConnection();
        DatabaseType databaseType = DatabaseType.typeOfConnection(conn);
        return support(databaseType);
    }

    public abstract boolean support(DatabaseType databaseType);
}
