package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class DebuggerNode implements ExecutorNode {
    public static final String TAG_NAME = "debugger";

    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        boolean ok=true;
        String tag=node.getTagAttrMap().get(AttrConsts.TAG);
        String expr = node.getTagAttrMap().get(AttrConsts.TEST);
        if(expr!=null && !expr.isEmpty()){
            ok = (boolean) executor.attrValue(AttrConsts.TEST, FeatureConsts.TEST, node, context);
        }
        if (ok) {
            executor.openDebugger(tag,context,expr);
        }
    }


}
