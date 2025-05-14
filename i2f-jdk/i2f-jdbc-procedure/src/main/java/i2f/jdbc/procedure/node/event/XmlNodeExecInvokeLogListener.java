package i2f.jdbc.procedure.node.event;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.ParamsConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.event.XProc4jEvent;
import i2f.jdbc.procedure.event.XProc4jEventListener;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.ControlSignalException;
import i2f.jdbc.procedure.signal.impl.ReturnSignalException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2025/5/7 11:18
 */
@Data
@NoArgsConstructor
public class XmlNodeExecInvokeLogListener implements XProc4jEventListener {
    protected volatile Predicate<XmlNodeExecEvent> printFilter;

    @Override
    public boolean support(XProc4jEvent event) {
        return event instanceof XmlNodeExecEvent;
    }

    @Override
    public boolean handle(XProc4jEvent event) {
        XmlNodeExecEvent evt = (XmlNodeExecEvent) event;
        if (printFilter != null && !printFilter.test(evt)) {
            return false;
        }
        Map<String, Object> pointContext = evt.getPointContext();
        Map<String, Object> context = evt.getContext();
        XmlNodeExecEvent.Type type = evt.getType();
        XmlNode node = evt.getNode();
        JdbcProcedureExecutor executor = evt.getExecutor();
        Throwable e = evt.getThrowable();

        LinkedList<Map.Entry<String, String>> traceCalls = executor.visitAs(ParamsConsts.TRACE_CALLS, context);
        if (traceCalls == null) {
            traceCalls = new LinkedList<>();
            executor.visitSet(context, ParamsConsts.TRACE_CALLS, traceCalls);
        }


        if (type == XmlNodeExecEvent.Type.BEFORE) {

            String location = (String) pointContext.get("location");
            String snapshotTraceId = (String) pointContext.get("snapshotTraceId");

            boolean isDebugMode = executor.visitAs("isDebugMode", pointContext);

            if (isDebugMode) {
                String tagName = node.getTagName();
                if (tagName != null) {
                    if (TagConsts.PROCEDURE.equals(tagName)) {
                        String id = node.getTagAttrMap().get(AttrConsts.ID);
                        if (id != null && !id.isEmpty()) {

                            String callSnapshot = getCallSnapshot(node, context, executor);
                            callSnapshot = "BEFORE:" + snapshotTraceId + "\n" + callSnapshot;
                            traceCalls.add(new AbstractMap.SimpleEntry<>(id, callSnapshot));
                            int size = traceCalls.size();
                            while (size > 1000) {
                                traceCalls.removeFirst();
                                size--;
                            }
                            executor.logDebug("call-params:\n===================> " + callSnapshot);
                        }
                    }
                }
            }
        } else if (type == XmlNodeExecEvent.Type.AFTER) {
            boolean isDebugMode = executor.visitAs("isDebugMode", pointContext);
            if (isDebugMode) {
                String snapshotTraceId = executor.visitAs("snapshotTraceId", pointContext);

                String tagName = node.getTagName();
                if (tagName != null) {
                    if (TagConsts.PROCEDURE.equals(tagName)) {
                        String id = node.getTagAttrMap().get(AttrConsts.ID);
                        if (id != null && !id.isEmpty()) {
                            String callSnapshot = getCallSnapshot(node, context, executor);
                            callSnapshot = "AFTER:" + snapshotTraceId + "\n" + callSnapshot;
                            traceCalls.add(new AbstractMap.SimpleEntry<>(id, callSnapshot));
                            int size = traceCalls.size();
                            while (size > 1000) {
                                traceCalls.removeFirst();
                                size--;
                            }
                            executor.logDebug("call-params:\n===================> " + callSnapshot);
                        }
                    }
                }
            }
        } else if (type == XmlNodeExecEvent.Type.THROWING) {

            boolean isDebugMode = executor.visitAs("isDebugMode", pointContext);
            if (isDebugMode) {
                String snapshotTraceId = executor.visitAs("snapshotTraceId", pointContext);

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
                                    traceCalls.add(new AbstractMap.SimpleEntry<>(id, callSnapshot));
                                    int size = traceCalls.size();
                                    while (size > 1000) {
                                        traceCalls.removeFirst();
                                        size--;
                                    }
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
        return false;
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
}
