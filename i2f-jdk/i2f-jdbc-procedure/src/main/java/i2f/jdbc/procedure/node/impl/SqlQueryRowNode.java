package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class SqlQueryRowNode implements ExecutorNode {
    @Override
    public boolean support(XmlNode node) {
        if (!"element".equals(node.getNodeType())) {
            return false;
        }
        return "sql-query-row".equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, Map<String, Object> params, Map<String, XmlNode> nodeMap, JdbcProcedureExecutor executor) {
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
        Object row = executor.sqlQueryRow(datasource, script, params, resultType);
        if (result != null && !result.isEmpty()) {
            executor.setParamsObject(params, result, row);
        }
    }


}
