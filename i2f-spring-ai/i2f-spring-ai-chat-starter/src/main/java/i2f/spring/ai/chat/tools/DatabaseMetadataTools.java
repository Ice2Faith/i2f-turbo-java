package i2f.spring.ai.chat.tools;

import i2f.database.metadata.data.ColumnMeta;
import i2f.database.metadata.data.IndexColumnMeta;
import i2f.database.metadata.data.IndexMeta;
import i2f.database.metadata.data.TableMeta;
import i2f.database.metadata.impl.DatabaseMetadataProviders;
import i2f.database.metadata.std.DatabaseMetadataProvider;
import i2f.database.type.DatabaseType;
import i2f.lru.LruMap;
import i2f.spring.ai.tool.annotations.AiTools;
import lombok.Data;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * test chat:
 * 首先查询系统中有哪些表，然后根据需要使用的表查询表结构，最后根据用户要求写一条SQL，回答结果只保留SQL语句，不要其他内容；
 * 统计前三种角色的用户数，显示角色名和用户数，只显示用户和角色状态为启用的
 *
 * @author Ice2Faith
 * @date 2025/5/27 22:35
 * @desc
 */
@Data
@AiTools
public class DatabaseMetadataTools {
    private final AtomicReference<String> cacheDatabaseType = new AtomicReference<>();
    private final AtomicReference<String> cacheAllTables = new AtomicReference<>();
    private final LruMap<String, String> cacheTableColumns = new LruMap<>(300);
    private DataSource dataSource;

    public DatabaseMetadataTools(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Tool(description = "get current system database type/" +
            "获取当前系统的数据库类型")
    public String getSystemDatabaseType() {
        return cacheDatabaseType.updateAndGet(v -> {
            if (v != null && !v.startsWith("error,")) {
                return v;
            }
            Connection conn = DataSourceUtils.getConnection(dataSource);
            try {
                DatabaseMetadataProvider provider = DatabaseMetadataProviders.getProvider(conn);
                String database = provider.detectDefaultDatabase(conn);
                DatabaseType databaseType = DatabaseType.typeOfConnection(conn);
                StringBuilder builder = new StringBuilder();
                builder.append("databaseType/数据库类型:").append(databaseType.db()).append("\n\tcomment/类型说明:").append(databaseType.desc()).append("\n");
                String ret = builder.toString();
                System.out.println(ret);
                return ret;
            } catch (Exception e) {
                return "error, cannot get tables information!";
            } finally {
                DataSourceUtils.releaseConnection(conn, dataSource);
            }
        });
    }

    @Tool(description = "get all system table names, include comment/" +
            "获取所有的系统表名称，包含表注释")
    public String getAllSystemTableNames() {
        return cacheAllTables.updateAndGet(v -> {
            if (v != null && !v.startsWith("error,")) {
                return v;
            }
            Connection conn = DataSourceUtils.getConnection(dataSource);
            try {
                DatabaseMetadataProvider provider = DatabaseMetadataProviders.getProvider(conn);
                String database = provider.detectDefaultDatabase(conn);
                DatabaseType databaseType = DatabaseType.typeOfConnection(conn);
                List<TableMeta> tables = provider.getTables(conn, database);
                StringBuilder builder = new StringBuilder();
                builder.append("databaseType/数据库类型:").append(databaseType.db()).append("\n\tcomment/类型说明:").append(databaseType.desc()).append("\n");
                for (TableMeta table : tables) {
                    builder.append("tableName/表名称:").append(table.getName()).append("\n\tcomment/表注释:").append(table.getComment()).append("\n");
                }
                String ret = builder.toString();
                System.out.println(ret);
                return ret;
            } catch (Exception e) {
                return "error, cannot get tables information!";
            } finally {
                DataSourceUtils.releaseConnection(conn, dataSource);
            }
        });
    }

    @Tool(description = "get a table structure include columns, primary key, index, .etc information/" +
            "获取指定系统表的表结构信息，包括列信息，主键，索引等")
    public String getTableStructureInformation(@ToolParam(description = "系统表表名，例如：sys_user") String tableName) {
        return cacheTableColumns.compute(tableName, (k, v) -> {
            if (v != null && !v.startsWith("error,")) {
                return v;
            }
            String targetTableName = k;
            Connection conn = DataSourceUtils.getConnection(dataSource);
            try {
                DatabaseMetadataProvider provider = DatabaseMetadataProviders.getProvider(conn);
                String database = provider.detectDefaultDatabase(conn);
                DatabaseType databaseType = DatabaseType.typeOfConnection(conn);
                if (Arrays.asList(
                        DatabaseType.ORACLE,
                        DatabaseType.ORACLE_12C,
                        DatabaseType.DM
                ).contains(databaseType)) {
                    targetTableName = targetTableName.toUpperCase();
                }
                TableMeta meta = provider.getTableInfo(conn, database, targetTableName);
                if (meta == null) {
                    return "error, not found table columns of :" + targetTableName;
                }
                StringBuilder builder = new StringBuilder();
                builder.append("databaseType/数据库类型:").append(databaseType.db()).append("\n\tcomment/类型说明:").append(databaseType.desc()).append("\n");
                builder.append("tableName/表名:").append(meta.getName()).append("\n\tcomment/表注释:").append(meta.getComment()).append("\n");
                builder.append("columns/列结构:").append("\n");
                List<ColumnMeta> columns = meta.getColumns();
                for (ColumnMeta item : columns) {
                    builder.append("\tcolumnName/列名:").append(item.getName()).append("\n\t\tdataType/列类型:").append(item.getColumnType()).append("\n\t\tcomment/列注释:").append(item.getComment()).append("\n");
                }
                IndexMeta primary = meta.getPrimary();
                if (primary != null) {
                    builder.append("primary key/表主键:").append("\n");
                    builder.append("\tkeyName/主键名:").append(primary.getName()).append("\n");
                    List<IndexColumnMeta> indexColumns = primary.getColumns();
                    for (IndexColumnMeta item : indexColumns) {
                        builder.append("\tcolumnName/列名:").append(item.getName()).append("\n\t\tisAsc/是否升序:").append(!item.isDesc()).append("\n");
                    }
                }
                List<IndexMeta> uniqueIndexes = meta.getUniqueIndexes();
                if (uniqueIndexes != null && !uniqueIndexes.isEmpty()) {
                    for (IndexMeta index : uniqueIndexes) {
                        builder.append("unique index/唯一索引:").append("\n");
                        builder.append("\tindexName/索引名:").append(index.getName()).append("\n");
                        List<IndexColumnMeta> indexColumns = index.getColumns();
                        for (IndexColumnMeta item : indexColumns) {
                            builder.append("\tcolumnName/列名:").append(item.getName()).append("\n\t\tisAsc/是否升序:").append(!item.isDesc()).append("\n");
                        }
                    }
                }
                List<IndexMeta> indexes = meta.getIndexes();
                if (indexes != null && !indexes.isEmpty()) {
                    for (IndexMeta index : indexes) {
                        builder.append("normal index/普通索引:").append("\n");
                        builder.append("\tindexName/索引名:").append(index.getName()).append("\n");
                        List<IndexColumnMeta> indexColumns = index.getColumns();
                        for (IndexColumnMeta item : indexColumns) {
                            builder.append("\tcolumnName/列名:").append(item.getName()).append("\n\t\tisAsc/是否升序:").append(!item.isDesc()).append("\n");
                        }
                    }
                }
                String ret = builder.toString();
                System.out.println(ret);
                return ret;
            } catch (Exception e) {
                return "error, not found table columns of :" + targetTableName;
            } finally {
                DataSourceUtils.releaseConnection(conn, dataSource);
            }
        });

    }
}
