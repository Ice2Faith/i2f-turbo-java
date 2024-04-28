package i2f.bindsql;

import i2f.bindsql.count.CountWrappers;
import i2f.bindsql.count.ICountWrapper;
import i2f.bindsql.data.PageBindSql;
import i2f.bindsql.page.IPageWrapper;
import i2f.bindsql.page.PageWrappers;
import i2f.database.type.DatabaseType;
import i2f.page.ApiPage;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Ice2Faith
 * @date 2024/4/28 15:38
 * @desc
 */
public class BindSqlWrappers {

    public static PageBindSql page(Connection conn, BindSql sql, ApiPage page) throws SQLException {
        return page(DatabaseType.typeOfConnection(conn), sql, page);
    }

    public static PageBindSql page(String jdbcUrl, BindSql sql, ApiPage page) {
        return page(DatabaseType.typeOfJdbcUrl(jdbcUrl), sql, page);
    }

    public static PageBindSql page(DatabaseType type, BindSql sql, ApiPage page) {
        PageBindSql ret = new PageBindSql();
        IPageWrapper pageWrapper = PageWrappers.wrapper(type);
        BindSql pageSql = pageWrapper.apply(sql, page);
        ICountWrapper countWrapper = CountWrappers.wrapper();
        BindSql countSql = countWrapper.apply(sql);

        ret.setPage(page);
        ret.setPageSql(pageSql);
        ret.setCountSql(countSql);
        return ret;
    }
}
