package i2f.jdbc.procedure.node;

import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.parser.data.XmlNode;

/**
 * @author Ice2Faith
 * @date 2025/1/20 10:36
 */
public interface ExecutorNode {
    boolean support(XmlNode node);

    void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor);
}
