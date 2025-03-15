package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class TextNode extends AbstractExecutorNode {
    @Override
    public boolean support(XmlNode node) {
        String type = node.getNodeType();
        return XmlNode.NODE_TEXT.equals(type)
                || XmlNode.NODE_CDATA.equals(type)
                ;
    }

    @Override
    public void execInner(XmlNode node, Map<String,Object> context, JdbcProcedureExecutor executor) {
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        String text = node.getTextBody();
        if (result != null && !result.isEmpty()) {
            Object obj = executor.resultValue(text, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
            executor.visitSet(context, result, obj);
        }
    }
}
