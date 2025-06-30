package i2f.database.dialect;

import i2f.database.dialect.impl.dialect.*;

import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2025/6/26 14:52
 */
public class DatabaseObject2SqlStringifiers {
    public static final CopyOnWriteArrayList<DatabaseObject2SqlStringifier> stringifiers = new CopyOnWriteArrayList<>();

    static {
        ServiceLoader<DatabaseObject2SqlStringifier> iter = ServiceLoader.load(DatabaseObject2SqlStringifier.class);
        for (DatabaseObject2SqlStringifier item : iter) {
            stringifiers.add(item);
        }
        stringifiers.add(Db2DatabaseObject2SqlStringifier.INSTANCE);
        stringifiers.add(GuassDatabaseObject2SqlStringifier.INSTANCE);
        stringifiers.add(H2DatabaseObject2SqlStringifier.INSTANCE);
        stringifiers.add(HiveDatabaseObject2SqlStringifier.INSTANCE);
        stringifiers.add(MysqlDatabaseObject2SqlStringifier.INSTANCE);
        stringifiers.add(OracleDatabaseObject2SqlStringifier.INSTANCE);
        stringifiers.add(PhoenixDatabaseObject2SqlStringifier.INSTANCE);
        stringifiers.add(PostgreSqlDatabaseObject2SqlStringifier.INSTANCE);
        stringifiers.add(SqlServerDatabaseObject2SqlStringifier.INSTANCE);
    }

    public static DatabaseObject2SqlStringifier getStringifier(String databaseType) {
        for (DatabaseObject2SqlStringifier item : stringifiers) {
            if (item.support(databaseType)) {
                return item;
            }
        }
        return DefaultDatabaseObject2SqlStringifier.INSTANCE;
    }

    public static String stringify(Object value, String databaseType) {
        DatabaseObject2SqlStringifier stringifier = getStringifier(databaseType);
        return stringifier.stringify(value);
    }
}
