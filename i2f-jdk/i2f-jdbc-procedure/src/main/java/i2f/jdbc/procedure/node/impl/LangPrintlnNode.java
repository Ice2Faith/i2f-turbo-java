package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangPrintlnNode implements ExecutorNode {
    @Override
    public boolean support(XmlNode node) {
        if (!"element".equals(node.getNodeType())) {
            return false;
        }
        return "lang-println".equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, Map<String, Object> params, Map<String, XmlNode> nodeMap, JdbcProcedureExecutor executor) {
        StringBuilder builder = new StringBuilder();
        String tag = node.getTagAttrMap().get("tag");
        Object tagObj=executor.applyFeatures(tag,node.getAttrFeatureMap().get("tag"),params,node);
        if(tagObj!=null){
            tag=String.valueOf(tagObj);
        }
        builder.append("[").append(tag == null ? "" : tag).append("]");
        boolean isFirst = true;
        for (Map.Entry<String, String> entry : node.getTagAttrMap().entrySet()) {
            if ("tag".equals(entry.getKey())) {
                continue;
            }
            if (!isFirst) {
                builder.append(", ");
            }
            String script = entry.getValue();
            Object val = executor.visit(script, params);
            val=executor.applyFeatures(val,node.getAttrFeatureMap().get(entry.getKey()),params,node);
            builder.append(entry.getKey()).append(":").append(val);
            isFirst = false;
        }
        System.out.println(builder.toString());
    }
}
