package i2f.database.dialect.impl;


import i2f.database.dialect.DatabaseObject2SqlStringifier;
import i2f.database.dialect.util.DatabaseDialectUtil;

/**
 * @author Ice2Faith
 * @date 2025/6/26 14:06
 */
public abstract class AbsDatabaseObject2SqlStringifier implements DatabaseObject2SqlStringifier {

    @Override
    public String stringify(Object value) {
        String str = toSql(value);
        if (str != null) {
            return str;
        }
        return DatabaseDialectUtil.typeStringifier(value);
    }

    public String toSql(Object value) {
        return null;
    }
}
