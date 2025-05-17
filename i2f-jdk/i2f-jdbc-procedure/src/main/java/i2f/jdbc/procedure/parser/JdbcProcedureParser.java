package i2f.jdbc.procedure.parser;

import i2f.io.stream.StreamUtil;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.xml.XmlUtil;
import i2f.xml.data.Xml;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2025/1/20 9:24
 */
public class JdbcProcedureParser {
    public static final String MACRO_FILE_NAME = "__FILE__";
    public static final String MACRO_LINE_NUMBER = "__LINE__";

    public static String replaceMacro(String fileName, String str) {
        if (str == null) {
            return str;
        }
        if (fileName == null) {
            fileName = "";
        }
        str = str.replace(MACRO_FILE_NAME, fileName);
        String[] arr = str.split("\n");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            builder.append(arr[i].replace(MACRO_LINE_NUMBER, String.valueOf(i + 1))).append("\n");
        }
        return builder.toString();
    }

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
        return parse(bis);
    }

    public static XmlNode parse(URL url) throws Exception {
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
        return parse(name, url.openStream());
    }

    public static XmlNode parse(File file) throws Exception {
        return parse(file.getName(), new FileInputStream(file));
    }

    public static XmlNode parse(InputStream is) throws Exception {
        return parse(null, is);
    }

    public static XmlNode parse(String fileName, InputStream is) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        StreamUtil.streamCopy(is, bos);
        String str = new String(bos.toByteArray(), "UTF-8");
        str = removeProcedureDtd(str);
        str = replaceMacro(fileName, str);
        ByteArrayInputStream bis = new ByteArrayInputStream(str.getBytes("UTF-8"));
        Xml document = XmlUtil.parseXmlSax(fileName, bis);
        bis.close();
        return parse(document);
    }

    public static XmlNode parse(Xml node) throws Exception {
        Xml root = XmlUtil.getRootNode(node);
        XmlUtil.walkFillDomAndInnerXml(node, true);
        return parseNode(root);
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

    public static Map.Entry<String, List<String>> parseAttrFeatures(String key) {
        String[] arr = key.split("\\.");
        String name = arr[0];
        List<String> features = new ArrayList<>();
        for (int i = 1; i < arr.length; i++) {
            features.add(arr[i]);
        }
        return new AbstractMap.SimpleEntry<>(name, features);
    }

    public static XmlNode parseNode(Xml node) throws Exception {
        if (node == null) {
            return null;
        }
        XmlNode ret = null;
        Xml.Type nodeType = node.getType();
        if (nodeType == Xml.Type.TEXT) {
            ret = new XmlNode();
            ret.setNodeType(XmlNode.NodeType.TEXT);
            ret.setTagName(null);
            ret.setTagAttrMap(new LinkedHashMap<>());
            ret.setAttrFeatureMap(new LinkedHashMap<>());
            ret.setTagBody(node.getValue());
            ret.setTextBody(node.getValue());
            ret.setChildren(null);

            ret.setLocationFile(node.getLocationName());
            ret.setLocationLineNumber(node.getLocationLineNumber());
        } else if (nodeType == Xml.Type.CDATA) {
            ret = new XmlNode();
            ret.setNodeType(XmlNode.NodeType.CDATA);
            ret.setTagName(null);
            ret.setTagAttrMap(new LinkedHashMap<>());
            ret.setAttrFeatureMap(new LinkedHashMap<>());
            ret.setTagBody(node.getValue());
            ret.setTextBody(node.getValue());
            ret.setChildren(null);

            ret.setLocationFile(node.getLocationName());
            ret.setLocationLineNumber(node.getLocationLineNumber());
        } else if (nodeType == Xml.Type.ELEMENT) {
            ret = new XmlNode();
            ret.setNodeType(XmlNode.NodeType.ELEMENT);
            ret.setTagName(node.getName());
            ret.setTagAttrMap(new LinkedHashMap<>());
            ret.setAttrFeatureMap(new LinkedHashMap<>());
            ret.setTagBody(node.getInnerXml());
            ret.setTextBody(node.getValue());

            ret.setLocationFile(node.getLocationName());
            ret.setLocationLineNumber(node.getLocationLineNumber());

            Map<String, String> tagAttrMap = new LinkedHashMap<>();
            Map<String, List<String>> attrFeatureMap = new LinkedHashMap<>();
            List<Xml> attributes = node.getAttributes();
            if (attributes != null) {
                for (Xml entry : attributes) {
                    String key = entry.getName();
                    try {
                        if (XmlUtil.ATTR_FILE.equals(key)) {
                            ret.setLocationFile(URLDecoder.decode(entry.getValue(), "UTF-8"));
                            continue;
                        }
                        if (XmlUtil.ATTR_LINE_NUMBER.equals(key)) {
                            String value = entry.getValue();
                            ret.setLocationLineNumber(Integer.parseInt(value));
                            continue;
                        }
                    } catch (Exception e) {

                    }
                    Map.Entry<String, List<String>> attrFeatures = parseAttrFeatures(key);
                    tagAttrMap.put(attrFeatures.getKey(), entry.getValue());
                    if (!attrFeatureMap.containsKey(attrFeatures.getKey())) {
                        attrFeatureMap.put(attrFeatures.getKey(), new ArrayList<>());
                    }
                    attrFeatureMap.get(attrFeatures.getKey()).addAll(attrFeatures.getValue());
                }
            }

            ret.setTagAttrMap(tagAttrMap);
            ret.setAttrFeatureMap(attrFeatureMap);

            List<XmlNode> children = new ArrayList<>();
            List<Xml> childNodes = node.getChildren();
            if (childNodes != null) {
                for (Xml item : childNodes) {
                    if (item != null) {
                        XmlNode nextNode = parseNode(item);
                        if (nextNode != null) {
                            children.add(nextNode);
                        }
                    }
                }
            }
            ret.setChildren(children);
        }
        if (ret != null) {
            String nodeLocation = XmlNode.getNodeLocation(ret);
            ret.setNodeLocation(nodeLocation);
        }
        return ret;
    }


}
