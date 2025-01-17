package i2f.jdbc.producer.node.impl;

import i2f.jdbc.producer.executor.SqlProducerExecutor;
import i2f.jdbc.producer.node.ExecutorNode;
import i2f.jdbc.producer.parser.data.XmlNode;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class ScriptIncludeNode implements ExecutorNode {
    @Override
    public boolean support(XmlNode node) {
        if (!"element".equals(node.getNodeType())) {
            return false;
        }
        return "script-include".equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, Map<String, Object> params, Map<String, XmlNode> nodeMap, SqlProducerExecutor executor) {
        String refid = node.getTagAttrMap().get("refid");
        XmlNode nextNode = nodeMap.get(refid);
        if (nextNode == null) {
            return;
        }
        // 备份堆栈
        Map<String, Object> bakParams = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : node.getTagAttrMap().entrySet()) {
            String name = entry.getKey();
            if ("refid".equals(name)) {
                continue;
            }
            String script = entry.getValue();
            // 备份堆栈
            bakParams.put(name, params.get(script));
            Object val = executor.eval(script, params);
            params.put(name, val);
        }

        executor.execAsProducer(nextNode, params, nodeMap);

        // 恢复堆栈
        params.putAll(bakParams);
    }

}
