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
public class Db2PageWrapper implements IPageWrapper {
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

            builder.append(" SELECT * FROM ( ")
                    .append(" SELECT TMP_PAGE.*,ROWNUMBER() OVER() AS ROW_ID FROM ( ")
                    .append(bql.getSql())
                    .append(" ) AS TMP_PAGE ")
                    .append(" ) TMP_PAGE ")
                    .append(" WHERE ROW_ID >= ").append(embed ? (page.getOffset() + 1) : "?")
                    .append(" AND ROW_ID < ").append(embed ? (page.getEnd() + 1) : "?").append(" ");

            if (!embed) {
                pageSql.getArgs().add(page.getOffset() + 1);
                pageSql.getArgs().add(page.getEnd() + 1);
            }
        } else if (page.getOffset() != null) {
            builder.append(" SELECT * FROM ( ")
                    .append(" SELECT TMP_PAGE.*,ROWNUMBER() OVER() AS ROW_ID FROM ( ")
                    .append(bql.getSql())
                    .append(" ) AS TMP_PAGE ")
                    .append(" ) TMP_PAGE ")
                    .append(" WHERE ROW_ID >= ").append(embed ? (page.getOffset() + 1) : "?").append(" ");

            if (!embed) {
                pageSql.getArgs().add(page.getOffset() + 1);
            }
        } else if (page.getSize() != null) {
            builder.append(" SELECT * FROM ( ")
                    .append(" SELECT TMP_PAGE.*,ROWNUMBER() OVER() AS ROW_ID FROM ( ")
                    .append(bql.getSql())
                    .append(" ) AS TMP_PAGE ")
                    .append(" ) TMP_PAGE ")
                    .append(" WHERE ROW_ID < ").append(embed ? (page.getSize() + 1) : "?").append(" ");

            if (!embed) {
                pageSql.getArgs().add(page.getSize() + 1);
            }
        }

        pageSql.setSql(builder.toString());
        return pageSql;
    }
}
