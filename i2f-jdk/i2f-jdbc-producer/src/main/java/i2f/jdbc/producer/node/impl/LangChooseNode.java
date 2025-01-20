package i2f.jdbc.producer.node.impl;

import i2f.jdbc.producer.executor.SqlProducerExecutor;
import i2f.jdbc.producer.node.ExecutorNode;
import i2f.jdbc.producer.parser.data.XmlNode;

import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangChooseNode implements ExecutorNode {
    @Override
    public boolean support(XmlNode node) {
        if (!"element".equals(node.getNodeType())) {
            return false;
        }
        return "lang-choose".equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, Map<String, Object> params, Map<String, XmlNode> nodeMap, SqlProducerExecutor executor) {
        List<XmlNode> list = node.getChildren();
        XmlNode testNode = null;
        XmlNode otherNode = null;
        for (XmlNode itemNode : list) {
            String type = itemNode.getNodeType();
            if (!"element".equals(type)) {
                continue;
            }
            if ("lang-when".equals(itemNode.getTagName())) {
                if (testNode == null) {
                    String test = itemNode.getTagAttrMap().get("test");
                    boolean ok = executor.test(test, params);
                    if (ok) {
                        testNode = itemNode;
                    }
                }
            } else if ("lang-otherwise".equals(itemNode.getTagName())) {
                otherNode = itemNode;
            }
        }
        XmlNode invokeNode = null;
        if (testNode != null) {
            invokeNode = testNode;
        } else {
            invokeNode = otherNode;
        }
        if (invokeNode != null) {
            executor.execAsProducer(invokeNode, params, nodeMap);
        }
    }

}
