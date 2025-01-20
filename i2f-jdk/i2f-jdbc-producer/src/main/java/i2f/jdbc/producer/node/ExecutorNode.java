package i2f.jdbc.producer.node;

import i2f.jdbc.producer.executor.SqlProducerExecutor;
import i2f.jdbc.producer.parser.data.XmlNode;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 10:36
 */
public interface ExecutorNode {
    boolean support(XmlNode node);

    void exec(XmlNode node, Map<String, Object> params, Map<String, XmlNode> nodeMap, SqlProducerExecutor executor);
}
