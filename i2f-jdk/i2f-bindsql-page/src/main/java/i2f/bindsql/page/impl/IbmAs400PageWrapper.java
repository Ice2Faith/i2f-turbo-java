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
public class IbmAs400PageWrapper implements IPageWrapper {
    public static final IbmAs400PageWrapper INSTANCE = new IbmAs400PageWrapper();

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

            builder
                    .append(bql.getSql())
                    .append("\n offset ").append(embed ? (page.getOffset()) : "?").append(" ")
                    .append(" rows fetch first ").append(embed ? (page.getSize()) : "?").append(" rows only ");

            if (!embed) {
                pageSql.getArgs().add(page.getOffset());
                pageSql.getArgs().add(page.getSize());
            }
        } else if (page.getOffset() != null) {
            builder
                    .append(bql.getSql())
                    .append("\n offset ").append(embed ? (page.getOffset()) : "?").append(" ");

            if (!embed) {
                pageSql.getArgs().add(page.getOffset());
            }
        } else if (page.getSize() != null) {
            builder
                    .append(bql.getSql())
                    .append("\n offset ").append(embed ? (0) : "?").append(" ")
                    .append(" rows fetch first ").append(embed ? (page.getSize()) : "?").append(" rows only ");

            if (!embed) {
                pageSql.getArgs().add(0);
                pageSql.getArgs().add(page.getSize());
            }
        }

        pageSql.setSql(builder.toString());

        return pageSql;
    }
}
