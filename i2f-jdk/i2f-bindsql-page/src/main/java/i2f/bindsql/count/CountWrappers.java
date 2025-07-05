package i2f.bindsql.count;

import i2f.bindsql.count.impl.SqlCountWrapper;
import i2f.database.type.DatabaseType;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ServiceLoader;

/**
 * @author Ice2Faith
 * @date 2024/4/28 15:22
 * @desc
 */
public class CountWrappers {
    public static final ThreadLocal<ICountWrapper> WRAPPER_HOLDER = new ThreadLocal<>();
    public static final ThreadLocal<DatabaseType> DATABASE_HOLDER = new ThreadLocal<>();

    public static ICountWrapper wrapper(Connection conn) throws SQLException {
        ICountWrapper ret = WRAPPER_HOLDER.get();
        if (ret != null) {
            return ret;
        }
        DatabaseType type = DATABASE_HOLDER.get();
        if (type != null) {
            return wrapper(type);
        }
        type = DatabaseType.typeOfConnection(conn);
        return wrapper(type);
    }

    public static ICountWrapper wrapper(String jdbcUrl) {
        ICountWrapper ret = WRAPPER_HOLDER.get();
        if (ret != null) {
            return ret;
        }
        DatabaseType type = DATABASE_HOLDER.get();
        if (type != null) {
            return wrapper(type);
        }
        type = DatabaseType.typeOfJdbcUrl(jdbcUrl);
        return wrapper(type);
    }

    public static ICountWrapper wrapper(DatabaseType type) {
        ICountWrapper ret = WRAPPER_HOLDER.get();
        if (ret != null) {
            return ret;
        }
        ServiceLoader<ICountWrapperProvider> list = ServiceLoader.load(ICountWrapperProvider.class);
        for (ICountWrapperProvider item : list) {
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
        return SqlCountWrapper.INSTANCE;
    }

}
