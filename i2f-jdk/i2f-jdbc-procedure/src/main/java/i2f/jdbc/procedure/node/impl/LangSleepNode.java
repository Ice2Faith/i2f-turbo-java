package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.base.NodeTime;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangSleepNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.LANG_SLEEP;

    @Override
    public boolean support(XmlNode node) {
        if (XmlNode.Type.NODE_ELEMENT!=node.getNodeType()) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        String timeout = node.getTagAttrMap().get(AttrConsts.TIMEOUT);
        if(timeout==null || timeout.isEmpty()){
            warnPoster.accept(TAG_NAME+" missing attribute "+AttrConsts.TIMEOUT);
        }
    }

    @Override
    public void execInner(XmlNode node, Map<String,Object> context, JdbcProcedureExecutor executor) {
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
