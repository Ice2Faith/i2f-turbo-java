package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class ScriptIncludeNode extends AbstractExecutorNode {
    public static final String TAG_NAME = "script-include";

    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void execInner(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        String refid = node.getTagAttrMap().get(AttrConsts.REFID);
        XmlNode nextNode = context.getNodeMap().get(refid);
        if (nextNode == null) {
            return;
        }
        // 备份堆栈
        Map<String, Object> bakParams = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : node.getTagAttrMap().entrySet()) {
            String name = entry.getKey();
            if (AttrConsts.REFID.equals(name)) {
                continue;
            }
            String script = entry.getValue();
            // 备份堆栈
            bakParams.put(name, context.getParams().get(script));
            Object val = executor.attrValue(name, FeatureConsts.VISIT, node, context);
            context.getParams().put(name, val);
        }

        executor.execAsProcedure(nextNode, context, false, false);

        // 恢复堆栈
        context.getParams().putAll(bakParams);
    }

}
