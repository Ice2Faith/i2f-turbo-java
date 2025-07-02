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
public class FirebirdPageWrapper implements IPageWrapper {
    public static final FirebirdPageWrapper INSTANCE = new FirebirdPageWrapper();

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
                    .append(" rows ").append(embed ? (page.getOffset() + 1) : "?")
                    .append(" to ").append(embed ? (page.getEnd() + 1) : "?").append(" ");

            if (!embed) {
                pageSql.getArgs().add(page.getOffset() + 1);
                pageSql.getArgs().add(page.getEnd() + 1);
            }
        } else if (page.getOffset() != null) {

            builder.append(bql.getSql())
                    .append(" rows ").append(embed ? (page.getOffset() + 1) : "?")
                    .append(" to ").append(embed ? (Integer.MAX_VALUE) : "?").append(" ");

            if (!embed) {
                pageSql.getArgs().add(page.getOffset() + 1);
                pageSql.getArgs().add(Integer.MAX_VALUE);
            }
        } else if (page.getEnd() != null) {

            builder.append(bql.getSql())
                    .append(" rows ").append(embed ? (1) : "?")
                    .append(" to ").append(embed ? (page.getEnd() + 1) : "?").append(" ");

            if (!embed) {
                pageSql.getArgs().add(1);
                pageSql.getArgs().add(page.getEnd() + 1);
            }
        }

        pageSql.setSql(builder.toString());
        return pageSql;
    }
}
