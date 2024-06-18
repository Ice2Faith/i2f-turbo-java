package i2f.extension.velocity.bindsql;

import i2f.bindsql.BindSql;
import i2f.io.stream.StreamUtil;
import i2f.xml.XmlUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2024/6/11 8:34
 * @desc
 */
@Data
@NoArgsConstructor
public class VelocityResourceSqlTemplateResolver {
    private List<URL> resourceList = new ArrayList<>();
    private ConcurrentHashMap<String, BindSql> templateMap = new ConcurrentHashMap<>();

    public VelocityResourceSqlTemplateResolver(List<URL> resourceList) {
        this.resourceList = resourceList;
    }

    public void refreshResources() {
        templateMap.clear();
        for (URL url : resourceList) {
            try {
                Map<String, BindSql> map = parseXmlTemplates(url);
                templateMap.putAll(map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Map<String, BindSql> parseXmlTemplates(URL url) throws Exception {
        Map<String, BindSql> ret = new LinkedHashMap<>();

        byte[] bytes = StreamUtil.readBytes(url);

        Document document = XmlUtil.parseXml(bytes);

        Node mapperNode = XmlUtil.getRootNode(document);
        String className = XmlUtil.getAttribute(mapperNode, "class");
        if (className == null) {
            className = "";
        }
        className = className.trim();

        List<Node> matchedNodes = XmlUtil.getNodesByTagName(document, Arrays.asList("query", "update", "call", "sql"));

        for (Node item : matchedNodes) {
            String nodeName = item.getNodeName();
            if (nodeName == null) {
                nodeName = "";
            }
            nodeName = nodeName.trim();
            String method = XmlUtil.getAttribute(item, "method");
            String body = XmlUtil.getNodeContent(item);

            if (method == null) {
                continue;
            }
            method = method.trim();
            if (method.isEmpty()) {
                continue;
            }
            if (!method.contains(".")) {
                if (!className.isEmpty()) {
                    method = className + "." + method;
                }
            }
            if ("query".equals(nodeName)) {
                ret.put(method, BindSql.of(BindSql.Type.QUERY, body));
            } else if ("update".equals(nodeName)) {
                ret.put(method, BindSql.of(BindSql.Type.UPDATE, body));
            } else if ("call".equals(nodeName)) {
                ret.put(method, BindSql.of(BindSql.Type.CALL, body));
            } else if ("sql".equals(nodeName)) {
                ret.put(method, BindSql.of(BindSql.Type.UNSET, body));
            } else {
                ret.put(method, BindSql.of(BindSql.Type.UNSET, body));
            }

        }

        return ret;
    }

    public BindSql getScript(String methodId) {
        return templateMap.get(methodId);
    }

    public static String getDemoXmlVmContent() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<!--\n" +
                "    'mapper' is root node,\n" +
                "    and with 'class' attribute,\n" +
                "    mean's that children nodes all default in this class\n" +
                "-->\n" +
                "<mapper class=\"com.test.mapper.TestMapper\">\n" +
                "\n" +
                "    <!--\n" +
                "        children nodes:\n" +
                "        support node type of 'query','update','call','sql',\n" +
                "        query: for jdbc executeQuery,\n" +
                "        update: for jdbc executeUpdate,\n" +
                "        call: for jdbc call,\n" +
                "        sql: auto detect type by keywords to 'query'/'update','call', but sometime maybe wrong detect.\n" +
                "    -->\n" +
                "\n" +
                "    <!--\n" +
                "        every child node,\n" +
                "        with 'method' attribute,\n" +
                "        mean's that this node refer to witch java method on root node's attribute 'mapper.class',\n" +
                "        but also, method cloud be full method name assigned, which will use it instead of bind 'mapper.class',\n" +
                "        such as below 'sql' node\n" +
                "    -->\n" +
                "    <query method=\"listAll\">\n" +
                "        select * from sys_user\n" +
                "    </query>\n" +
                "\n" +
                "    <query method=\"list\">\n" +
                "        select a.*\n" +
                "        from sys_user a\n" +
                "        <!--\n" +
                "        you can use #sqlWhere directive to trim and/or for inner text,\n" +
                "        use like mybatis's 'where' tag,\n" +
                "        but rule in velocity grammar is this form\n" +
                "        -->\n" +
                "        #sqlWhere()\n" +
                "            #if($post.username != null && $post.username !=\"\")\n" +
                "                and a.username = #sql($post.username)\n" +
                "            #end\n" +
                "            #if($post.age != null)\n" +
                "                and a.age &gt;= #sql($post.age)\n" +
                "            #end\n" +
                "        #end\n" +
                "    </query>\n" +
                "\n" +
                "    <update method=\"update\">\n" +
                "        update sys_user\n" +
                "        <!--\n" +
                "            you also can use #sqlSet directive to trim , for inner text,\n" +
                "            and also like mybatis's ‘set' tag,\n" +
                "        -->\n" +
                "        #sqlSet()\n" +
                "            #if($post.username != null && $post.username !=\"\")\n" +
                "               username = #sql($post.username),\n" +
                "            #end\n" +
                "            #if($post.age != null)\n" +
                "                age &gt;= #sql($post.age),\n" +
                "            #end\n" +
                "        #end\n" +
                "        #sqlWhere()\n" +
                "            and status=1\n" +
                "            #if($post.id != null)\n" +
                "                and id = #sql($post.id)\n" +
                "            #end\n" +
                "        #end\n" +
                "    </update>\n" +
                "\n" +
                "    <call method=\"callTest\">\n" +
                "        {\n" +
                "        call sp_test(#sql($post.username),#sql($post.id))\n" +
                "        }\n" +
                "    </call>\n" +
                "\n" +
                "    <sql method=\"com.hello.mapper.HelloMapper.hello\">\n" +
                "        select 1 from dual\n" +
                "    </sql>\n" +
                "\n" +
                "</mapper>\n";
    }
}
