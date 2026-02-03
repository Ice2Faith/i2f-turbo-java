package i2f.jdbc.proxy.xml.mybatis.parser;

import i2f.jdbc.proxy.xml.mybatis.data.MybatisMapperNode;
import i2f.match.regex.RegexUtil;
import i2f.match.regex.data.RegexFindPartMeta;
import i2f.xml.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2024/10/9 9:31
 */
public class MybatisMapperParser {
    public static final String[] XML_ELEMENT_TAG_NAME = {
            "resultMap", "sql", "select", "update", "insert", "delete"
    };
    public static final Set<String> XML_ELEMENT_TAG_NAME_SET = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(XML_ELEMENT_TAG_NAME)));

    public static final String[] SCRIPT_ELEMENT_TAG_NAME = {
            "if", "dialect", "choose", "when", "otherwise",
            "dialect-choose", "dialect-when", "dialect-otherwise",
            "foreach", "trim", "set", "where", "include","bind",
            "script"
    };
    public static final Set<String> SCRIPT_ELEMENT_TAG_NAME_SET = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(SCRIPT_ELEMENT_TAG_NAME)));


    public static Map<String, MybatisMapperNode> parseXmlUrlMappers(List<URL> urls, Map<String, MybatisMapperNode> ret) {
        if (ret == null) {
            ret = new LinkedHashMap<>();
        }
        for (URL url : urls) {
            try (InputStream stream = url.openStream()) {
                try {
                    Document document = XmlUtil.parseXml(stream);
                    parseXmlMapper(document, ret);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public static Map<String, MybatisMapperNode> parseXmlStreamMappers(List<InputStream> streams, Map<String, MybatisMapperNode> ret) {
        if (ret == null) {
            ret = new LinkedHashMap<>();
        }
        for (InputStream stream : streams) {
            try {
                Document document = XmlUtil.parseXml(stream);
                parseXmlMapper(document, ret);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public static Map<String, MybatisMapperNode> parseXmlDocumentMappers(List<Document> documents, Map<String, MybatisMapperNode> ret) {
        if (ret == null) {
            ret = new LinkedHashMap<>();
        }
        for (Document document : documents) {
            parseXmlMapper(document, ret);
        }
        return ret;
    }

    public static Map<String, MybatisMapperNode> parseXmlMapper(Document document, Map<String, MybatisMapperNode> ret) {
        if (ret == null) {
            ret = new LinkedHashMap<>();
        }
        Node rootNode = XmlUtil.getRootNode(document);
        String namespace = Optional.ofNullable(XmlUtil.getAttribute(rootNode, "namespace")).orElse("");

        List<Node> rootNodes = XmlUtil.getChildNodes(rootNode, XML_ELEMENT_TAG_NAME_SET);
        for (Node node : rootNodes) {
            MybatisMapperNode sqlNode = parseSqlNode(node);
            sqlNode.setNamespace(namespace);
            String id = Optional.ofNullable(sqlNode.getAttributes().get("id")).orElse("");
            if (id.contains(".")) {
                int idx = id.lastIndexOf(".");
                String space = id.substring(0, idx);
                id = id.substring(idx + 1);
                sqlNode.setId(id);
                if (space.isEmpty()) {
                    space = namespace;
                }
                if (!space.isEmpty()) {
                    id = space + "." + id;
                }
                sqlNode.setUnqId(id);
            } else {
                sqlNode.setId(id);
                if (!namespace.isEmpty()) {
                    id = namespace + "." + id;
                }
                sqlNode.setUnqId(id);
            }

            ret.put(sqlNode.getUnqId(), sqlNode);
        }
        return ret;
    }

    public static String resolveEscapeXml(String script) {
        if (script == null) {
            return null;
        }
        boolean isEscapeChar = false;
        int len = script.length();
        for (int i = 0; i < len; i++) {
            char ch = script.charAt(i);
            if (ch == '<' || ch == '&') {
                isEscapeChar = true;
                break;
            }
        }
        if (!isEscapeChar) {
            return script;
        }
        StringBuilder builder = new StringBuilder();
        List<RegexFindPartMeta> parts = RegexUtil.regexFindParts(script, "<(/)?[a-zA-Z0-9\\-_]+.|<[^a-zA-Z0-9\\-_]|&#[0-9]+.|&[a-zA-Z0-9]+.|&[^a-zA-Z0-9]");
        for (RegexFindPartMeta item : parts) {
            String part = item.getPart();
            if (item.isMatch()) {
                if (part.startsWith("<")) {
                    if (part.length() == 2) {
                        builder.append("&lt;").append(part.substring(1));
                    } else {
                        if (part.startsWith("</")) {
                            if (!part.endsWith(">")) {
                                builder.append("&lt;").append(part.substring(1));
                            } else {
                                String tagName = part.substring(2, part.length() - 1);
                                if (!SCRIPT_ELEMENT_TAG_NAME_SET.contains(tagName)) {
                                    builder.append("&lt;").append(part.substring(1));
                                } else {
                                    builder.append(part);
                                }
                            }
                        } else {
                            char endCh = part.charAt(part.length() - 1);
                            boolean isLegalTag = false;
                            if (endCh == '>' || endCh == ' ' || endCh == '\t' || endCh == '\r' || endCh == '\n') {
                                isLegalTag = true;
                            }
                            if (isLegalTag) {
                                String tagName = part.substring(1, part.length() - 1);
                                if (!SCRIPT_ELEMENT_TAG_NAME_SET.contains(tagName)) {
                                    builder.append("&lt;").append(part.substring(1));
                                } else {
                                    builder.append(part);
                                }
                            } else {
                                builder.append("&lt;").append(part.substring(1));
                            }
                        }
                    }
                } else if (part.startsWith("&")) {
                    if (part.length() == 2) {
                        builder.append("&amp;").append(part.substring(1));
                    } else {
                        if (part.startsWith("&#")) {
                            if (!part.endsWith(";")) {
                                builder.append("&amp;").append(part.substring(1));
                            } else {
                                builder.append(part);
                            }
                        } else {
                            if (!part.endsWith(";")) {
                                builder.append("&amp;").append(part.substring(1));
                            } else {
                                builder.append(part);
                            }
                        }
                    }
                }
            } else {
                builder.append(part);
            }
        }
        return builder.toString();
    }

    public static MybatisMapperNode parseScriptNode(String script) throws Exception {
        Document document = null;
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("<script>");
            builder.append(resolveEscapeXml(script));
            builder.append("</script>");
            document = XmlUtil.parseXml(builder.toString());
        } catch (Throwable e) {
            try {
                StringBuilder builder = new StringBuilder();
                builder.append("<script>");
                builder.append(script);
                builder.append("</script>");
                document = XmlUtil.parseXml(builder.toString());
            } catch (Exception ex) {
                ex.addSuppressed(e);
                throw ex;
            }
        }
        Node node = XmlUtil.getRootNode(document);
        return parseSqlNode(node);
    }

    public static MybatisMapperNode parseSqlNode(Node node) {
        MybatisMapperNode ret = new MybatisMapperNode();
        short nodeType = node.getNodeType();
        ret.setNodeType(nodeType);
        if (Node.COMMENT_NODE == nodeType) {
            ret.setXmlType(false);
            ret.setContent(node.getNodeValue());
        } else if (Node.CDATA_SECTION_NODE == nodeType) {
            ret.setXmlType(false);
            ret.setContent(node.getNodeValue());
        } else if (Node.TEXT_NODE == nodeType) {
            ret.setXmlType(false);
            ret.setContent(XmlUtil.getNodeContent(node));
        } else if (Node.ELEMENT_NODE == nodeType) {
            ret.setXmlType(true);
            ret.setTagName(XmlUtil.getTagName(node));
            ret.setAttributes(XmlUtil.getAttributes(node));
            ret.setChildren(new ArrayList<>());
            List<Node> nodes = XmlUtil.getChildNodes(node);
            if (!nodes.isEmpty()) {
                for (Node item : nodes) {
                    MybatisMapperNode sqlNode = parseSqlNode(item);
                    ret.getChildren().add(sqlNode);
                }
            }
        } else {
            ret.setXmlType(false);
            ret.setContent("");
        }
        return ret;
    }

}
