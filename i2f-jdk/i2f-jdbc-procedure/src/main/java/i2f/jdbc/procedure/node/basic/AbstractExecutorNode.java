package i2f.jdbc.procedure.node.basic;

import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;

/**
 * @author Ice2Faith
 * @date 2025/2/28 8:44
 */
public abstract class AbstractExecutorNode implements ExecutorNode {
    @Override
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        try {
            execInner(node, context, executor);
        } catch (Throwable e) {
            executor.errorLog(() -> "exec node error, at file:" + node.getLocationFile() + ", line:" + node.getLocationLineNumber() + ", message:" + e.getMessage(), e);
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new ThrowSignalException(e.getMessage(), e);
            }
        }
    }

    public abstract void execInner(XmlNode node,ExecuteContext context,JdbcProcedureExecutor executor);
}
