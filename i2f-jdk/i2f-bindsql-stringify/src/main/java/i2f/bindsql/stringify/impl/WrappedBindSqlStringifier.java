package i2f.bindsql.stringify.impl;

import i2f.bindsql.BindSql;
import i2f.bindsql.stringify.BindSqlStringifier;
import i2f.database.dialect.DatabaseObject2SqlStringifier;
import i2f.database.dialect.impl.dialect.DefaultDatabaseObject2SqlStringifier;
import i2f.match.regex.RegexUtil;

import java.util.Iterator;

/**
 * @author Ice2Faith
 * @date 2025/3/20 14:56
 */
public class WrappedBindSqlStringifier implements BindSqlStringifier {
    public static final WrappedBindSqlStringifier INSTANCE = new WrappedBindSqlStringifier(DefaultDatabaseObject2SqlStringifier.INSTANCE);
    protected DatabaseObject2SqlStringifier object2SqlStringifier;

    public WrappedBindSqlStringifier(DatabaseObject2SqlStringifier object2SqlStringifier) {
        this.object2SqlStringifier = object2SqlStringifier;
    }

    @Override
    public String stringify(BindSql bql) {
        Iterator<Object> iterator = bql.getArgs().iterator();
        return RegexUtil.regexFindAndReplace(bql.getSql(), "\\?", (str) -> {
            Object obj = iterator.next();
            return paramToString(obj);
        });
    }


    public String paramToString(Object obj) {
        String ret = preParamToString(obj);
        if (ret != null) {
            return ret;
        }
        return object2SqlStringifier.stringify(obj);
    }

    public String preParamToString(Object obj) {
        return null;
    }

}
