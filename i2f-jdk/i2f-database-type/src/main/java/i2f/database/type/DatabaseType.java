package i2f.database.type;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Pattern;

/**
 * @author Ice2Faith
 * @date 2024/4/7 16:52
 * @desc
 */
public enum DatabaseType {

    /**
     * MYSQL
     */
    MYSQL("mysql", "MySql数据库"),
    /**
     * MARIADB
     */
    MARIADB("mariadb", "MariaDB数据库"),
    /**
     * ORACLE
     */
    ORACLE("oracle", "Oracle11g及以下数据库(高版本推荐使用ORACLE_NEW)"),
    /**
     * oracle12c new pagination
     */
    ORACLE_12C("oracle12c", "Oracle12c+数据库"),
    /**
     * DB2
     */
    DB2("db2", "DB2数据库"),
    /**
     * H2
     */
    H2("h2", "H2数据库"),
    /**
     * HSQL
     */
    HSQL("hsql", "HSQL数据库"),
    /**
     * SQLITE
     */
    SQLITE("sqlite", "SQLite数据库"),
    /**
     * POSTGRE
     */
    POSTGRE_SQL("postgresql", "Postgre数据库"),
    /**
     * SQLSERVER2005
     */
    SQL_SERVER2005("sqlserver2005", "SQLServer2005数据库"),
    /**
     * SQLSERVER
     */
    SQL_SERVER("sqlserver", "SQLServer数据库"),
    /**
     * DM
     */
    DM("dm", "达梦数据库"),
    /**
     * xugu
     */
    XU_GU("xugu", "虚谷数据库"),
    /**
     * Kingbase
     */
    KINGBASE_ES("kingbasees", "人大金仓数据库"),
    /**
     * Phoenix
     */
    PHOENIX("phoenix", "Phoenix HBase数据库"),
    /**
     * Gauss
     */
    GAUSS("zenith", "Gauss 数据库"),
    /**
     * ClickHouse
     */
    CLICK_HOUSE("clickhouse", "clickhouse 数据库"),
    /**
     * GBase
     */
    GBASE("gbase", "南大通用数据库"),
    /**
     * Oscar
     */
    OSCAR("oscar", "神通数据库"),
    /**
     * Sybase
     */
    SYBASE("sybase", "Sybase ASE 数据库"),
    /**
     * OceanBase
     */
    OCEAN_BASE("oceanbase", "OceanBase 数据库"),
    /**
     * Firebird
     */
    FIREBIRD("Firebird", "Firebird 数据库"),

    /**
     * HighGo
     */
    HighGo("highgo", "瀚高数据库"),

    /**
     * Hive
     */
    Hive("hive", "Hive数据库"),

    /**
     * YaShanDB
     */
    YaShanDB("YaShanDB", "崖山数据库"),

    /**
     * TDengine
     */
    TDengine("TDengine", "涛思数据库"),

    /**
     * HerdDB
     */
    HerdDB("HerdDB", "HerdDB嵌入式分布式数据库"),

    /**
     * CirroData
     */
    CirroData("CirroData", "东方国信数据库"),

    /**
     * IbmAs400
     */
    IbmAs400("IbmAs400", "IBM AS/400数据库"),

    /**
     * Informix
     */
    Informix("Informix", "Informix数据库"),

    /**
     * Snowflake
     */
    Snowflake("Snowflake", "Snowflake数据库"),

    /**
     * Databricks
     */
    Databricks("Databricks", "Databricks数据库"),

    /**
     * RedShift
     */
    RedShift("RedShift", "RedShift数据库"),

    /**
     * Trino
     */
    Trino("Trino", "Trino分布式协调数据库"),

    /**
     * UNKONWN DB
     */
    OTHER("other", "其他数据库"),

    /**
     * URL参数无法读取或者错误
     */
    ERROR("error", "URL参数读取错误");

    /**
     * 数据库名称
     */
    private final String db;
    /**
     * 描述
     */
    private final String desc;

    public String db() {
        return this.db;
    }

    public String desc() {
        return this.desc;
    }

    private DatabaseType(String db, String desc) {
        this.db = db;
        this.desc = desc;
    }

    /**
     * 获取数据库类型
     *
     * @param dbType 数据库类型字符串
     */
    public static DatabaseType typeOfName(String dbType) {
        for (DatabaseType type : DatabaseType.values()) {
            if (type.db.equalsIgnoreCase(dbType)) {
                return type;
            }
        }
        return OTHER;
    }

    protected static final Map<Connection, DatabaseType> TYPE_MAP = new WeakHashMap<>();

    public static DatabaseType typeOfConnection(Connection conn) throws SQLException {
        DatabaseType type = TYPE_MAP.get(conn);
        if (type != null) {
            return type;
        }
        synchronized (TYPE_MAP) {
            type = TYPE_MAP.get(conn);
            if (type != null) {
                return type;
            }
            type = typeOfJdbcUrl(conn.getMetaData().getURL());
            TYPE_MAP.put(conn, type);
            return type;
        }
    }

    /**
     * 根据连接地址判断数据库类型
     *
     * @param jdbcUrl 连接地址
     * @return ignore
     */
    public static DatabaseType typeOfJdbcUrl(String jdbcUrl) {
        if (jdbcUrl == null || jdbcUrl.trim().isEmpty()) {
            //"Error: The jdbcUrl is Null, Cannot read database type"
            return DatabaseType.ERROR;
        }
        String url = jdbcUrl.toLowerCase();
        if (url.contains(":mysql:") || url.contains(":cobar:")) {
            return DatabaseType.MYSQL;
        } else if (url.contains(":mariadb:")) {
            return DatabaseType.MARIADB;
        } else if (url.contains(":oracle:")) {
            return DatabaseType.ORACLE;
        } else if (url.contains(":sqlserver:") || url.contains(":microsoft:")) {
            return DatabaseType.SQL_SERVER2005;
        } else if (regexFind(":sqlserver\\d*:", url)) { // :sqlserver2012:
            return DatabaseType.SQL_SERVER;
        } else if (url.contains(":postgresql:")) {
            return DatabaseType.POSTGRE_SQL;
        } else if (url.contains(":hsqldb:")) {
            return DatabaseType.HSQL;
        } else if (url.contains(":db2:")) {
            return DatabaseType.DB2;
        } else if (url.contains(":sqlite:")) {
            return DatabaseType.SQLITE;
        } else if (url.contains(":h2:")) {
            return DatabaseType.H2;
        } else if (regexFind(":dm\\d*:", url)) { // :dm8:
            return DatabaseType.DM;
        } else if (url.contains(":xugu:")) {
            return DatabaseType.XU_GU;
        } else if (regexFind(":kingbase\\d*:", url)) {
            return DatabaseType.KINGBASE_ES;
        } else if (url.contains(":phoenix:")) {
            return DatabaseType.PHOENIX;
        } else if (jdbcUrl.contains(":zenith:")) {
            return DatabaseType.GAUSS;
        } else if (jdbcUrl.contains(":gbase:")) {
            return DatabaseType.GBASE;
        } else if (jdbcUrl.contains(":clickhouse:")) {
            return DatabaseType.CLICK_HOUSE;
        } else if (jdbcUrl.contains(":oscar:")) {
            return DatabaseType.OSCAR;
        } else if (jdbcUrl.contains(":sybase:")) {
            return DatabaseType.SYBASE;
        } else if (jdbcUrl.contains(":oceanbase:")) {
            return DatabaseType.OCEAN_BASE;
        } else if (url.contains(":highgo:")) {
            return DatabaseType.HighGo;
        } else if (regexFind(":hive\\d*:", url)) { // :hive2:
            return DatabaseType.Hive;
        } else if (url.contains(":yasdb:")) {
            return DatabaseType.YaShanDB;
        } else if (url.contains(":taos:")
                || url.contains(":taos-ws:")
                || url.contains(":taos-rs:")) {
            return DatabaseType.TDengine;
        } else if (url.contains(":herddb:")) {
            return DatabaseType.HerdDB;
        } else if (url.contains(":xcloud:")) {
            return DatabaseType.CirroData;
        } else if (url.contains(":as400:")) {
            return DatabaseType.IbmAs400;
        } else if (url.contains(":informix-sqli:")) {
            return DatabaseType.Informix;
        } else if (url.contains(":snowflake:")) {
            return DatabaseType.Snowflake;
        } else if (url.contains(":databricks:")) {
            return DatabaseType.Databricks;
        } else if (url.contains(":redshift:")) {
            return DatabaseType.RedShift;
        } else if (url.contains(":trino:")) {
            return DatabaseType.Trino;
        } else {
            //logger.warn("The jdbcUrl is " + jdbcUrl + ", Mybatis Plus Cannot Read Database type or The Database's Not Supported!");
            return DatabaseType.OTHER;
        }
    }

    /**
     * 正则匹配
     *
     * @param regex 正则
     * @param input 字符串
     * @return 验证成功返回 true，验证失败返回 false
     */
    public static boolean regexFind(String regex, CharSequence input) {
        if (null == input) {
            return false;
        }
        return Pattern.compile(regex).matcher(input).find();
    }
}
