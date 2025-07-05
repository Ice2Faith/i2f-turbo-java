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
public class MysqlPageWrapper implements IPageWrapper {
    public static final MysqlPageWrapper INSTANCE = new MysqlPageWrapper();

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
                    .append("\n limit ").append(embed ? (page.getOffset()) : "?")
                    .append(" , ").append(embed ? (page.getSize()) : "?").append(" ");

            if (!embed) {
                pageSql.getArgs().add(page.getOffset());
                pageSql.getArgs().add(page.getSize());
            }
        } else if (page.getOffset() != null) {

            builder.append(bql.getSql())
                    .append("\n limit ").append(embed ? (page.getOffset()) : "?")
                    .append(" , ").append(embed ? (Integer.MAX_VALUE) : "?").append(" ");

            if (!embed) {
                pageSql.getArgs().add(page.getOffset());
                pageSql.getArgs().add(Integer.MAX_VALUE);
            }
        } else if (page.getSize() != null) {

            builder.append(bql.getSql())
                    .append("\n limit ").append(embed ? (page.getSize()) : "?").append(" ");

            if (!embed) {
                pageSql.getArgs().add(page.getSize());
            }
        }

        pageSql.setSql(builder.toString());
        return pageSql;
    }
}
