package i2f.bindsql.page;

import i2f.database.type.DatabaseType;

/**
 * @author Ice2Faith
 * @date 2025/7/2 21:20
 * @desc
 */
public interface IPageWrapperProvider {

    boolean support(DatabaseType databaseType);

    IPageWrapper getPageWrapper();
}
