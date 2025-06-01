package i2f.jdbc.procedure.node.impl;


import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.node.event.XmlCustomEvent;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class EventPublishNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.EVENT_PUBLISH;

    @Override
    public boolean support(XmlNode node) {
        if (XmlNode.NodeType.ELEMENT != node.getNodeType()) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        String refid = node.getTagAttrMap().get(AttrConsts.EVENT_TYPE);
        if (refid == null || refid.isEmpty()) {
            warnPoster.accept(TAG_NAME + " missing attribute " + AttrConsts.EVENT_TYPE);
        }
    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        String evenType = (String) executor.attrValue(AttrConsts.EVENT_TYPE, FeatureConsts.STRING, node, context);


        Map<String, Object> callParams = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : node.getTagAttrMap().entrySet()) {
            String name = entry.getKey();
            if (AttrConsts.EVENT_TYPE.equals(name)) {
                continue;
            }
            String script = entry.getValue();
            Object val = executor.attrValue(name, FeatureConsts.VISIT, node, context);
            callParams.put(name, val);
        }


        XmlCustomEvent event = new XmlCustomEvent();
        event.setExecutor(executor);
        event.setContext(context);
        event.setEventType(evenType);
        event.setNode(node);
        event.setParams(callParams);

        executor.publishEvent(event);
    }

}
