package i2f.jdbc.procedure.node.basic;

import i2f.jdbc.procedure.consts.ParamsConsts;
import i2f.jdbc.procedure.context.ContextHolder;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.SignalException;
import i2f.jdbc.procedure.signal.impl.ControlSignalException;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/2/28 8:44
 */
public abstract class AbstractExecutorNode implements ExecutorNode {
    @Override
    public void exec(XmlNode node, Map<String,Object> context, JdbcProcedureExecutor executor) {
        String location=getNodeLocation(node);
        executor.logDebug(() -> "exec node on tag:" + node.getTagName() + " , at " + location);
        try {
            executor.visitSet(context, ParamsConsts.TRACE_LOCATION,node.getLocationFile());
            executor.visitSet(context,ParamsConsts.TRACE_LINE,node.getLocationLineNumber());
            executor.visitSet(context,ParamsConsts.TRACE_NODE,node);
            ContextHolder.TRACE_LOCATION.set(node.getLocationFile());
            ContextHolder.TRACE_LINE.set(node.getLocationLineNumber());
            ContextHolder.TRACE_NODE.set(node);
            execInner(node, context, executor);
        } catch (Throwable e) {
            if(e instanceof ControlSignalException){
                throw (ControlSignalException)e;
            }

            String errorMsg="exec node error, at node:"+node.getTagName()+", file:" + location +", attrs:"+node.getTagAttrMap()+ ", message: " + e.getMessage();
            String traceErrMsg=e.getClass().getName()+": "+errorMsg;
            executor.visitSet(context,ParamsConsts.TRACE_ERRMSG,traceErrMsg);
            ContextHolder.TRACE_ERRMSG.set(traceErrMsg);

            if(e instanceof SignalException){
                Throwable cause = e.getCause();
                if(cause==null){
                    executor.logError(() -> errorMsg, e);
                }
                SignalException se = (SignalException) e;
                se.setMessage(errorMsg);
                throw se;
            } else {
                executor.logError(() -> errorMsg, e);
                throw new ThrowSignalException(errorMsg, e);
            }
        }
    }

    public static String getNodeLocation(XmlNode node){
        if(node==null){
            return "";
        }
        return ""+node.getLocationFile()+":"+node.getLocationLineNumber()+"";
    }

    public static String getTrackingComment(XmlNode node){
        if(node==null){
            return " ";
        }
        return " /* tracking at "+getNodeLocation(node)+" */ ";
    }

    public abstract void execInner(XmlNode node,Map<String,Object> context,JdbcProcedureExecutor executor);
}
