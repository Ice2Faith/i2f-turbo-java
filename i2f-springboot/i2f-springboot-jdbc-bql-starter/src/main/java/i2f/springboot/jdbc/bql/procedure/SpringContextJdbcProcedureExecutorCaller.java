package i2f.springboot.jdbc.bql.procedure;

import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.parser.data.XmlNode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/2/8 11:02
 */
@Data
@NoArgsConstructor
public class SpringContextJdbcProcedureExecutorCaller {
    protected JdbcProcedureExecutor executor;
    protected SpringJdbcProcedureNodeMapRefresher refresher;

    public SpringContextJdbcProcedureExecutorCaller(JdbcProcedureExecutor executor, SpringJdbcProcedureNodeMapRefresher refresher) {
        this.executor = executor;
        this.refresher = refresher;
    }

    public void call(String procedureId, Map<String, Object> params) {
        Map<String, XmlNode> nodeMap = refresher.getNodeMap();
        XmlNode node = nodeMap.get(procedureId);
        ExecuteContext context = new ExecuteContext(params, nodeMap);
        executor.exec(node, context);
    }
}
