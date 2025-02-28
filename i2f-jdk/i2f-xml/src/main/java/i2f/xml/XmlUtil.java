package i2f.xml;

import i2f.match.regex.RegexUtil;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
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
    public static final String ATTR_FILE = "__file";
    public static final String ATTR_LINE_NUMBER = "__line";

    public static DocumentBuilderFactory getFactory() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false); // 关闭验证
        try {
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        } catch (ParserConfigurationException e) {

        }
        factory.setExpandEntityReferences(false); // 防止 XXE 攻击
        return factory;
    }

    public static Document parseXml(byte[] bytes) throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        return parseXml(bis);
    }

    public static Document parseXml(String str) throws Exception {
        return parseXml(str, "UTF-8");
    }

    public static Document parseXml(String str, String charset) throws Exception {
        byte[] bytes = str.getBytes(charset);
        return parseXml(bytes);
    }

    public static Document parseXml(URL url) throws Exception {
        String name = null;
        if (name == null) {
            try {
                String path = url.getPath();
                File file = new File(path);
                name = file.getName();
            } catch (Exception e) {

            }
        }
        if (name == null) {
            try {
                String path = url.getFile();
                File file = new File(path);
                name = file.getName();
            } catch (Exception e) {

            }
        }
        return parseXml(name, url.openStream());
    }

    public static Document parseXml(File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        return parseXml(file.getName(), fis);
    }

    public static Document parseXml(InputStream is) throws Exception {
        return parseXml(null, is);
    }

    public static Document parseXml(String fileName, InputStream is) throws Exception {
        DocumentBuilderFactory factory = getFactory();
        DocumentBuilder builder = factory.newDocumentBuilder();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buff = new byte[4096];
        int len = 0;
        while ((len = is.read(buff)) > 0) {
            bos.write(buff, 0, len);
        }
        is.close();

        try {


            if (fileName == null) {
                fileName = "";
            }
            fileName = URLEncoder.encode(fileName, "UTF-8");

            String xmlFileName = fileName;
            StringBuilder xml = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bos.toByteArray())));
            String line = null;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                String locationAttributes =
                        " " + ATTR_LINE_NUMBER + "=\"" + lineNumber + "\""
                                + " " + ATTR_FILE + "=\"" + xmlFileName + "\""
                                + " ";
                String str = RegexUtil.regexFindAndReplace(line, "<[a-zA-Z0-9\\-\\_:]+(\\s+|\\s*>)", (group) -> {
                    if (group.endsWith(">")) {
                        return group.substring(0, group.length() - 1) + locationAttributes + ">";
                    } else {
                        return group + locationAttributes;
                    }
                });
                xml.append(str).append("\n");
                lineNumber++;
            }

            is = new ByteArrayInputStream(xml.toString().getBytes());

            Document document = builder.parse(is);
            is.close();
            return document;
        } catch (Exception e) {
            is = new ByteArrayInputStream(bos.toByteArray());
            Document document = builder.parse(is);
            is.close();
            return document;
        }
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

    public static void removeNodeLocationAttributes(Node node) {
        if (node == null) {
            return;
        }
        NamedNodeMap attributes = node.getAttributes();
        if(attributes!=null){
            Node fileNode = attributes.getNamedItem(ATTR_FILE);
            if(fileNode!=null){
                attributes.removeNamedItem(ATTR_FILE);
            }
            Node lineNode = attributes.getNamedItem(ATTR_LINE_NUMBER);
            if(lineNode!=null) {
                attributes.removeNamedItem(ATTR_LINE_NUMBER);
            }
        }
        NodeList childNodes = node.getChildNodes();
        if (childNodes == null) {
            return;
        }
        int len = childNodes.getLength();
        for (int i = 0; i < len; i++) {
            Node item = childNodes.item(i);
            removeNodeLocationAttributes(item);
        }
    }

    public static String extraInnerXml(Node node) throws ParserConfigurationException, TransformerException {
        return extraInnerXml(node, false);
    }

    public static String extraInnerXml(Node node, boolean removeLocationAttribute) throws ParserConfigurationException, TransformerException {
        // 创建一个新的Document来构建新的XML结构
        DocumentBuilderFactory factory = getFactory();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document newDoc = builder.newDocument();

        // 导入目标节点到新文档中，不保留其属性
        Node importedNode = newDoc.importNode(node, true);
        newDoc.appendChild(importedNode);

        // 移除所有属性
        NamedNodeMap attributes = importedNode.getAttributes();
        while (attributes != null && attributes.getLength() > 0) {
            attributes.removeNamedItem(attributes.item(0).getNodeName());
        }

        if (removeLocationAttribute) {
            removeNodeLocationAttributes(newDoc);
        }

        // 将新文档转换为字符串，只保留文本内容
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
        transformer.setOutputProperty(OutputKeys.INDENT, "no"); // 不缩进输出
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(newDoc), new StreamResult(writer));

        // 去除首尾空白字符
        String rootXml = writer.toString().trim();

        // 移除收尾的根节点标签
        String nodeName = getTagName(node);
        String prefix = "<" + nodeName + ">";
        String suffix = "</" + nodeName + ">";

        if (rootXml.startsWith(prefix)) {
            rootXml = rootXml.substring(prefix.length());
        }
        if (rootXml.endsWith(suffix)) {
            rootXml = rootXml.substring(0, rootXml.length() - suffix.length());
        }
        return rootXml;
    }

}
