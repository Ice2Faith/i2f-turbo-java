package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;


/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangRenderNode extends AbstractExecutorNode {
    public static final String TAG_NAME = "lang-render";

    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void execInner(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        String script = node.getTextBody();
        String val = executor.render(script, context.getParams());
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        if (result != null && !result.isEmpty()) {
            Object res = executor.resultValue(val, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
            executor.setParamsObject(context.getParams(), result, res);
        }
    }

}
