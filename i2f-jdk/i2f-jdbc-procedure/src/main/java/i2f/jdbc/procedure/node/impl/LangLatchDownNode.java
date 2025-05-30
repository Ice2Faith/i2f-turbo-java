package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangLatchDownNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.LANG_LATCH_DOWN;

    @Override
    public boolean support(XmlNode node) {
        if (XmlNode.NodeType.ELEMENT != node.getNodeType()) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        String name = node.getTagAttrMap().get(AttrConsts.NAME);
        if (name == null || name.isEmpty()) {
            warnPoster.accept(TAG_NAME + " missing attribute " + AttrConsts.NAME);
        }
    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        CountDownLatch latch = (CountDownLatch) executor.attrValue(AttrConsts.NAME, FeatureConsts.VISIT, node, context);
        latch.countDown();
    }
}
