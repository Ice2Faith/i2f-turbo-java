package i2f.xml;

import i2f.match.regex.RegexUtil;
import i2f.xml.data.Xml;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.function.Consumer;

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
        factory.setNamespaceAware(false); // 禁用命名空间检查‌
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
        String name = getLocationNameByUrl(url);
        return parseXml(name, url.openStream());
    }

    public static String getLocationNameByUrl(URL url) {
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
        return name;
    }

    public static Document parseXml(File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        return parseXml(file.getName(), fis);
    }

    public static Document parseXml(InputStream is) throws Exception {
        return parseXml(null, is);
    }

    public static Document parseXml(String fileName, InputStream is) throws Exception {
        DocumentBuilder builder = getDocumentBuilder();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buff = new byte[4096];
        int len = 0;
        while ((len = is.read(buff)) > 0) {
            bos.write(buff, 0, len);
        }
        is.close();

        try {
            is = wrapLocationXmlStream(fileName, bos.toByteArray());

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

    public static DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory factory = getFactory();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder;
    }

    public static InputStream wrapLocationXmlStream(String fileName, byte[] data) throws IOException {
        if (fileName == null) {
            fileName = "";
        }
        fileName = URLEncoder.encode(fileName, "UTF-8");

        String xmlFileName = fileName;
        StringBuilder xml = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)));
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

        return new ByteArrayInputStream(xml.toString().getBytes());
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

    public static List<Node> getNodesByTagName(Document document, Collection<String> tagNameList) {
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

    public static List<Node> getChildNodes(Node node, Collection<String> tagNames) {
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
        if (attributes != null) {
            Node fileNode = attributes.getNamedItem(ATTR_FILE);
            if (fileNode != null) {
                attributes.removeNamedItem(ATTR_FILE);
            }
            Node lineNode = attributes.getNamedItem(ATTR_LINE_NUMBER);
            if (lineNode != null) {
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

    public static String extraInnerXml(Xml node) throws ParserConfigurationException, TransformerException {
        return extraInnerXml(node, false);
    }

    public static String extraInnerXml(Xml node, boolean removeLocationAttribute) throws ParserConfigurationException, TransformerException {
        Node dom = toDomNode(node);
        return extraInnerXml(dom, removeLocationAttribute);
    }

    public static String extraInnerXml(Node node) throws ParserConfigurationException, TransformerException {
        return extraInnerXml(node, false);
    }


    public static String extraInnerXml(Node node, boolean removeLocationAttribute) throws ParserConfigurationException, TransformerException {
        short nodeType = node.getNodeType();
        // 如果内部是文本，直接返回
        if (nodeType == Node.COMMENT_NODE
                || nodeType == Node.TEXT_NODE
                || nodeType == Node.CDATA_SECTION_NODE) {
            String text = getNodeContent(node);
            return text;
        }
        // 创建一个新的Document来构建新的XML结构
        DocumentBuilder builder = getDocumentBuilder();
        Document newDoc = builder.newDocument();

        // 导入目标节点到新文档中，不保留其属性
        if (nodeType == Node.DOCUMENT_NODE) {
            node = getRootNode((Document) node);
        }


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

        String rootXml = writer.toString();
        // 去除首尾空白字符
        rootXml = rootXml.trim();

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

    public static Xml getRootNode(Xml node) {
        if (node == null) {
            return null;
        }
        if (node.getType() == Xml.Type.DOCUMENT) {
            List<Xml> children = node.getChildren();
            if (children != null) {
                Xml lastNode = null;
                for (Xml item : children) {
                    lastNode = item;
                    if (item.getType() == Xml.Type.ELEMENT) {
                        return item;
                    }
                }
                if (lastNode != null) {
                    return lastNode;
                }
            }
            return null;
        }
        return node;
    }

    public static Xml parseXmlDom(URL url) throws Exception {
        String name = getLocationNameByUrl(url);
        return parseXmlDom(name, url.openStream());
    }

    public static Xml parseXmlDom(File file) throws Exception {
        return parseXmlDom(file.getName(), new FileInputStream(file));
    }

    public static Xml parseXmlDom(String fileName, InputStream is) throws Exception {
        Document document = parseXml(fileName, is);
        is.close();
        return parseXmlDom(fileName, document);
    }

    public static Xml parseXmlDom(String fileName, Document document) throws Exception {
        return parseNodeNext(fileName, document);
    }

    public static Xml parseNodeNext(String fileName, Node node) throws Exception {
        if (node == null) {
            return null;
        }
        short nodeType = node.getNodeType();
        if (nodeType == Node.DOCUMENT_NODE) {
            Xml ret = new Xml();
            ret.setType(Xml.Type.DOCUMENT);
            ret.setName(null);
            ret.setValue(getNodeContent(node));
            ret.setAttributes(null);
            ret.setChildren(new ArrayList<>());

            ret.setLocationName(fileName);
            ret.setLocationLineNumber(-1);
            ret.setLocationColumnNumber(-1);

            NodeList childNodes = node.getChildNodes();
            if (childNodes != null) {
                int len = childNodes.getLength();
                for (int i = 0; i < len; i++) {
                    Node item = childNodes.item(i);
                    if (item != null) {
                        Xml nextNode = parseNodeNext(fileName, item);
                        if (nextNode != null) {
                            ret.getChildren().add(nextNode);
                        }
                    }
                }

            }

            ret.setDom(node);
            return ret;
        } else if (nodeType == Node.TEXT_NODE) {
            Xml ret = new Xml();
            ret.setType(Xml.Type.TEXT);
            ret.setName(null);
            ret.setValue(getNodeContent(node));
            ret.setAttributes(null);
            ret.setChildren(null);

            ret.setLocationName(fileName);
            ret.setLocationLineNumber(-1);
            ret.setLocationColumnNumber(-1);

            ret.setDom(node);
            return ret;
        } else if (nodeType == Node.CDATA_SECTION_NODE) {
            Xml ret = new Xml();
            ret.setType(Xml.Type.CDATA);
            ret.setName(null);
            ret.setValue(getNodeContent(node));
            ret.setAttributes(null);
            ret.setChildren(null);

            ret.setLocationName(fileName);
            ret.setLocationLineNumber(-1);
            ret.setLocationColumnNumber(-1);

            ret.setDom(node);
            return ret;
        } else if (nodeType == Node.COMMENT_NODE) {
            Xml ret = new Xml();
            ret.setType(Xml.Type.COMMENT);
            ret.setName(null);
            ret.setValue(getNodeContent(node));
            ret.setAttributes(null);
            ret.setChildren(null);

            ret.setLocationName(fileName);
            ret.setLocationLineNumber(-1);
            ret.setLocationColumnNumber(-1);

            ret.setDom(node);
            return ret;
        } else if (nodeType == Node.ATTRIBUTE_NODE) {
            Xml ret = new Xml();
            ret.setType(Xml.Type.ATTRIBUTE);
            ret.setName(getTagName(node));
            ret.setValue(getNodeContent(node));
            ret.setAttributes(null);
            ret.setChildren(null);

            ret.setLocationName(fileName);
            ret.setLocationLineNumber(-1);
            ret.setLocationColumnNumber(-1);

            ret.setDom(node);
            return ret;
        } else if (nodeType == Node.ELEMENT_NODE) {
            Xml ret = new Xml();
            ret.setType(Xml.Type.ELEMENT);
            ret.setName(getTagName(node));
            ret.setValue(getNodeContent(node));
            ret.setAttributes(new ArrayList<>());
            ret.setChildren(new ArrayList<>());

            ret.setLocationName(fileName);
            ret.setLocationLineNumber(-1);
            ret.setLocationColumnNumber(-1);

            Map<String, String> rawAttrMap = getAttributes(node);
            for (Map.Entry<String, String> entry : rawAttrMap.entrySet()) {
                String key = entry.getKey();
                try {
                    if (ATTR_FILE.equals(key)) {
                        ret.setLocationName(URLDecoder.decode(entry.getValue(), "UTF-8"));
                        continue;
                    }
                    if (ATTR_LINE_NUMBER.equals(key)) {
                        String value = entry.getValue();
                        ret.setLocationLineNumber(Integer.parseInt(value));
                        continue;
                    }

                    Xml attr = new Xml();
                    attr.setType(Xml.Type.ATTRIBUTE);
                    attr.setName(key);
                    attr.setValue(entry.getValue());
                    attr.setAttributes(new ArrayList<>());
                    attr.setChildren(new ArrayList<>());

                    ret.getAttributes().add(attr);
                } catch (Exception e) {

                }
            }

            for (Xml attr : ret.getAttributes()) {
                attr.setLocationName(ret.getLocationName());
                attr.setLocationLineNumber(ret.getLocationLineNumber());
                attr.setLocationColumnNumber(ret.getLocationColumnNumber());
            }


            NodeList childNodes = node.getChildNodes();
            if (childNodes != null) {
                int len = childNodes.getLength();
                for (int i = 0; i < len; i++) {
                    Node item = childNodes.item(i);
                    if (item != null) {
                        Xml nextNode = parseNodeNext(fileName, item);
                        if (nextNode != null) {
                            ret.getChildren().add(nextNode);
                        }
                    }
                }

            }

            ret.setDom(node);
            return ret;
        }
        return null;
    }

    public static Xml parseXmlSax(InputStream is) throws Exception {
        return parseXmlSax(null, is);
    }

    public static Xml parseXmlSax(URL url) throws Exception {
        String name = getLocationNameByUrl(url);
        return parseXmlSax(name, url.openStream());
    }

    public static Xml parseXmlSax(File file) throws Exception {
        return parseXmlSax(file.getName(), new FileInputStream(file));
    }

    public static void walkXml(Xml node, Consumer<Xml> consumer) {
        walkXml(node, true, consumer);
    }

    public static void walkXml(Xml node, boolean root2leaf, Consumer<Xml> consumer) {
        if (node == null) {
            return;
        }
        List<Xml> children = node.getChildren();
        if (root2leaf) {
            consumer.accept(node);
            if (children == null || children.isEmpty()) {
                return;
            }
            for (Xml item : children) {
                walkXml(item, root2leaf, consumer);
            }
        } else {
            if (children != null) {
                for (Xml item : children) {
                    walkXml(item, root2leaf, consumer);
                }
            }
            consumer.accept(node);
        }
    }

    public static void walkClean(Xml node) {
        walkXml(node, false, (item) -> {
            item.setInnerXml(null);
            item.setDom(null);
            Xml.Type type = node.getType();
            if (type == Xml.Type.DOCUMENT
                    || type == Xml.Type.ELEMENT) {
                item.setValue(null);
            }
        });
    }

    public static void walkFillDomAndInnerXml(Xml node) throws Exception {
        walkFillDomAndInnerXml(node, false);
    }

    public static void walkFillDomAndInnerXml(Xml node, boolean removeLocationAttribute) throws Exception {
        DocumentBuilder builder = getDocumentBuilder();
        walkXml(node, false, (item) -> {
            try {
                fillDomAndInnerXml(item, removeLocationAttribute, builder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void walkFillValue(Xml node) {
        walkFillValue(node, false);
    }

    public static void walkFillValue(Xml node, boolean useCached) {
        walkXml(node, false, (item) -> {
            Xml.Type type = item.getType();
            if (type == Xml.Type.DOCUMENT
                    || type == Xml.Type.ELEMENT) {
                String text = toText(item, useCached);
                item.setValue(text);
            }
        });
    }

    public static void fillDomAndInnerXml(Xml item) throws Exception {
        fillDomAndInnerXml(item, false, getDocumentBuilder());
    }

    public static void fillDomAndInnerXml(Xml item, boolean removeLocationAttribute) throws Exception {
        fillDomAndInnerXml(item, removeLocationAttribute, getDocumentBuilder());
    }

    public static void fillDomAndInnerXml(Xml item, boolean removeLocationAttribute, DocumentBuilder builder) throws Exception {
        if (item.getDom() == null) {
            try {
                Node dom = toDomNode(item, builder.newDocument());
                item.setDom(dom);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (item.getInnerXml() == null) {
            if (item.getDom() != null) {
                try {
                    String innerXml = extraInnerXml(item.getDom(), removeLocationAttribute);
                    item.setInnerXml(innerXml);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param locationName
     * @param is
     * @return
     * @throws XMLStreamException
     */
    public static Xml parseXmlSax(String locationName, InputStream is) throws Exception {
        XMLInputFactory staxFactory = getXmlInputFactory();
        XMLStreamReader reader = staxFactory.createXMLStreamReader(is);

        Xml document = null;
        Stack<Xml> stack = new Stack<>();

        try {
            while (reader.hasNext()) {
                if (document == null) {
                    Xml item = new Xml();
                    item.setType(Xml.Type.DOCUMENT);

                    stack.push(item);
                    document = item;
                }
                int type = reader.next();
                if (XMLStreamConstants.START_DOCUMENT == type) {
                    document.setName(reader.getLocalName());
                    document.setValue(reader.getElementText());
                    Location loc = reader.getLocation();
                    document.setLocationName(locationName);
                    if (loc != null) {
                        document.setLocationLineNumber(loc.getLineNumber());
                        document.setLocationColumnNumber(loc.getColumnNumber());
                    }

                } else if (XMLStreamConstants.END_DOCUMENT == type) {

                    stack.pop();
                } else if (XMLStreamConstants.START_ELEMENT == type) {

                    Xml item = new Xml();
                    item.setType(Xml.Type.ELEMENT);
                    item.setName(reader.getLocalName());
                    item.setValue(null);
                    Location loc = reader.getLocation();
                    item.setLocationName(locationName);
                    if (loc != null) {
                        item.setLocationLineNumber(loc.getLineNumber());
                        item.setLocationColumnNumber(loc.getColumnNumber());
                    }

                    int count = reader.getAttributeCount();
                    for (int i = 0; i < count; i++) {
                        String prefix = reader.getAttributePrefix(i);
                        String name = reader.getAttributeLocalName(i);
                        if (prefix != null && !prefix.isEmpty()) {
                            name = prefix + ":" + name;
                        }
                        String value = reader.getAttributeValue(i);
                        Xml attr = new Xml();
                        attr.setType(Xml.Type.ATTRIBUTE);
                        attr.setName(name);
                        attr.setValue(value);
                        attr.setLocationName(item.getLocationName());
                        attr.setLocationLineNumber(item.getLocationLineNumber());
                        attr.setLocationColumnNumber(item.getLocationColumnNumber());
                        if (item.getAttributes() == null) {
                            item.setAttributes(new ArrayList<>());
                        }
                        item.getAttributes().add(attr);
                    }

                    Xml parent = stack.peek();
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(item);

                    stack.push(item);
                } else if (XMLStreamConstants.END_ELEMENT == type) {

                    stack.pop();
                } else if (XMLStreamConstants.ATTRIBUTE == type) {
                    Xml parent = stack.peek();
                    int count = reader.getAttributeCount();
                    for (int i = 0; i < count; i++) {
                        String prefix = reader.getAttributePrefix(i);
                        String name = reader.getAttributeLocalName(i);
                        if (prefix != null && !prefix.isEmpty()) {
                            name = prefix + ":" + name;
                        }
                        String value = reader.getAttributeValue(i);
                        Xml item = new Xml();
                        item.setType(Xml.Type.ATTRIBUTE);
                        item.setName(name);
                        item.setValue(value);
                        Location loc = reader.getLocation();
                        item.setLocationName(locationName);
                        if (loc != null) {
                            item.setLocationLineNumber(loc.getLineNumber());
                            item.setLocationColumnNumber(loc.getColumnNumber());
                        }
                        if (parent.getAttributes() == null) {
                            parent.setAttributes(new ArrayList<>());
                        }
                        parent.getAttributes().add(item);
                    }
                } else if (XMLStreamConstants.CDATA == type) {
                    String text = reader.getText();
                    Xml item = new Xml();
                    item.setType(Xml.Type.CDATA);
                    item.setName(null);
                    item.setValue(text);
                    Location loc = reader.getLocation();
                    item.setLocationName(locationName);
                    if (loc != null) {
                        item.setLocationLineNumber(loc.getLineNumber());
                        item.setLocationColumnNumber(loc.getColumnNumber());
                    }
                    Xml parent = stack.peek();
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(item);
                } else if (XMLStreamConstants.CHARACTERS == type) {
                    String text = reader.getText();
                    Xml item = new Xml();
                    item.setType(Xml.Type.TEXT);
                    item.setName(null);
                    item.setValue(text);
                    Location loc = reader.getLocation();
                    item.setLocationName(locationName);
                    if (loc != null) {
                        item.setLocationLineNumber(loc.getLineNumber());
                        item.setLocationColumnNumber(loc.getColumnNumber());
                    }
                    Xml parent = stack.peek();
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(item);
                } else if (XMLStreamConstants.COMMENT == type) {
                    String text = reader.getText();
                    Xml item = new Xml();
                    item.setType(Xml.Type.COMMENT);
                    item.setName(null);
                    item.setValue(text);
                    Location loc = reader.getLocation();
                    item.setLocationName(locationName);
                    if (loc != null) {
                        item.setLocationLineNumber(loc.getLineNumber());
                        item.setLocationColumnNumber(loc.getColumnNumber());
                    }
                    Xml parent = stack.peek();
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(item);
                } else if (XMLStreamConstants.DTD == type) {
                    System.out.println("ok");
                } else if (XMLStreamConstants.ENTITY_DECLARATION == type) {
                    System.out.println("ok");
                } else if (XMLStreamConstants.ENTITY_REFERENCE == type) {
                    System.out.println("ok");
                } else if (XMLStreamConstants.NAMESPACE == type) {
                    System.out.println("ok");
                } else if (XMLStreamConstants.NOTATION_DECLARATION == type) {
                    System.out.println("ok");
                } else if (XMLStreamConstants.PROCESSING_INSTRUCTION == type) {
                    System.out.println("ok");
                } else if (XMLStreamConstants.SPACE == type) {
                    String text = reader.getText();
                    Xml item = new Xml();
                    item.setType(Xml.Type.TEXT);
                    item.setName(null);
                    item.setValue(text);
                    Location loc = reader.getLocation();
                    item.setLocationName(locationName);
                    if (loc != null) {
                        item.setLocationLineNumber(loc.getLineNumber());
                        item.setLocationColumnNumber(loc.getColumnNumber());
                    }
                    Xml parent = stack.peek();
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(item);
                }
            }

            walkFillValue(document, true);

            return document;
        } finally {
            reader.close();
        }
    }

    public static XMLInputFactory getXmlInputFactory() {
        XMLInputFactory staxFactory = XMLInputFactory.newInstance();
        // 禁用DTD支持（防止实体注入）
        staxFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);

        // 禁用外部实体解析
        staxFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);

        // 禁用命名空间处理
        staxFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);
        return staxFactory;
    }

    public static Node toDomNode(Xml node) throws ParserConfigurationException {
        return toDomNode(node, getDocumentBuilder().newDocument());
    }

    public static Node toDomNode(Xml node, Document doc) {
        if (node == null) {
            return null;
        }

        if (Xml.Type.DOCUMENT == node.getType()) {
            if (node.getChildren() != null) {
                for (Xml item : node.getChildren()) {
                    Node next = toDomNode(item, doc);
                    doc.appendChild(next);
                }
            }
            return doc;
        } else if (Xml.Type.ELEMENT == node.getType()) {
            Element elem = doc.createElement(node.getName());
            if (node.getAttributes() != null) {
                for (Xml item : node.getAttributes()) {
                    elem.setAttribute(item.getName(), item.getValue());
                }
            }
            if (node.getChildren() != null) {
                for (Xml item : node.getChildren()) {
                    Node next = toDomNode(item, doc);
                    elem.appendChild(next);
                }
            }
            return elem;
        } else if (Xml.Type.ATTRIBUTE == node.getType()) {
            Attr attr = doc.createAttribute(node.getName());
            attr.setValue(node.getValue());
            return attr;
        } else if (Xml.Type.COMMENT == node.getType()) {
            Comment comment = doc.createComment(node.getValue());
            return comment;
        } else if (Xml.Type.CDATA == node.getType()) {
            CDATASection cdata = doc.createCDATASection(node.getValue());
            return cdata;
        } else if (Xml.Type.TEXT == node.getType()) {
            Text text = doc.createTextNode(node.getValue());
            return text;
        }

        return null;
    }

    public static String toText(Xml node) {
        return toText(node, false, new StringBuilder()).toString();
    }

    public static String toText(Xml node, boolean useCached) {
        return toText(node, useCached, new StringBuilder()).toString();
    }

    public static StringBuilder toText(Xml node, boolean useCached, StringBuilder builder) {
        if (node == null) {
            return builder;
        }
        if (useCached) {
            if (node.getValue() != null) {
                builder.append(node.getValue());
                return builder;
            }
        }

        if (Xml.Type.DOCUMENT == node.getType()) {
            if (node.getChildren() != null) {
                for (Xml item : node.getChildren()) {
                    toText(item, useCached, builder);
                }
            }
            return builder;
        } else if (Xml.Type.ELEMENT == node.getType()) {

            if (node.getChildren() != null) {
                for (Xml item : node.getChildren()) {
                    toText(item, useCached, builder);
                }
            }
            return builder;
        } else if (Xml.Type.ATTRIBUTE == node.getType()) {
            return builder;
        } else if (Xml.Type.COMMENT == node.getType()) {
            return builder;
        } else if (Xml.Type.CDATA == node.getType()) {
            builder.append(node.getValue());
            return builder;
        } else if (Xml.Type.TEXT == node.getType()) {
            builder.append(node.getValue());
            return builder;
        }

        return builder;
    }
}
