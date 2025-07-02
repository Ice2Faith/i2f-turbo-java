package i2f.bindsql.count;

import i2f.database.type.DatabaseType;

/**
 * @author Ice2Faith
 * @date 2025/7/2 21:20
 * @desc
 */
public interface ICountWrapperProvider {

    boolean support(DatabaseType databaseType);

    ICountWrapper getCountWrapper();
}
