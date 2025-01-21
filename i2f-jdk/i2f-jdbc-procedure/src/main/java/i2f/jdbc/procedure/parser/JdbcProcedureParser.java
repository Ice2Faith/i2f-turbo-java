package i2f.jdbc.procedure.parser;

import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.xml.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

    public static XmlNode parse(InputStream is) throws Exception {
        Document document = XmlUtil.parseXml(is);
        is.close();
        return parse(document);
    }

    public static XmlNode parse(Document document) throws Exception {
        Node rootNode = XmlUtil.getRootNode(document);
        return parseNode(rootNode);
    }

    public static XmlNode parseNode(Node node) throws Exception {
        if (node == null) {
            return null;
        }
        short nodeType = node.getNodeType();
        if (nodeType == Node.TEXT_NODE) {
            XmlNode ret = new XmlNode();
            ret.setNodeType("text");
            ret.setTagName(null);
            ret.setTagAttrMap(new LinkedHashMap<>());
            ret.setTagBody(XmlUtil.getNodeContent(node));
            ret.setTextBody(XmlUtil.getNodeContent(node));
            ret.setChildren(null);
        } else if (nodeType == Node.CDATA_SECTION_NODE) {
            XmlNode ret = new XmlNode();
            ret.setNodeType("cdata");
            ret.setTagName(null);
            ret.setTagAttrMap(new LinkedHashMap<>());
            ret.setTagBody(XmlUtil.getNodeContent(node));
            ret.setTextBody(XmlUtil.getNodeContent(node));
            ret.setChildren(null);
        } else if (nodeType == Node.ELEMENT_NODE) {
            XmlNode ret = new XmlNode();
            ret.setNodeType("element");
            Map<String, String> tagAttrMap = XmlUtil.getAttributes(node);
            ret.setTagName(XmlUtil.getTagName(node));
            ret.setTagAttrMap(tagAttrMap);
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
