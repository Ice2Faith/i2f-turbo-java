package i2f.spring.ai.chat;

import i2f.spring.ai.tool.annotations.AiTools;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
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
@ConditionalOnExpression("${test.ai.chat.tools.database.execute.update:false}")
@Slf4j
@Component
@Data
@AiTools
public class DatabaseExecuteUpdateTools {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Tool(description = "execute an sql for update, only support update/delete/insert segment, " +
            "which like 'update ... set ... where ...' " +
            "or like 'delete from ... where ...'" +
            "or like 'insert into ... values ...'/" +
            "执行一条SQL更新语句，只支持update或delete更新语句，" +
            "形如  'update ... set ... where ...' " +
            "或形如 'delete from ... where ...' " +
            "或形如 'insert into ... values ...' " +
            "的更新语句")
    public int executeUpdateSql(@ToolParam(description = "need execute update sql/" +
            "需要执行的更新SQL语句") String sql) throws Exception {

        log.info("execute update sql: \n"+sql);

        CCJSqlParser parser = new CCJSqlParser(sql);
        parser.withAllowComplexParsing(false)
                .withUnsupportedStatements(false);
        parser.setErrorRecovery(false);
        Statement statement = parser.Statement();
        if(!(statement instanceof Insert)
        &&!(statement instanceof Update)
        &&!(statement instanceof Delete)){
            throw new IllegalArgumentException("un-support execute sql type, only support insert/update/delete segment sql/" +
                    "不支持的执行SQL类型，仅支持更新insert/update/delete语句");
        }

        int effectLine = jdbcTemplate.update(sql);

        return effectLine;

    }

}
