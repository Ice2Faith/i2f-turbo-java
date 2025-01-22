package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class ProcedureCallNode implements ExecutorNode {
    @Override
    public boolean support(XmlNode node) {
        if (!"element".equals(node.getNodeType())) {
            return false;
        }
        return "procedure-call".equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        String refid = node.getTagAttrMap().get("refid");
        XmlNode nextNode = context.getNodeMap().get(refid);
        if (nextNode == null) {
            return;
        }
        Map<String, Object> callParams = context.getParams();
        String paramsText = node.getTagAttrMap().get("params");
        if (paramsText != null && !paramsText.isEmpty()) {
            Object value = executor.attrValue("params", "visit", node, context);
            if (value instanceof Map) {
                callParams = (Map<String, Object>) value;
            }
        }
        // 备份堆栈
        Map<String, Object> bakParams = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : node.getTagAttrMap().entrySet()) {
            String name = entry.getKey();
            if ("refid".equals(name)) {
                continue;
            }
            if ("params".equals(name)) {
                continue;
            }
            String script = entry.getValue();
            // 备份堆栈
            bakParams.put(name, context.getParams().get(script));
            Object val = executor.attrValue(name, "visit", node, context);
            callParams.put(name, val);
        }

        ExecuteContext callContext = new ExecuteContext();
        callContext.setNodeMap(context.getNodeMap());
        callContext.setParams(callParams);
        executor.execAsProcedure(nextNode, callContext);

        // 恢复堆栈
        context.getParams().putAll(bakParams);
    }

}
