package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;


/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class ScriptSegmentNode implements ExecutorNode {
    public static final String TAG_NAME="script-segment";
    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        String id = node.getTagAttrMap().get(AttrConsts.ID);
        if (id != null && !id.isEmpty()) {
            context.getNodeMap().put(id, node);
        }
    }

}
