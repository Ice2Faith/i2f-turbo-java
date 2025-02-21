package i2f.jdbc.procedure.node.impl;

import i2f.extension.antlr4.script.tiny.impl.DefaultTinyScriptResolver;
import i2f.extension.antlr4.script.tiny.impl.TinyScript;
import i2f.extension.antlr4.script.tiny.impl.TinyScriptResolver;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.context.ContextHolder;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.reflect.ReflectResolver;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangEvalTinyScriptNode implements ExecutorNode {
    public static final String TAG_NAME = "lang-eval-tinyscript";
    public static final String ALIAS_TAG_NAME="lang-eval-ts";

    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName())
                || ALIAS_TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        String script = node.getTextBody();
        Object obj = evalTinyScript(script, context, executor);

        if (result != null && !result.isEmpty()) {
            obj = executor.resultValue(obj, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
            executor.setParamsObject(context.getParams(), result, obj);
        }

    }

    public static Object evalTinyScript(String script, ExecuteContext context, JdbcProcedureExecutor executor) {

        Object obj = null;

        try {
            TinyScriptResolver resolver=new ProcedureTinyScriptResolver(context,executor);
            obj = TinyScript.script(script, context.getParams(), resolver);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return obj;
    }

    public static class ProcedureTinyScriptResolver extends DefaultTinyScriptResolver{
        protected ExecuteContext context;
        protected JdbcProcedureExecutor executor;

        public ProcedureTinyScriptResolver(ExecuteContext context, JdbcProcedureExecutor executor) {
            this.context = context;
            this.executor = executor;
        }

        @Override
        public void setValue(Object context, String name, Object value) {
            executor.setParamsObject((Map<String,Object>)context,name,value);
        }

        @Override
        public Object getValue(Object context, String name) {
            return executor.visit(name,(Map<String,Object>)context);
        }

        @Override
        public Class<?> loadClass(String className) {
            return executor.loadClass(className);
        }

        @Override
        public Method findMethod(String naming, List<Object> args) {
            List<Method> list= ContextHolder.INVOKE_METHOD_MAP.get(naming);
            Method method = ReflectResolver.matchExecutable(list, args);
            if(method!=null){
                return method;
            }

            return super.findMethod(naming, args);
        }

        @Override
        public String renderString(String text, Object context) {
            return executor.render(text, (Map<String, Object>) context);
        }
    }


}
