package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangAsyncNode implements ExecutorNode {
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
        Long delay = (Long) executor.attrValue("delay", "visit", node, params, nodeMap);
        String timeUnit = node.getTagAttrMap().get("time-unit");
        TimeUnit unit = TimeUnit.SECONDS;
        if ("SECONDS".equalsIgnoreCase(timeUnit)) {
            unit = TimeUnit.MILLISECONDS;
        } else if ("MILLISECONDS".equalsIgnoreCase(timeUnit)) {
            unit = TimeUnit.MILLISECONDS;
        } else if ("NANOSECONDS".equalsIgnoreCase(timeUnit)) {
            unit = TimeUnit.NANOSECONDS;
        } else if ("MICROSECONDS".equalsIgnoreCase(timeUnit)) {
            unit = TimeUnit.MICROSECONDS;
        } else if ("MINUTES".equalsIgnoreCase(timeUnit)) {
            unit = TimeUnit.MINUTES;
        } else if ("HOURS".equalsIgnoreCase(timeUnit)) {
            unit = TimeUnit.HOURS;
        } else if ("DAYS".equalsIgnoreCase(timeUnit)) {
            unit = TimeUnit.DAYS;
        }
        TimeUnit delayUnit = unit;
        CountDownLatch latch = new CountDownLatch(1);
        Thread thread = new Thread(() -> {
            try {
                if (delay != null) {
                    try {
                        Thread.sleep(delayUnit.toMillis(delay));
                    } catch (Exception e) {
                    }
                }
                executor.execAsProducer(node, params, nodeMap);
            } catch (Throwable e) {
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
