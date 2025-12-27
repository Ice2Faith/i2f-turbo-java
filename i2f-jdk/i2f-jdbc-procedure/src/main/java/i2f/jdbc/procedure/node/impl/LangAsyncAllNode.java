package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.ParamsConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangAsyncAllNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.LANG_ASYNC_ALL;

    @Override
    public String tag() {
        return TAG_NAME;
    }


    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        boolean await = executor.toBoolean(executor.attrValue(AttrConsts.AWAIT, FeatureConsts.BOOLEAN, node, context));

        List<XmlNode> children = node.getChildren();
        if (children == null || children.isEmpty()) {
            return;
        }
        CountDownLatch latch = new CountDownLatch(children.size());
        for (XmlNode item : children) {
            Map<String, Object> callParams = executor.cloneParams(context);
            for (Map.Entry<String, Object> entry : context.entrySet()) {
                if (!ParamsConsts.KEEP_NAME_SET.contains(entry.getKey())) {
                    callParams.put(entry.getKey(), entry.getValue());
                }
            }
            final XmlNode itemNode = item;
            new Thread(() -> {
                try {
                    if (TagConsts.LANG_ASYNC.equals(node.getTagName())) {
                        Map<String, Object> map = executor.exec(itemNode, callParams, false, false);
                    } else {
                        Map<String, Object> map = executor.execAsProcedure(itemNode, callParams, false, false);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            }).start();
        }
        if (await) {
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new ThrowSignalException(e.getMessage(), e);
            }
        }

    }
}
