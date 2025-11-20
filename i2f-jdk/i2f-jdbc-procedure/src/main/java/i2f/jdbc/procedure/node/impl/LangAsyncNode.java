package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.ParamsConsts;
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
    public String tag() {
        return TAG_NAME;
    }


    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        boolean await = executor.toBoolean(executor.attrValue(AttrConsts.AWAIT, FeatureConsts.BOOLEAN, node, context));
        Long delay = (Long) executor.attrValue(AttrConsts.DELAY, FeatureConsts.LONG, node, context);
        String timeUnit = node.getTagAttrMap().get(AttrConsts.TIME_UNIT);
        TimeUnit delayUnit = NodeTime.getTimeUnit(timeUnit, TimeUnit.SECONDS);

        Map<String,Object> callParams= executor.cloneParams(context);
        for (Map.Entry<String, Object> entry : context.entrySet()) {
            if (!ParamsConsts.KEEP_NAME_SET.contains(entry.getKey())) {
                callParams.put(entry.getKey(),entry.getValue());
            }
        }
        CountDownLatch latch = new CountDownLatch(1);
        Thread thread = new Thread(() -> {
            try {
                if (delay != null && delay >= 0) {
                    try {
                        Thread.sleep(delayUnit.toMillis(delay));
                    } catch (Exception e) {
                    }
                }
                executor.execAsProcedure(node, callParams, false, false);
            } catch (Throwable e) {
                executor.logger().logWarn(() -> e.getMessage(), e);
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        });
        thread.start();
        if (await) {
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new ThrowSignalException(e.getMessage(), e);
            }
        }
    }
}
