package i2f.bindsql.stringify;

import i2f.bindsql.stringify.impl.WrappedBindSqlStringifier;
import i2f.database.dialect.impl.dialect.*;
import i2f.database.type.DatabaseDialectMapping;
import i2f.database.type.DatabaseType;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ServiceLoader;

/**
 * @author Ice2Faith
 * @date 2025/3/20 15:31
 */
public class BindSqlStringifiers {

    public static final WrappedBindSqlStringifier DEFAULT = WrappedBindSqlStringifier.INSTANCE;

    public static final WrappedBindSqlStringifier MYSQL = new WrappedBindSqlStringifier(MysqlDatabaseObject2SqlStringifier.INSTANCE);
    public static final WrappedBindSqlStringifier ORACLE = new WrappedBindSqlStringifier(OracleDatabaseObject2SqlStringifier.INSTANCE);
    public static final WrappedBindSqlStringifier POSTGRE = new WrappedBindSqlStringifier(PostgreSqlDatabaseObject2SqlStringifier.INSTANCE);
    public static final WrappedBindSqlStringifier H2 = new WrappedBindSqlStringifier(H2DatabaseObject2SqlStringifier.INSTANCE);
    public static final WrappedBindSqlStringifier PHOENIX = new WrappedBindSqlStringifier(PhoenixDatabaseObject2SqlStringifier.INSTANCE);
    public static final WrappedBindSqlStringifier GUASS = new WrappedBindSqlStringifier(GuassDatabaseObject2SqlStringifier.INSTANCE);
    public static final WrappedBindSqlStringifier DB2 = new WrappedBindSqlStringifier(Db2DatabaseObject2SqlStringifier.INSTANCE);
    public static final WrappedBindSqlStringifier SQL_SERVER = new WrappedBindSqlStringifier(SqlServerDatabaseObject2SqlStringifier.INSTANCE);
    public static final WrappedBindSqlStringifier HIVE = new WrappedBindSqlStringifier(HiveDatabaseObject2SqlStringifier.INSTANCE);

    public static DatabaseDialectMapping DIALECT_MAPPING = new DatabaseDialectMapping() {
        @Override
        public void init() {
            redirect(DatabaseType.MARIADB, DatabaseType.MYSQL);
            redirect(DatabaseType.GBASE, DatabaseType.MYSQL);
            redirect(DatabaseType.OSCAR, DatabaseType.MYSQL);
            redirect(DatabaseType.XU_GU, DatabaseType.MYSQL);
            redirect(DatabaseType.CLICK_HOUSE, DatabaseType.MYSQL);
            redirect(DatabaseType.OCEAN_BASE, DatabaseType.MYSQL);
            redirect(DatabaseType.KINGBASE_ES, DatabaseType.MYSQL);
            redirect(DatabaseType.DM, DatabaseType.ORACLE);
            redirect(DatabaseType.ORACLE_12C, DatabaseType.ORACLE);
        }
    };

    public static BindSqlStringifier of(Connection conn) throws SQLException {
        DatabaseType type = DIALECT_MAPPING.dialectOf(conn);
        return of(type);
    }

    public static BindSqlStringifier of(String jdbcUrl) {
        DatabaseType type = DIALECT_MAPPING.dialectOf(jdbcUrl);
        return of(type);
    }

    public static BindSqlStringifier of(DatabaseType type) {
        ServiceLoader<BindSqlStringifier> list = ServiceLoader.load(BindSqlStringifier.class);
        for (BindSqlStringifier item : list) {
            if (item.support(type)) {
                return item;
            }
        }
        type = DIALECT_MAPPING.dialectOf(type);
        switch (type) {
            case MYSQL:
                return MYSQL;
            case ORACLE:
                return ORACLE;
            case POSTGRE_SQL:
                return POSTGRE;
            case H2:
                return H2;
            case PHOENIX:
                return PHOENIX;
            case GAUSS:
                return GUASS;
            case DB2:
                return DB2;
            case SQL_SERVER:
                return SQL_SERVER;
            case Hive:
                return HIVE;
            default:
                return DEFAULT;
        }
    }
}
