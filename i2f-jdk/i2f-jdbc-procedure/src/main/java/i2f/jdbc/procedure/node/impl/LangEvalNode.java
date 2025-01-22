package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangEvalNode implements ExecutorNode {
    @Override
    public boolean support(XmlNode node) {
        if (!"element".equals(node.getNodeType())) {
            return false;
        }
        return "lang-eval".equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        String script = node.getTextBody();
        Object val = executor.eval(script, context.getParams());
        String result = node.getTagAttrMap().get("result");
        if (result != null && !result.isEmpty()) {
            val = executor.resultValue(val, node.getAttrFeatureMap().get("result"), node, context);
            executor.setParamsObject(context.getParams(), result, val);
        }
    }

}
