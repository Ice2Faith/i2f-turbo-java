package i2f.jdbc.procedure.node.impl;

import i2f.extension.antlr4.script.tiny.impl.TinyScript;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

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
            obj = TinyScript.script(script, context.getParams());
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return obj;
    }
}
