package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangPrintfNode implements ExecutorNode {
    @Override
    public boolean support(XmlNode node) {
        if (!"element".equals(node.getNodeType())) {
            return false;
        }
        return "lang-printf".equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        StringBuilder builder = new StringBuilder();
        String tag = node.getTagAttrMap().get("tag");
        String value = node.getTagAttrMap().get("value");
        if (value != null) {
            Object obj = executor.attrValue("value", "visit", node, context);
            if (obj != null) {
                value = String.valueOf(obj);
            }
        } else {
            value = node.getTagBody();
        }
        builder.append("[").append(tag == null ? "" : tag).append("]");
        String str = executor.render(value, context.getParams());
        builder.append(str);
        System.out.println(builder.toString());
    }
}
