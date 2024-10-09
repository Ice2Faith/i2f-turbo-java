package i2f.jdbc.proxy.xml.mybatis.test;

import i2f.bindsql.BindSql;
import i2f.jdbc.proxy.xml.mybatis.MybatisMapperContext;
import i2f.jdbc.proxy.xml.mybatis.data.MybatisMapperNode;
import i2f.jdbc.proxy.xml.mybatis.inflater.MybatisMapperInflater;
import i2f.xml.XmlUtil;
import org.w3c.dom.Document;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/10/9 17:27
 */
public class TestMybatisMapperContext {
    public static void main(String[] args) throws Exception {


        Map<String, Object> params = new LinkedHashMap<>();
        TestReqVo req = new TestReqVo();
        req.setTableName("sys_user");
        req.setIdColumn("id");
        req.setCodeColumn("code");
        req.setNameColumn("name");
        params.put("req", req);
        TestVo post = new TestVo();
        post.setId(1);
        post.setCode("123");
        post.setName("zhang");
        post.setVals(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6)));
        params.put("post", post);
        params.put("offset", 0);
        params.put("size", 100);

            System.out.println("=====================================================");
            MybatisMapperContext context = new MybatisMapperContext();
            context.setInflater(MybatisMapperInflater.INSTANCE);

        String file = "i2f-jdk/i2f-jdbc-proxy-xml/src/main/resources/sample/TestMapper.xml";
            Document document = XmlUtil.parseXml(new File(file));
            context.loadDocuments(Arrays.asList(document));
            testContext(context, params);



    }

    private static void testContext(MybatisMapperContext context, Map<String, Object> params) {
        Map<String, MybatisMapperNode> nodeMap = context.getNodeMap();

        for (Map.Entry<String, MybatisMapperNode> entry : nodeMap.entrySet()) {
            MybatisMapperNode sqlNode = entry.getValue();
            String tagName = sqlNode.getTagName();
            String nodeId = entry.getKey();
            String nodeContent = sqlNode.getTextContent();
            System.out.println(tagName + "[" + nodeId + "]:\n" + nodeContent);
            BindSql bindSql = context.inflate(nodeId, params);
            System.out.println(bindSql);
        }
    }
}
