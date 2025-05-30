package i2f.bindsql.page.impl;

import i2f.bindsql.BindSql;
import i2f.bindsql.page.IPageWrapper;
import i2f.page.ApiOffsetSize;

import java.util.ArrayList;

/**
 * @author Ice2Faith
 * @date 2024/4/28 14:42
 * @desc
 */
public class SqlServerPageWrapper implements IPageWrapper {
    @Override
    public BindSql apply(BindSql bql, ApiOffsetSize page) {
        if (page == null) {
            return bql;
        }
        page.prepare();

        BindSql pageSql = new BindSql();
        pageSql.setSql(bql.getSql());
        pageSql.setArgs(new ArrayList<>(bql.getArgs()));

        StringBuilder builder = new StringBuilder();
        if (page.getOffset() != null && page.getSize() != null) {

            builder
                    .append(bql.getSql())
                    .append(" offset ? ")
                    .append(" rows fetch next ? rows only ");

            pageSql.getArgs().add(page.getOffset());
            pageSql.getArgs().add(page.getSize());
        } else if (page.getOffset() != null) {
            builder
                    .append(bql.getSql())
                    .append(" offset ? ");

            pageSql.getArgs().add(page.getOffset());
        } else if (page.getSize() != null) {
            builder
                    .append(bql.getSql())
                    .append(" offset ? ")
                    .append(" rows fetch next ? rows only ");

            pageSql.getArgs().add(0);
            pageSql.getArgs().add(page.getSize());
        }

        pageSql.setSql(builder.toString());

        return pageSql;
    }
}
