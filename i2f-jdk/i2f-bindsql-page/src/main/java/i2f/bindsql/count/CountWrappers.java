package i2f.bindsql.count;

import i2f.bindsql.count.impl.SqlCountWrapper;
import i2f.database.type.DatabaseDialectMapping;
import i2f.database.type.DatabaseType;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Ice2Faith
 * @date 2024/4/28 15:22
 * @desc
 */
public class CountWrappers {
    public static final ThreadLocal<ICountWrapper> WRAPPER_HOLDER = new ThreadLocal<>();
    public static final CopyOnWriteArraySet<ICountWrapperProvider> PROVIDERS = new CopyOnWriteArraySet<>();
    public static DatabaseDialectMapping DIALECT_MAPPING = new DatabaseDialectMapping() {
        @Override
        public void init() {

        }
    };

    public static ICountWrapper wrapper(Connection conn) throws SQLException {
        ICountWrapper ret = WRAPPER_HOLDER.get();
        if (ret != null) {
            return ret;
        }
        DatabaseType type = DatabaseType.typeOfConnection(conn);
        return wrapper(type);
    }

    public static ICountWrapper wrapper(String jdbcUrl) {
        ICountWrapper ret = WRAPPER_HOLDER.get();
        if (ret != null) {
            return ret;
        }
        DatabaseType type = DatabaseType.typeOfJdbcUrl(jdbcUrl);
        return wrapper(type);
    }

    public static ICountWrapper wrapper(DatabaseType type) {
        ICountWrapper ret = WRAPPER_HOLDER.get();
        if (ret != null) {
            return ret;
        }
        for (ICountWrapperProvider item : PROVIDERS) {
            if (item == null) {
                continue;
            }
            if (item.support(type)) {
                ICountWrapper countWrapper = item.getCountWrapper();
                if (countWrapper != null) {
                    return countWrapper;
                }
            }
        }
        type = DIALECT_MAPPING.dialectOf(type);
        return SqlCountWrapper.INSTANCE;
    }

}
