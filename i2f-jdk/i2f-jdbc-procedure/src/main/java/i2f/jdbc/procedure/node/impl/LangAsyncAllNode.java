package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangAsyncAllNode extends AbstractExecutorNode {
    public static final String TAG_NAME = "lang-async-all";

    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void execInner(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        Boolean await = (Boolean) executor.attrValue(AttrConsts.AWAIT, FeatureConsts.BOOLEAN, node, context);

        List<XmlNode> children = node.getChildren();
        CountDownLatch latch = new CountDownLatch(children.size());
        for (XmlNode item : children) {
            new Thread(() -> {
                try {
                    executor.execAsProcedure(item, context, false, false);
                } catch (Throwable e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            }).start();
        }
        if (await != null && await) {
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new ThrowSignalException(e.getMessage(), e);
            }
        }

    }
}
