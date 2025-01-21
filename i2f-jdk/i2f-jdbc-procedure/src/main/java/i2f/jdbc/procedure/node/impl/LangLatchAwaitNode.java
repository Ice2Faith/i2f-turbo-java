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
public class LangLatchAwaitNode implements ExecutorNode {
    @Override
    public boolean support(XmlNode node) {
        if (!"element".equals(node.getNodeType())) {
            return false;
        }
        return "lang-latch-down".equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, Map<String, Object> params, Map<String, XmlNode> nodeMap, JdbcProcedureExecutor executor) {
        long timeout = (long) executor.attrValue("timeout", "visit", node, params, nodeMap);
        String timeUnit = node.getTagAttrMap().get("time-unit");
        CountDownLatch latch = (CountDownLatch) executor.attrValue("name", "visit", node, params, nodeMap);
        try {
            if (timeout >= 0) {
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
                latch.await(timeout, unit);
            } else {
                latch.await();
            }
        } catch (InterruptedException e) {
            throw new ThrowSignalException(e.getMessage(), e);
        }
    }
}
