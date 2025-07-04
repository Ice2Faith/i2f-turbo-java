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
public class InformixPageWrapper implements IPageWrapper {
    public static final InformixPageWrapper INSTANCE = new InformixPageWrapper();

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

            builder.append(" select ")
                    .append(" skip ").append(embed ? (page.getOffset()) : "?")
                    .append(" first ").append(embed ? (page.getSize()) : "?")
                    .append(" * from ( ")
                    .append(bql.getSql())
                    .append(" ) TMP_PAGE ");

            if (!embed) {
                pageSql.getArgs().add(page.getOffset());
                pageSql.getArgs().add(page.getSize());
            }
        } else if (page.getOffset() != null) {

            builder.append(" select ")
                    .append(" skip ").append(embed ? (page.getOffset()) : "?")
                    .append(" * from ( ")
                    .append(bql.getSql())
                    .append(" ) TMP_PAGE ");

            if (!embed) {
                pageSql.getArgs().add(page.getOffset());
            }
        } else if (page.getSize() != null) {

            builder.append(" select ")
                    .append(" first ").append(embed ? (page.getSize()) : "?")
                    .append(" * from ( ")
                    .append(bql.getSql())
                    .append(" ) TMP_PAGE ");

            if (!embed) {
                pageSql.getArgs().add(page.getSize());
            }
        }

        pageSql.setSql(builder.toString());
        return pageSql;
    }
}
