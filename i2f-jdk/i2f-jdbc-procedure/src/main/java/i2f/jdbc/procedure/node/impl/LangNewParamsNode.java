package i2f.jdbc.procedure.node.impl;

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
    public void exec(XmlNode node, Map<String, Object> params, Map<String, XmlNode> nodeMap, JdbcProcedureExecutor executor) {
        String result = node.getTagAttrMap().get("result");
        Map<String, Object> newParams = executor.newParams(params, nodeMap);
        for (Map.Entry<String, String> entry : node.getTagAttrMap().entrySet()) {
            String key = entry.getKey();
            if ("result".equals(key)) {
                continue;
            }
            Object value = executor.attrValue(key, "visit", node, params, nodeMap);
            executor.setParamsObject(newParams, key, value);
        }
        if (result != null && !result.isEmpty()) {
            Object res = executor.resultValue(newParams, node.getAttrFeatureMap().get("result"), node, params, nodeMap);
            executor.setParamsObject(params, result, res);
        }
    }
}
