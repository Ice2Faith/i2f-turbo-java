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
public class Db2PageWrapper implements IPageWrapper {
    @Override
    public BindSql apply(BindSql bql, ApiPage page) {
        page.prepare();

        BindSql pageSql = new BindSql();
        pageSql.setSql(bql.getSql());
        pageSql.setArgs(new ArrayList<>(bql.getArgs()));

        StringBuilder builder = new StringBuilder();
        if (page.getIndex() != null && page.getSize() != null) {

            builder.append(" SELECT * FROM ( ")
                    .append(" SELECT TMP_PAGE.*,ROWNUMBER() OVER() AS ROW_ID FROM ( ")
                    .append(bql.getSql())
                    .append(" ) AS TMP_PAGE ")
                    .append(" ) TMP_PAGE ")
                    .append(" WHERE ROW_ID BETWEEN ? AND ? ");

            pageSql.getArgs().add(page.getOffset());
            pageSql.getArgs().add(page.getEnd());
        } else if (page.getSize() != null) {
            builder.append(" SELECT * FROM ( ")
                    .append(" SELECT TMP_PAGE.*,ROWNUMBER() OVER() AS ROW_ID FROM ( ")
                    .append(bql.getSql())
                    .append(" ) AS TMP_PAGE ")
                    .append(" ) TMP_PAGE ")
                    .append(" WHERE ROW_ID BETWEEN ? AND ? ");

            pageSql.getArgs().add(0);
            pageSql.getArgs().add(page.getSize());
        }

        pageSql.setSql(builder.toString());
        return pageSql;
    }
}
