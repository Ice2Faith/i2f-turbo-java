package i2f.database.metadata.reverse.ddl;

import i2f.database.metadata.reverse.ddl.impl.MysqlDdlDatabaseReverseEngineer;
import i2f.database.metadata.reverse.ddl.impl.OracleDdlDatabaseReverseEngineer;
import i2f.database.metadata.reverse.ddl.impl.PostgreDdlDatabaseReverseEngineer;
import i2f.database.metadata.reverse.ddl.impl.StdDdlDatabaseReverseEngineer;
import i2f.database.type.DatabaseType;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Ice2Faith
 * @date 2025/7/30 11:02
 */
public class DdlDatabaseReverseEngineers {
    public static DdlDatabaseReverseEngineer getEngineer(Connection conn) throws SQLException {
        DatabaseType databaseType = DatabaseType.typeOfConnection(conn);
        return getEngineer(databaseType);
    }

    public static DdlDatabaseReverseEngineer getEngineer(DatabaseType type) throws SQLException {

        switch (type) {
            case MYSQL:
                return MysqlDdlDatabaseReverseEngineer.INSTANCE;
            case ORACLE:
                return OracleDdlDatabaseReverseEngineer.INSTANCE;
            case MARIADB:
                return MysqlDdlDatabaseReverseEngineer.INSTANCE;
            case GBASE:
                return MysqlDdlDatabaseReverseEngineer.INSTANCE;
//            case OSCAR:
//                return PostgreDdlDatabaseReverseEngineer.INSTANCE;
//            case XU_GU:
//                return MysqlDdlDatabaseReverseEngineer.INSTANCE;
            case CLICK_HOUSE:
                return MysqlDdlDatabaseReverseEngineer.INSTANCE;
            case OCEAN_BASE:
                return MysqlDdlDatabaseReverseEngineer.INSTANCE;
            case POSTGRE_SQL:
                return PostgreDdlDatabaseReverseEngineer.INSTANCE;
            case H2:
                return MysqlDdlDatabaseReverseEngineer.INSTANCE;
//            case SQLITE:
//                return PostgreDdlDatabaseReverseEngineer.INSTANCE;
//            case HSQL:
//                return PostgreDdlDatabaseReverseEngineer.INSTANCE;
//            case KINGBASE_ES:
//                return PostgreDdlDatabaseReverseEngineer.INSTANCE;
            case PHOENIX:
                return MysqlDdlDatabaseReverseEngineer.INSTANCE;
            case DM:
                return OracleDdlDatabaseReverseEngineer.INSTANCE;
//            case GAUSS:
//                return OracleDdlDatabaseReverseEngineer.INSTANCE;
            case ORACLE_12C:
                return OracleDdlDatabaseReverseEngineer.INSTANCE;
//            case DB2:
//                return MysqlDdlDatabaseReverseEngineer.INSTANCE;
//            case SQL_SERVER:
//                return MysqlDdlDatabaseReverseEngineer.INSTANCE;
//            case FIREBIRD:
//                return MysqlDdlDatabaseReverseEngineer.INSTANCE;
//            case HighGo:
//                return PostgreDdlDatabaseReverseEngineer.INSTANCE;
            case Hive:
                return MysqlDdlDatabaseReverseEngineer.INSTANCE;
//            case YaShanDB:
//                return PostgreDdlDatabaseReverseEngineer.INSTANCE;
//            case TDengine:
//                return MysqlDdlDatabaseReverseEngineer.INSTANCE;
//            case HerdDB:
//                return MysqlDdlDatabaseReverseEngineer.INSTANCE;
//            case CirroData:
//                return MysqlDdlDatabaseReverseEngineer.INSTANCE;
//            case IbmAs400:
//                return MysqlDdlDatabaseReverseEngineer.INSTANCE;
//            case Informix:
//                return MysqlDdlDatabaseReverseEngineer.INSTANCE;
//            case Snowflake:
//                return PostgreDdlDatabaseReverseEngineer.INSTANCE;
//            case Databricks:
//                return PostgreDdlDatabaseReverseEngineer.INSTANCE;
//            case RedShift:
//                return PostgreDdlDatabaseReverseEngineer.INSTANCE;
//            case Trino:
//                return PostgreDdlDatabaseReverseEngineer.INSTANCE;
            default:
                return StdDdlDatabaseReverseEngineer.INSTANCE;
        }
    }
}
