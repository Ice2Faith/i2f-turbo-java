package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class ScriptIncludeNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.SCRIPT_INCLUDE;

    @Override
    public boolean support(XmlNode node) {
        if (XmlNode.NodeType.ELEMENT != node.getNodeType()) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        String refid = node.getTagAttrMap().get(AttrConsts.REFID);
        if (refid == null || refid.isEmpty()) {
            warnPoster.accept(TAG_NAME + " missing attribute " + AttrConsts.REFID);
        }
    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        String refid = (String) executor.attrValue(AttrConsts.REFID, FeatureConsts.STRING, node, context);
        ProcedureMeta meta = executor.getMeta(refid);
        if (meta == null) {
            return;
        }
        if (meta.getType() != ProcedureMeta.Type.XML) {
            return;
        }
        XmlNode nextNode = (XmlNode) meta.getTarget();
        // 备份堆栈
        Map<String, Object> bakParams = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : node.getTagAttrMap().entrySet()) {
            String name = entry.getKey();
            if (AttrConsts.REFID.equals(name)) {
                continue;
            }
            String script = entry.getValue();
            // 备份堆栈
            bakParams.put(name, executor.visit(script, context));
            Object val = executor.attrValue(name, FeatureConsts.VISIT, node, context);
            executor.visitSet(context, name, val);
        }

        executor.execAsProcedure(nextNode, context, false, false);

        // 恢复堆栈
        for (Map.Entry<String, Object> entry : bakParams.entrySet()) {
            executor.visitSet(context, entry.getKey(), entry.getValue());
        }
    }

}
