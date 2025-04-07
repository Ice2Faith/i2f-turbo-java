package i2f.jdbc.procedure.node.impl;

import i2f.extension.antlr4.script.tiny.impl.DefaultTinyScriptResolver;
import i2f.extension.antlr4.script.tiny.impl.TinyScript;
import i2f.extension.antlr4.script.tiny.impl.TinyScriptResolver;
import i2f.invokable.method.IMethod;
import i2f.jdbc.procedure.consts.*;
import i2f.jdbc.procedure.context.ContextHolder;
import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.reportor.GrammarReporter;
import i2f.jdbc.procedure.script.EvalScriptProvider;
import i2f.jdbc.procedure.signal.impl.NotFoundSignalException;
import i2f.reference.Reference;
import i2f.reflect.ReflectResolver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangEvalTinyScriptNode extends AbstractExecutorNode implements EvalScriptProvider {
    public static final String TAG_NAME = TagConsts.LANG_EVAL_TINYSCRIPT;
    public static final String ALIAS_TAG_NAME = TagConsts.LANG_EVAL_TS;
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
        if (XmlNode.NodeType.ELEMENT !=node.getNodeType()) {
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

    @Override
    public boolean support(String lang) {
        return Arrays.asList(LangConsts.TINYSCRIPT,LangConsts.TS).contains(lang);
    }

    @Override
    public Object eval(String script, Map<String, Object> params, JdbcProcedureExecutor executor) {
        Object obj=evalTinyScript(script,params,executor);
        return obj;
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
//            executor.logDebug(() -> "tiny-script:" + supplier.get() + " , at " + ContextHolder.TRACE_LOCATION.get() + ":" + ContextHolder.TRACE_LINE.get());
        }

        @Override
        public void openDebugger(Object context,String tag,  String conditionExpression) {
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
        public Class<?> loadClass(Object context,String className) {
            return executor.loadClass(className);
        }

        @Override
        public Reference<Object> beforeFunctionCall(Object context,Object target, boolean isNew, String naming, List<Object> argList) {
            try {
                ProcedureMeta meta = executor.getMeta(naming);
                if (meta == null) {
                    return Reference.nop();
                }

                Map<String, Object> callParams= executor.newParams((Map<String,Object>)context);

                 Map<String,Object> argsMap= castArgumentListAsNamingMap(context,argList);
                if(argsMap.isEmpty()){
                    List<String> arguments = meta.getArguments().stream().filter(e->!Arrays.asList(
                            AttrConsts.ID,AttrConsts.REFID,
                            AttrConsts.RESULT,ParamsConsts.RETURN
                    ).contains(e)).collect(Collectors.toList());
                    for (int i = 0; i < argList.size(); i++) {
                        if(i>=arguments.size()){
                            break;
                        }
                        String name = arguments.get(i);
                        Object val = argList.get(i);
                        argsMap.put(name,val);
                    }
                }

                callParams.putAll(argsMap);

                Map<String, Object> ret = executor.exec(naming, callParams, false, false);
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
        public IMethod findMethod(Object context,String naming, List<Object> args) {
            List<IMethod> list = ContextHolder.INVOKE_METHOD_MAP.get(naming);
            if (list != null && !list.isEmpty()) {
                IMethod method = ReflectResolver.matchExecMethod(list, args);
                if (method != null) {
                    return method;
                }
            }

            return super.findMethod(context,naming, args);
        }

        @Override
        public String renderString(Object context,String text) {
            return executor.render(text, (Map<String, Object>) context);
        }
    }


}
