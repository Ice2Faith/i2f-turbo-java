package i2f.xml;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

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
        DocumentBuilderFactory factory = getFactory();
        DocumentBuilder builder = factory.newDocumentBuilder();

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Document document = builder.parse(bis);
        bis.close();

        return document;
    }

    public static Node getRootNode(Document document) {
        return document.getFirstChild();
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
