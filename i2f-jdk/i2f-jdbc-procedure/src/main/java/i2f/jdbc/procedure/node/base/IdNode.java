package i2f.jdbc.procedure.node.base;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/24 9:19
 */
public class IdNode {
    public static Map<String, XmlNode> getAllIdNode(XmlNode node) {
        Map<String, XmlNode> nodeMap = new HashMap<>();
        getAllIdNode(node, nodeMap);
        return nodeMap;
    }

    public static void getAllIdNode(XmlNode node, Map<String, XmlNode> nodeMap) {
        if (node == null) {
            return;
        }
        String id = node.getTagAttrMap().get(AttrConsts.ID);
        if (id != null && !id.isEmpty()) {
            nodeMap.put(id, node);
        }
        List<XmlNode> children = node.getChildren();
        if (children == null) {
            return;
        }
        for (XmlNode item : children) {
            getAllIdNode(item, nodeMap);
        }
    }
}
