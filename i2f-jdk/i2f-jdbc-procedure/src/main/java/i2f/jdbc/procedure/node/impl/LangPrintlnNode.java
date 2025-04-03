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
public class LangPrintlnNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.LANG_PRINTLN;

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
        if (tag != null) {
            builder.append("[").append(tag).append("]");
        }
        boolean isFirst = true;
        for (Map.Entry<String, String> entry : node.getTagAttrMap().entrySet()) {
            if (AttrConsts.TAG.equals(entry.getKey())) {
                continue;
            }
            if (!isFirst) {
                builder.append(", ");
            }
            String script = entry.getValue();
            Object val = executor.attrValue(entry.getKey(), FeatureConsts.VISIT, node, context);
            builder.append(entry.getKey()).append(":").append(val);
            isFirst = false;
        }
        System.out.println(builder.toString());
    }
}
