package i2f.jdbc.procedure.node.impl;

import i2f.extension.groovy.GroovyScript;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.reportor.GrammarReporter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangEvalGroovyNode extends AbstractExecutorNode {
    public static final String TAG_NAME = "lang-eval-groovy";

    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        String script = node.getTextBody();
        if(script!=null && !script.isEmpty()) {
            GrammarReporter.reportExprFeatureGrammar(script, FeatureConsts.EVAL_GROOVY, node, "element body ", warnPoster);
        }
    }

    @Override
    public void execInner(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        String script = node.getTextBody();
        Object obj = evalGroovyScript(script, context, executor);

        if (result != null && !result.isEmpty()) {
            obj = executor.resultValue(obj, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
            executor.setParamsObject(context.getParams(), result, obj);
        }

    }

    public static Object evalGroovyScript(String script, ExecuteContext context, JdbcProcedureExecutor executor) {
        Map<String, Object> bindings = new HashMap<>();
        bindings.put("context", context);
        bindings.put("executor", executor);
        bindings.put("params", context.getParams());

        Object obj = null;

        try {
            obj = GroovyScript.eval(script, bindings);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return obj;
    }
}
