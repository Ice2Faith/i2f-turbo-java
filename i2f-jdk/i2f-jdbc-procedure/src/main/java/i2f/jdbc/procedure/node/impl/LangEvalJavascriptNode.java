package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.script.ScriptProvider;

import javax.script.Bindings;
import javax.script.ScriptException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangEvalJavascriptNode extends AbstractExecutorNode {
    public static final String TAG_NAME = "lang-eval-javascript";
    public static final String ALIAS_TAG_NAME="lang-eval-js";
    public static void main(String[] args){
        /*language=javascript*/
        String script= "params.get(\"a\")+params.get(\"b\")";
        Map<String,Object> context=new HashMap<>();
        context.put("a",1);
        context.put("b",2.5);
        Object obj = evalJavascript(script,context,null);
        System.out.println(obj);
    }
    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName())
                || ALIAS_TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void execInner(XmlNode node, Map<String,Object> context, JdbcProcedureExecutor executor) {
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        String script = node.getTextBody();
        Object obj = evalJavascript(script, context, executor);

        if (result != null && !result.isEmpty()) {
            obj = executor.resultValue(obj, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
            executor.visitSet(context, result, obj);
        }

    }

    public static Object evalJavascript(String script, Object context, JdbcProcedureExecutor executor) {
        ScriptProvider provider = ScriptProvider.getJavaScriptInstance();
        Bindings bindings = provider.createBindings();
        bindings.put("executor", executor);
        bindings.put("params", context);

        Object obj = null;

        try {
            obj = provider.eval(script, bindings);
        } catch (ScriptException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return obj;
    }
}
