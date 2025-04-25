package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.ParamsConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.ReturnSignalException;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangReturnNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.LANG_RETURN;

    @Override
    public boolean support(XmlNode node) {
        if (XmlNode.NodeType.ELEMENT != node.getNodeType()) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        String value = node.getTagAttrMap().get(AttrConsts.VALUE);
        if (value != null) {
            Object val = executor.attrValue(AttrConsts.VALUE, FeatureConsts.VISIT, node, context);
            executor.visitSet(context, ParamsConsts.RETURN, val);
        }
        throw new ReturnSignalException();
    }


}
