package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.concurrent.CountDownLatch;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangLatchDownNode implements ExecutorNode {
    @Override
    public boolean support(XmlNode node) {
        if (!"element".equals(node.getNodeType())) {
            return false;
        }
        return "lang-latch-down".equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        CountDownLatch latch = (CountDownLatch) executor.attrValue("name", "visit", node, context);
        latch.countDown();
    }
}
