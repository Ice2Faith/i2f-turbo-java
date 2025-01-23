package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
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
    public static final String TAG_NAME="procedure-call";
    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        String refid = node.getTagAttrMap().get(AttrConsts.REFID);
        XmlNode nextNode = context.getNodeMap().get(refid);
        if (nextNode == null) {
            return;
        }
        Map<String, Object> callParams = context.getParams();
        String paramsText = node.getTagAttrMap().get(AttrConsts.PARAMS);
        if (paramsText != null && !paramsText.isEmpty()) {
            Object value = executor.attrValue(AttrConsts.PARAMS, FeatureConsts.VISIT, node, context);
            if (value instanceof Map) {
                callParams = (Map<String, Object>) value;
            }
        }
        // 备份堆栈
        Map<String, Object> bakParams = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : node.getTagAttrMap().entrySet()) {
            String name = entry.getKey();
            if (AttrConsts.REFID.equals(name)) {
                continue;
            }
            if (AttrConsts.PARAMS.equals(name)) {
                continue;
            }
            String script = entry.getValue();
            // 备份堆栈
            bakParams.put(name, context.getParams().get(script));
            Object val = executor.attrValue(name, FeatureConsts.VISIT, node, context);
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
