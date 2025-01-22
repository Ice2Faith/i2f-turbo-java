package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangNewParamsNode implements ExecutorNode {
    @Override
    public boolean support(XmlNode node) {
        if (!"element".equals(node.getNodeType())) {
            return false;
        }
        return "lang-new-params".equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        String result = node.getTagAttrMap().get("result");
        Map<String, Object> newParams = executor.newParams(context);
        for (Map.Entry<String, String> entry : node.getTagAttrMap().entrySet()) {
            String key = entry.getKey();
            if ("result".equals(key)) {
                continue;
            }
            Object value = executor.attrValue(key, "visit", node, context);
            executor.setParamsObject(newParams, key, value);
        }
        if (result != null && !result.isEmpty()) {
            Object res = executor.resultValue(newParams, node.getAttrFeatureMap().get("result"), node, context);
            executor.setParamsObject(context.getParams(), result, res);
        }
    }
}
