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
public class LangSynchronizedNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.LANG_SYNCHRONIZED;

    @Override
    public boolean support(XmlNode node) {
        if (XmlNode.Type.NODE_ELEMENT!=node.getNodeType()) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void execInner(XmlNode node, Map<String,Object> context, JdbcProcedureExecutor executor) {
        Object target = executor.attrValue(AttrConsts.TARGET, FeatureConsts.VISIT, node, context);
        if (target == null) {
            target = context;
        }
        synchronized (target) {
            executor.execAsProcedure(node, context, false, false);
        }
    }
}
