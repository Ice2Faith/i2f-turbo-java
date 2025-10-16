package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.Map;


/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangRenderNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.LANG_RENDER;

    @Override
    public String tag() {
        return TAG_NAME;
    }


    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        String script = node.getTextBody();
        String rs = executor.render(script, context);
        if (executor.isDebug()) {
            String lang = node.getTagAttrMap().get("_lang");
            if ("sql".equalsIgnoreCase(lang)) {
                rs = rs + getTrackingComment(node);
            }
        }
        String val = rs;
        if (executor.isDebug()) {
            executor.logger().logDebug("at " + getNodeLocation(node) + " render string: " + val);
        }
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        if (result != null) {
            Object res = executor.resultValue(val, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
            executor.visitSet(context, result, res);
        }
    }

}
