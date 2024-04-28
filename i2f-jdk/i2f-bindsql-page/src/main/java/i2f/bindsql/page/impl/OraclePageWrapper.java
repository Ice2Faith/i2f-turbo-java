package i2f.bindsql.page.impl;

import i2f.bindsql.BindSql;
import i2f.bindsql.page.IPageWrapper;
import i2f.page.ApiPage;

import java.util.ArrayList;

/**
 * @author Ice2Faith
 * @date 2024/4/28 14:42
 * @desc
 */
public class OraclePageWrapper implements IPageWrapper {
    @Override
    public BindSql apply(BindSql bql, ApiPage page) {
        page.prepare();

        BindSql pageSql = new BindSql();
        pageSql.setSql(bql.getSql());
        pageSql.setArgs(new ArrayList<>(bql.getArgs()));

        StringBuilder builder = new StringBuilder();
        if (page.getIndex() != null && page.getSize() != null) {

            builder.append(" SELECT * ")
                    .append(" FROM (SELECT TMP.*, ROWNUM ROW_ID ")
                    .append(" FROM ( ")
                    .append(bql.getSql())
                    .append(" ) TMP ")
                    .append(" WHERE ROWNUM <= ?) TMP ")
                    .append(" WHERE ROW_ID > ? ");

            pageSql.getArgs().add(page.getEnd());
            pageSql.getArgs().add(page.getOffset());
        } else if (page.getSize() != null) {
            builder.append(" SELECT * ")
                    .append(" FROM (SELECT TMP.*, ROWNUM ROW_ID ")
                    .append(" FROM ( ")
                    .append(bql.getSql())
                    .append(" ) TMP ")
                    .append(" WHERE ROWNUM <= ?) TMP ");

            pageSql.getArgs().add(page.getEnd());
        }

        pageSql.setSql(builder.toString());
        return pageSql;
    }
}
