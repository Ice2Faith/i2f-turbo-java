package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/1/20 10:37
 */
public class ProcedureNode implements ExecutorNode {
    public static final String TAG_NAME="procedure";
    public static final ProcedureNode INSTANCE = new ProcedureNode();

    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        List<XmlNode> children = node.getChildren();
        for (int i = 0; i < children.size(); i++) {
            XmlNode item = children.get(i);
            if (item == null) {
                continue;
            }
            executor.exec(item, context);
        }
    }
}
