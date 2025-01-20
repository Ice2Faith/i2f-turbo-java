package i2f.jdbc.producer.node.impl;

import i2f.jdbc.producer.executor.SqlProducerExecutor;
import i2f.jdbc.producer.node.ExecutorNode;
import i2f.jdbc.producer.parser.data.XmlNode;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class TextNode implements ExecutorNode {
    @Override
    public boolean support(XmlNode node) {
        String type = node.getNodeType();
        return "text".equals(type)
                || "cdata".equals(type)
                ;
    }

    @Override
    public void exec(XmlNode node, Map<String, Object> params, Map<String, XmlNode> nodeMap, SqlProducerExecutor executor) {
        String result = node.getTagAttrMap().get("result");
        String text = node.getTextBody();
        if (result != null && !result.isEmpty()) {
            params.put(result, text);
        }
    }
}
