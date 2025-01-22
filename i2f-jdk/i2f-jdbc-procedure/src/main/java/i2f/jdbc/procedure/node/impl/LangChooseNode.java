package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangChooseNode implements ExecutorNode {
    @Override
    public boolean support(XmlNode node) {
        if (!"element".equals(node.getNodeType())) {
            return false;
        }
        return "lang-choose".equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        List<XmlNode> list = node.getChildren();
        XmlNode testNode = null;
        XmlNode otherNode = null;
        for (XmlNode itemNode : list) {
            String type = itemNode.getNodeType();
            if (!"element".equals(type)) {
                continue;
            }
            if ("lang-when".equals(itemNode.getTagName())) {
                if (testNode == null) {
                    boolean ok = (boolean) executor.attrValue("test", "test", node, context);
                    if (ok) {
                        testNode = itemNode;
                    }
                }
            } else if ("lang-otherwise".equals(itemNode.getTagName())) {
                otherNode = itemNode;
            }
        }
        XmlNode invokeNode = null;
        if (testNode != null) {
            invokeNode = testNode;
        } else {
            invokeNode = otherNode;
        }
        if (invokeNode != null) {
            executor.execAsProcedure(invokeNode, context);
        }
    }

}
