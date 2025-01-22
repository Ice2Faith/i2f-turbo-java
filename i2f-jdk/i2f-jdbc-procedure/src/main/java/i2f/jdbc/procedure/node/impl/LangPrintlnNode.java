package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.context.ExecuteContext;
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
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        StringBuilder builder = new StringBuilder();
        String tag = node.getTagAttrMap().get("tag");
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
            Object val = executor.attrValue(entry.getKey(), "visit", node, context);
            builder.append(entry.getKey()).append(":").append(val);
            isFirst = false;
        }
        System.out.println(builder.toString());
    }
}
