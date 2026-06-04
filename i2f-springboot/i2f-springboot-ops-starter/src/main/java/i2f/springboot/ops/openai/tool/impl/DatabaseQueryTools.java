package i2f.springboot.ops.openai.tool.impl;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.bindsql.BindSql;
import i2f.jdbc.JdbcResolver;
import i2f.page.ApiOffsetSize;
import i2f.page.Page;
import i2f.springboot.ops.datasource.provider.DatasourceProvider;
import i2f.springboot.ops.datasource.provider.impl.DefaultDatasourceProvider;
import i2f.springboot.ops.openai.tool.impl.sql.OpsSqlValidator;
import i2f.springboot.ops.openai.tool.impl.sql.OpsSqlValidators;
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
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/6/2 11:34
 * @desc
 */
@ConditionalOnClass(DataSource.class)
@AutoConfigureAfter(DefaultDatasourceProvider.class)
@ConditionalOnBean(DatasourceProvider.class)
@ConditionalOnExpression("${ai.tools.database.query.enable:true}")
@Data
@NoArgsConstructor
@Component
@Tools
public class DatabaseQueryTools {

    @Autowired
    private DatasourceProvider datasourceProvider;

    @Tool(
            tags = {
                    AiTags.READONLY_VALUE,
                    AiTags.SENSIBLE_VALUE
            },
            description = "execute sql query from offset fetch next limit rows at a given datasource"
    )
    public Map<String, Object> sql_query_datasource(@ToolParam(value = "datasourceName", description = "the datasource name, cloud be null means default, for example primary or slave")
                                                    String datasourceName,
                                                    @ToolParam(value = "sql", description = "the query sql, must is query sql which result is an result-set, for example \"select ... from ...\"")
                                                    String sql,
                                                    @ToolParam(value = "offset", description = "result set offset, cloud be null, default is 0, range in [0,...] , for example 0 or 10")
                                                    Integer offset,
                                                    @ToolParam(value = "limit", description = "result set limit, cloud be null, default is 30, range in [0,100], for example 10 or 100")
                                                    Integer limit
    ) throws Exception {
        if (datasourceName == null || datasourceName.isEmpty()) {
            datasourceName = datasourceProvider.getDefaultDataSourceName();
        }
        if (offset == null || offset < 0) {
            offset = 0;
        }
        if (limit == null || limit < 0) {
            limit = 30;
        }
        if (limit > 100) {
            throw new IllegalArgumentException("limit value range require in range [0,100], but found: " + limit);
        }
        if (sql == null || sql.isEmpty()) {
            throw new IllegalArgumentException("sql is required, and not empty");
        }
        // 校验SQL
        OpsSqlValidator sqlValidator = OpsSqlValidators.getSqlValidator();
        sqlValidator.validateQuery(sql);

        DataSource datasource = datasourceProvider.getDatasource(datasourceName);
        try (Connection conn = datasource.getConnection()) {
            Page<Map<String, Object>> page = JdbcResolver.page(conn, new BindSql(sql), new ApiOffsetSize(offset, limit));
            Map<String, Object> map = new HashMap<>();
            map.put("rows", page.getList());
            map.put("offset", page.getOffset());
            map.put("limit", page.getSize());
            map.put("total", page.getTotal());
            map.put("datasource", datasourceName);
            return map;
        }
    }


}
