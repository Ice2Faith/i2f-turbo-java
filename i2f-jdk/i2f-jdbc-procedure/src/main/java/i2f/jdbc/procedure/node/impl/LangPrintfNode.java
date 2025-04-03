package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangPrintfNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.LANG_PRINTF;

    @Override
    public boolean support(XmlNode node) {
        if (XmlNode.NodeType.ELEMENT !=node.getNodeType()) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void execInner(XmlNode node, Map<String,Object> context, JdbcProcedureExecutor executor) {
        StringBuilder builder = new StringBuilder();
        String tag = (String) executor.attrValue(AttrConsts.TAG, FeatureConsts.STRING, node, context);
        String value = node.getTagAttrMap().get(AttrConsts.VALUE);
        if (value != null) {
            Object obj = executor.attrValue(AttrConsts.VALUE, FeatureConsts.VISIT, node, context);
            if (obj != null) {
                value = String.valueOf(obj);
            }
        } else {
            value = node.getTagBody();
        }
        if (tag != null) {
            builder.append("[").append(tag).append("]");
        }
        String str = executor.render(value, context);
        builder.append(str);
        System.out.println(builder.toString());
    }
}
