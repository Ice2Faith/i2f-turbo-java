package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

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
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        String datasource = node.getTagAttrMap().get("datasource");
        String script = node.getTagAttrMap().get("script");
        String result = node.getTagAttrMap().get("result");
        if (script != null && !script.isEmpty()) {
            script = (String) executor.visit(script, context.getParams());
        } else {
            script = node.getTagBody();
        }
        int row = executor.sqlUpdate(datasource, script, context.getParams());
        if (result != null && !result.isEmpty()) {
            Object val = executor.resultValue(row, node.getAttrFeatureMap().get("result"), node, context);
            executor.setParamsObject(context.getParams(), result, val);
        }
    }

}
