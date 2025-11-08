package i2f.jdbc.procedure.node.basic;

import i2f.clock.SystemClock;
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
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/2/28 8:44
 */
public abstract class AbstractExecutorNode implements ExecutorNode {

    public static final String EXCEPTION_MESSAGE_PREFIX = "exec node error, at ";
    public static final String POINT_KEY_LOCATION = "location";
    public static final String POINT_KEY_BEGIN_TS = "beginTs";
    public static final String POINT_KEY_IS_DEBUG_MODE = "isDebugMode";
    public static final String POINT_KEY_SNAPSHOT_TRACE_ID = "snapshotTraceId";
    public static final String POINT_KEY_END_TS = "endTs";
    public static final String POINT_KEY_USE_TS = "useTs";

    public static final String POINT_KEY_LOOP_IS_FIRST = "loopIsFirst";
    public static final String POINT_KEY_LOOP_INDEX = "loopIndex";
    public static final String POINT_KEY_LOOP_VALUE = "loopValue";

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
        return getNodeLocation(node, null);
    }

    public static String getNodeLocation(XmlNode node, Integer lineNumber) {
        return XmlNode.getNodeLocation(node, lineNumber);
    }

    public static String getTrackingComment(XmlNode node) {
        return getTrackingComment(node, null);
    }

    public static String getTrackingComment(XmlNode node, Integer lineNumber) {
        if (node == null) {
            return "";
        }
        return " /* tracking at " + getNodeLocation(node, lineNumber) + " */ ";
    }

    @Override
    public void exec(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        XmlNode beforeNode=ContextHolder.TRACE_NODE.get();

        XmlNode.NodeType nodeType = node.getNodeType();
        if (nodeType == XmlNode.NodeType.TEXT
                || nodeType == XmlNode.NodeType.CDATA) {
            return;
        }

        String location = getNodeLocation(node);
        boolean isDebugMode = executor.isDebug();

        Stack<String> traceStack = executor.visitAs(ParamsConsts.TRACE_STACK, context);
        if (traceStack == null) {
            traceStack = new Stack<>();
            executor.visitSet(context, ParamsConsts.TRACE_STACK, traceStack);
        }

        if (traceStack.isEmpty()) {
            traceStack.push(location);
        } else {
            String top = traceStack.peek();
            if (top.startsWith(String.valueOf(node.getLocationFile()))) {
                traceStack.pop();
            }
            traceStack.push(location);
        }


        Map<String, Object> pointContext = new HashMap<>();

        String snapshotTraceId = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();

        long bts = SystemClock.currentTimeMillis();
        pointContext.put(POINT_KEY_LOCATION, location);
        pointContext.put(POINT_KEY_BEGIN_TS, bts);
        pointContext.put(POINT_KEY_IS_DEBUG_MODE, isDebugMode);
        pointContext.put(POINT_KEY_SNAPSHOT_TRACE_ID, snapshotTraceId);
        try {
            updateTraceInfo(node, context, executor);

            try {
                onBefore(pointContext, node, context, executor);
            } catch (Throwable e) {
                executor.logger().logWarn(() -> e.getMessage(), e);
            }

            execInner(node, context, executor);

            updateTraceInfo(node, context, executor);

            try {
                onAfter(pointContext, node, context, executor);
            } catch (Throwable e) {
                executor.logger().logWarn(() -> e.getMessage(), e);
            }

            if (TagConsts.PROCEDURE.equals(node.getTagName())) {
                String pop = traceStack.peek();
                if (pop.startsWith(node.getLocationFile())) {
                    traceStack.pop();
                }
            }

        } catch (Throwable e) {
            updateTraceInfo(node, context, executor);

            if (e instanceof ControlSignalException) {
                try {
                    onThrowing(e, pointContext, node, context, executor);
                } catch (Throwable ex) {
                    executor.logger().logWarn(() -> ex.getMessage(), e);
                }
            }

            if (e instanceof ControlSignalException) {
                if (e instanceof ReturnSignalException) {
                    if (TagConsts.LANG_RETURN.equals(node.getTagName())) {
                        String pop = traceStack.peek();
                        if (pop.endsWith(TagConsts.LANG_RETURN)) {
                            traceStack.pop();
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
                    executor.logger().logError(() -> se.getMessage(), se);
                    se.setHasLogout(true);
                }
            }

            LinkedList<Throwable> traceErrors = executor.visitAs(ParamsConsts.TRACE_ERRORS, context);
            if (traceErrors == null) {
                traceErrors = new LinkedList<>();
                executor.visitSet(context, ParamsConsts.TRACE_ERRORS, traceErrors);
            }

            if (traceErrors.isEmpty()) {
                traceErrors.add(re);
            } else {
                if (!traceErrors.contains(re)) {
                    traceErrors.add(re);
                }
                int size = traceErrors.size();
                while (size > 10) {
                    traceErrors.removeFirst();
                    size--;
                }
            }

            executor.visitSet(context, ParamsConsts.TRACE_ERROR, re);
            ContextHolder.TRACE_ERROR.set(re);

            try {
                onThrowing(re, pointContext, node, context, executor);
            } catch (Throwable ex) {
                executor.logger().logWarn(() -> ex.getMessage(), ex);
            }

            throw re;
        } finally {
            updateTraceInfo(node, context, executor);

            long ets = SystemClock.currentTimeMillis();
            long useTs = ets - bts;
            pointContext.put(POINT_KEY_END_TS, ets);
            pointContext.put(POINT_KEY_USE_TS, useTs);

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
                executor.logger().logWarn(() -> e.getMessage(), e);
            }

            if(beforeNode!=null) {
                updateTraceInfo(beforeNode, context, executor);
            }
        }
    }

    public static void updateTraceInfo(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        executor.visitSet(context, ParamsConsts.TRACE_LOCATION, getNodeLocation(node));
        executor.visitSet(context, ParamsConsts.TRACE_FILE, node.getLocationFile());
        executor.visitSet(context, ParamsConsts.TRACE_LINE, node.getLocationLineNumber());
        executor.visitSet(context, ParamsConsts.TRACE_TAG, node.getTagName());
        executor.visitSet(context, ParamsConsts.TRACE_NODE, node);
        ContextHolder.TRACE_LOCATION.set(node.getNodeLocation());
        ContextHolder.TRACE_FILE.set(node.getLocationFile());
        ContextHolder.TRACE_LINE.set(node.getLocationLineNumber());
        ContextHolder.TRACE_TAG.set(node.getTagName());
        ContextHolder.TRACE_NODE.set(node);
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
