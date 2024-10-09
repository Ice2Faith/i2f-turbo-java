package i2f.xml;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/6/11 17:01
 * @desc
 */
public class XmlUtil {

    public static DocumentBuilderFactory getFactory() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setExpandEntityReferences(false); // 防止 XXE 攻击
        return factory;
    }

    public static Document parseXml(byte[] bytes) throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        return parseXml(bis);
    }

    public static Document parseXml(File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        return parseXml(fis);
    }

    public static Document parseXml(InputStream is) throws Exception {
        DocumentBuilderFactory factory = getFactory();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(is);
        is.close();

        return document;
    }

    public static Node getRootNode(Document document) {
        NodeList list = document.getChildNodes();
        int len = list.getLength();
        for (int i = 0; i < len; i++) {
            Node item = list.item(i);
            if (Node.ELEMENT_NODE == item.getNodeType()) {
                return item;
            }
        }
        return document.getLastChild();
    }

    public static String getNodeContent(Node node) {
        return node.getTextContent();
    }

    public static String getAttribute(Node node, String attributeName) {
        NamedNodeMap attributes = node.getAttributes();
        Node item = attributes.getNamedItem(attributeName);
        if (item == null) {
            return null;
        }
        return item.getTextContent();
    }

    public static Map<String, String> getAttributes(Node node) {
        Map<String, String> ret = new LinkedHashMap<>();
        NamedNodeMap attributes = node.getAttributes();
        if (attributes != null) {
            int length = attributes.getLength();
            for (int i = 0; i < length; i++) {
                Node item = attributes.item(i);
                String nodeName = item.getNodeName();
                String nodeValue = item.getNodeValue();
                ret.put(nodeName, nodeValue);
            }
        }
        return ret;
    }

    public static List<Node> getNodesByTagName(Document document, List<String> tagNameList) {
        List<Node> ret = new ArrayList<>();
        for (String item : tagNameList) {
            NodeList list = document.getElementsByTagName(item);
            int len = list.getLength();
            for (int i = 0; i < len; i++) {
                Node node = list.item(i);
                ret.add(node);
            }
        }
        return ret;
    }

    public static List<Node> getChildNodes(Node node, List<String> tagNames) {
        List<Node> ret = new ArrayList<>();
        NodeList list = node.getChildNodes();
        int len = list.getLength();
        for (int i = 0; i < len; i++) {
            Node item = list.item(i);
            String name = getTagName(item);
            if (tagNames.contains(name)) {
                ret.add(item);
            }
        }
        return ret;
    }

    public static List<Node> getChildNodes(Node node) {
        List<Node> ret = new ArrayList<>();
        NodeList list = node.getChildNodes();
        int len = list.getLength();
        for (int i = 0; i < len; i++) {
            Node item = list.item(i);
            ret.add(item);
        }
        return ret;
    }

    public static String getTagName(Node node) {
        return node.getNodeName();
    }

}
