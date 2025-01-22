package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangSetNode implements ExecutorNode {
    @Override
    public boolean support(XmlNode node) {
        if (!"element".equals(node.getNodeType())) {
            return false;
        }
        return "lang-set".equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        Object value = executor.attrValue("value", "visit", node, context);
        String result = node.getTagAttrMap().get("result");
        if (result != null && !result.isEmpty()) {
            value = executor.resultValue(value, node.getAttrFeatureMap().get("result"), node, context);
            executor.setParamsObject(context.getParams(), result, value);
        }
    }


}
