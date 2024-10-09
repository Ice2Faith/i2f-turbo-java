package i2f.extension.jdbc.proxy.mybatis;

import i2f.bindsql.BindSql;
import i2f.extension.jdbc.proxy.mybatis.data.MybatisMapperNode;
import i2f.extension.jdbc.proxy.mybatis.inflater.MybatisMapperInflater;
import i2f.extension.jdbc.proxy.mybatis.parser.MybatisMapperParser;
import i2f.xml.XmlUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.Document;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2024/10/9 16:59
 */
@Data
@NoArgsConstructor
public class MybatisMapperContext {
    protected final Map<String, MybatisMapperNode> nodeMap = new ConcurrentHashMap<>();
    protected final List<URL> xmlUrls = new CopyOnWriteArrayList<>();
    protected volatile MybatisMapperInflater inflater = MybatisMapperInflater.INSTANCE;

    public MybatisMapperContext(List<URL> xmlUrls) {
        this.xmlUrls.addAll(xmlUrls);
        refresh();
    }

    public void loadDocuments(List<Document> documents) {
        Map<String, MybatisMapperNode> ret = MybatisMapperParser.parseXmlDocumentMappers(documents, null);
        nodeMap.putAll(ret);
    }

    public void refresh() {
        Map<String, MybatisMapperNode> ret = MybatisMapperParser.parseXmlUrlMappers(xmlUrls, null);
        nodeMap.putAll(ret);
    }

    public BindSql inflate(String unqId, Map<String, Object> params) {
        return inflater.inflateSql(unqId, params, nodeMap);
    }

    public BindSql inflateTempSelect(String script, Map<String, Object> params) {
        MybatisMapperNode node = createNodeSelect(script);
        return inflateTemp(node, params);
    }

    public BindSql inflateTempUpdate(String script, Map<String, Object> params) {
        MybatisMapperNode node = createNodeUpdate(script);
        return inflateTemp(node, params);
    }

    public BindSql inflateTemp(String script, Map<String, Object> params) {
        MybatisMapperNode node = createNode(script);
        return inflateTemp(node, params);
    }

    public BindSql inflateTemp(MybatisMapperNode node, Map<String, Object> params) {
        String unqId = node.getUnqId();
        try {
            nodeMap.put(unqId, node);
            BindSql ret = inflate(unqId, params);
            return ret;
        } finally {
            nodeMap.remove(unqId);
        }
    }

    public MybatisMapperNode createNodeSelect(String script) {
        return createNode("<select id=\"query\">\n" +
                script + "\n" +
                "</select>");
    }

    public MybatisMapperNode createNodeUpdate(String script) {
        return createNode("<update id=\"query\">\n" +
                script + "\n" +
                "</update>");
    }

    public MybatisMapperNode createNode(String script) {
        if (script == null) {
            return null;
        }
        if (!script.contains("<mapper")) {
            String namespace = "tmp." + UUID.randomUUID().toString().replaceAll("-", "").toLowerCase() + "." + Thread.currentThread().getId();
            script = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n" +
                    "<mapper namespace=\"" + namespace + "\">\n" +
                    script + "\n"
                    + "</mapper>";
        }
        try {
            Document document = XmlUtil.parseXml(script.getBytes("UTF-8"));
            Map<String, MybatisMapperNode> map = MybatisMapperParser.parseXmlMapper(document, null);
            if (map.isEmpty()) {
                return null;
            }
            return map.entrySet().iterator().next().getValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
