package i2f.jdbc.procedure.node.impl;

import i2f.extension.antlr4.script.tiny.impl.DefaultTinyScriptResolver;
import i2f.extension.antlr4.script.tiny.impl.TinyScript;
import i2f.extension.antlr4.script.tiny.impl.TinyScriptResolver;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.ParamsConsts;
import i2f.jdbc.procedure.context.ContextHolder;
import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.executor.impl.DefaultJdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.reportor.GrammarReporter;
import i2f.jdbc.procedure.signal.impl.NotFoundSignalException;
import i2f.reference.Reference;
import i2f.reflect.ReflectResolver;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangEvalTinyScriptNode extends AbstractExecutorNode {
    public static final String TAG_NAME = "lang-eval-tinyscript";
    public static final String ALIAS_TAG_NAME = "lang-eval-ts";
    public static void main(String[] args){
        /*language=tinyscript*/
        String script= "${a}+${b}";
        Map<String,Object> context=new HashMap<>();
        context.put("a",1);
        context.put("b",2.5);
        Object obj =evalTinyScript(script,context,null);
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
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        String script = node.getTextBody();
        if(script!=null && !script.isEmpty()) {
            GrammarReporter.reportExprFeatureGrammar(script, FeatureConsts.EVAL_TINYSCRIPT, node, "element body ", warnPoster);
        }
    }

    @Override
    public void execInner(XmlNode node, Map<String,Object> context, JdbcProcedureExecutor executor) {
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        String script = node.getTextBody();
        Object obj = evalTinyScript(script, context, executor);

        if (result != null && !result.isEmpty()) {
            obj = executor.resultValue(obj, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
            executor.visitSet(context, result, obj);
        }

    }

    public static Object evalTinyScript(String script, Object context, JdbcProcedureExecutor executor) {

        Object obj = null;

        try {
            TinyScriptResolver resolver = null;
            if(executor!=null){
                resolver=new ProcedureTinyScriptResolver(executor);
            }
            obj = TinyScript.script(script, context, resolver);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return obj;
    }

    public static class ProcedureTinyScriptResolver extends DefaultTinyScriptResolver {
        protected JdbcProcedureExecutor executor;

        public ProcedureTinyScriptResolver(JdbcProcedureExecutor executor) {
            this.executor = executor;
        }

        @Override
        public void debugLog(Supplier<Object> supplier) {
            executor.debugLog(()->"tiny-script:"+supplier.get());
        }

        @Override
        public void openDebugger(String tag, Object context, String conditionExpression) {
            executor.openDebugger("tiny-script:"+tag, context, conditionExpression);
        }

        @Override
        public void setValue(Object context, String name, Object value) {
            executor.visitSet((Map<String, Object>) context, name, value);
        }

        @Override
        public Object getValue(Object context, String name) {
            return executor.visit(name, (Map<String, Object>) context);
        }

        @Override
        public Class<?> loadClass(String className) {
            return executor.loadClass(className);
        }

        @Override
        public Reference<Object> beforeFunctionCall(Object target, boolean isNew, String naming, List<Object> argList) {
            try {
                ProcedureMeta meta = executor.getMeta(naming);
                if (meta == null) {
                    return Reference.nop();
                }
                Map<String, Object> callParams = castArgumentListAsNamingMap(argList);
                Map<String, Object> ret = executor.exec(naming, callParams);
                if (ret.containsKey(ParamsConsts.RETURN)) {
                    Object val = ret.get(ParamsConsts.RETURN);
                    if(val instanceof Reference){
                        return (Reference<Object>) val;
                    }
                    return Reference.of(val);
                }
                return Reference.of(ret);
            } catch (NotFoundSignalException e) {
                return Reference.nop();
            }
        }

        @Override
        public Method findMethod(String naming, List<Object> args) {
            List<Method> list = ContextHolder.INVOKE_METHOD_MAP.get(naming);
            if (list != null && !list.isEmpty()) {
                Method method = ReflectResolver.matchExecutable(list, args);
                if (method != null) {
                    return method;
                }
            }

            return super.findMethod(naming, args);
        }

        @Override
        public String renderString(String text, Object context) {
            return executor.render(text, (Map<String, Object>) context);
        }
    }


}
