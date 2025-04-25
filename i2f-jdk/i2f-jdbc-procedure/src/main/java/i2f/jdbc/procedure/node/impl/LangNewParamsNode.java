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
public class LangNewParamsNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.LANG_NEW_PARAMS;

    @Override
    public boolean support(XmlNode node) {
        if (XmlNode.NodeType.ELEMENT != node.getNodeType()) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        Map<String, Object> newParams = executor.newParams(context);
        for (Map.Entry<String, String> entry : node.getTagAttrMap().entrySet()) {
            String key = entry.getKey();
            if (AttrConsts.RESULT.equals(key)) {
                continue;
            }
            Object value = executor.attrValue(key, FeatureConsts.VISIT, node, context);
            executor.visitSet(newParams, key, value);
        }
        if (result != null) {
            Object res = executor.resultValue(newParams, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
            executor.visitSet(context, result, res);
        }
    }
}
