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
public class OraclePageWrapper implements IPageWrapper {
    public static final OraclePageWrapper INSTANCE = new OraclePageWrapper();

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
        if (page.getOffset() != null && page.getEnd() != null) {

            builder.append(" SELECT * \n")
                    .append(" FROM (SELECT ROWNUM PAGE_ROW_ID,TMP_PAGE.* \n")
                    .append(" FROM ( \n")
                    .append(bql.getSql())
                    .append("\n ) TMP_PAGE \n")
                    .append(" WHERE ROWNUM < ").append(embed ? (page.getEnd() + 1) : "?").append(" ) TMP \n")
                    .append(" WHERE PAGE_ROW_ID >= ").append(embed ? (page.getOffset() + 1) : "?").append(" ");

            if (!embed) {
                pageSql.getArgs().add(page.getEnd() + 1);
                pageSql.getArgs().add(page.getOffset() + 1);
            }
        } else if (page.getOffset() != null) {
            builder.append(" SELECT TMP_PAGE.* \n")
                    .append(" FROM ( \n")
                    .append(bql.getSql())
                    .append("\n ) TMP_PAGE \n")
                    .append(" WHERE ROWNUM >= ").append(embed ? (page.getOffset() + 1) : "?").append(" ");

            if (!embed) {
                pageSql.getArgs().add(page.getOffset() + 1);
            }
        } else if (page.getEnd() != null) {
            builder.append(" SELECT TMP_PAGE.* \n")
                    .append(" FROM ( \n")
                    .append(bql.getSql())
                    .append("\n ) TMP_PAGE \n")
                    .append(" WHERE ROWNUM < ").append(embed ? (page.getEnd() + 1) : "?").append(" ");

            if (!embed) {
                pageSql.getArgs().add(page.getEnd() + 1);
            }
        }

        pageSql.setSql(builder.toString());
        return pageSql;
    }
}
