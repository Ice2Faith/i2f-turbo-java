package i2f.database.metadata.impl.postgresql;

import i2f.database.metadata.data.ColumnMeta;
import i2f.database.metadata.data.IndexColumnMeta;
import i2f.database.metadata.data.IndexMeta;
import i2f.database.metadata.data.TableMeta;
import i2f.database.metadata.impl.base.BaseDatabaseMetadataProvider;
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
public class PostgreSqlDatabaseMetadataProvider extends BaseDatabaseMetadataProvider {
    public static final PostgreSqlDatabaseMetadataProvider INSTANCE = new PostgreSqlDatabaseMetadataProvider();

    public static final String DRIVER_NAME = "org.postgresql.Driver";

    public static final String JDBC_URL = "jdbc:postgresql://localhost:5432/test_db";

    public static final String MAVEN_DEPENDENCY = "<dependency>\n" +
            "            <groupId>org.postgresql</groupId>\n" +
            "            <artifactId>postgresql</artifactId>\n" +
            "            <version>42.6.0</version>\n" +
            "        </dependency>";

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
        return asString(row.get("table_schem"), null);
    }

    @Override
    public List<String> getDatabases(Connection conn) throws SQLException {
        return super.getCatalogs(conn);
    }

    @Override
    public TableMeta parseTable(Map<String, Object> row) {
        TableMeta table = new TableMeta();
        table.setCatalog((String) row.get("TABLE_CAT"));
        table.setSchema((String) row.get("TABLE_SCHEM"));
        table.setDatabase(null);
        table.setName((String) row.get("TABLE_NAME"));
        table.setComment((String) row.get("REMARKS"));
        table.setType((String) row.get("TABLE_TYPE"));

        return table;
    }

    @Override
    public ResultSet getTables(DatabaseMetaData metaData, String database) throws SQLException {
        return metaData.getTables(database, null, null, new String[]{"TABLE"});
    }

    @Override
    public QueryResult getTablesComment(Connection conn, String database) throws SQLException {
        String sql = "SELECT tb.table_name, d.description,tb.table_catalog,tb.table_schema,tb.table_type\n" +
                "FROM information_schema.tables tb\n" +
                "JOIN pg_class c ON c.relname = tb.table_name\n" +
                "LEFT JOIN pg_description d ON d.objoid = c.oid AND d.objsubid = '0'\n" +
                "WHERE tb.table_catalog = ? \n" +
                "and tb.table_schema = 'public'";
        QueryResult result = JdbcResolver.query(conn, sql, Arrays.asList(database));
        return result;
    }

    @Override
    public String extractTablesCommentTableName(Map<String, Object> row) {
        return asString(row.get("table_name"), null);
    }

    @Override
    public String extractTablesCommentTableComment(Map<String, Object> row) {
        return asString(row.get("description"), null);
    }

    @Override
    public QueryResult getTableInfo(DatabaseMetaData metaData, String database, String table) throws SQLException {
        ResultSet rs = metaData.getTables(database, null, table, null);
        QueryResult ret = JdbcResolver.parseResultSet(rs);
        if(!ret.getRows().isEmpty()){
            return ret;
        }
        if(table!=null){
            rs = metaData.getTables(database, null, table.toLowerCase(), null);
            ret = JdbcResolver.parseResultSet(rs);
        }
        return ret;
    }

    @Override
    public QueryResult getColumns(DatabaseMetaData metaData, String database, String table) throws SQLException {
        ResultSet rs = metaData.getColumns(database, null, table, null);
        QueryResult ret = JdbcResolver.parseResultSet(rs);
        if(!ret.getRows().isEmpty()){
            return ret;
        }
        if(table!=null){
            rs = metaData.getColumns(database, null, table.toLowerCase(), null);
            ret = JdbcResolver.parseResultSet(rs);
        }
        return ret;
    }

    @Override
    public QueryResult getPrimaryKeys(DatabaseMetaData metaData, String database, String table) throws SQLException {
        ResultSet rs = metaData.getPrimaryKeys(database, null, table);
        QueryResult ret = JdbcResolver.parseResultSet(rs);
        if(!ret.getRows().isEmpty()){
            return ret;
        }
        if(table!=null){
            rs = metaData.getPrimaryKeys(database, null, table.toLowerCase());
            ret = JdbcResolver.parseResultSet(rs);
        }
        return ret;
    }

    @Override
    public QueryResult getIndexInfo(DatabaseMetaData metaData, String database, String table) throws SQLException {
        ResultSet rs = metaData.getIndexInfo(database, null, table, false, false);
        QueryResult ret = JdbcResolver.parseResultSet(rs);
        if(!ret.getRows().isEmpty()){
            return ret;
        }
        if(table!=null){
            rs = metaData.getIndexInfo(database, null, table, false, false);
            ret = JdbcResolver.parseResultSet(rs);
        }
        return ret;
    }


    @Override
    public QueryResult getColumnsComment(Connection conn, String database, String table) throws SQLException {
        List<Object> args = new ArrayList<>();
        args.add(table);
        String sql = "SELECT col.table_name, col.column_name, col.ordinal_position, d.description, \n" +
                "col.table_catalog,col.table_schema, \n" +
                "col.column_default,col.is_nullable,col.data_type,col.character_maximum_length, \n" +
                "col.numeric_precision,col.numeric_scale \n" +
                "FROM information_schema.columns col \n" +
                "JOIN pg_class c ON c.relname = col.table_name \n" +
                "LEFT JOIN pg_description d ON d.objoid = c.oid AND d.objsubid = col.ordinal_position \n" +
                "WHERE col.table_name = ? \n";
        if (!StringUtils.isEmpty(database)) {
            sql += "and col.table_catalog = ? \n";
            args.add(database);
        }
        sql += "and col.table_schema = 'public' \n" +
                "ORDER BY col.table_name, col.ordinal_position ";
        return JdbcResolver.query(conn, sql, args);
    }

    @Override
    public String extractColumnsCommentColumnName(Map<String, Object> row) {
        return asString(row.get("column_name"), null);
    }

    @Override
    public String extractColumnsCommentColumnComment(Map<String, Object> row) {
        return asString(row.get("description"), null);
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
        col.setDefaultValue(asString(row.get("COLUMN_DEF"), null));


        StdType type = StdType.VARCHAR;
        PostgreSqlType postgreType = null;
        for (PostgreSqlType item : PostgreSqlType.values()) {
            if (item.text().equalsIgnoreCase(col.getType())) {
                type = item.stdType();
                postgreType = item;
                break;
            }
        }

        String columnType = col.getType();
        if (postgreType != null) {
            if (postgreType.precision()) {
                columnType += "(" + col.getPrecision();
                if (postgreType.scale()) {
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

        col.setRawDialectType(postgreType);
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
                primary.setName(asString(row.get("pk_name"), null));
                primary.setUnique(true);
            }
            IndexColumnMeta meta = new IndexColumnMeta();
            meta.setDesc(false);
            meta.setIndex(asInteger(row.get("key_seq"), 0));
            meta.setName(asString(row.get("column_name"), null));
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
                meta.setUnique(!asBoolean(row.get("NON_UNIQUE"), true));
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
            if (ret.getPrimary() != null) {
                if (meta.getName().equalsIgnoreCase(ret.getPrimary().getName())) {
                    continue;
                }
            }
            if (ret.getPrimary() == null && meta.getName().equalsIgnoreCase(ret.getName() + "_pkey")) {
                ret.setPrimary(meta);
                continue;
            }
            if (meta.isUnique()) {
                ret.getUniqueIndexes().add(meta);
            } else {
                ret.getIndexes().add(meta);
            }
        }
    }


}
