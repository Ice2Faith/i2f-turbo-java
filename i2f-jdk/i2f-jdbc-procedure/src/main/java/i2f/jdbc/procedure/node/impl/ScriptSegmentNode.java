package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.executor.SqlProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.Map;


/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class ScriptSegmentNode implements ExecutorNode {
    @Override
    public boolean support(XmlNode node) {
        if (!"element".equals(node.getNodeType())) {
            return false;
        }
        return "script-segment".equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, Map<String, Object> params, Map<String, XmlNode> nodeMap, SqlProcedureExecutor executor) {
        String id = node.getTagAttrMap().get("id");
        if (id != null && !id.isEmpty()) {
            nodeMap.put(id, node);
        }
    }

}
