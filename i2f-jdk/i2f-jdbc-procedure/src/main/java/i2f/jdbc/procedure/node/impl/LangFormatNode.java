package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangFormatNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.LANG_FORMAT;

    @Override
    public boolean support(XmlNode node) {
        if (XmlNode.Type.NODE_ELEMENT!=node.getNodeType()) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        String value = node.getTagAttrMap().get(AttrConsts.VALUE);
        if(value==null || value.isEmpty()){
            warnPoster.accept(TAG_NAME+" missing attribute "+AttrConsts.VALUE);
        }
        String pattern = node.getTagAttrMap().get(AttrConsts.PATTERN);
        if(pattern==null || pattern.isEmpty()){
            warnPoster.accept(TAG_NAME+" missing attribute "+AttrConsts.PATTERN);
        }
    }

    @Override
    public void execInner(XmlNode node, Map<String,Object> context, JdbcProcedureExecutor executor) {
        Object value = executor.attrValue(AttrConsts.VALUE, FeatureConsts.VISIT, node, context);
        String pattern = (String) executor.attrValue(AttrConsts.PATTERN, FeatureConsts.STRING, node, context);
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        value = String.format(pattern, value);
        if (result != null && !result.isEmpty()) {
            value = executor.resultValue(value, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
            executor.visitSet(context, result, value);
        }
    }


}
