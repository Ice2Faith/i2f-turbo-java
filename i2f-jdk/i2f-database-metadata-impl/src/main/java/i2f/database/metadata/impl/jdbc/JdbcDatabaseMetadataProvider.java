package i2f.database.metadata.impl.jdbc;

import i2f.database.metadata.data.ColumnMeta;
import i2f.database.metadata.data.IndexColumnMeta;
import i2f.database.metadata.data.IndexMeta;
import i2f.database.metadata.data.TableMeta;
import i2f.database.metadata.impl.base.BaseDatabaseMetadataProvider;
import i2f.database.metadata.impl.mysql.MySqlType;
import i2f.database.metadata.impl.oracle.OracleType;
import i2f.database.metadata.impl.postgresql.PostgreSqlType;
import i2f.database.metadata.std.StdType;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.data.QueryColumn;
import i2f.jdbc.data.QueryResult;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2024/3/21 16:13
 * @desc
 */
public class JdbcDatabaseMetadataProvider extends BaseDatabaseMetadataProvider {
    public static final JdbcDatabaseMetadataProvider INSTANCE = new JdbcDatabaseMetadataProvider();

    @Override
    public String extractTypeName(Map<String, Object> row) {
        for (Map.Entry<String, Object> entry : row.entrySet()) {
            if ("TYPE_NAME".equalsIgnoreCase(entry.getKey())) {
                return asString(entry.getValue(), null);
            }
        }
        return null;
    }

    @Override
    public String extractCatalog(Map<String, Object> row) {
        for (Map.Entry<String, Object> entry : row.entrySet()) {
            if ("TABLE_CAT".equalsIgnoreCase(entry.getKey())) {
                return asString(entry.getValue(), null);
            }
        }
        return null;
    }

    @Override
    public String extractSchema(Map<String, Object> row) {
        for (Map.Entry<String, Object> entry : row.entrySet()) {
            if ("TABLE_SCHEM".equalsIgnoreCase(entry.getKey())) {
                return asString(entry.getValue(), null);
            }
        }
        return null;
    }

    @Override
    public List<String> getDatabases(Connection conn) throws SQLException {
        return super.getSchemas(conn);
    }

    @Override
    public TableMeta parseTable(Map<String, Object> row) {
        TableMeta table = new TableMeta();
        for (Map.Entry<String, Object> entry : row.entrySet()) {
            if ("TABLE_CAT".equalsIgnoreCase(entry.getKey())) {
                table.setCatalog(asString(entry.getValue(), null));
            } else if ("TABLE_SCHEM".equalsIgnoreCase(entry.getKey())) {
                table.setSchema(asString(entry.getValue(), null));
            } else if ("TABLE_NAME".equalsIgnoreCase(entry.getKey())) {
                table.setName(asString(entry.getValue(), null));
            } else if ("REMARKS".equalsIgnoreCase(entry.getKey())) {
                table.setComment(asString(entry.getValue(), null));
            } else if ("TABLE_TYPE".equalsIgnoreCase(entry.getKey())) {
                table.setType(asString(entry.getValue(), null));
            } else if ("TABLE_COMMENTS".equalsIgnoreCase(entry.getKey())) {
                table.setComment(asString(entry.getValue(), null));
            } else if ("TABLE_COMMENT".equalsIgnoreCase(entry.getKey())) {
                table.setComment(asString(entry.getValue(), null));
            } else if ("COMMENTS".equalsIgnoreCase(entry.getKey())) {
                table.setComment(asString(entry.getValue(), null));
            } else if ("COMMENT".equalsIgnoreCase(entry.getKey())) {
                table.setComment(asString(entry.getValue(), null));
            } else if ("DESCRIPTION".equalsIgnoreCase(entry.getKey())) {
                table.setComment(asString(entry.getValue(), null));
            }
        }
        table.setDatabase(table.getSchema());
        return table;
    }

    @Override
    public ResultSet getTables(DatabaseMetaData metaData, String database,String tablePattern) throws SQLException {
        return metaData.getTables(null, null, tablePattern, null);
    }


    @Override
    public QueryResult getTableInfo(DatabaseMetaData metaData, String database, String table) throws SQLException {
        ResultSet rs = metaData.getTables(null, null, table, null);
        QueryResult ret = JdbcResolver.parseResultSet(rs);
        return ret;
    }

    @Override
    public QueryResult getColumns(DatabaseMetaData metaData, String database, String table) throws SQLException {
        ResultSet rs = metaData.getColumns(null, null, table, null);
        QueryResult ret = JdbcResolver.parseResultSet(rs);
        return ret;
    }

    @Override
    public QueryResult getPrimaryKeys(DatabaseMetaData metaData, String database, String table) throws SQLException {
        ResultSet rs = metaData.getPrimaryKeys(null, null, table);
        QueryResult ret = JdbcResolver.parseResultSet(rs);
        return ret;
    }

    @Override
    public QueryResult getIndexInfo(DatabaseMetaData metaData, String database, String table) throws SQLException {
        ResultSet rs = metaData.getIndexInfo(null, null, table, false, false);
        QueryResult ret = JdbcResolver.parseResultSet(rs);
        return ret;
    }

    @Override
    public ColumnMeta parseColumn(Map<String, Object> row) {
        ColumnMeta col = new ColumnMeta();
        for (Map.Entry<String, Object> entry : row.entrySet()) {
            if ("ORDINAL_POSITION".equalsIgnoreCase(entry.getKey())) {
                col.setIndex(asInteger(entry.getValue(), 0));
            } else if ("COLUMN_NAME".equalsIgnoreCase(entry.getKey())) {
                col.setName(asString(entry.getValue(), null));
            } else if ("TYPE_NAME".equalsIgnoreCase(entry.getKey())) {
                col.setType(asString(entry.getValue(), null));
                if (col.getType() != null) {
                    col.setType(col.getType().split("\\(", 2)[0]);
                }
            } else if ("REMARKS".equalsIgnoreCase(entry.getKey())) {
                col.setComment(asString(entry.getValue(), null));
            } else if ("COLUMN_SIZE".equalsIgnoreCase(entry.getKey())) {
                col.setPrecision(asInteger(entry.getValue(), 0));
            } else if ("DECIMAL_DIGITS".equalsIgnoreCase(entry.getKey())) {
                col.setScale(asInteger(entry.getValue(), 0));
            } else if ("IS_NULLABLE".equalsIgnoreCase(entry.getKey())) {
                String val = asString(entry.getValue(), null);
                col.setNullable("YES".equalsIgnoreCase(val)
                        || "1".equalsIgnoreCase(val)
                        || "Y".equalsIgnoreCase(val)
                );
            } else if ("IS_AUTOINCREMENT".equalsIgnoreCase(entry.getKey())) {
                String val = asString(entry.getValue(), null);
                col.setAutoIncrement("YES".equalsIgnoreCase(val)
                        || "1".equalsIgnoreCase(val)
                        || "Y".equalsIgnoreCase(val)
                );
            } else if ("IS_GENERATEDCOLUMN".equalsIgnoreCase(entry.getKey())) {
                String val = asString(entry.getValue(), null);
                col.setGenerated("YES".equalsIgnoreCase(val)
                        || "1".equalsIgnoreCase(val)
                        || "Y".equalsIgnoreCase(val)
                );
            } else if ("COLUMN_DEF".equalsIgnoreCase(entry.getKey())) {
                col.setDefaultValue(asString(entry.getValue(), null));
            } else if ("COLUMN_COMMENTS".equalsIgnoreCase(entry.getKey())) {
                col.setComment(asString(entry.getValue(), null));
            } else if ("COLUMN_COMMENT".equalsIgnoreCase(entry.getKey())) {
                col.setComment(asString(entry.getValue(), null));
            } else if ("COMMENTS".equalsIgnoreCase(entry.getKey())) {
                col.setComment(asString(entry.getValue(), null));
            } else if ("COMMENT".equalsIgnoreCase(entry.getKey())) {
                col.setComment(asString(entry.getValue(), null));
            } else if ("DESCRIPTION".equalsIgnoreCase(entry.getKey())) {
                col.setComment(asString(entry.getValue(), null));
            }
        }

        Map<String, StdType> typeMap = new HashMap<>();
        for (PostgreSqlType item : PostgreSqlType.values()) {
            typeMap.put(item.text(), item.stdType());
        }
        for (OracleType item : OracleType.values()) {
            typeMap.put(item.text(), item.stdType());
        }
        for (MySqlType item : MySqlType.values()) {
            typeMap.put(item.text(), item.stdType());
        }

        StdType type = StdType.VARCHAR;
        for (Map.Entry<String, StdType> entry : typeMap.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(col.getType())) {
                type = entry.getValue();
                break;
            }
        }

        String columnType = col.getType();

        if (type.precision()) {
            columnType += "(" + col.getPrecision();
            if (type.scale()) {
                columnType += "," + col.getScale();
            }
            columnType += ")";
        }

        col.setColumnType(columnType);

        col.setRawDialectType(type);
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
            IndexColumnMeta meta = new IndexColumnMeta();
            meta.setDesc(false);
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                if ("PK_NAME".equalsIgnoreCase(entry.getKey())) {
                    if (primary.getName() == null) {
                        primary.setName(asString(entry.getValue(), null));
                        primary.setUnique(true);
                    }
                } else if ("KEY_SEQ".equalsIgnoreCase(entry.getKey())) {
                    meta.setIndex(asInteger(entry.getValue(), 0));
                } else if ("COLUMN_NAME".equalsIgnoreCase(entry.getKey())) {
                    meta.setName(asString(entry.getValue(), null));
                }
            }

            primary.getColumns().add(meta);
        }
        ret.setPrimary(primary);
    }


    @Override
    public void parseIndexes(DatabaseMetaData metaData, TableMeta ret) throws SQLException {
        QueryResult result = getIndexInfo(metaData, ret.getDatabase(), ret.getName());
        Map<String, String> upperColumnNameMap = new HashMap<>();
        for (QueryColumn column : result.getColumns()) {
            upperColumnNameMap.put(column.getName().toUpperCase(), column.getName());
        }

        Map<String, IndexMeta> indexMap = new LinkedHashMap<>();
        for (Map<String, Object> row : result.getRows()) {
            String indexName = asString(row.get(upperColumnNameMap.get("INDEX_NAME")), null);
            if (indexName == null) {
                continue;
            }
            if (!indexMap.containsKey(indexName)) {
                IndexMeta meta = new IndexMeta();
                meta.setName(indexName);
                Boolean nonUnique = asBoolean(row.get(upperColumnNameMap.get("NON_UNIQUE")), null);
                if (nonUnique == null) {
                    nonUnique = true;
                }
                meta.setUnique(!nonUnique);
                meta.setColumns(new ArrayList<>());
                indexMap.put(indexName, meta);
            }
            IndexColumnMeta cm = new IndexColumnMeta();
            cm.setName(asString(row.get(upperColumnNameMap.get("COLUMN_NAME")), null));
            cm.setIndex(asInteger(row.get(upperColumnNameMap.get("ORDINAL_POSITION")), 0));
            String val = asString(row.get(upperColumnNameMap.get("ASC_OR_DESC")), null);
            cm.setDesc(!("A".equalsIgnoreCase(val)
                    || "0".equalsIgnoreCase(val)
                    || "ASC".equalsIgnoreCase(val)
                    || "Y".equalsIgnoreCase(val)
                    || "YES".equalsIgnoreCase(val)
            ));
            Integer type = asInteger(row.get(upperColumnNameMap.get("TYPE")), -1);
            cm.setType(getIndexType(type));
            indexMap.get(indexName).getColumns().add(cm);
        }

        ret.setUniqueIndexes(new ArrayList<>());
        ret.setIndexes(new ArrayList<>());
        for (Map.Entry<String, IndexMeta> entry : indexMap.entrySet()) {
            IndexMeta meta = entry.getValue();
            String lowerName = meta.getName();
            if (lowerName == null) {
                lowerName = "";
            }
            lowerName = lowerName.toLowerCase();
            if ("primary".equals(lowerName)
                    || lowerName.endsWith("_pkey")
                    || lowerName.endsWith("_pri")
                    || lowerName.endsWith("_pk")
                    || lowerName.startsWith("pkey_")
                    || lowerName.startsWith("pk_")
                    || lowerName.startsWith("pri_")
            ) {
                ret.setPrimary(meta);
            } else if (meta.isUnique()) {
                ret.getUniqueIndexes().add(meta);
            } else {
                ret.getIndexes().add(meta);
            }
        }
    }


}
