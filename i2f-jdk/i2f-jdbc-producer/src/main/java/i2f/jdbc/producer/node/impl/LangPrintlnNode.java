package i2f.jdbc.producer.node.impl;

import i2f.jdbc.producer.executor.SqlProducerExecutor;
import i2f.jdbc.producer.node.ExecutorNode;
import i2f.jdbc.producer.parser.data.XmlNode;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangPrintlnNode implements ExecutorNode {
    @Override
    public boolean support(XmlNode node) {
        if (!"element".equals(node.getNodeType())) {
            return false;
        }
        return "lang-println".equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, Map<String, Object> params, Map<String, XmlNode> nodeMap, SqlProducerExecutor executor) {
        StringBuilder builder = new StringBuilder();
        String tag = node.getTagAttrMap().get("tag");
        builder.append("[").append(tag == null ? "" : tag).append("]");
        boolean isFirst = true;
        for (Map.Entry<String, String> entry : node.getTagAttrMap().entrySet()) {
            if ("tag".equals(entry.getKey())) {
                continue;
            }
            if (!isFirst) {
                builder.append(", ");
            }
            String script = entry.getValue();
            Object val = executor.visit(script, params);
            builder.append(entry.getKey()).append(":").append(val);
            isFirst = false;
        }
        System.out.println(builder);
    }
}
