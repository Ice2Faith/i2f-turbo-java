package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangAsyncAllNode implements ExecutorNode {
    @Override
    public boolean support(XmlNode node) {
        if (!"element".equals(node.getNodeType())) {
            return false;
        }
        return "lang-async".equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, Map<String, Object> params, Map<String, XmlNode> nodeMap, JdbcProcedureExecutor executor) {
        Boolean await = (Boolean) executor.attrValue("await", "visit", node, params, nodeMap);

        List<XmlNode> children = node.getChildren();
        CountDownLatch latch = new CountDownLatch(children.size());
        for (XmlNode item : children) {
            new Thread(() -> {
                try {
                    executor.execAsProducer(item, params, nodeMap);
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
