package i2f.jdbc.procedure.node.basic;

import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.SignalException;
import i2f.jdbc.procedure.signal.impl.ControlSignalException;
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
            if(e instanceof ControlSignalException){
                throw (ControlSignalException)e;
            }
            String errorMsg="exec node error, at node:"+node.getTagName()+", file:" + node.getLocationFile() + ", line:" + node.getLocationLineNumber() +", attrs:"+node.getTagAttrMap()+ ", message: " + e.getMessage();
            executor.errorLog(() -> errorMsg, e);
            if(e instanceof SignalException){
                throw (SignalException)e;
            } else {
                throw new ThrowSignalException(errorMsg, e);
            }
        }
    }

    public abstract void execInner(XmlNode node,ExecuteContext context,JdbcProcedureExecutor executor);
}
