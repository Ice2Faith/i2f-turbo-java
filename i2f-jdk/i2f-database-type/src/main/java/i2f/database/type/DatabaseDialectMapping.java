package i2f.database.type;

import lombok.Data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2025/9/1 19:16
 * @desc
 */
@Data
public class DatabaseDialectMapping {
    protected final ConcurrentHashMap<String, DatabaseType> jdbcUrlDialectMap = new ConcurrentHashMap<>();
    protected final ConcurrentHashMap<DatabaseType, DatabaseType> redirectDialectMap = new ConcurrentHashMap<>();

    protected static final Map<Connection, String> TYPE_MAP = new WeakHashMap<>();

    public DatabaseDialectMapping() {
        init();
    }

    public void init() {

    }

    public DatabaseDialectMapping redirect(DatabaseType source, DatabaseType destination) {
        redirectDialectMap.put(source, destination);
        return this;
    }

    public DatabaseDialectMapping redirect(String jdbcUrl, DatabaseType destination) {
        jdbcUrlDialectMap.put(jdbcUrl, destination);
        return this;
    }

    protected String urlOf(Connection conn) throws SQLException {
        String jdbcUrl = TYPE_MAP.get(conn);
        if (jdbcUrl != null) {
            return jdbcUrl;
        }
        jdbcUrl = conn.getMetaData().getURL();
        TYPE_MAP.put(conn, jdbcUrl);
        return jdbcUrl;
    }

    public DatabaseType dialectOf(Connection conn) throws SQLException {
        return dialectOf(urlOf(conn));
    }

    public DatabaseType dialectOf(String url) {
        DatabaseType urlType = jdbcUrlDialectMap.get(url);
        if (DatabaseType.isValid(urlType)) {
            return urlType;
        }
        DatabaseType type = DatabaseType.typeOfJdbcUrl(url);
        return dialectOf(type);
    }

    public DatabaseType dialectOf(DatabaseType type) {
        DatabaseType redirectType = redirectDialectMap.get(type);
        if (DatabaseType.isValid(redirectType)) {
            return redirectType;
        }
        return type;
    }
}
