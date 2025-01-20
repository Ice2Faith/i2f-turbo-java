package i2f.jdbc.producer.node.impl;

import i2f.jdbc.producer.executor.SqlProducerExecutor;
import i2f.jdbc.producer.node.ExecutorNode;
import i2f.jdbc.producer.parser.data.XmlNode;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class SqlUpdateNode implements ExecutorNode {
    @Override
    public boolean support(XmlNode node) {
        if (!"element".equals(node.getNodeType())) {
            return false;
        }
        return "sql-update".equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, Map<String, Object> params, Map<String, XmlNode> nodeMap, SqlProducerExecutor executor) {
        String datasource = node.getTagAttrMap().get("datasource");
        String script = node.getTagAttrMap().get("script");
        String result = node.getTagAttrMap().get("result");
        if (script != null && !script.isEmpty()) {
            script = (String) executor.visit(script, params);
        } else {
            script = node.getTagBody();
        }
        int row = executor.sqlUpdate(datasource, script, params);
        if (result != null && !result.isEmpty()) {
            params.put(result, row);
        }
    }

}
