package i2f.jdbc.procedure.node.basic;

import i2f.clock.SystemClock;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.ParamsConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.context.ContextHolder;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.node.event.XmlExecUseTimeEvent;
import i2f.jdbc.procedure.node.event.XmlNodeExecEvent;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.SignalException;
import i2f.jdbc.procedure.signal.impl.ControlSignalException;
import i2f.jdbc.procedure.signal.impl.ReturnSignalException;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/2/28 8:44
 */
public abstract class AbstractExecutorNode implements ExecutorNode {
    protected volatile long slowProcedureMillsSeconds=TimeUnit.SECONDS.toMillis(45);
    protected volatile long slowNodeMillsSeconds=TimeUnit.SECONDS.toMillis(15);
    @Override
    public void exec(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        String location = getNodeLocation(node);
        boolean isDebugMode = executor.isDebug();
//        if (isDebugMode) {
//            executor.logDebug("exec node on tag:" + node.getTagName() + " , at " + location);
//        }

        Map<String, Object> trace = executor.visitAs(ParamsConsts.TRACE, context);
        Stack<String> traceStack = null;
        LinkedList<Map.Entry<String, String>> traceCalls = null;
        LinkedList<Throwable>  traceErrors=null;
        synchronized (trace) {
            traceStack = executor.visitAs(ParamsConsts.TRACE_STACK, context);
            if (traceStack == null) {
                traceStack = new Stack<>();
                executor.visitSet(context, ParamsConsts.TRACE_STACK, traceStack);
            }

            traceCalls =executor.visitAs(ParamsConsts.TRACE_CALLS, context);
            if (traceCalls == null) {
                traceCalls = new LinkedList<>();
                executor.visitSet(context, ParamsConsts.TRACE_CALLS, traceCalls);
            }

            traceErrors =executor.visitAs(ParamsConsts.TRACE_ERRORS, context);
            if (traceErrors == null) {
                traceErrors = new LinkedList<>();
                executor.visitSet(context, ParamsConsts.TRACE_ERRORS, traceErrors);
            }
        }

        if (isDebugMode) {
            synchronized (trace) {
                int size = traceCalls.size();
                while (size > 1000) {
                    traceCalls.removeFirst();
                    size--;
                }
            }
        }

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

        synchronized (trace){
            int size = traceErrors.size();
            while (size > 10) {
                traceErrors.removeFirst();
                size--;
            }
        }

        Map<String, Object> pointContext = new HashMap<>();

        String snapshotTraceId = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();

        long bts= SystemClock.currentTimeMillis();
        pointContext.put("location",location);
        pointContext.put("beginTs",bts);
        pointContext.put("isDebugMode",isDebugMode);
        pointContext.put("snapshotTraceId", snapshotTraceId);
        try {
            executor.visitSet(context, ParamsConsts.TRACE_LOCATION, node.getLocationFile());
            executor.visitSet(context, ParamsConsts.TRACE_LINE, node.getLocationLineNumber());
            executor.visitSet(context, ParamsConsts.TRACE_NODE, node);
            ContextHolder.TRACE_LOCATION.set(node.getLocationFile());
            ContextHolder.TRACE_LINE.set(node.getLocationLineNumber());
            ContextHolder.TRACE_NODE.set(node);

            try {
                onBefore(pointContext, node, context, executor);
            } catch (Exception e) {
                executor.logWarn(() -> e.getMessage(), e);
            }

            execInner(node, context, executor);

            try {
                onAfter(pointContext, node, context, executor);
            } catch (Exception e) {
                executor.logWarn(() -> e.getMessage(), e);
            }

            if (TagConsts.PROCEDURE.equals(node.getTagName())) {
                synchronized (trace) {
                    String pop = traceStack.peek();
                    if (pop.startsWith(node.getLocationFile())) {
                        traceStack.pop();
                    }
                }
            }

        } catch (Throwable e) {
            if (e instanceof ControlSignalException) {
                try {
                    onThrowing(e, pointContext, node, context, executor);
                } catch (Throwable ex) {
                    executor.logWarn(() -> ex.getMessage(), e);
                }
            }

            if (e instanceof ControlSignalException) {
                if (e instanceof ReturnSignalException) {
                    if (TagConsts.LANG_RETURN.equals(node.getTagName())) {
                        synchronized (trace) {
                            String pop = traceStack.peek();
                            if (pop.endsWith(TagConsts.LANG_RETURN)) {
                                traceStack.pop();
                            }
                        }
                    }
                }
                throw (ControlSignalException) e;
            }

            StringBuilder builder = new StringBuilder();

            int stackSize = traceStack.size();
            int printStackSize = 50;
            ListIterator<String> iterator = traceStack.listIterator(stackSize);
            for (int i = 0; i < printStackSize; i++) {
                if (!iterator.hasPrevious()) {
                    break;
                }
                builder.append("\tat node ").append(iterator.previous()).append("\n");
            }
            if (iterator.hasPrevious()) {
                builder.append("\t... ").append(stackSize - printStackSize).append(" common frames omitted\n");
            }

            String errorMsg = "exec node error, at node:" + node.getTagName() + ", file:" + location + ", attrs:" + node.getTagAttrMap() + ", message: " + e.getMessage();
            String traceErrMsg = e.getClass().getName() + ": " + errorMsg;
            executor.visitSet(context, ParamsConsts.TRACE_ERRMSG, traceErrMsg);
            ContextHolder.TRACE_ERRMSG.set(traceErrMsg);

            RuntimeException re=null;
            if (e instanceof SignalException) {
                SignalException se = (SignalException) e;
                if (se.getCause() == null || !se.hasLogout()) {
                    executor.logError(() -> errorMsg + "\n node trace:\n" + builder, e);
                    se.setHasLogout(true);
                }
                se.setMessage(errorMsg);
                try {
                    onThrowing(se, pointContext, node, context, executor);
                } catch (Throwable ex) {
                    executor.logWarn(() -> ex.getMessage(), e);
                }
                re= se;
            } else {
                ThrowSignalException se = new ThrowSignalException(errorMsg, e);
                if (se.getCause() == null || !se.hasLogout()) {
                    executor.logError(() -> errorMsg + "\n node trace:\n" + builder, e);
                    se.setHasLogout(true);
                }
                try {
                    onThrowing(e, pointContext, node, context, executor);
                } catch (Throwable ex) {
                    executor.logWarn(() -> ex.getMessage(), e);
                }
                re= se;
            }
            synchronized (trace) {
                if (!traceErrors.contains(re)) {
                    traceErrors.add(re);
                }
            }
            throw re;
        } finally {
            long ets=SystemClock.currentTimeMillis();
            long useTs=ets-bts;
            pointContext.put("endTs",ets);
            pointContext.put("useTs",useTs);

            if (TagConsts.PROCEDURE.equals(node.getTagName())) {
                if(useTs> slowProcedureMillsSeconds){
                    executor.logWarn(()->"slow procedure, use "+useTs+"(ms) : "+node.getTagAttrMap().get(AttrConsts.ID));
                }
            }else{
                if(!Arrays.asList(TagConsts.PROCEDURE_CALL,
                        TagConsts.FUNCTION_CALL).contains(node.getTagName())) {
                    if (useTs > slowNodeMillsSeconds) {
                        executor.logWarn(() -> "slow node, use " + useTs + "(ms) : " + location);
                    }
                }
            }

            XmlExecUseTimeEvent event = new XmlExecUseTimeEvent();
            event.setExecutorNode(this);
            event.setPointContext(pointContext);
            event.setNode(node);
            event.setContext(context);
            event.setExecutor(executor);
            executor.sendEvent(event);
            event.setUseTs(useTs);
            executor.publishEvent(event);

            try {
                onFinally(pointContext, node, context, executor);
            } catch (Throwable e) {
                executor.logWarn(() -> e.getMessage(), e);
            }
        }
    }

    public void onBefore(Map<String, Object> pointContext, XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        XmlNodeExecEvent event = new XmlNodeExecEvent();
        event.setExecutorNode(this);
        event.setType(XmlNodeExecEvent.Type.BEFORE);
        event.setPointContext(pointContext);
        event.setNode(node);
        event.setContext(context);
        event.setExecutor(executor);
        executor.sendEvent(event);


        String location = (String)pointContext.get("location");
        String snapshotTraceId=(String)pointContext.get("snapshotTraceId");

        boolean isDebugMode = executor.isDebug();
        Map<String, Object> trace = executor.visitAs(ParamsConsts.TRACE, context);
        LinkedList<Map.Entry<String, String>> traceCalls = executor.visitAs(ParamsConsts.TRACE_CALLS,context);
        if (isDebugMode) {
            synchronized (trace) {
                int size = traceCalls.size();
                while (size > 1000) {
                    traceCalls.removeFirst();
                    size--;
                }
            }
        }



        String tagName = node.getTagName();
        if (tagName != null) {
            if (TagConsts.PROCEDURE.equals(tagName)) {
                String id = node.getTagAttrMap().get(AttrConsts.ID);
                if (id != null && !id.isEmpty()) {
                    executor.logInfo("exec node:" + id + " at " + location);
                    if (isDebugMode) {
                        String callSnapshot = getCallSnapshot(node, context, executor);
                        callSnapshot = "BEFORE:" + snapshotTraceId + "\n" + callSnapshot;
                        traceCalls.add(new AbstractMap.SimpleEntry<>(id, callSnapshot));
                        executor.logDebug("call-params:\n===================> " + callSnapshot);
                    }
                }
            }
        }


    }

    public void onAfter(Map<String, Object> pointContext, XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        XmlNodeExecEvent event = new XmlNodeExecEvent();
        event.setExecutorNode(this);
        event.setType(XmlNodeExecEvent.Type.AFTER);
        event.setPointContext(pointContext);
        event.setNode(node);
        event.setContext(context);
        event.setExecutor(executor);
        executor.sendEvent(event);

        boolean isDebugMode = executor.visitAs("isDebugMode", pointContext);
        String snapshotTraceId = executor.visitAs("snapshotTraceId", pointContext);
        LinkedList<Map.Entry<String, String>> traceCallRecords = executor.visitAs(ParamsConsts.TRACE_CALLS, context);

        if (isDebugMode) {
            String tagName = node.getTagName();
            if (tagName != null) {
                if (TagConsts.PROCEDURE.equals(tagName)) {
                    String id = node.getTagAttrMap().get(AttrConsts.ID);
                    if (id != null && !id.isEmpty()) {
                        String callSnapshot = getCallSnapshot(node, context, executor);
                        callSnapshot = "AFTER:" + snapshotTraceId + "\n" + callSnapshot;
                        traceCallRecords.add(new AbstractMap.SimpleEntry<>(id, callSnapshot));
                        executor.logDebug("call-params:\n===================> " + callSnapshot);
                    }
                }
            }
        }

    }

    public void onThrowing(Throwable e, Map<String, Object> pointContext, XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        XmlNodeExecEvent event = new XmlNodeExecEvent();
        event.setExecutorNode(this);
        event.setType(XmlNodeExecEvent.Type.THROWING);
        event.setThrowable(e);
        event.setPointContext(pointContext);
        event.setNode(node);
        event.setContext(context);
        event.setExecutor(executor);
        executor.sendEvent(event);

        boolean isDebugMode = executor.visitAs("isDebugMode", pointContext);
        String snapshotTraceId = executor.visitAs("snapshotTraceId", pointContext);
        LinkedList<Map.Entry<String, String>> traceCallRecords = executor.visitAs(ParamsConsts.TRACE_CALLS, context);

        if (isDebugMode) {
            if (e instanceof ControlSignalException) {
                // do nothing
                if (e instanceof ReturnSignalException) {
                    String tagName = node.getTagName();
                    if (tagName != null) {
                        if (TagConsts.PROCEDURE.equals(tagName)) {
                            String id = node.getTagAttrMap().get(AttrConsts.ID);
                            if (id != null && !id.isEmpty()) {
                                String callSnapshot = getCallSnapshot(node, context, executor);
                                callSnapshot = "AFTER:" + snapshotTraceId + "\n" + callSnapshot;
                                traceCallRecords.add(new AbstractMap.SimpleEntry<>(id, callSnapshot));
                                executor.logDebug("call-params:\n===================> " + callSnapshot);
                            }
                        }
                    }

                }
            } else {
                System.out.println("exception pointcut!");
            }
        }
    }

    public void onFinally(Map<String, Object> pointContext, XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        XmlNodeExecEvent event = new XmlNodeExecEvent();
        event.setExecutorNode(this);
        event.setType(XmlNodeExecEvent.Type.FINALLY);
        event.setPointContext(pointContext);
        event.setNode(node);
        event.setContext(context);
        event.setExecutor(executor);
        executor.sendEvent(event);
//        System.out.println("finally pointcut!");
    }

    public static Object trimContextKeepAttribute(Object value) {
        if (value == null) {
            return null;
        }
        if (!(value instanceof Map)) {
            return value;
        }
        Map<Object, Object> ret = new HashMap<>();
        Map<?, ?> map = (Map<?, ?>) value;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (ParamsConsts.KEEP_NAME_SET.contains(entry.getKey())) {
                continue;
            }
            ret.put(entry.getKey(), trimContextKeepAttribute(entry.getValue()));
        }
        return ret;
    }

    public static String getCallSnapshot(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        Stack<String> traceStack = executor.visitAs(ParamsConsts.TRACE_STACK, context);
        String id = node.getTagAttrMap().get(AttrConsts.ID);
        StringBuilder builder = new StringBuilder();
        builder.append("call " + id).append("\n");
        for (Map.Entry<String, Object> entry : context.entrySet()) {
            if (ParamsConsts.KEEP_NAME_SET.contains(entry.getKey())) {
                continue;
            }
            Object value = entry.getValue();
            value = trimContextKeepAttribute(value);
            builder.append("\targ:").append(entry.getKey()).append("==> ").append("(").append(value == null ? "null" : value.getClass().getName()).append(") :").append(value).append("\n");
        }
        builder.append("\n");

        int stackSize = traceStack.size();
        int printStackSize = 50;
        ListIterator<String> iterator = traceStack.listIterator(stackSize);
        for (int i = 0; i < printStackSize; i++) {
            if (!iterator.hasPrevious()) {
                break;
            }
            builder.append("\tat node ").append(iterator.previous()).append("\n");
        }
        if (iterator.hasPrevious()) {
            builder.append("\t... ").append(stackSize - printStackSize).append(" common frames omitted\n");
        }
        return builder.toString();
    }

    public static void walkTree(XmlNode node, Consumer<XmlNode> consumer) {
        if (node == null) {
            return;
        }
        consumer.accept(node);
        List<XmlNode> children = node.getChildren();
        if (children == null || children.isEmpty()) {
            return;
        }
        for (XmlNode item : children) {
            walkTree(item, consumer);
        }
    }

    public static String getNodeLocation(XmlNode node) {
        if (node == null) {
            return "";
        }
        return "" + node.getLocationFile() + ":" + node.getLocationLineNumber() + ":" + node.getTagName() + "";
    }

    public static String getTrackingComment(XmlNode node) {
        if (node == null) {
            return " ";
        }
        return " /* tracking at " + getNodeLocation(node) + " */ ";
    }

    public abstract void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor);
}
