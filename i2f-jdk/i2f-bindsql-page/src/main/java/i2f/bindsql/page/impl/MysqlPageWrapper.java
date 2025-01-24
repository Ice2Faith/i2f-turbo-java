package i2f.bindsql.page.impl;

import i2f.bindsql.BindSql;
import i2f.bindsql.page.IPageWrapper;
import i2f.page.ApiPage;

import java.util.ArrayList;

/**
 * @author Ice2Faith
 * @date 2024/4/28 9:24
 * @desc
 */
public class MysqlPageWrapper implements IPageWrapper {
    @Override
    public BindSql apply(BindSql bql, ApiPage page) {
        page.prepare();

        BindSql pageSql = new BindSql();
        pageSql.setSql(bql.getSql());
        pageSql.setArgs(new ArrayList<>(bql.getArgs()));

        StringBuilder builder = new StringBuilder();
        if (page.getIndex() != null && page.getSize() != null) {

            builder.append(bql.getSql())
                    .append(" limit ?, ? ");

            pageSql.getArgs().add(page.getOffset());
            pageSql.getArgs().add(page.getSize());
        } else if (page.getSize() != null) {

            builder.append(bql.getSql())
                    .append(" limit ? ");

            pageSql.getArgs().add(page.getSize());
        }

        pageSql.setSql(builder.toString());
        return pageSql;
    }
}
