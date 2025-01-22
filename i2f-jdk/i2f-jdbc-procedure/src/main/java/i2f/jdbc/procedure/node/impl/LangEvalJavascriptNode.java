package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.script.ScriptProvider;

import javax.script.Bindings;
import javax.script.ScriptException;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangEvalJavascriptNode implements ExecutorNode {
    @Override
    public boolean support(XmlNode node) {
        if (!"element".equals(node.getNodeType())) {
            return false;
        }
        return "lang-eval-javascript".equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        String result = node.getTagAttrMap().get("result");
        ScriptProvider provider = ScriptProvider.getJavaScriptInstance();
        Bindings bindings = provider.createBindings();
        bindings.put("context", context);
        bindings.put("executor", executor);
        bindings.put("params", context.getParams());

        String script = node.getTextBody();
        Object obj = null;

        try {
            obj = provider.eval(script, bindings);
        } catch (ScriptException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

        if (result != null && !result.isEmpty()) {
            obj = executor.resultValue(obj, node.getAttrFeatureMap().get("result"), node, context);
            executor.setParamsObject(context.getParams(), result, obj);
        }

    }
}
