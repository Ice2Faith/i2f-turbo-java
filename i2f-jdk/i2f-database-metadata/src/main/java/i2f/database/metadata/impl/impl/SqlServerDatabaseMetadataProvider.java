package i2f.database.metadata.impl.impl;

import i2f.database.metadata.data.ColumnMeta;
import i2f.database.metadata.data.IndexColumnMeta;
import i2f.database.metadata.data.IndexMeta;
import i2f.database.metadata.data.TableMeta;
import i2f.database.metadata.impl.base.BaseDatabaseMetadataProvider;
import i2f.database.metadata.std.StdType;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.data.QueryResult;
import i2f.url.FormUrlEncodedEncoder;
import i2f.url.UriMeta;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2024/3/14 10:56
 * @desc
 */
public class SqlServerDatabaseMetadataProvider extends BaseDatabaseMetadataProvider {

    public static final String DRIVER_NAME = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    public static final String JDBC_URL = "=jdbc:sqlserver://localhost:1433;databaseName=test_db";

    public static final String MAVEN_DEPENDENCY = "<dependency>\n" +
            "            <groupId>com.microsoft.sqlserver</groupId>\n" +
            "            <artifactId>mssql-jdbc</artifactId>\n" +
            "            <version>7.4.1.jre8</version>\n" +
            "        </dependency>";

    @Override
    public String detectDefaultDatabase(String jdbcUrl) {
        UriMeta meta = UriMeta.parseJdbcSqlserver(jdbcUrl);
        String query = meta.getQuery();
        if (query == null || query.isEmpty()) {
            return null;
        }
        Map<String, Object> map = FormUrlEncodedEncoder.ofFormMapTree(query);
        String databaseName = (String) map.get("databaseName");
        if (databaseName == null || databaseName.isEmpty()) {
            return null;
        }
        return databaseName;
    }

    @Override
    public String extractTypeName(Map<String, Object> row) {
        return asString(row.get("TYPE_NAME"), null);
    }

    @Override
    public String extractCatalog(Map<String, Object> row) {
        return asString(row.get("TABLE_CAT"), null);
    }

    @Override
    public String extractSchema(Map<String, Object> row) {
        return asString(row.get("TABLE_SCHEM"), null);
    }

    @Override
    public List<String> getDatabases(Connection conn) throws SQLException {
        return super.getCatalogs(conn);
    }

    @Override
    public TableMeta parseTable(Map<String, Object> row) {
        TableMeta table = new TableMeta();
        table.setCatalog(asString(row.get("TABLE_CAT"), null));
        table.setSchema(asString(row.get("TABLE_SCHEM"), null));
        table.setDatabase(table.getCatalog());
        table.setName(asString(row.get("TABLE_NAME"), null));
        table.setComment(asString(row.get("REMARKS"), null));
        table.setType(asString(row.get("TABLE_TYPE"), null));

        return table;
    }

    public boolean filterTableMeta(TableMeta tableMeta) {
        String schema = tableMeta.getSchema();
        if (Arrays.asList("INFORMATION_SCHEMA", "sys").contains(schema)) {
            return false;
        }
        return true;
    }

    @Override
    public ResultSet getTables(DatabaseMetaData metaData, String database) throws SQLException {
        return metaData.getTables(database, null, null, null);
    }

    @Override
    public QueryResult getTablesComment(Connection conn, String database) throws SQLException {
        String sql = "SELECT a.name as TABLE_NAME,\n" +
                "b.value as REMARKS,\n" +
                "c.TABLE_CATALOG,\n" +
                "c.TABLE_SCHEMA\n" +
                "FROM sys.tables a\n" +
                "left join sys.extended_properties b on a.object_id = b.major_id\n" +
                "left join INFORMATION_SCHEMA.TABLES c on a.name =c.TABLE_NAME\n" +
                "where b.minor_id =0\n" +
                "and a.type = 'U'\n" +
                "and c.TABLE_CATALOG = ?";
        QueryResult result = JdbcResolver.query(conn, sql, Arrays.asList(database));
        return result;
    }

    @Override
    public String extractTablesCommentTableName(Map<String, Object> row) {
        return asString(row.get("TABLE_NAME"), null);
    }

    @Override
    public String extractTablesCommentTableComment(Map<String, Object> row) {
        return asString(row.get("REMARKS"), null);
    }

    @Override
    public ResultSet getTableInfo(DatabaseMetaData metaData, String database, String table) throws SQLException {
        return metaData.getTables(database, null, table, null);
    }

    @Override
    public ResultSet getColumns(DatabaseMetaData metaData, String database, String table) throws SQLException {
        return metaData.getColumns(database, null, table, null);
    }

    @Override
    public ResultSet getPrimaryKeys(DatabaseMetaData metaData, String database, String table) throws SQLException {
        return metaData.getPrimaryKeys(database, null, table);
    }

    @Override
    public ResultSet getIndexInfo(DatabaseMetaData metaData, String database, String table) throws SQLException {
        return metaData.getIndexInfo(database, null, table, false, false);
    }

    @Override
    public QueryResult getColumnsComment(Connection conn, String database, String table) throws SQLException {
        String sql = "SELECT a.object_id,\n" +
                "a.name as COLUMN_NAME,\n" +
                "a.column_id,a.system_type_id,a.user_type_id,\n" +
                "a.max_length,a.precision,a.scale,\n" +
                "b.name as TABLE_NAME,\n" +
                "c.value as REMARKS,\n" +
                "d.TABLE_CATALOG,\n" +
                "d.TABLE_SCHEMA\n" +
                "FROM sys.columns a\n" +
                "left join sys.tables b on a.object_id =b.object_id \n" +
                "left join sys.extended_properties c on b.object_id = c.major_id and a.column_id =c.minor_id \n" +
                "left join INFORMATION_SCHEMA.TABLES d on b.name =d.TABLE_NAME\n" +
                "where b.type='U'\n" +
                "and d.TABLE_CATALOG = ? \n" +
                "and b.name = ? \n" +
                "order by a.column_id ";
        return JdbcResolver.query(conn, sql, Arrays.asList(database, table));
    }

    @Override
    public String extractColumnsCommentColumnName(Map<String, Object> row) {
        return asString(row.get("COLUMN_NAME"), null);
    }

    @Override
    public String extractColumnsCommentColumnComment(Map<String, Object> row) {
        return asString(row.get("REMARKS"), null);
    }

    @Override
    public ColumnMeta parseColumn(Map<String, Object> row) {
        ColumnMeta col = new ColumnMeta();
        col.setIndex(asInteger(row.get("ORDINAL_POSITION"), 0));
        col.setName(asString(row.get("COLUMN_NAME"), null));
        col.setType(asString(row.get("TYPE_NAME"), null));
        if (col.getType() != null) {
            col.setType(col.getType().split("\\(", 2)[0]);
        }
        col.setComment(asString(row.get("REMARKS"), null));
        col.setPrecision(asInteger(row.get("COLUMN_SIZE"), 0));
        col.setScale(asInteger(row.get("DECIMAL_DIGITS"), 0));
        col.setNullable(("YES".equals(asString(row.get("IS_NULLABLE"), null))));
        col.setAutoIncrement(("YES".equals(asString(row.get("IS_AUTOINCREMENT"), null))));
        col.setGenerated(("YES".equals(asString(row.get("IS_GENERATEDCOLUMN"), null))));
        String defVal = asString(row.get("COLUMN_DEF"), null);
        if (defVal != null) {
            while (defVal.startsWith("(")) {
                defVal = defVal.substring(1, defVal.length() - 1);
            }
        }
        col.setDefaultValue(defVal);


        StdType type = StdType.VARCHAR;
        SqlServerType sqlServerType = null;
        for (SqlServerType item : SqlServerType.values()) {
            if (item.text().equalsIgnoreCase(col.getType())) {
                type = item.stdType();
                sqlServerType = item;
                break;
            }
        }

        String columnType = col.getType();
        if (sqlServerType != null) {
            if (sqlServerType.precision()) {
                columnType += "(" + col.getPrecision();
                if (sqlServerType.scale()) {
                    columnType += "," + col.getScale();
                }
                columnType += ")";
            }
        } else {
            if (type.precision()) {
                columnType += "(" + col.getPrecision();
                if (type.scale()) {
                    columnType += "," + col.getScale();
                }
                columnType += ")";
            }
        }
        col.setColumnType(columnType);

        col.setJavaType(type.javaType().getSimpleName());
        col.setJdbcType(type.jdbcType().getName());
        col.setStdType(type.text());
        col.setLooseJavaType(type.looseJavaType().getSimpleName());
        col.setLooseJdbcType(type.looseJdbcType().getName());
        return col;
    }

    @Override
    public void parsePrimaryKey(DatabaseMetaData metaData, TableMeta ret) throws SQLException {
        ResultSet rs = getPrimaryKeys(metaData, ret.getDatabase(), ret.getName());
        QueryResult result = JdbcResolver.parseResultSet(rs);
        IndexMeta primary = null;
        for (Map<String, Object> row : result.getRows()) {
            if (primary == null) {
                primary = new IndexMeta();
                List<IndexColumnMeta> primaryColumns = new ArrayList<>();
                primary.setColumns(primaryColumns);
            }
            if (primary.getName() == null) {
                primary.setName(asString(row.get("PK_NAME"), null));
                primary.setUnique(true);
            }
            IndexColumnMeta meta = new IndexColumnMeta();
            meta.setDesc(false);
            meta.setIndex(asInteger(row.get("KEY_SEQ"), 0));
            meta.setName(asString(row.get("COLUMN_NAME"), null));
            primary.getColumns().add(meta);
        }
        ret.setPrimary(primary);
    }

    @Override
    public void parseIndexes(DatabaseMetaData metaData, TableMeta ret) throws SQLException {
        ResultSet rs = getIndexInfo(metaData, ret.getDatabase(), ret.getName());
        QueryResult result = JdbcResolver.parseResultSet(rs);

        Map<String, IndexMeta> indexMap = new LinkedHashMap<>();
        for (Map<String, Object> row : result.getRows()) {
            String indexName = asString(row.get("INDEX_NAME"), null);
            if (indexName == null) {
                continue;
            }
            if (!indexMap.containsKey(indexName)) {
                IndexMeta meta = new IndexMeta();
                meta.setName(indexName);
                Boolean nonUnique = asBoolean(row.get("NON_UNIQUE"), null);
                if (nonUnique == null) {
                    nonUnique = true;
                }
                meta.setUnique(!nonUnique);
                meta.setColumns(new ArrayList<>());
                indexMap.put(indexName, meta);
            }
            IndexColumnMeta cm = new IndexColumnMeta();
            cm.setName(asString(row.get("COLUMN_NAME"), null));
            cm.setIndex(asInteger(row.get("ORDINAL_POSITION"), 0));
            cm.setDesc(!"A".equalsIgnoreCase(asString(row.get("ASC_OR_DESC"), null)));
            Integer type = asInteger(row.get("TYPE"), -1);
            cm.setType(getIndexType(type));
            indexMap.get(indexName).getColumns().add(cm);
        }

        ret.setUniqueIndexes(new ArrayList<>());
        ret.setIndexes(new ArrayList<>());
        for (Map.Entry<String, IndexMeta> entry : indexMap.entrySet()) {
            IndexMeta meta = entry.getValue();
            if (meta.getName().startsWith("PK__")) {
                ret.setPrimary(meta);
            } else if (meta.isUnique()) {
                ret.getUniqueIndexes().add(meta);
            } else {
                ret.getIndexes().add(meta);
            }
        }
    }

}
