package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.executor.SqlProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class TextNode implements ExecutorNode {
    @Override
    public boolean support(XmlNode node) {
        String type = node.getNodeType();
        return "text".equals(type)
                || "cdata".equals(type)
                ;
    }

    @Override
    public void exec(XmlNode node, Map<String, Object> params, Map<String, XmlNode> nodeMap, SqlProcedureExecutor executor) {
        String result = node.getTagAttrMap().get("result");
        String text = node.getTextBody();
        if (result != null && !result.isEmpty()) {
            params.put(result, text);
        }
    }
}
