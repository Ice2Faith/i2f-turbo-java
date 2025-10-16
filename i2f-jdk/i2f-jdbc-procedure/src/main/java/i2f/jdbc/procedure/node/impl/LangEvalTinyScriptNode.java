package i2f.jdbc.procedure.node.impl;

import i2f.extension.antlr4.script.tiny.impl.TinyScript;
import i2f.extension.antlr4.script.tiny.impl.TinyScriptResolver;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.LangConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.node.impl.tinyscript.ProcedureTinyScriptResolver;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.reportor.GrammarReporter;
import i2f.jdbc.procedure.script.EvalScriptProvider;
import i2f.jdbc.procedure.signal.SignalException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangEvalTinyScriptNode extends AbstractExecutorNode implements EvalScriptProvider {
    public static final String TAG_NAME = TagConsts.LANG_EVAL_TINYSCRIPT;
    public static final String ALIAS_TAG_NAME = TagConsts.LANG_EVAL_TS;
    public static final String[] ALIAS = {ALIAS_TAG_NAME};

    public static void main(String[] args) {
        /*language=tinyscript*/
        String script = "${a}+${b}";
        Map<String, Object> context = new HashMap<>();
        context.put("a", 1);
        context.put("b", 2.5);
        Object obj = evalTinyScript(script, context, null);
        System.out.println(obj);
    }

    public static Object evalTinyScript(String script, Object context, JdbcProcedureExecutor executor) {
        return evalTinyScript(null, script, context, executor);
    }

    public static Object evalTinyScript(XmlNode node, String script, Object context, JdbcProcedureExecutor executor) {

        Object obj = null;

        try {
            TinyScriptResolver resolver = new ProcedureTinyScriptResolver(executor, node);
            obj = TinyScript.script(script, context, resolver);
        } catch (Exception e) {
            if (e instanceof SignalException) {
                throw (SignalException) e;
            } else {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
        return obj;
    }

    @Override
    public String[] alias() {
        return ALIAS;
    }

    @Override
    public String tag() {
        return TAG_NAME;
    }


    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        String script = node.getTextBody();
        if (script != null && !script.isEmpty()) {
            GrammarReporter.reportExprFeatureGrammar(script, FeatureConsts.EVAL_TINYSCRIPT, node, "element body ", warnPoster);
        }
    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        String script = node.getTextBody();
        Object obj = evalTinyScript(script, context, executor);

        if (result != null) {
            obj = executor.resultValue(obj, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
            executor.visitSet(context, result, obj);
        }

    }

    @Override
    public boolean support(String lang) {
        return Arrays.asList(LangConsts.TINYSCRIPT, LangConsts.TS).contains(lang);
    }

    @Override
    public Object eval(String script, Map<String, Object> params, JdbcProcedureExecutor executor) {
        Object obj = evalTinyScript(script, params, executor);
        return obj;
    }




}
