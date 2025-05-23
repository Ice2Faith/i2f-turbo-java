package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.*;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.executor.impl.DefaultJdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.reportor.GrammarReporter;
import i2f.jdbc.procedure.script.EvalScriptProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangEvalNode extends AbstractExecutorNode implements EvalScriptProvider {
    public static final String TAG_NAME = TagConsts.LANG_EVAL;

    public static void main(String[] args) {
        /*language=scala*/
        String script = "a+b";
        Map<String, Object> context = new HashMap<>();
        context.put("a", 1);
        context.put("b", 2.5);
        Object obj = new DefaultJdbcProcedureExecutor().eval(script, context);
        System.out.println(obj);
    }

    @Override
    public boolean support(XmlNode node) {
        if (XmlNode.NodeType.ELEMENT != node.getNodeType()) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        String value = node.getTagAttrMap().get(AttrConsts.VALUE);
        String script = node.getTextBody();
        if ((value == null || value.isEmpty()) && (script == null || script.isEmpty())) {
            String errorMsg = "missing value attribute or element body";
            warnPoster.accept(XProc4jConsts.NAME + " report xml grammar, at " + getNodeLocation(node) + " error: " + errorMsg);
            return;
        }
        if (value != null && !value.isEmpty()) {
            try {
                GrammarReporter.reportExprFeatureGrammar(value, FeatureConsts.EVAL, node, "attribute " + AttrConsts.VALUE, warnPoster);
            } catch (Exception e) {
                String errorMsg = "attribute " + AttrConsts.VALUE + " expression error: " + e.getMessage();
                warnPoster.accept(XProc4jConsts.NAME + " report xml grammar, at " + getNodeLocation(node) + " error: " + errorMsg);
            }
        }
        if (script != null && !script.isEmpty()) {
            try {
                GrammarReporter.reportExprFeatureGrammar(script, FeatureConsts.EVAL, node, "element body", warnPoster);
            } catch (Exception e) {
                String errorMsg = "element body expression error: " + e.getMessage();
                warnPoster.accept(XProc4jConsts.NAME + " report xml grammar, at " + getNodeLocation(node) + " error: " + errorMsg);
            }
        }
    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        String value = node.getTagAttrMap().get(AttrConsts.VALUE);
        String script = node.getTextBody();
        if (value != null) {
            script = (String) executor.attrValue(AttrConsts.VALUE, FeatureConsts.STRING, node, context);
        }
        Object val = executor.eval(script, context);
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        if (result != null) {
            val = executor.resultValue(val, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
            executor.visitSet(context, result, val);
        }
    }

    @Override
    public boolean support(String lang) {
        return LangConsts.OGNL.equals(lang);
    }

    @Override
    public Object eval(String script, Map<String, Object> params, JdbcProcedureExecutor executor) {
        Object val = executor.eval(script, params);
        return val;
    }
}
