package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.concurrent.CountDownLatch;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangLatchNode extends AbstractExecutorNode {
    public static final String TAG_NAME = "lang-latch";

    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void execInner(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        int count = (int) executor.attrValue(AttrConsts.COUNT, FeatureConsts.EVAL, node, context);
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        if (result != null && !result.isEmpty()) {
            CountDownLatch latch = new CountDownLatch(count);
            executor.setParamsObject(context.getParams(), result, latch);
        }
    }
}
