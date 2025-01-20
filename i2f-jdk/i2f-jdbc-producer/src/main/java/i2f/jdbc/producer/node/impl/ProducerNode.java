package i2f.jdbc.producer.node.impl;

import i2f.jdbc.producer.executor.SqlProducerExecutor;
import i2f.jdbc.producer.node.ExecutorNode;
import i2f.jdbc.producer.parser.data.XmlNode;

import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 10:37
 */
public class ProducerNode implements ExecutorNode {
    public static final ProducerNode INSTANCE = new ProducerNode();

    @Override
    public boolean support(XmlNode node) {
        if (!"element".equals(node.getNodeType())) {
            return false;
        }
        return "producer".equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, Map<String, Object> params, Map<String, XmlNode> nodeMap, SqlProducerExecutor executor) {
        List<XmlNode> children = node.getChildren();
        for (int i = 0; i < children.size(); i++) {
            XmlNode item = children.get(i);
            executor.exec(item, params, nodeMap);
        }
    }
}
