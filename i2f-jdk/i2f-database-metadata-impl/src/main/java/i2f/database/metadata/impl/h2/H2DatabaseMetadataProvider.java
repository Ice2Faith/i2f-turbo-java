package i2f.database.metadata.impl.h2;

import i2f.database.metadata.data.ColumnMeta;
import i2f.database.metadata.data.IndexColumnMeta;
import i2f.database.metadata.data.IndexMeta;
import i2f.database.metadata.data.TableMeta;
import i2f.database.metadata.impl.base.BaseDatabaseMetadataProvider;
import i2f.database.metadata.impl.mysql.MySqlType;
import i2f.database.metadata.std.StdType;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.data.QueryResult;
import i2f.text.StringUtils;

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
public class H2DatabaseMetadataProvider extends BaseDatabaseMetadataProvider {
    public static final H2DatabaseMetadataProvider INSTANCE = new H2DatabaseMetadataProvider();

    public static final String DRIVER_NAME = "org.h2.Driver";

    public static final String JDBC_URL = "jdbc:h2:file:../h2/test.h2.db";

    public static final String MAVEN_DEPENDENCY = "<dependency>\n" +
            "            <groupId>com.h2database</groupId>\n" +
            "            <artifactId>h2</artifactId>\n" +
            "            <version>2.2.224</version>\n" +
            "        </dependency>";

    @Override
    public String detectDefaultDatabase(String jdbcUrl) {
        return "PUBLIC";
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
        return super.getSchemas(conn);
    }

    @Override
    public TableMeta parseTable(Map<String, Object> row) {
        TableMeta table = new TableMeta();
        table.setCatalog(asString(row.get("TABLE_CAT"), null));
        table.setSchema(asString(row.get("TABLE_SCHEM"), null));
        table.setDatabase(table.getSchema());
        table.setName(asString(row.get("TABLE_NAME"), null));
        table.setComment(asString(row.get("REMARKS"), null));
        table.setType(asString(row.get("TABLE_TYPE"), null));

        return table;
    }

    @Override
    public ResultSet getTables(DatabaseMetaData metaData, String database) throws SQLException {
        return metaData.getTables(null, database, null, null);
    }

    @Override
    public QueryResult getTablesComment(Connection conn, String database) throws SQLException {
        String sql = " select TABLE_CATALOG,TABLE_SCHEMA,TABLE_NAME,TABLE_TYPE,REMARKS\n" +
                " from INFORMATION_SCHEMA.TABLES \n" +
                "where TABLE_SCHEMA  = ? ";
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
    public QueryResult getTableInfo(DatabaseMetaData metaData, String database, String table) throws SQLException {
        ResultSet rs = metaData.getTables(null, database, table, null);
        QueryResult ret = JdbcResolver.parseResultSet(rs);
        if(!ret.getRows().isEmpty()){
            return ret;
        }
        if(table!=null){
            rs = metaData.getTables(null, database, table.toLowerCase(), null);
            ret = JdbcResolver.parseResultSet(rs);
        }
        return ret;
    }

    @Override
    public QueryResult getColumns(DatabaseMetaData metaData, String database, String table) throws SQLException {
        ResultSet rs = metaData.getColumns(null, database, table, null);
        QueryResult ret = JdbcResolver.parseResultSet(rs);
        if(!ret.getRows().isEmpty()){
            return ret;
        }
        if(table!=null){
            metaData.getColumns(null, database, table.toLowerCase(), null);
            ret = JdbcResolver.parseResultSet(rs);
        }
        return ret;
    }

    @Override
    public QueryResult getPrimaryKeys(DatabaseMetaData metaData, String database, String table) throws SQLException {
        ResultSet rs = metaData.getPrimaryKeys(null, database, table);
        QueryResult ret = JdbcResolver.parseResultSet(rs);
        if(!ret.getRows().isEmpty()){
            return ret;
        }
        if(table!=null){
            rs = metaData.getPrimaryKeys(null, database, table.toLowerCase());
            ret = JdbcResolver.parseResultSet(rs);
        }
        return ret;
    }

    @Override
    public QueryResult getIndexInfo(DatabaseMetaData metaData, String database, String table) throws SQLException {
        ResultSet rs = metaData.getIndexInfo(null, database, table, false, false);
        QueryResult ret = JdbcResolver.parseResultSet(rs);
        if(!ret.getRows().isEmpty()){
            return ret;
        }
        if(table!=null){
            rs = metaData.getIndexInfo(null, database, table.toLowerCase(), false, false);
            ret = JdbcResolver.parseResultSet(rs);
        }
        return ret;
    }

    @Override
    public QueryResult getColumnsComment(Connection conn, String database, String table) throws SQLException {
        List<Object> args = new ArrayList<>();
        args.add(table);
        String sql = " select TABLE_SCHEMA,TABLE_NAME,COLUMN_NAME,ORDINAL_POSITION,REMARKS, \n" +
                " COLUMN_DEFAULT,IS_NULLABLE, \n" +
                " CASE WHEN DATA_TYPE='CHARACTER VARYING' THEN 'VARCHAR' ELSE DATA_TYPE END AS DATA_TYPE,\n" +
                " CHARACTER_MAXIMUM_LENGTH,NUMERIC_PRECISION,NUMERIC_SCALE \n" +
                " from information_schema.COLUMNS \n" +
                " where TABLE_NAME = ? \n";
        if (!StringUtils.isEmpty(database)) {
            sql += " and TABLE_SCHEMA  = ? \n";
            args.add(database);
        }

        sql += " order by ORDINAL_POSITION ";

        return JdbcResolver.query(conn, sql, args);
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
        String typeName = asString(row.get("TYPE_NAME"), null);
        if ("CHARACTER VARYING".equals(typeName)) {
            typeName = "VARCHAR";
        }
        ColumnMeta col = new ColumnMeta();
        col.setIndex(asInteger(row.get("ORDINAL_POSITION"), 0));
        col.setName(asString(row.get("COLUMN_NAME"), null));
        col.setType(typeName);
        if (col.getType() != null) {
            col.setType(col.getType().split("\\(", 2)[0]);
        }
        col.setComment(asString(row.get("REMARKS"), null));
        col.setPrecision(asInteger(row.get("COLUMN_SIZE"), 0));
        col.setScale(asInteger(row.get("DECIMAL_DIGITS"), 0));
        col.setNullable(("YES".equals(asString(row.get("IS_NULLABLE"), null))));
        col.setAutoIncrement(("YES".equals(asString(row.get("IS_AUTOINCREMENT"), null))));
        col.setGenerated(("YES".equals(asString(row.get("IS_GENERATEDCOLUMN"), null))));
        col.setDefaultValue(asString(row.get("COLUMN_DEF"), null));

        StdType type = StdType.VARCHAR;
        MySqlType mysqlType = null;
        for (MySqlType item : MySqlType.values()) {
            if (item.text().equalsIgnoreCase(col.getType())) {
                type = item.stdType();
                mysqlType = item;
                break;
            }
        }

        String columnType = col.getType();
        if (mysqlType != null) {
            if (mysqlType.precision()) {
                columnType += "(" + col.getPrecision();
                if (mysqlType.scale()) {
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

        col.setRawDialectType(mysqlType);
        col.setRawJavaType(type.javaType());
        col.setJavaType(type.javaType().getSimpleName());
        col.setRawJdbcType(type.jdbcType());
        col.setJdbcType(type.jdbcType().getName());
        col.setRawStdType(type);
        col.setStdType(type.text());
        col.setRawLooseJavaType(type.looseJavaType());
        col.setLooseJavaType(type.looseJavaType().getSimpleName());
        col.setRawLooseJdbcType(type.looseJdbcType());
        col.setLooseJdbcType(type.looseJdbcType().getName());
        return col;
    }

    @Override
    public void parsePrimaryKey(DatabaseMetaData metaData, TableMeta ret) throws SQLException {
        QueryResult result = getPrimaryKeys(metaData, ret.getDatabase(), ret.getName());
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
        QueryResult result = getIndexInfo(metaData, ret.getDatabase(), ret.getName());

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
            if ("PRIMARY".equalsIgnoreCase(meta.getName())) {
                ret.setPrimary(meta);
            } else if (meta.isUnique()) {
                ret.getUniqueIndexes().add(meta);
            } else {
                ret.getIndexes().add(meta);
            }
        }
    }

}
