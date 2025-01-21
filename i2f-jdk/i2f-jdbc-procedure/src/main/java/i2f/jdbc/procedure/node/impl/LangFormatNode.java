package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangFormatNode implements ExecutorNode {
    @Override
    public boolean support(XmlNode node) {
        if (!"element".equals(node.getNodeType())) {
            return false;
        }
        return "lang-format".equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, Map<String, Object> params, Map<String, XmlNode> nodeMap, JdbcProcedureExecutor executor) {
        Object value = executor.attrValue("value", "visit", node, params, nodeMap);
        String pattern = (String) executor.attrValue("pattern", "visit", node, params, nodeMap);
        String result = node.getTagAttrMap().get("result");
        value = String.format(pattern, value);
        if (result != null && !result.isEmpty()) {
            value = executor.resultValue(value, node.getAttrFeatureMap().get("result"), node, params, nodeMap);
            executor.setParamsObject(params, result, value);
        }
    }


}
