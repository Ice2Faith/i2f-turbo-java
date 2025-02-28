package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.base.NodeTime;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangLatchAwaitNode extends AbstractExecutorNode {
    public static final String TAG_NAME = "lang-latch-await";

    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void execInner(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        long timeout = (long) executor.attrValue(AttrConsts.TIMEOUT, FeatureConsts.LONG, node, context);
        String timeUnit = node.getTagAttrMap().get(AttrConsts.TIME_UNIT);
        CountDownLatch latch = (CountDownLatch) executor.attrValue(AttrConsts.NAME, FeatureConsts.VISIT, node, context);
        try {
            if (timeout >= 0) {
                TimeUnit unit = NodeTime.getTimeUnit(timeUnit, TimeUnit.SECONDS);
                latch.await(timeout, unit);
            } else {
                latch.await();
            }
        } catch (InterruptedException e) {
            throw new ThrowSignalException(e.getMessage(), e);
        }
    }
}
