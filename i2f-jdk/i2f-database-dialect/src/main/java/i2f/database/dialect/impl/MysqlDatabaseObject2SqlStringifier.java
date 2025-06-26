package i2f.database.dialect.impl;

import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2025/6/26 14:06
 */
public class MysqlDatabaseObject2SqlStringifier extends AbsDatabaseObject2SqlStringifier {
    public static final MysqlDatabaseObject2SqlStringifier INSTANCE = new MysqlDatabaseObject2SqlStringifier();

    @Override
    public boolean support(String databaseType) {
        if (databaseType == null) {
            return false;
        }
        String name = databaseType.toLowerCase().trim();
        if (name.startsWith("mysql")) {
            return true;
        }
        if (name.startsWith("maria")) {
            return true;
        }
        if (name.startsWith("gbase")) {
            return true;
        }
        return Arrays.asList(
                "mysql",
                "mariadb",
                "maria",
                "gbase"
        ).contains(name);
    }

    @Override
    public String toSql(Object value) {
        return null;
    }

}
