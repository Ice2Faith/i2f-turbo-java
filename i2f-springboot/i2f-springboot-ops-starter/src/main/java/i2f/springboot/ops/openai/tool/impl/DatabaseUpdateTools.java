package i2f.springboot.ops.openai.tool.impl;

import i2f.ai.std.mcp.server.McpServerExpose;
import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.ai.std.tool.intent.ToolIntent;
import i2f.ai.std.tool.intent.ToolIntentItem;
import i2f.bindsql.BindSql;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.script.JdbcScriptRunner;
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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/6/2 11:34
 * @desc
 */
@McpServerExpose
@ToolIntent(items = @ToolIntentItem(value = "database_update", description = "提供数据库数据的更新能力，包括增删改、脚本执行能力"))
@ConditionalOnClass(DataSource.class)
@AutoConfigureAfter(DefaultDatasourceProvider.class)
@ConditionalOnBean(DatasourceProvider.class)
@ConditionalOnExpression("${ai.tools.database.update.enable:false}")
@Data
@NoArgsConstructor
@Component
@Tools(tags = {
        AiTags.DATABASE_VALUE
})
public class DatabaseUpdateTools {

    @Autowired
    private DatasourceProvider datasourceProvider;

    @Tool(
            tags = {
                    AiTags.WRITABLE_VALUE,
                    AiTags.EXECUTABLE_VALUE,
                    AiTags.HUMAN_VALUE,
                    AiTags.SENSIBLE_VALUE
            },
            description = "execute sql update(insert/update/delete/create/drop/grant/...) at a given datasource"
    )
    public Map<String, Object> sql_update_datasource(@ToolParam(value = "datasourceName", description = "the datasource name, cloud be null means default, for example primary or slave")
                                                     String datasourceName,
                                                     @ToolParam(value = "sql", description = "the update sql, must is update sql which result is an effect count")
                                                     String sql) throws Throwable {
        if (datasourceName == null || datasourceName.isEmpty()) {
            datasourceName = datasourceProvider.getDefaultDataSourceName();
        }
        if (sql == null || sql.isEmpty()) {
            throw new IllegalArgumentException("sql is required, and not empty");
        }


        DataSource datasource = datasourceProvider.getDatasource(datasourceName);
        if (datasource == null) {
            throw new IllegalStateException("datasource not exists!");
        }
        try (Connection conn = datasource.getConnection()) {
            int effectCount = JdbcResolver.update(conn, BindSql.of(sql));
            Map<String, Object> map = new HashMap<>();
            map.put("effectCount", effectCount);
            map.put("datasource", datasourceName);
            return map;
        }
    }

    @Tool(
            tags = {
                    AiTags.WRITABLE_VALUE,
                    AiTags.EXECUTABLE_VALUE,
                    AiTags.HUMAN_VALUE,
                    AiTags.SENSIBLE_VALUE
            },
            description = "execute sql script(multiple segments) at a given datasource"
    )
    public Map<String, Object> sql_script_datasource(@ToolParam(value = "datasourceName", description = "the datasource name, cloud be null means default, for example primary or slave")
                                                     String datasourceName,
                                                     @ToolParam(value = "sql", description = "the script sql, must is multiple segments sql which result is every sql statement results")
                                                     String sql,
                                                     @ToolParam(value = "autoCommit", description = "execute every statement after commit, cloud be null means default is `false`, `false` means all in one transaction, `true` means auto-commit")
                                                     Boolean autoCommit,
                                                     @ToolParam(value = "delimiter", description = "statement delimiter, cloud be null means default is ';'")
                                                     String delimiter,
                                                     @ToolParam(value = "sendFullScript", description = "full send script to jdbc driver, cloud be null means default is `false`, `false` means send every statement one-by-one to driver")
                                                     Boolean sendFullScript,
                                                     @ToolParam(value = "stopOnError", description = "stop execute when error occurred, cloud be null means default is `true`, `false` means skip error and continue execute afters")
                                                     Boolean stopOnError) throws Throwable {
        if (datasourceName == null || datasourceName.isEmpty()) {
            datasourceName = datasourceProvider.getDefaultDataSourceName();
        }
        if (sql == null || sql.isEmpty()) {
            throw new IllegalArgumentException("sql is required, and not empty");
        }

        if (autoCommit == null) {
            autoCommit = false;
        }
        if (delimiter == null || delimiter.isEmpty()) {
            delimiter = ";";
        }
        if (sendFullScript == null) {
            sendFullScript = false;
        }
        if (stopOnError == null) {
            stopOnError = true;
        }


        DataSource datasource = datasourceProvider.getDatasource(datasourceName);
        if (datasource == null) {
            throw new IllegalStateException("datasource not exists!");
        }
        try (Connection conn = datasource.getConnection()) {
            JdbcScriptRunner runner = new JdbcScriptRunner(conn);
            StringWriter writer = new StringWriter();
            PrintWriter printer = new PrintWriter(writer);
            runner.setLogPrinter(e -> {
                printer.println(e);
            });
            runner.setLogErrorPrinter((e, ex) -> {
                printer.println(e);
                ex.printStackTrace(printer);
            });
            runner.setAutoCommit(autoCommit);
            runner.setDelimiter(delimiter);
            runner.setSendFullScript(sendFullScript);
            runner.setStopOnError(stopOnError);
            runner.runScript(sql);

            printer.flush();
            writer.flush();
            Map<String, Object> map = new HashMap<>();
            map.put("logs", writer.toString());
            map.put("datasource", datasourceName);
            return map;
        }
    }


}
