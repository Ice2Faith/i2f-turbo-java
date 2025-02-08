package i2f.jdbc.procedure.parser;

import i2f.io.stream.StreamUtil;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.xml.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 9:24
 */
public class JdbcProcedureParser {

    public static String removeProcedureDtd(String str) {
        if (str == null) {
            return str;
        }
        return str.replaceAll("<\\!DOCTYPE\\s+procedure\\s+[^>]+>", " ");
    }

    public static XmlNode parse(String xml) throws Exception {
        if (xml == null) {
            return null;
        }
        String str = removeProcedureDtd(xml);
        ByteArrayInputStream bis = new ByteArrayInputStream(str.getBytes("UTF-8"));
        Document document = XmlUtil.parseXml(bis);
        bis.close();
        return parse(document);
    }

    public static XmlNode parse(InputStream is) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        StreamUtil.streamCopy(is, bos);
        String str = new String(bos.toByteArray(), "UTF-8");
        str = removeProcedureDtd(str);
        ByteArrayInputStream bis = new ByteArrayInputStream(str.getBytes("UTF-8"));
        Document document = XmlUtil.parseXml(bis);
        bis.close();
        return parse(document);
    }

    public static XmlNode parse(Document document) throws Exception {
        Node rootNode = XmlUtil.getRootNode(document);
        return parseNode(rootNode);
    }

    public static void resolveEmbedIdNode(XmlNode node, Map<String, XmlNode> nodeMap) {
        if (node == null || nodeMap == null) {
            return;
        }
        String id = node.getTagAttrMap().get(AttrConsts.ID);
        if (id != null) {
            nodeMap.put(id, node);
        }
        List<XmlNode> children = node.getChildren();
        if (children == null) {
            return;
        }
        for (XmlNode item : children) {
            resolveEmbedIdNode(item, nodeMap);
        }
    }

    public static XmlNode parseNode(Node node) throws Exception {
        if (node == null) {
            return null;
        }
        short nodeType = node.getNodeType();
        if (nodeType == Node.TEXT_NODE) {
            XmlNode ret = new XmlNode();
            ret.setNodeType(XmlNode.NODE_TEXT);
            ret.setTagName(null);
            ret.setTagAttrMap(new LinkedHashMap<>());
            ret.setTagBody(XmlUtil.getNodeContent(node));
            ret.setTextBody(XmlUtil.getNodeContent(node));
            ret.setChildren(null);
        } else if (nodeType == Node.CDATA_SECTION_NODE) {
            XmlNode ret = new XmlNode();
            ret.setNodeType(XmlNode.NODE_CDATA);
            ret.setTagName(null);
            ret.setTagAttrMap(new LinkedHashMap<>());
            ret.setTagBody(XmlUtil.getNodeContent(node));
            ret.setTextBody(XmlUtil.getNodeContent(node));
            ret.setChildren(null);
        } else if (nodeType == Node.ELEMENT_NODE) {
            XmlNode ret = new XmlNode();
            ret.setNodeType(XmlNode.NODE_ELEMENT);
            Map<String, String> tagAttrMap = new LinkedHashMap<>();
            Map<String, List<String>> attrFeatureMap = new LinkedHashMap<>();
            Map<String, String> rawAttrMap = XmlUtil.getAttributes(node);
            for (Map.Entry<String, String> entry : rawAttrMap.entrySet()) {
                String key = entry.getKey();
                String[] arr = key.split("\\.");
                tagAttrMap.put(arr[0], entry.getValue());
                for (int i = 1; i < arr.length; i++) {
                    if (!attrFeatureMap.containsKey(arr[0])) {
                        attrFeatureMap.put(arr[0], new ArrayList<>());
                    }
                    attrFeatureMap.get(arr[0]).add(arr[i]);
                }
            }
            ret.setTagName(XmlUtil.getTagName(node));
            ret.setTagAttrMap(tagAttrMap);
            ret.setAttrFeatureMap(attrFeatureMap);
            ret.setTagBody(XmlUtil.extraInnerXml(node));
            ret.setTextBody(XmlUtil.getNodeContent(node));
            List<XmlNode> children = new ArrayList<>();
            NodeList childNodes = node.getChildNodes();
            if (childNodes != null) {
                int len = childNodes.getLength();
                for (int i = 0; i < len; i++) {
                    Node item = childNodes.item(i);
                    if (item != null) {
                        XmlNode nextNode = parseNode(item);
                        if (nextNode != null) {
                            children.add(nextNode);
                        }
                    }
                }

            }
            ret.setChildren(children);
            return ret;
        }
        return null;
    }
}
