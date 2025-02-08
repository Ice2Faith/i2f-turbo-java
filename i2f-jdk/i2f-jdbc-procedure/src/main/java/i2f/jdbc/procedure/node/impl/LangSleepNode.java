package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.node.base.NodeTime;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangSleepNode implements ExecutorNode {
    public static final String TAG_NAME = "lang-sleep";

    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        long timeout = (long) executor.attrValue(AttrConsts.TIMEOUT, FeatureConsts.LONG, node, context);
        String timeUnit = node.getTagAttrMap().get(AttrConsts.TIME_UNIT);
        try {
            if (timeout >= 0) {
                TimeUnit unit = NodeTime.getTimeUnit(timeUnit, TimeUnit.SECONDS);
                Thread.sleep(unit.toMillis(timeout));
            } else {
                Thread.sleep(0);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
