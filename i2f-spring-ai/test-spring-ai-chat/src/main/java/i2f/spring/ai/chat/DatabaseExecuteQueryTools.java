package i2f.spring.ai.chat;

import i2f.spring.ai.tool.annotations.AiTools;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 执行数据库语句
 *
 * @author Ice2Faith
 * @date 2025/5/27 22:35
 * @desc
 */
@ConditionalOnExpression("${test.ai.chat.tools.database.execute.query:false}")
@Slf4j
@Component
@Data
@AiTools
public class DatabaseExecuteQueryTools {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Tool(description = "execute an sql for query, only support select segment, which like 'select ... from ...' /" +
            "执行一条SQL查询语句，只支持select查询语句，形如  'select ... from ...' 的查询语句")
    public List<Map<String, Object>> executeQuerySql(@ToolParam(description = "need execute query sql/" +
            "需要执行的查询SQL语句") String sql) throws Exception {

        log.info("execute query sql: \n" + sql);

        CCJSqlParser parser = new CCJSqlParser(sql);
        parser.withAllowComplexParsing(false)
                .withUnsupportedStatements(false);
        parser.setErrorRecovery(false);
        Statement statement = parser.Statement();
        if (!(statement instanceof Select)) {
            throw new IllegalArgumentException("un-support execute sql type, only support select segment sql/" +
                    "不支持的执行SQL类型，仅支持查询select语句");
        }

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

        return list;

    }


}
