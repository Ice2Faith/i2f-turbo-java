package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangStringNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.LANG_STRING;

    @Override
    public boolean support(XmlNode node) {
        if (XmlNode.NodeType.ELEMENT != node.getNodeType()) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        String rs = node.getTextBody();
        if (executor.isDebug()) {
            String lang = node.getTagAttrMap().get("_lang");
            if ("sql".equalsIgnoreCase(lang)) {
                rs = rs + getTrackingComment(node);
            }
        }
        String text = rs;
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        if (result != null) {
            Object res = executor.resultValue(text, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
            executor.visitSet(context, result, res);
        }
    }
}
