package i2f.jdbc.procedure.node.basic;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.ParamsConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.context.ContextHolder;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.SignalException;
import i2f.jdbc.procedure.signal.impl.*;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Stack;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/2/28 8:44
 */
public abstract class AbstractExecutorNode implements ExecutorNode {
    @Override
    public void exec(XmlNode node, Map<String,Object> context, JdbcProcedureExecutor executor) {
        String location=getNodeLocation(node);
        executor.logDebug(() -> "exec node on tag:" + node.getTagName() + " , at " + location);
        boolean isDebugMode=executor.isDebug();
        Map<String,Object> trace=(Map<String,Object>)executor.visit(ParamsConsts.TRACE,context);
        Stack<String> traceStack =null;
        if(isDebugMode){
            synchronized (trace) {
                traceStack = (Stack<String>) executor.visit(ParamsConsts.TRACE_STACK, context);
                if (traceStack == null) {
                    traceStack = new Stack<>();
                    executor.visitSet(context, ParamsConsts.TRACE_STACK, traceStack);
                }
            }
        }
        if(isDebugMode) {
            synchronized (trace) {
                if (traceStack.isEmpty()) {
                    traceStack.push(location);
                } else {
                    String top = traceStack.peek();
                    if (top.startsWith(node.getLocationFile())) {
                        traceStack.pop();
                    }
                    traceStack.push(location);
                }
            }
        }
        try {
            executor.visitSet(context, ParamsConsts.TRACE_LOCATION,node.getLocationFile());
            executor.visitSet(context,ParamsConsts.TRACE_LINE,node.getLocationLineNumber());
            executor.visitSet(context,ParamsConsts.TRACE_NODE,node);
            ContextHolder.TRACE_LOCATION.set(node.getLocationFile());
            ContextHolder.TRACE_LINE.set(node.getLocationLineNumber());
            ContextHolder.TRACE_NODE.set(node);
            if(isDebugMode){
                String tagName = node.getTagName();
                if(tagName!=null) {
                    if(tagName.contains("-call") || tagName.contains("-include")) {
                        String refid = node.getTagAttrMap().get(AttrConsts.REFID);
                        if(refid!=null && !refid.isEmpty()) {
                            StringBuilder builder=new StringBuilder();
                            builder.append("call "+refid).append("\n");
                            for (Map.Entry<String, Object> entry : context.entrySet()) {
                                if(ParamsConsts.KEEP_NAME_SET.contains(entry.getKey())){
                                    continue;
                                }
                                Object value = entry.getValue();
                                builder.append("\targ:").append(entry.getKey()).append("==> ").append("(").append(value==null?"null":value.getClass().getName()).append(") :").append(value).append("\n");
                            }
                            builder.append("\n");

                            int stackSize = traceStack.size();
                            int printStackSize=50;
                            ListIterator<String> iterator = traceStack.listIterator(stackSize);
                            for (int i = 0; i < printStackSize; i++) {
                                if(!iterator.hasPrevious()){
                                    break;
                                }
                                builder.append("\tat node ").append(iterator.previous()).append("\n");
                            }
                            if(iterator.hasPrevious()){
                                builder.append("\t... ").append(stackSize-printStackSize).append(" common frames omitted\n");
                            }

                            executor.logDebug("call-params:\n===================> "+builder);
                        }
                    }
                }
            }
            execInner(node, context, executor);
            if(isDebugMode){
                if(TagConsts.PROCEDURE.equals(node.getTagName())){
                    synchronized (trace){
                        String pop = traceStack.peek();
                        if(pop.startsWith(node.getLocationFile())){
                            traceStack.pop();
                        }
                    }
                }
            }
        } catch (Throwable e) {
            if(e instanceof ControlSignalException){
                if(isDebugMode){
                    if(e instanceof ReturnSignalException){
                        if(TagConsts.LANG_RETURN.equals(node.getTagName())){
                            synchronized (trace){
                                String pop = traceStack.peek();
                                if(pop.endsWith(TagConsts.LANG_RETURN)){
                                    traceStack.pop();
                                }
                            }
                        }
                    }
                }
                throw (ControlSignalException)e;
            }
            StringBuilder builder=new StringBuilder();
            if(isDebugMode){
                int stackSize = traceStack.size();
                int printStackSize=50;
                ListIterator<String> iterator = traceStack.listIterator(stackSize);
                for (int i = 0; i < printStackSize; i++) {
                    if(!iterator.hasPrevious()){
                        break;
                    }
                    builder.append("\tat node ").append(iterator.previous()).append("\n");
                }
                if(iterator.hasPrevious()){
                    builder.append("\t... ").append(stackSize-printStackSize).append(" common frames omitted\n");
                }
            }
            String errorMsg="exec node error, at node:"+node.getTagName()+", file:" + location +", attrs:"+node.getTagAttrMap()+ ", message: " + e.getMessage();
            String traceErrMsg=e.getClass().getName()+": "+errorMsg;
            executor.visitSet(context,ParamsConsts.TRACE_ERRMSG,traceErrMsg);
            ContextHolder.TRACE_ERRMSG.set(traceErrMsg);

            if(e instanceof SignalException){
                Throwable cause = e.getCause();
                if(cause==null){
                    executor.logError(() -> errorMsg+"\n node trace:\n"+builder, e);
                }
                SignalException se = (SignalException) e;
                se.setMessage(errorMsg);
                throw se;
            } else {
                executor.logError(() -> errorMsg+"\n node trace:\n"+builder, e);
                throw new ThrowSignalException(errorMsg, e);
            }
        }
    }

    public static void walkTree(XmlNode node, Consumer<XmlNode> consumer){
        if(node==null){
            return;
        }
        consumer.accept(node);
        List<XmlNode> children = node.getChildren();
        if(children==null || children.isEmpty()){
            return;
        }
        for (XmlNode item : children) {
            walkTree(item,consumer);
        }
    }

    public static String getNodeLocation(XmlNode node){
        if(node==null){
            return "";
        }
        return ""+node.getLocationFile()+":"+node.getLocationLineNumber()+":"+node.getTagName()+"";
    }

    public static String getTrackingComment(XmlNode node){
        if(node==null){
            return " ";
        }
        return " /* tracking at "+getNodeLocation(node)+" */ ";
    }

    public abstract void execInner(XmlNode node,Map<String,Object> context,JdbcProcedureExecutor executor);
}
