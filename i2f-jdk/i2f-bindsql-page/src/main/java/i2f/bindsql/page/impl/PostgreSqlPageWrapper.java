package i2f.bindsql.page.impl;

import i2f.bindsql.BindSql;
import i2f.bindsql.page.IPageWrapper;
import i2f.page.ApiOffsetSize;

import java.util.ArrayList;

/**
 * @author Ice2Faith
 * @date 2024/4/28 9:24
 * @desc
 */
public class PostgreSqlPageWrapper implements IPageWrapper {
    public static final PostgreSqlPageWrapper INSTANCE = new PostgreSqlPageWrapper();
    @Override
    public BindSql apply(BindSql bql, ApiOffsetSize page, boolean embed) {
        if (page == null) {
            return bql;
        }
        page.prepare();

        BindSql pageSql = new BindSql();
        pageSql.setSql(bql.getSql());
        pageSql.setArgs(new ArrayList<>(bql.getArgs()));

        StringBuilder builder = new StringBuilder();
        if (page.getOffset() != null && page.getSize() != null) {

            builder.append(bql.getSql())
                    .append(" limit ").append(embed ? (page.getSize()) : "?").append(" ")
                    .append(" offset ").append(embed ? (page.getOffset()) : "?").append(" ");

            if (!embed) {
                pageSql.getArgs().add(page.getSize());
                pageSql.getArgs().add(page.getOffset());
            }
        } else if (page.getOffset() != null) {

            builder.append(bql.getSql())
                    .append(" offset ").append(embed ? (page.getOffset()) : "?").append(" ");

            if (!embed) {
                pageSql.getArgs().add(page.getOffset());
            }
        } else if (page.getSize() != null) {

            builder.append(bql.getSql())
                    .append(" limit ").append(embed ? (page.getSize()) : "?").append(" ");

            if (!embed) {
                pageSql.getArgs().add(page.getSize());
            }
        }

        pageSql.setSql(builder.toString());
        return pageSql;
    }
}
