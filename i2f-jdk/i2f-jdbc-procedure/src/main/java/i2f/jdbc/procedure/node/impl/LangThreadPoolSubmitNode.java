package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangThreadPoolSubmitNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.LANG_THREAD_POOL_SUBMIT;

    @Override
    public String tag() {
        return TAG_NAME;
    }

    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        String name = node.getTagAttrMap().get(AttrConsts.POOL);
        if (name == null || name.isEmpty()) {
            warnPoster.accept(TAG_NAME + " missing attribute " + AttrConsts.POOL);
        }
    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        try {
            ExecutorService pool = (ExecutorService) executor.attrValue(AttrConsts.POOL, FeatureConsts.VISIT, node, context);

            Map<String, Object> callParams = executor.cloneParams(context);

            for (Map.Entry<String, String> entry : node.getTagAttrMap().entrySet()) {
                String name = entry.getKey();
                if (AttrConsts.RESULT.equals(name)) {
                    continue;
                }
                if (AttrConsts.POOL.equals(name)) {
                    continue;
                }
                Object val = executor.attrValue(name, FeatureConsts.VISIT, node, context);
                executor.visitSet(callParams, name, val);
            }

            CountDownLatch latch = new CountDownLatch(1);
            pool.submit(() -> {
                try {
                    executor.execAsProcedure(node, callParams, true, true);
                } catch (Throwable e) {
                    executor.logger().logWarn(() -> e.getMessage(), e);
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
            String result = node.getTagAttrMap().get(AttrConsts.RESULT);
            if (result != null) {
                executor.visitSet(context, result, latch);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ThrowSignalException(e.getMessage(), e);
        }
    }


}
