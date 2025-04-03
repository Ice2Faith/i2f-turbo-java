package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.base.NodeTime;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangAsyncNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.LANG_ASYNC;

    @Override
    public boolean support(XmlNode node) {
        if (XmlNode.NodeType.ELEMENT !=node.getNodeType()) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void execInner(XmlNode node, Map<String,Object> context, JdbcProcedureExecutor executor) {
        Boolean await = (Boolean) executor.attrValue(AttrConsts.AWAIT, FeatureConsts.BOOLEAN, node, context);
        Long delay = (Long) executor.attrValue(AttrConsts.DELAY, FeatureConsts.LONG, node, context);
        String timeUnit = node.getTagAttrMap().get(AttrConsts.TIME_UNIT);
        TimeUnit delayUnit = NodeTime.getTimeUnit(timeUnit, TimeUnit.SECONDS);
        CountDownLatch latch = new CountDownLatch(1);
        Thread thread = new Thread(() -> {
            try {
                if (delay != null&& delay>=0) {
                    try {
                        Thread.sleep(delayUnit.toMillis(delay));
                    } catch (Exception e) {
                    }
                }
                executor.execAsProcedure(node, context, false, false);
            } catch (Throwable e) {
                executor.logWarn(() -> e.getMessage(), e);
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        });
        thread.start();
        if (await != null && await) {
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new ThrowSignalException(e.getMessage(), e);
            }
        }
    }
}
