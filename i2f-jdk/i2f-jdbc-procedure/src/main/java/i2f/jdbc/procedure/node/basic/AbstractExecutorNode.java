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
    protected volatile long slowProcedureMillsSeconds = TimeUnit.SECONDS.toMillis(45);
    protected volatile long slowNodeMillsSeconds = TimeUnit.SECONDS.toMillis(15);

    public static final String EXCEPTION_MESSAGE_PREFIX = "exec node error, at ";


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
        return XmlNode.getNodeLocation(node);
    }

    public static String getTrackingComment(XmlNode node) {
        if (node == null) {
            return " ";
        }
        return " /* tracking at " + getNodeLocation(node) + " */ ";
    }

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
        LinkedList<Throwable> traceErrors = null;
        synchronized (trace) {
            traceStack = executor.visitAs(ParamsConsts.TRACE_STACK, context);
            if (traceStack == null) {
                traceStack = new Stack<>();
                executor.visitSet(context, ParamsConsts.TRACE_STACK, traceStack);
            }

            traceCalls = executor.visitAs(ParamsConsts.TRACE_CALLS, context);
            if (traceCalls == null) {
                traceCalls = new LinkedList<>();
                executor.visitSet(context, ParamsConsts.TRACE_CALLS, traceCalls);
            }

            traceErrors = executor.visitAs(ParamsConsts.TRACE_ERRORS, context);
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
                if (top.startsWith(String.valueOf(node.getLocationFile()))) {
                    traceStack.pop();
                }
                traceStack.push(location);
            }
        }

        synchronized (trace) {
            int size = traceErrors.size();
            while (size > 10) {
                traceErrors.removeFirst();
                size--;
            }
        }

        Map<String, Object> pointContext = new HashMap<>();

        String snapshotTraceId = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();

        long bts = SystemClock.currentTimeMillis();
        pointContext.put("location", location);
        pointContext.put("beginTs", bts);
        pointContext.put("isDebugMode", isDebugMode);
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
            } catch (Throwable e) {
                executor.logWarn(() -> e.getMessage(), e);
            }

            execInner(node, context, executor);

            try {
                onAfter(pointContext, node, context, executor);
            } catch (Throwable e) {
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

            String errorMsg = e.getMessage();
            if (!errorMsg.startsWith(EXCEPTION_MESSAGE_PREFIX)) {
                errorMsg = EXCEPTION_MESSAGE_PREFIX + location + ", attrs:" + node.getTagAttrMap() + ", message: " + e.getMessage();
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

                errorMsg = errorMsg + "\n node trace:\n" + builder;
            }

            String traceErrMsg = e.getClass().getName() + ": " + errorMsg;
            executor.visitSet(context, ParamsConsts.TRACE_ERRMSG, traceErrMsg);
            ContextHolder.TRACE_ERRMSG.set(traceErrMsg);

            RuntimeException re = null;
            if (e instanceof SignalException) {
                SignalException se = (SignalException) e;
                se.setMessage(errorMsg);
                re = se;
            } else {
                ThrowSignalException se = new ThrowSignalException(errorMsg, e);
                re = se;
            }

            if (re instanceof SignalException) {
                SignalException se = (SignalException) re;

                if (se.getCause() == null || !se.hasLogout()) {
                    executor.logError(() -> se.getMessage(), se);
                    se.setHasLogout(true);
                }
            }

            synchronized (trace) {
                if (!traceErrors.contains(re)) {
                    traceErrors.add(re);
                }
            }

            try {
                onThrowing(e, pointContext, node, context, executor);
            } catch (Throwable ex) {
                executor.logWarn(() -> ex.getMessage(), ex);
            }

            throw re;
        } finally {
            long ets = SystemClock.currentTimeMillis();
            long useTs = ets - bts;
            pointContext.put("endTs", ets);
            pointContext.put("useTs", useTs);

            if (TagConsts.PROCEDURE.equals(node.getTagName())) {
                if (useTs > slowProcedureMillsSeconds) {
                    executor.logWarn(() -> "slow procedure, use " + useTs + "(ms) : " + node.getTagAttrMap().get(AttrConsts.ID));
                }
            } else {
                if (!Arrays.asList(TagConsts.PROCEDURE_CALL,
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

//        System.out.println("before pointcut!");
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

//        System.out.println("after pointcut!");
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

//        System.out.println("throwing pointcut!");
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

    public abstract void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor);
}
