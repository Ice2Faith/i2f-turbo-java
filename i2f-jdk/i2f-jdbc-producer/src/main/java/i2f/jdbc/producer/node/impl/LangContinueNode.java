package i2f.jdbc.producer.node.impl;

import i2f.jdbc.producer.executor.SqlProducerExecutor;
import i2f.jdbc.producer.node.ExecutorNode;
import i2f.jdbc.producer.parser.data.XmlNode;
import i2f.jdbc.producer.signal.impl.ContinueSignalException;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangContinueNode implements ExecutorNode {
    @Override
    public boolean support(XmlNode node) {
        if (!"element".equals(node.getNodeType())) {
            return false;
        }
        return "lang-continue".equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, Map<String, Object> params, Map<String, XmlNode> nodeMap, SqlProducerExecutor executor) {
        throw new ContinueSignalException();
    }


}