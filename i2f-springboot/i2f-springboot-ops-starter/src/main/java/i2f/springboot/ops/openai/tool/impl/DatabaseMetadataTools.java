package i2f.springboot.ops.openai.tool.impl;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.database.metadata.data.TableMeta;
import i2f.database.metadata.impl.DatabaseMetadataProviders;
import i2f.database.metadata.std.DatabaseMetadataProvider;
import i2f.database.type.DatabaseType;
import i2f.springboot.ops.datasource.provider.DatasourceProvider;
import i2f.springboot.ops.datasource.provider.impl.DefaultDatasourceProvider;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/6/2 11:34
 * @desc
 */
@ConditionalOnClass(DataSource.class)
@AutoConfigureAfter(DefaultDatasourceProvider.class)
@ConditionalOnBean(DatasourceProvider.class)
@ConditionalOnExpression("${ai.tools.database.metadata.enable:true}")
@Data
@NoArgsConstructor
@Component
@Tools
public class DatabaseMetadataTools {

    @Autowired
    private DatasourceProvider datasourceProvider;

    @Tool(
            tags = {
                    AiTags.READONLY_VALUE
            },
            description = "get system managed database datasource list"
    )
    public List<String> get_datasource_list() {
        Map<String, DataSource> map = datasourceProvider.getDatasourceMap();
        return new ArrayList<>(map.keySet());
    }

    @Tool(
            tags = {
                    AiTags.READONLY_VALUE
            },
            description = "detect a given datasource real database type"
    )
    public String get_datasource_database_type(@ToolParam(value = "datasourceName", description = "the datasource name, cloud be null means default, for example primary or slave")
                                               String datasourceName) throws Exception {
        if (datasourceName == null || datasourceName.isEmpty()) {
            datasourceName = datasourceProvider.getDefaultDataSourceName();
        }
        DataSource datasource = datasourceProvider.getDatasource(datasourceName);
        if (datasource == null) {
            throw new IllegalStateException("datasource not exists!");
        }
        try (Connection conn = datasource.getConnection()) {
            DatabaseType type = DatabaseType.typeOfConnection(conn);
            return type.db() + ":" + type.desc();
        }
    }

    @Tool(tags = {AiTags.READONLY_VALUE}, description = "list databases of a given datasource")
    public List<String> get_datasource_database_list(@ToolParam(value = "datasourceName", description = "the datasource name, cloud be null means default, for example primary or slave")
                                                     String datasourceName) throws Exception {
        if (datasourceName == null || datasourceName.isEmpty()) {
            datasourceName = datasourceProvider.getDefaultDataSourceName();
        }
        DataSource datasource = datasourceProvider.getDatasource(datasourceName);
        if (datasource == null) {
            throw new IllegalStateException("datasource not exists!");
        }
        try (Connection conn = datasource.getConnection()) {
            DatabaseMetadataProvider provider = DatabaseMetadataProviders.getProvider(conn);
            List<String> ret = provider.getDatabases(conn);
            return ret;
        }
    }

    @Tool(
            tags = {
                    AiTags.READONLY_VALUE
            },
            description = "list tables of a given table pattern"
    )
    public List<Map<String, Object>> get_datasource_table_list(@ToolParam(value = "datasourceName", description = "the datasource name, cloud be null means default, for example primary or slave")
                                                               String datasourceName,
                                                               @ToolParam(value = "database", description = "the database name, cloud be null means default database schema, for example test_db or user_db")
                                                               String database,
                                                               @ToolParam(value = "tablePattern", description = "the table pattern name, cloud be null means all tables, for example sys_user or sys_% or %user")
                                                               String tablePattern) throws Exception {
        if (datasourceName == null || datasourceName.isEmpty()) {
            datasourceName = datasourceProvider.getDefaultDataSourceName();
        }
        DataSource datasource = datasourceProvider.getDatasource(datasourceName);
        if (datasource == null) {
            throw new IllegalStateException("datasource not exists!");
        }
        try (Connection conn = datasource.getConnection()) {
            DatabaseMetadataProvider provider = DatabaseMetadataProviders.getProvider(conn);
            List<TableMeta> tables = provider.getTables(conn, database, tablePattern);
            List<Map<String, Object>> ret = new ArrayList<>();
            for (TableMeta table : tables) {
                Map<String, Object> row = new HashMap<>();
                row.put("name", table.getName());
                row.put("database", table.getDatabase());
                row.put("comment", table.getComment());
                ret.add(row);
            }
            return ret;
        }
    }

    @Tool(
            tags = {
                    AiTags.READONLY_VALUE
            },
            description = "get table structure info of a given table name"
    )
    public TableMeta get_datasource_table_info(@ToolParam(value = "datasourceName", description = "the datasource name, cloud be null means default, for example primary or slave")
                                               String datasourceName,
                                               @ToolParam(value = "database", description = "the database name, cloud be null means default database schema, for example test_db or user_db")
                                               String database,
                                               @ToolParam(value = "tableName", description = "the table name, required, for example sys_user or sys_role")
                                               String tableName
    ) throws Exception {
        if (datasourceName == null || datasourceName.isEmpty()) {
            datasourceName = datasourceProvider.getDefaultDataSourceName();
        }
        DataSource datasource = datasourceProvider.getDatasource(datasourceName);
        if (datasource == null) {
            throw new IllegalStateException("datasource not exists!");
        }
        try (Connection conn = datasource.getConnection()) {
            DatabaseMetadataProvider provider = DatabaseMetadataProviders.getProvider(conn);
            TableMeta ret = provider.getTableInfo(conn, database, tableName);
            return ret;
        }
    }

}
