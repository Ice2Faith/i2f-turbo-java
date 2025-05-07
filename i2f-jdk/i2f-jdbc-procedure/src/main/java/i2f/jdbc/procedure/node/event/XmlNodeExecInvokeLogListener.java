package i2f.jdbc.procedure.node.event;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.ParamsConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.event.XProc4jEvent;
import i2f.jdbc.procedure.event.XProc4jEventListener;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.ControlSignalException;
import i2f.jdbc.procedure.signal.impl.ReturnSignalException;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/5/7 11:18
 */
public class XmlNodeExecInvokeLogListener implements XProc4jEventListener {
    @Override
    public boolean support(XProc4jEvent event) {
        return event instanceof XmlNodeExecEvent;
    }

    @Override
    public boolean handle(XProc4jEvent event) {
        XmlNodeExecEvent evt = (XmlNodeExecEvent) event;
        Map<String, Object> pointContext = evt.getPointContext();
        Map<String, Object> context = evt.getContext();
        XmlNodeExecEvent.Type type = evt.getType();
        XmlNode node = evt.getNode();
        JdbcProcedureExecutor executor = evt.getExecutor();
        Throwable e = evt.getThrowable();
        if(type== XmlNodeExecEvent.Type.BEFORE){

            String location = (String) pointContext.get("location");
            String snapshotTraceId = (String) pointContext.get("snapshotTraceId");

            boolean isDebugMode = executor.visitAs("isDebugMode", pointContext);
            Map<String, Object> trace = executor.visitAs(ParamsConsts.TRACE, context);
            LinkedList<Map.Entry<String, String>> traceCalls = executor.visitAs(ParamsConsts.TRACE_CALLS, context);
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
                            String callSnapshot = AbstractExecutorNode.getCallSnapshot(node, context, executor);
                            callSnapshot = "BEFORE:" + snapshotTraceId + "\n" + callSnapshot;
                            traceCalls.add(new AbstractMap.SimpleEntry<>(id, callSnapshot));
                            executor.logDebug("call-params:\n===================> " + callSnapshot);
                        }
                    }
                }
            }
        } else if(type== XmlNodeExecEvent.Type.AFTER){

            boolean isDebugMode = executor.visitAs("isDebugMode", pointContext);
            String snapshotTraceId = executor.visitAs("snapshotTraceId", pointContext);
            LinkedList<Map.Entry<String, String>> traceCallRecords = executor.visitAs(ParamsConsts.TRACE_CALLS, context);

            if (isDebugMode) {
                String tagName = node.getTagName();
                if (tagName != null) {
                    if (TagConsts.PROCEDURE.equals(tagName)) {
                        String id = node.getTagAttrMap().get(AttrConsts.ID);
                        if (id != null && !id.isEmpty()) {
                            String callSnapshot = AbstractExecutorNode.getCallSnapshot(node, context, executor);
                            callSnapshot = "AFTER:" + snapshotTraceId + "\n" + callSnapshot;
                            traceCallRecords.add(new AbstractMap.SimpleEntry<>(id, callSnapshot));
                            executor.logDebug("call-params:\n===================> " + callSnapshot);
                        }
                    }
                }
            }
        } else if(type== XmlNodeExecEvent.Type.THROWING){

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
                                    String callSnapshot = AbstractExecutorNode.getCallSnapshot(node, context, executor);
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
        return false;
    }
}
