package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangStringJoinNode implements ExecutorNode {
    @Override
    public boolean support(XmlNode node) {
        if (!"element".equals(node.getNodeType())) {
            return false;
        }
        return "lang-string-join".equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, Map<String, Object> params, Map<String, XmlNode> nodeMap, JdbcProcedureExecutor executor) {
        Object separator = executor.attrValue("separator", "visit", node, params, nodeMap);
        String result = node.getTagAttrMap().get("result");
        StringBuilder builder = new StringBuilder();
        List<Map.Entry<Integer, String>> argList = new ArrayList<>();
        Map<String, String> attrMap = node.getTagAttrMap();
        for (Map.Entry<String, String> entry : attrMap.entrySet()) {
            String key = entry.getKey();
            int idx = Integer.parseInt(key.substring("arg".length()));
            argList.add(new AbstractMap.SimpleEntry<>(idx, entry.getValue()));
        }
        argList.sort((o1, o2) -> Integer.compare(o1.getKey(), o2.getKey()));

        boolean isFirst = true;
        for (Map.Entry<Integer, String> entry : argList) {
            String attrName = "arg" + entry.getKey();
            if (!isFirst) {
                if (separator != null) {
                    builder.append(separator);
                }
            }
            Object val = executor.attrValue(attrName, "visit", node, params, nodeMap);
            builder.append(val);
            isFirst = false;
        }

        Object value = builder.toString();
        if (result != null && !result.isEmpty()) {
            value = executor.resultValue(value, node.getAttrFeatureMap().get("result"), node, params, nodeMap);
            executor.setParamsObject(params, result, value);
        }
    }


}
