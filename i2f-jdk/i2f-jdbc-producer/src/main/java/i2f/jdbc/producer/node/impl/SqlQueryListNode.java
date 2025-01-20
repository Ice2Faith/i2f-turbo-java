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
public class SqlQueryListNode implements ExecutorNode {
    @Override
    public boolean support(XmlNode node) {
        if (!"element".equals(node.getNodeType())) {
            return false;
        }
        return "sql-query-list".equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, Map<String, Object> params, Map<String, XmlNode> nodeMap, SqlProducerExecutor executor) {
        String datasource = node.getTagAttrMap().get("datasource");
        String script = node.getTagAttrMap().get("script");
        String result = node.getTagAttrMap().get("result");
        String resultTypeName = node.getTagAttrMap().get("result-type");
        Class<?> resultType = executor.loadClass(resultTypeName);
        if (resultType == null) {
            resultType = Map.class;
        }
        if (script != null && !script.isEmpty()) {
            script = (String) executor.visit(script, params);
        } else {
            script = node.getTagBody();
        }
        List<?> row = executor.sqlQueryList(datasource, script, params, resultType);
        if (result != null && !result.isEmpty()) {
            params.put(result, row);
        }
    }


}
