package i2f.jdbc.procedure.node.impl;

import i2f.extension.groovy.GroovyScript;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.LangConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.reportor.GrammarReporter;
import i2f.jdbc.procedure.script.EvalScriptProvider;
import i2f.jdbc.procedure.signal.SignalException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangEvalGroovyNode extends AbstractExecutorNode implements EvalScriptProvider {
    public static final String TAG_NAME = TagConsts.LANG_EVAL_GROOVY;

    public static void main(String[] args){
        /*language=groovy*/
        String script="params.a+params[\"b\"]";
        Map<String,Object> context=new HashMap<>();
        context.put("a",1);
        context.put("b",2.5);
        Object obj = evalGroovyScript(script, context, null);
        System.out.println(obj);
    }

    @Override
    public boolean support(XmlNode node) {
        if (XmlNode.NodeType.ELEMENT !=node.getNodeType()) {
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
    public void execInner(XmlNode node, Map<String,Object> context, JdbcProcedureExecutor executor) {
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        String script = node.getTextBody();
        Object obj = evalGroovyScript(script, context, executor);

        if (result != null) {
            obj = executor.resultValue(obj, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
            executor.visitSet(context, result, obj);
        }

    }

    @Override
    public boolean support(String lang) {
        return LangConsts.GROOVY.equals(lang);
    }

    @Override
    public Object eval(String script, Map<String,Object> params,JdbcProcedureExecutor executor) {
        Object obj = evalGroovyScript(script, params, executor);
        return obj;
    }

    public static Object evalGroovyScript(String script, Map<String,Object> context, JdbcProcedureExecutor executor) {
        Map<String, Object> bindings = new HashMap<>();
        bindings.put("executor", executor);
        bindings.put("params", context);

        String sourceCode=new StringBuilder()
                .append(LangEvalJavaNode.EVAL_JAVA_IMPORTS).append("\n")
                .append("def exec(JdbcProcedureExecutor executor, Map<String,Object> params) throws Throwable {").append("\n")
                .append(script).append("\n")
                .append("}").append("\n")
                .append("exec(executor,params);").append("\n")
                .toString();

        Object obj = null;

        try {
            obj = GroovyScript.eval(sourceCode, bindings);
        } catch (Exception e) {
            if (e instanceof SignalException) {
                throw (SignalException) e;
            }else {
                throw new IllegalStateException(e.getMessage() + "\n\t source code:\n" + sourceCode, e);
            }
        }
        return obj;
    }


}
