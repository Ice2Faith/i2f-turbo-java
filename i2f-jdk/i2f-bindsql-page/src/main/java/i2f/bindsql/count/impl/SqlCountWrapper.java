package i2f.bindsql.count.impl;

import i2f.bindsql.BindSql;
import i2f.bindsql.count.ICountWrapper;

import java.util.ArrayList;

/**
 * @author Ice2Faith
 * @date 2024/4/28 9:24
 * @desc
 */
public class SqlCountWrapper implements ICountWrapper {
    public static final SqlCountWrapper INSTANCE = new SqlCountWrapper();

    @Override
    public BindSql apply(BindSql bql) {

        BindSql pageSql = new BindSql();
        pageSql.setSql(bql.getSql());
        pageSql.setArgs(new ArrayList<>(bql.getArgs()));

        StringBuilder builder = new StringBuilder();
        builder.append(" select count(1) cnt \n")
                .append(" from ( \n")
                .append(bql.getSql())
                .append("\n ) tmp_cnt ");

        pageSql.setSql(builder.toString());
        return pageSql;
    }
}
